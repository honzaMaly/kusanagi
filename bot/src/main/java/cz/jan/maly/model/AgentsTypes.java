package cz.jan.maly.model;

import bwapi.UnitType;
import cz.jan.maly.model.game.wrappers.APosition;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;

import java.util.*;
import java.util.stream.Collectors;

import static cz.jan.maly.model.BasicFactsKeys.IS_UNIT;
import static cz.jan.maly.model.DesiresKeys.MINE_MINERALS;
import static cz.jan.maly.model.DesiresKeys.SELECT_MINERAL;
import static cz.jan.maly.model.FactsKeys.MINERAL_TO_MINE;
import static cz.jan.maly.model.FactsKeys.MINING_MINERAL;

/**
 * Created by Jan on 15-Mar-17.
 */
public class AgentsTypes {

    public static final AgentTypeObservingGame WORKER = AgentTypeObservingGame.builder()
            .name("WORKER")
            .desiresWithIntentionToAct(new HashSet<>(Arrays.asList(new DesireKey[]{MINE_MINERALS})))
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(new DesireKey[]{SELECT_MINERAL})))
            .usingTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{MINING_MINERAL, MINERAL_TO_MINE})))
            .initializationStrategy(type -> {

                //select nearest unoccupied mineral to mine
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf selectMineral = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                Set<AUnit> mineralsBeingMined = memory.getReadOnlyMemoriesForAgentType(WORKER).stream()
                                        .filter(readOnlyMemory -> readOnlyMemory.getAgentId() != memory.getAgentId())
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MINING_MINERAL))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .collect(Collectors.toSet());
                                APosition myPosition = memory.returnFactValueForGivenKey(IS_UNIT).get().getPosition();
                                Optional<AUnit> unoccupiedMineral = memory.returnFactValueForGivenKey(IS_UNIT).get().getResourceUnitsInRadiusOfSight().stream()
                                        .filter(unit -> !unit.getType().isForType(UnitType.Resource_Vespene_Geyser))
                                        .filter(unit -> !mineralsBeingMined.contains(unit))
                                        .min(Comparator.comparingDouble(o -> myPosition.distanceTo(o.getPosition())));
                                unoccupiedMineral.ifPresent(aUnit -> memory.updateFact(new Fact<>(aUnit, MINERAL_TO_MINE)));
                                return true;
                            }
                        })
                        //todo we do not mind or mineral is mind by other agent
                        .decisionInDesire((desire, dataForDecision) -> !desire.returnFactValueForGivenKey(MINING_MINERAL).isPresent())
                        //we made reasoning
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();
                type.addConfiguration(SELECT_MINERAL, selectMineral);

                //go mining
                ConfigurationWithCommand.WithActingCommandDesiredBySelf miningConfiguration = ConfigurationWithCommand.
                        WithActingCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                AUnit mineral = intention.returnFactValueForGivenKeyInDesireParameters(MINERAL_TO_MINE).get();
                                boolean commandSent = intention.returnFactValueForGivenKey(IS_UNIT).get().gather(mineral);
                                if (commandSent) {
                                    memory.updateFact(new Fact<>(mineral, MINING_MINERAL));
                                }
                                return commandSent;
                            }
                        })
                        //todo we have mineral to mine and currently we do not mine any other mineral, we don't carry mineral
                        .decisionInDesire((desire, dataForDecision) -> desire.returnFactValueForGivenKeyInParameters(MINERAL_TO_MINE).isPresent() && !desire.returnFactValueForGivenKey(MINING_MINERAL).isPresent())
                        //todo we are mining mineral "officially"
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();
                type.addConfiguration(MINE_MINERALS, miningConfiguration);

                //todo abstract plan to mine the closest resources

            })
            .build();

}
