package cz.jan.maly.model;

import cz.jan.maly.model.agent.types.AgentTypeUnit;
import cz.jan.maly.model.game.wrappers.APosition;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithAbstractPlan;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValue;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValueSet;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;

import java.util.*;
import java.util.stream.Collectors;

import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.FactsKeys.*;
import static cz.jan.maly.model.bot.BasicFactsKeys.IS_UNIT;
import static cz.jan.maly.model.bot.BasicFactsKeys.MINERAL;

/**
 * Created by Jan on 15-Mar-17.
 */
public class AgentsUnitTypes {

    public static final AgentTypeUnit HATCHERY = AgentTypeUnit.builder()
            .name("HATCHERY")
            .initializationStrategy(type -> {
            })
            .build();

    public static final AgentTypeUnit OVERLORD = AgentTypeUnit.builder()
            .name("OVERLORD")
            .initializationStrategy(type -> {
            })
            .build();

    public static final AgentTypeUnit ZERGLING = AgentTypeUnit.builder()
            .name("ZERGLING")
            .initializationStrategy(type -> {
            })
            .build();

    public static final AgentTypeUnit EGG = AgentTypeUnit.builder()
            .name("EGG")
            .initializationStrategy(type -> {
            })
            .build();

    public static final AgentTypeUnit SPAWNING_POOL = AgentTypeUnit.builder()
            .name("SPAWNING_POOL")
            .initializationStrategy(type -> {
            })
            .build();

    //morph
    public static final AgentTypeUnit LARVA = AgentTypeUnit.builder()
            .name("LARVA")
            .initializationStrategy(type -> {
            })
            .build();

