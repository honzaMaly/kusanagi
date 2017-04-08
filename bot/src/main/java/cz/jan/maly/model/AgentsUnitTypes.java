package cz.jan.maly.model;

import bwta.BWTA;
import cz.jan.maly.model.agent.types.AgentTypeUnit;
import cz.jan.maly.model.game.wrappers.APosition;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithAbstractPlan;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;

import java.util.*;
import java.util.stream.Collectors;

import static cz.jan.maly.model.BasicFactsKeys.*;
import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.FactsKeys.*;

/**
 * Created by Jan on 15-Mar-17.
 */
public class AgentsUnitTypes {
    public static final AgentTypeUnit HATCHERY = AgentTypeUnit.builder()
            .name("HATCHERY")
            .initializationStrategy(type -> {
            })
            .build();
    private static final Random RANDOM = new Random();
    public static final AgentTypeUnit WORKER = AgentTypeUnit.builder()
            .name("WORKER")
//            .desiresWithIntentionToAct(new HashSet<>(Arrays.asList(new DesireKey[]{MINE_MINERALS})))
//            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(new DesireKey[]{SELECT_MINERAL})))
            .usingTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{MINING_MINERAL, MINERAL_TO_MINE, LAST_TIME_REASON_ABOUT_MINERAL_TO_MINE})))
            .initializationStrategy((AgentType type) -> {

                //abstract plan to mine minerals in base
                ConfigurationWithAbstractPlan mineInBase = ConfigurationWithAbstractPlan.builder()
                        //commit if there is actually less committed workers then there is active minerals. this is also reason to remove commitment
                        .decisionInDesire((desire, dataForDecision) -> {
                            Set<AUnit> mineralsBeingMined = desire.getReadOnlyMemories().stream()
                                    .filter(readOnlyMemory -> readOnlyMemory.getAgentId() != desire.getAgentId())
                                    .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(MINING_MINERAL))
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MINING_MINERAL))
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .collect(Collectors.toSet());
                            Set<AUnit> mineralsToMine = desire.returnFactSetValueForGivenKeyInParameters(MINERAL).get().stream()
                                    .filter(AUnit::isAlive)
                                    .filter(unit -> !mineralsBeingMined.contains(unit))
                                    .collect(Collectors.toSet());
                            return !mineralsToMine.isEmpty();
                        })
                        .decisionInIntention((intention, dataForDecision) -> {
                            Set<AUnit> mineralsBeingMined = intention.getReadOnlyMemories().stream()
                                    .filter(readOnlyMemory -> readOnlyMemory.getAgentId() != intention.getAgentId())
                                    .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(MINING_MINERAL))
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MINING_MINERAL))
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .collect(Collectors.toSet());
                            Set<AUnit> mineralsToMine = intention.returnFactSetValueForGivenKeyInDesireParameters(MINERAL).get().stream()
                                    .filter(AUnit::isAlive)
                                    .filter(unit -> !mineralsBeingMined.contains(unit))
                                    .collect(Collectors.toSet());
                            return mineralsToMine.isEmpty();
                        })
                        .desiresWithIntentionToAct(new HashSet<>(Arrays.asList(new DesireKey[]{MINE_MINERALS})))
                        .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(new DesireKey[]{SELECT_MINERAL})))
                        .build();
                type.addConfiguration(MINE_MINERALS_IN_BASE, mineInBase, false);

                //select closest mineral to mine from set of available
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
                                Set<AUnit> mineralsToMine = intention.returnFactSetValueOfParentIntentionForGivenKey(MINERAL).get().stream()
                                        .filter(AUnit::isAlive)
                                        .filter(unit -> !mineralsBeingMined.contains(unit))
                                        .collect(Collectors.toSet());
                                if (!mineralsToMine.isEmpty()) {

                                    //select free nearest mineral to closest hatchery
                                    APosition myPosition = intention.returnFactValueForGivenKey(IS_UNIT).get().getPosition();
                                    Optional<AUnit> hatchery = intention.returnFactSetValueOfParentIntentionForGivenKey(HAS_HATCHERY).get().stream()
                                            .min(Comparator.comparingDouble(o -> BWTA.getGroundDistance(myPosition.getATilePosition().getTilePosition(), o.getPosition().getATilePosition().getTilePosition())));
                                    if (hatchery.isPresent()) {
                                        Optional<AUnit> mineralToPick = mineralsToMine.stream()
                                                .min(Comparator.comparingDouble(o -> BWTA.getGroundDistance(hatchery.get().getPosition().getATilePosition().getTilePosition(), o.getPosition().getATilePosition().getTilePosition())));
                                        mineralToPick.ifPresent(aUnit -> {
                                            memory.updateFact(MINERAL_TO_MINE, aUnit);
                                            memory.updateFact(LAST_TIME_REASON_ABOUT_MINERAL_TO_MINE, memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).orElse(null));
                                        });
                                    }
                                }
                                return true;
                            }
                        })
                        .decisionInDesire((desire, dataForDecision) -> {

                                    //there is no selected mineral
                                    if (!desire.returnFactValueForGivenKey(MINING_MINERAL).isPresent()) {
                                        return true;
                                    }

                                    //skip if reason about mineral to mine recently
                                    if (desire.returnFactValueForGivenKey(LAST_TIME_REASON_ABOUT_MINERAL_TO_MINE).orElse(0) + RANDOM.nextInt(10) + 1
                                            > desire.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).orElse(0)) {
                                        return false;
                                    }

                                    //selected mineral is not nearest unoccupied mineral
                                    Set<AUnit> mineralsBeingMined = desire.getReadOnlyMemories().stream()
                                            .filter(readOnlyMemory -> readOnlyMemory.getAgentId() != desire.getAgentId())
                                            .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(MINING_MINERAL))
                                            .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MINING_MINERAL))
                                            .filter(Optional::isPresent)
                                            .map(Optional::get)
                                            .collect(Collectors.toSet());
                                    Set<AUnit> mineralsToMine = desire.returnFactSetValueOfParentIntentionForGivenKey(MINERAL).get().stream()
                                            .filter(AUnit::isAlive)
                                            .filter(unit -> !mineralsBeingMined.contains(unit))
                                            .collect(Collectors.toSet());
                                    if (!mineralsToMine.isEmpty()) {

                                        //select free nearest mineral to closest hatchery and check if it is same as one currently mined
                                        APosition myPosition = desire.returnFactValueForGivenKey(IS_UNIT).get().getPosition();
                                        Optional<AUnit> hatchery = desire.returnFactSetValueOfParentIntentionForGivenKey(HAS_HATCHERY).get().stream()
                                                .min(Comparator.comparingDouble(o -> BWTA.getGroundDistance(myPosition.getATilePosition().getTilePosition(), o.getPosition().getATilePosition().getTilePosition())));
                                        if (hatchery.isPresent()) {
                                            Optional<AUnit> mineralToPick = mineralsToMine.stream()
                                                    .min(Comparator.comparingDouble(o -> BWTA.getGroundDistance(hatchery.get().getPosition().getATilePosition().getTilePosition(), o.getPosition().getATilePosition().getTilePosition())));
                                            //selected is not the nearest
                                            if (!mineralToPick.get().equals(desire.returnFactValueForGivenKey(MINING_MINERAL).get())) {
                                                return true;
                                            }
                                        }
                                    }
                                    return false;
                                }
                        )
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();
                type.addConfiguration(SELECT_MINERAL, MINE_MINERALS_IN_BASE, selectMineral);

                //go to mine it
                ConfigurationWithCommand.WithActingCommandDesiredBySelf miningConfiguration = ConfigurationWithCommand.
                        WithActingCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                AUnit mineral = intention.returnFactValueForGivenKeyInDesireParameters(MINERAL_TO_MINE).get();
                                boolean commandSent = intention.returnFactValueForGivenKey(IS_UNIT).get().gather(mineral);
                                if (commandSent) {
                                    memory.updateFact(MINING_MINERAL, mineral);
                                }
                                return commandSent;
                            }
                        })
                        //mineral is selected, agent does not carry mineral and mineral to mine differs from mineral which is being mined. do not start mining mineral if other agent has selected it
                        .decisionInDesire((desire, dataForDecision) -> desire.returnFactValueForGivenKeyInParameters(MINERAL_TO_MINE).isPresent()
                                && !desire.returnFactValueForGivenKey(IS_UNIT).get().isCarryingMinerals()
                                && !desire.returnFactValueForGivenKeyInParameters(MINERAL_TO_MINE).get().equals(desire.returnFactValueForGivenKey(MINING_MINERAL).orElse(null))
                                && !desire.getReadOnlyMemories().stream()
                                .filter(readOnlyMemory -> readOnlyMemory.getAgentId() != desire.getAgentId())
                                .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(MINING_MINERAL))
                                .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MINING_MINERAL))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .anyMatch(unit -> unit.equals(desire.returnFactValueForGivenKeyInParameters(MINERAL_TO_MINE).get()))
                        )
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();
                type.addConfiguration(MINE_MINERALS, MINE_MINERALS_IN_BASE, miningConfiguration);

            })
            .build();

}
