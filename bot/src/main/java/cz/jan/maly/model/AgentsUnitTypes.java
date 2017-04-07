package cz.jan.maly.model;

import cz.jan.maly.model.agent.types.AgentTypeUnit;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.planing.command.ActCommand;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;

import static cz.jan.maly.model.BasicFactsKeys.IS_UNIT;
import static cz.jan.maly.model.BasicFactsKeys.MINERAL;
import static cz.jan.maly.model.DesiresKeys.MINE_MINERALS_IN_BASE;
import static cz.jan.maly.model.FactsKeys.MINERAL_TO_MINE;
import static cz.jan.maly.model.FactsKeys.MINING_MINERAL;

/**
 * Created by Jan on 15-Mar-17.
 */
public class AgentsUnitTypes {

    public static final AgentTypeUnit HATCHERY = AgentTypeUnit.builder()
            .name("HATCHERY")
            .initializationStrategy(type -> {
            })
            .build();

    public static final AgentTypeUnit WORKER = AgentTypeUnit.builder()
            .name("WORKER")
//            .desiresWithIntentionToAct(new HashSet<>(Arrays.asList(new DesireKey[]{MINE_MINERALS})))
//            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(new DesireKey[]{SELECT_MINERAL})))
            .usingTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{MINING_MINERAL, MINERAL_TO_MINE})))
            .initializationStrategy(type -> {

                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent mineOneOfTheResources  = ConfigurationWithCommand
                        .WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                AUnitWithCommands me = intention.returnFactValueForGivenKey(IS_UNIT).get();
                                Optional<AUnit> mineralsToMine = intention.returnFactSetValueForGivenKeyInDesireParameters(MINERAL).get().stream()
                                        .min(Comparator.comparingDouble(o -> me.getPosition().distanceTo(o.getPosition())));
                                if (mineralsToMine.isPresent()){
                                    boolean commandSent = intention.returnFactValueForGivenKey(IS_UNIT).get().gather(mineralsToMine.get());
                                    if (commandSent) {
                                        memory.updateFact(MINING_MINERAL, mineralsToMine.get());
                                    }
                                    return commandSent;
                                }
                                return true;
                            }
                        })
                        .decisionInDesire((desire, dataForDecision) -> true)
                        .decisionInIntention((intention, dataForDecision) -> false)
                        .build();
                type.addConfiguration(MINE_MINERALS_IN_BASE, mineOneOfTheResources);

//                //select nearest unoccupied mineral to mine
//                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf selectMineral = ConfigurationWithCommand.
//                        WithReasoningCommandDesiredBySelf.builder()
//                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
//                            @Override
//                            public boolean act(WorkingMemory memory) {
//                                Set<AUnit> mineralsBeingMined = memory.getReadOnlyMemoriesForAgentType(WORKER).stream()
//                                        .filter(readOnlyMemory -> readOnlyMemory.getAgentId() != memory.getAgentId())
//                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MINING_MINERAL))
//                                        .filter(Optional::isPresent)
//                                        .map(Optional::get)
//                                        .collect(Collectors.toSet());
//                                APosition myPosition = memory.returnFactValueForGivenKey(IS_UNIT).get().getPosition();
//                                Optional<AUnit> unoccupiedMineral = memory.returnFactValueForGivenKey(IS_UNIT).get().getResourceUnitsInRadiusOfSight().stream()
//                                        .filter(unit -> !unit.getType().isForType(UnitType.Resource_Vespene_Geyser))
//                                        .filter(unit -> !mineralsBeingMined.contains(unit))
//                                        .min(Comparator.comparingDouble(o -> myPosition.distanceTo(o.getPosition())));
//                                unoccupiedMineral.ifPresent(aUnit -> memory.updateFact(MINERAL_TO_MINE, aUnit));
//
//                                //todo remove
////                                BotFacade.ADDITIONAL_OBSERVATIONS_PROCESSOR.requestObservation((m, e) -> {
////
////                                    ATilePosition position = new ATilePosition(e.getBuildLocation(UnitType.Zerg_Spawning_Pool, myPosition.getATilePosition().getTilePosition(), 20));
////
////                                    return true;
////                                }, memory, WORKER);
//
//
//                                return true;
//                            }
//                        })
//                        //todo we do not mind or mineral is mind by other agent
//                        .decisionInDesire((desire, dataForDecision) ->
//                                !desire.returnFactValueForGivenKey(MINING_MINERAL).isPresent())
//                        //we made reasoning
//                        .decisionInIntention((intention, dataForDecision) ->
//                                true)
//                        .build();
//                type.addConfiguration(SELECT_MINERAL, selectMineral);
//
//                //go mining
//                ConfigurationWithCommand.WithActingCommandDesiredBySelf miningConfiguration = ConfigurationWithCommand.
//                        WithActingCommandDesiredBySelf.builder()
//                        .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
//                            @Override
//                            public boolean act(WorkingMemory memory) {
//                                AUnit mineral = intention.returnFactValueForGivenKeyInDesireParameters(MINERAL_TO_MINE).get();
//                                boolean commandSent = intention.returnFactValueForGivenKey(IS_UNIT).get().gather(mineral);
//                                if (commandSent) {
//                                    memory.updateFact(MINING_MINERAL, mineral);
//                                }
//                                return commandSent;
//                            }
//                        })
//                        //todo we have mineral to mine and currently we do not mine any other mineral, we don't carry mineral
//                        .decisionInDesire((desire, dataForDecision) ->
//                                desire.returnFactValueForGivenKeyInParameters(MINERAL_TO_MINE).isPresent() && !desire.returnFactValueForGivenKey(MINING_MINERAL).isPresent())
//                        //todo we are mining mineral "officially"
//                        .decisionInIntention((intention, dataForDecision) -> true)
//                        .build();
//                type.addConfiguration(MINE_MINERALS, miningConfiguration);

                //todo abstract plan to mine the closest resources from request

            })
            .build();

}