    public static final AgentTypeUnit DRONE = AgentTypeUnit.builder()
            .name("DRONE")
            .usingTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{MINING_MINERAL, MINERAL_TO_MINE})))
            .initializationStrategy(type -> {

                //abstract plan to mine minerals in base
                ConfigurationWithAbstractPlan mineInBase = ConfigurationWithAbstractPlan.builder()
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> {
                                    if (dataForDecision.getNumberOfCommittedAgents() < dataForDecision.getFeatureValueDesireBeliefSets(MINERAL)) {
                                        return true;
                                    }
                                    return false;
                                })
                                .parameterValueSetTypes(new HashSet<>(Arrays.asList(new FactWithOptionalValueSet<?>[]{
                                        new FactWithOptionalValueSet<>(MINERAL, aUnitStream -> aUnitStream.map(aUnitStream1 -> (double) aUnitStream1.count()).orElse(0.0))
                                })))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> {
                                    if (dataForDecision.getNumberOfCommittedAgents() > dataForDecision.getFeatureValueDesireBeliefSets(MINERAL)) {
                                        return true;
                                    }
                                    return false;
                                })
                                .parameterValueSetTypes(new HashSet<>(Arrays.asList(new FactWithOptionalValueSet<?>[]{
                                        new FactWithOptionalValueSet<>(MINERAL, aUnitStream -> aUnitStream.map(aUnitStream1 -> (double) aUnitStream1.count()).orElse(0.0))
                                })))
                                .build()
                        )
                        .desiresWithIntentionToAct(new HashSet<>(Arrays.asList(new DesireKey[]{MINE_MINERALS})))
                        .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(new DesireKey[]{SELECT_MINERAL, UNSELECT_MINERAL})))
                        .build();
                type.addConfiguration(MINE_MINERALS_IN_BASE, mineInBase, false);

                //select closest mineral to mine from set of available
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf selectMineral = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                Set<AUnit> mineralsBeingMined = memory.getReadOnlyMemoriesForAgentType(DRONE)
                                        .filter(readOnlyMemory -> readOnlyMemory.getAgentId() != memory.getAgentId())
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MINING_MINERAL))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .collect(Collectors.toSet());
                                Set<AUnit> mineralsToMine = intention.returnFactSetValueOfParentIntentionForGivenKey(MINERAL).get()
                                        .filter(unit -> !mineralsBeingMined.contains(unit))
                                        .collect(Collectors.toSet());
                                if (!mineralsToMine.isEmpty()) {

                                    //select free nearest mineral to closest hatchery
                                    APosition myPosition = intention.returnFactValueForGivenKey(IS_UNIT).get().getPosition();
                                    Optional<AUnit> hatchery = intention.returnFactSetValueOfParentIntentionForGivenKey(HAS_HATCHERY).get()
                                            .min(Comparator.comparingDouble(o -> myPosition.distanceTo(o.getPosition())));
                                    if (hatchery.isPresent()) {
                                        Optional<AUnit> mineralToPick = mineralsToMine.stream()
                                                .min(Comparator.comparingDouble(o -> hatchery.get().getPosition().distanceTo(o.getPosition())));
                                        mineralToPick.ifPresent(aUnit -> {
                                            memory.updateFact(MINERAL_TO_MINE, aUnit);

                                            //release currently mined mineral if it differs with mineral about to be mined
                                            if (intention.returnFactValueForGivenKey(MINING_MINERAL).isPresent()
                                                    && !intention.returnFactValueForGivenKey(MINING_MINERAL).get().equals(MINERAL_TO_MINE)) {
                                                memory.updateFact(MINING_MINERAL, MINING_MINERAL.getInitValue());
                                            }
                                        });
                                    }
                                }
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> dataForDecision.getFeatureValueBeliefs(MINING_MINERAL) == 0
                                        && dataForDecision.getFeatureValueBeliefs(IS_UNIT) == 0)
                                .beliefTypes(new HashSet<>(Arrays.asList(new FactWithOptionalValue<?>[]{
                                        new FactWithOptionalValue<>(MINING_MINERAL, aUnit -> {
                                            if (aUnit.isPresent()) {
                                                return 1;
                                            }
                                            return 0;
                                        }),
                                        new FactWithOptionalValue<>(IS_UNIT, aUnit -> {
                                            if (aUnit.isPresent()) {
                                                if (aUnit.get().isCarryingMinerals()) {
                                                    return 1;
                                                }
                                            }
                                            return 0;
                                        }),
                                })))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> true)
                                .build()
                        )
                        .build();
                type.addConfiguration(SELECT_MINERAL, MINE_MINERALS_IN_BASE, selectMineral);

                //remove occupancy of mineral when carrying it
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf unselectMineral = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                memory.eraseFactValueForGivenKey(MINING_MINERAL);
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> dataForDecision.getFeatureValueBeliefs(MINING_MINERAL) == 1
                                        && dataForDecision.getFeatureValueBeliefs(IS_UNIT) == 1)
                                .beliefTypes(new HashSet<>(Arrays.asList(new FactWithOptionalValue<?>[]{
                                        new FactWithOptionalValue<>(MINING_MINERAL, aUnit -> {
                                            if (aUnit.isPresent()) {
                                                return 1;
                                            }
                                            return 0;
                                        }),
                                        new FactWithOptionalValue<>(IS_UNIT, aUnit -> {
                                            if (aUnit.isPresent()) {
                                                if (aUnit.get().isCarryingMinerals()) {
                                                    return 1;
                                                }
                                            }
                                            return 0;
                                        }),
                                })))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> true)
                                .build()
                        )
                        .build();
                type.addConfiguration(UNSELECT_MINERAL, MINE_MINERALS_IN_BASE, unselectMineral);

                //go to mine it
                ConfigurationWithCommand.WithActingCommandDesiredBySelf mine = ConfigurationWithCommand.
                        WithActingCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                boolean hasStartedMining = intention.returnFactValueForGivenKey(IS_UNIT).get().gather(intention.returnFactValueForGivenKeyInDesireParameters(MINERAL_TO_MINE).get());
                                if (hasStartedMining) {
                                    memory.updateFact(MINING_MINERAL, intention.returnFactValueForGivenKey(MINERAL_TO_MINE).get());
                                }
                                return hasStartedMining;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> dataForDecision.getFeatureValueBeliefs(MINERAL_TO_MINE) != 0
                                        && dataForDecision.getFeatureValueBeliefs(MINING_MINERAL) == 0
                                        && dataForDecision.getFeatureValueBeliefs(IS_UNIT) == 0)
                                .beliefTypes(new HashSet<>(Arrays.asList(new FactWithOptionalValue<?>[]{
                                        new FactWithOptionalValue<>(MINING_MINERAL, aUnit -> {
                                            if (aUnit.isPresent()) {
                                                return 1;
                                            }
                                            return 0;
                                        }),
                                        new FactWithOptionalValue<>(MINERAL_TO_MINE, aUnit -> {
                                            if (aUnit.isPresent()) {
                                                return 1;
                                            }
                                            return 0;
                                        }),
                                        new FactWithOptionalValue<>(IS_UNIT, aUnit -> {
                                            if (aUnit.isPresent()) {
                                                if (aUnit.get().isCarryingMinerals()) {
                                                    return 1;
                                                }
                                            }
                                            return 0;
                                        }),
                                })))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> true)
                                .build())
                        .build();
                type.addConfiguration(MINE_MINERALS, MINE_MINERALS_IN_BASE, mine);
            })
            .build();

}
