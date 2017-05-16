package cz.jan.maly.model;

import bwapi.Order;
import bwapi.UnitCommandType;
import cz.jan.maly.model.agent.types.AgentTypeUnit;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.bot.FactKeys;
import cz.jan.maly.model.game.wrappers.*;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithAbstractPlan;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.service.implementation.BotFacade;
import cz.jan.maly.utils.Util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.agent.types.AgentTypeUnit.*;
import static cz.jan.maly.model.bot.FactConverters.*;
import static cz.jan.maly.model.bot.FactKeys.*;
import static cz.jan.maly.model.bot.FactKeys.IS_BASE;
import static cz.jan.maly.model.bot.FactKeys.IS_BEING_CONSTRUCT;
import static cz.jan.maly.model.bot.FactKeys.IS_ISLAND;
import static cz.jan.maly.model.bot.FactKeys.IS_START_LOCATION;
import static cz.jan.maly.model.bot.FactKeys.LAST_TIME_SCOUTED;

/**
 * Created by Jan on 15-Mar-17.
 */
public class AgentsUnitTypes {

    //BUILDINGS
    public static final AgentTypeUnit HATCHERY = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.HATCHERY)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit SPAWNING_POOL = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.SPAWNING_POOL)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit EXTRACTOR = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.EXTRACTOR)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit LAIR = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.LAIR)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit SPIRE = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.SPIRE)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit EVOLUTION_CHAMBER = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.EVOLUTION_CHAMBER)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit HYDRALISK_DEN = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.HYDRALISK_DEN)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit SUNKEN_COLONY = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.SUNKEN_COLONY)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit CREEP_COLONY = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.CREEP_COLONY)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit SPORE_COLONY = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.SPORE_COLONY)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    //BUILDINGS

    //UNITS
    public static final AgentTypeUnit ZERGLING = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.ZERGLING)
            .initializationStrategy(type -> {
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);
            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(SURROUNDING_UNITS_AND_LOCATION)))
            .build();
    public static final AgentTypeUnit MUTALISK = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.MUTALISK)
            .initializationStrategy(type -> {
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);
            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(SURROUNDING_UNITS_AND_LOCATION)))
            .build();
    public static final AgentTypeUnit HYDRALISK = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.HYDRALISK)
            .initializationStrategy(type -> {
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);
            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(SURROUNDING_UNITS_AND_LOCATION)))
            .build();

    //worker
    public static final AgentTypeUnit DRONE = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.DRONE)
            .usingTypesForFacts(new HashSet<>(Arrays.asList(MINING_MINERAL, MINERAL_TO_MINE, IS_MORPHING_TO,
                    IS_GATHERING_MINERALS, IS_GATHERING_GAS, BASE_TO_SCOUT_BY_WORKER, PLACE_TO_GO, PLACE_FOR_POOL,
                    PLACE_FOR_EXPANSION, BASE_TO_MOVE, PLACE_FOR_EXTRACTOR, MINING_IN_EXTRACTOR, BUILDING_LAST_CHECK)))
            .initializationStrategy(type -> {

                //mine gas
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent mineGas = ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent.builder()
                        .reactionOnChangeStrategy((memory, desireParameters) -> memory.updateFact(MINING_IN_EXTRACTOR, desireParameters.returnFactSetValueForGivenKey(HAS_EXTRACTOR).get().findAny().get()))
                        .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.eraseFactValueForGivenKey(MINING_IN_EXTRACTOR))
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {
                                            AUnitOfPlayer extractor = dataForDecision.returnFactSetValueForGivenKey(HAS_EXTRACTOR).get().findAny().get();
                                            return !dataForDecision.madeDecisionToAny() && memory.getReadOnlyMemoriesForAgentType(AgentTypes.DRONE)
                                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MINING_IN_EXTRACTOR))
                                                    .filter(Optional::isPresent)
                                                    .map(Optional::get)
                                                    .filter(aUnitOfPlayer -> aUnitOfPlayer.equals(extractor))
                                                    .count() < 3;
                                        }
                                )
                                .useFactsInMemory(true)
                                .desiresToConsider(new HashSet<>(Arrays.asList(WORKER_SCOUT, MORPH_TO_POOL, EXPAND,
                                        MINE_MINERALS_IN_BASE, MINE_GAS_IN_BASE, MORPH_TO_EXTRACTOR)))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {
                                            AUnitOfPlayer extractor = dataForDecision.returnFactSetValueForGivenKey(HAS_EXTRACTOR).get().findAny().get();
                                            return dataForDecision.madeDecisionToAny() || memory.getReadOnlyMemoriesForAgentType(AgentTypes.DRONE)
                                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MINING_IN_EXTRACTOR))
                                                    .filter(Optional::isPresent)
                                                    .map(Optional::get)
                                                    .filter(aUnitOfPlayer -> aUnitOfPlayer.equals(extractor))
                                                    .count() > 3;
                                        }
                                )
                                .desiresToConsider(new HashSet<>(Arrays.asList(WORKER_SCOUT, MORPH_TO_POOL, EXPAND,
                                        MINE_MINERALS_IN_BASE, MORPH_TO_EXTRACTOR)))
                                .useFactsInMemory(true)
                                .build())
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                if (intention.returnFactValueForGivenKey(MINING_IN_EXTRACTOR).isPresent()) {
                                    return intention.returnFactValueForGivenKey(IS_UNIT).get().gather(intention.returnFactValueForGivenKey(MINING_IN_EXTRACTOR).get());
                                }
                                return true;
                            }
                        })
                        .build();
                type.addConfiguration(MINE_GAS_IN_BASE, mineGas);

                //go scouting, at least 5 drones and starting locations still not visited. Select one not visited
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent goScouting = ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent.builder()
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(BASE_TO_SCOUT_BY_WORKER).isPresent()
                                        || memory.returnFactValueForGivenKey(IS_UNIT).get().getPosition().distanceTo(memory.returnFactValueForGivenKey(BASE_TO_SCOUT_BY_WORKER).get()) < 5)
                                .useFactsInMemory(true)
                                .build())
                        .reactionOnChangeStrategy((memory, desireParameters) -> {
                            Optional<ABaseLocationWrapper> baseToScout = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                    .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(LAST_TIME_SCOUTED).isPresent())
                                    .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(IS_ISLAND).get())
                                    .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_START_LOCATION).get())
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION))
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .findAny();
                            baseToScout.ifPresent(aBaseLocationWrapper -> memory.updateFact(BASE_TO_SCOUT_BY_WORKER, aBaseLocationWrapper));
                        })
                        .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.eraseFactValueForGivenKey(BASE_TO_SCOUT_BY_WORKER))
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                if (intention.returnFactValueForGivenKey(BASE_TO_SCOUT_BY_WORKER).isPresent()) {
                                    return intention.returnFactValueForGivenKey(IS_UNIT).get().move(intention.returnFactValueForGivenKey(BASE_TO_SCOUT_BY_WORKER).get());
                                }
                                return true;
                            }
                        })
                        .build();
                type.addConfiguration(WORKER_SCOUT, goScouting);

                //reason about activities related to worker
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf workerConcerns = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                AUnitOfPlayer me = memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get();
                                memory.updateFact(IS_GATHERING_MINERALS, me.isCarryingMinerals() || me.isGatheringMinerals()
                                        || (me.getOrder().isPresent() && (me.getOrder().get().equals(Order.MiningMinerals)
                                        || me.getOrder().get().equals(Order.MoveToMinerals) || me.getOrder().get().equals(Order.WaitForMinerals)
                                        || me.getOrder().get().equals(Order.ReturnMinerals))));
                                memory.updateFact(IS_GATHERING_GAS, me.isCarryingGas() || me.isGatheringGas()
                                        || (me.getOrder().isPresent() && (me.getOrder().get().equals(Order.HarvestGas)
                                        || me.getOrder().get().equals(Order.MoveToGas) || me.getOrder().get().equals(Order.WaitForGas)
                                        || me.getOrder().get().equals(Order.ReturnGas))));
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, workingMemory) -> true)
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, workingMemory) -> true)
                                .build())
                        .build();
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_WORKER_ACTIVITIES, workerConcerns);

                //reason about morphing
                type.addConfiguration(MORPHING_TO, beliefsAboutMorphing);

                //abstract plan to mine minerals in base
                ConfigurationWithAbstractPlan mineInBase = ConfigurationWithAbstractPlan.builder()
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {
                                    if (!dataForDecision.madeDecisionToAny()
                                            //is in same base
                                            && dataForDecision.returnFactValueForGivenKey(IS_BASE_LOCATION).get().equals(memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getNearestBaseLocation().orElse(null))
                                            && dataForDecision.getNumberOfCommittedAgents() <= 1.5 * dataForDecision.getFeatureValueDesireBeliefSets(COUNT_OF_MINERALS_ON_BASE)) {
                                        return true;
                                    }
                                    return false;
                                })
                                .useFactsInMemory(true)
                                .parameterValueSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                                .desiresToConsider(new HashSet<>(Arrays.asList(WORKER_SCOUT, MORPH_TO_POOL, EXPAND,
                                        MINE_MINERALS_IN_BASE, MINE_GAS_IN_BASE, MORPH_TO_EXTRACTOR)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {
                                    if (dataForDecision.madeDecisionToAny() ||
                                            dataForDecision.getNumberOfCommittedAgents() >= 1.5 * dataForDecision.getFeatureValueDesireBeliefSets(COUNT_OF_MINERALS_ON_BASE)) {
                                        return true;
                                    }
                                    return false;
                                })
                                .parameterValueSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                                .desiresToConsider(new HashSet<>(Arrays.asList(WORKER_SCOUT, MORPH_TO_POOL, EXPAND,
                                        MINE_GAS_IN_BASE, MORPH_TO_EXTRACTOR)))
                                .build()
                        )
                        .desiresWithIntentionToAct(new HashSet<>(Collections.singletonList(MINE_MINERALS)))
                        .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(SELECT_MINERAL, UNSELECT_MINERAL)))
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
                                    Optional<AUnit> mineralToPick = mineralsToMine.stream()
                                            .min(Comparator.comparingDouble(o -> myPosition.distanceTo(o.getPosition())));
                                    mineralToPick.ifPresent(aUnit -> {
                                        memory.updateFact(MINERAL_TO_MINE, aUnit);

                                        //release currently mined mineral if it differs with mineral about to be mined
                                        if (intention.returnFactValueForGivenKey(MINING_MINERAL).isPresent()
                                                && !intention.returnFactValueForGivenKey(MINING_MINERAL).get().equals(aUnit)) {
                                            memory.updateFact(MINING_MINERAL, MINING_MINERAL.getInitValue());
                                        }
                                    });
                                }
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> (dataForDecision.getFeatureValueBeliefs(IS_MINING_MINERAL) == 0
                                        || dataForDecision.getFeatureValueBeliefs(IS_WAITING_ON_MINERAL) == 1)
                                        && dataForDecision.getFeatureValueBeliefs(IS_CARRYING_MINERAL) == 0
                                )
                                .beliefTypes(new HashSet<>(Arrays.asList(IS_MINING_MINERAL, IS_CARRYING_MINERAL, IS_WAITING_ON_MINERAL)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
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
                                memory.eraseFactValueForGivenKey(MINERAL_TO_MINE);
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(IS_MINING_MINERAL) == 1
                                        && dataForDecision.getFeatureValueBeliefs(IS_CARRYING_MINERAL) == 1)
                                .beliefTypes(new HashSet<>(Arrays.asList(IS_MINING_MINERAL, IS_CARRYING_MINERAL)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
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
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(HAS_SELECTED_MINERAL_TO_MINE) != 0
                                        && !memory.returnFactValueForGivenKey(MINERAL_TO_MINE).get().equals(memory.returnFactValueForGivenKey(MINING_MINERAL).orElse(null))
                                        && dataForDecision.getFeatureValueBeliefs(IS_CARRYING_MINERAL) == 0)
                                .beliefTypes(new HashSet<>(Arrays.asList(IS_MINING_MINERAL, IS_CARRYING_MINERAL, HAS_SELECTED_MINERAL_TO_MINE)))
                                .useFactsInMemory(true)
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .build();
                type.addConfiguration(MINE_MINERALS, MINE_MINERALS_IN_BASE, mine);

                //morphing
                type.addConfiguration(MORPHING_TO, beliefsAboutMorphing);

                //surrounding units and location belief update
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);

                //go to nearest own base with minerals when you have nothing to do
                ConfigurationWithCommand.WithActingCommandDesiredBySelf goToNearestBase = ConfigurationWithCommand.
                        WithActingCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                if (intention.returnFactValueForGivenKey(PLACE_TO_GO).isPresent()) {
                                    intention.returnFactValueForGivenKey(IS_UNIT).get().move(intention.returnFactValueForGivenKey(PLACE_TO_GO).get());
                                }
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) ->
                                        {
                                            AUnitOfPlayer me = memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get();

                                            //is in enemy base
                                            if (me.getNearestBaseLocation().isPresent()) {
                                                boolean isInEnemyBase = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(FactKeys.IS_ENEMY_BASE).get())
                                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION))
                                                        .filter(Optional::isPresent)
                                                        .map(Optional::get)
                                                        .anyMatch(aBaseLocationWrapper -> aBaseLocationWrapper.equals(me.getNearestBaseLocation().get()));
                                                //is in small distance
                                                if (isInEnemyBase) {
                                                    return me.getPosition().distanceTo(me.getNearestBaseLocation().get()) < 10;
                                                }
                                            }

                                            //todo is idle in base where are no minerals and gas to mine
                                            if (me.isIdle() && (me.getLastCommand().get().getUnitCommandType().equals(UnitCommandType.Move)
                                                    || me.getLastCommand().get().getUnitCommandType().equals(UnitCommandType.Build))) {
                                                return true;
                                            }

                                            return false;
                                        }
                                )
                                .useFactsInMemory(true)
                                .build()
                        )
                        .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.eraseFactValueForGivenKey(PLACE_TO_GO))
                        .reactionOnChangeStrategy((memory, desireParameters) -> {
                            AUnitOfPlayer me = memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get();
                            Optional<ABaseLocationWrapper> baseToGo = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                    .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE).get())
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION))
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .min(Comparator.comparingDouble(value -> me.getPosition().distanceTo(value)));
                            baseToGo.ifPresent(aBaseLocationWrapper -> memory.updateFact(PLACE_TO_GO, aBaseLocationWrapper.getPosition()));
                        })
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) ->
                                        !memory.returnFactValueForGivenKey(PLACE_TO_GO).isPresent()
                                                || (memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getNearestBaseLocation().isPresent()
                                                && memory.returnFactValueForGivenKey(PLACE_TO_GO).get().equals(memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getNearestBaseLocation().get().getPosition()))
                                )
                                .useFactsInMemory(true)
                                .build())
                        .build();
                type.addConfiguration(GO_TO_BASE, goToNearestBase);

                //build pool
                initAbstractBuildingPlan(type, AUnitTypeWrapper.SPAWNING_POOL_TYPE, PLACE_FOR_POOL, MORPH_TO_POOL,
                        MORPH_TO_POOL, FIND_PLACE_FOR_POOL, false, new HashSet<>(Arrays.asList(WORKER_SCOUT, EXPAND, MORPH_TO_EXTRACTOR)));

                //build expansion
                initAbstractBuildingPlan(type, AUnitTypeWrapper.HATCHERY_TYPE, PLACE_FOR_EXPANSION, EXPAND,
                        EXPAND, FIND_PLACE_FOR_HATCHERY, true, new HashSet<>(Arrays.asList(WORKER_SCOUT, MORPH_TO_POOL, MORPH_TO_EXTRACTOR)));

                //build extractor
                initAbstractBuildingPlan(type, AUnitTypeWrapper.EXTRACTOR_TYPE, PLACE_FOR_EXTRACTOR, MORPH_TO_EXTRACTOR,
                        MORPH_TO_EXTRACTOR, FIND_PLACE_FOR_EXTRACTOR, true, new HashSet<>(Arrays.asList(WORKER_SCOUT, EXPAND, MORPH_TO_POOL)));

            })
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(SURROUNDING_UNITS_AND_LOCATION,
                    UPDATE_BELIEFS_ABOUT_WORKER_ACTIVITIES, MORPHING_TO)))
            .desiresWithIntentionToAct(new HashSet<>(Collections.singletonList(GO_TO_BASE)))
            .build();

    /**
     * Init building plan
     *
     * @param type
     * @param typeOfBuilding
     * @param placeForBuilding
     * @param reactOn
     * @param buildCommand
     * @param findPlace
     */
    private static void initAbstractBuildingPlan(AgentType type, AUnitTypeWrapper typeOfBuilding,
                                                 FactKey<ATilePosition> placeForBuilding, DesireKey reactOn,
                                                 DesireKey buildCommand, DesireKey findPlace, boolean baseToMove, Set<DesireKey> desiresToConsider) {

        //abstract plan for building
        ConfigurationWithAbstractPlan buildPlan = ConfigurationWithAbstractPlan
                .builder()
                .reactionOnChangeStrategy((memory, desireParameters) -> {
                    if (baseToMove && desireParameters.returnFactValueForGivenKey(BASE_TO_MOVE).isPresent()) {
                        memory.updateFact(BASE_TO_MOVE, desireParameters.returnFactValueForGivenKey(BASE_TO_MOVE).get());
                    }
                    memory.updateFact(BUILDING_LAST_CHECK, memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).get());
                })
                .reactionOnChangeStrategyInIntention((memory, desireParameters) -> {
                    memory.eraseFactValueForGivenKey(placeForBuilding);
                    memory.eraseFactValueForGivenKey(BASE_TO_MOVE);
                    memory.eraseFactValueForGivenKey(BUILDING_LAST_CHECK);
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) ->
                                !dataForDecision.madeDecisionToAny() &&
                                        dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_MINERALS) >= (typeOfBuilding.getMineralPrice() - 0.1 * typeOfBuilding.getMineralPrice())
                                        && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_GAS) >= (typeOfBuilding.getGasPrice() - 0.1 * typeOfBuilding.getGasPrice())
                                        //is in our base
                                        && memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getNearestBaseLocation().isPresent() &&
                                        memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                                .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE).get())
                                                .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get())
                                                .anyMatch(aBaseLocationWrapper -> aBaseLocationWrapper.equals(memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getNearestBaseLocation().orElse(null)))
                        )
                        .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(COUNT_OF_MINERALS, COUNT_OF_GAS)))
                        .desiresToConsider(Stream.concat(desiresToConsider.stream(), Stream.of(reactOn)).collect(Collectors.toSet()))
                        .useFactsInMemory(true)
                        .build())
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(MADE_BUILDING_LAST_CHECK) + 300 < dataForDecision.getFeatureValueBeliefs(LAST_OBSERVATION)
                                || dataForDecision.madeDecisionToAny() || memory.returnFactValueForGivenKey(IS_MORPHING_TO).isPresent()
                                || (dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_MINERALS) < (typeOfBuilding.getMineralPrice() - 0.1 * typeOfBuilding.getMineralPrice())
                                || dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_GAS) < (typeOfBuilding.getGasPrice() - 0.1 * typeOfBuilding.getGasPrice()))
                        )
                        .useFactsInMemory(true)
                        .beliefTypes(new HashSet<>(Arrays.asList(MADE_BUILDING_LAST_CHECK, LAST_OBSERVATION)))
                        .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(COUNT_OF_MINERALS, COUNT_OF_GAS)))
                        .desiresToConsider(desiresToConsider)
                        .build())
                .desiresWithIntentionToReason(new HashSet<>(Collections.singleton(findPlace)))
                .desiresWithIntentionToAct(new HashSet<>(Arrays.asList(buildCommand, GO_TO_BASE)))
                .build();
        type.addConfiguration(reactOn, buildPlan, false);

        //move to place first (if present)
        ConfigurationWithCommand.WithActingCommandDesiredBySelf move = ConfigurationWithCommand.
                WithActingCommandDesiredBySelf.builder()
                .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                    @Override
                    public boolean act(WorkingMemory memory) {
                        if (intention.returnFactValueForGivenKey(BASE_TO_MOVE).isPresent()) {
                            memory.updateFact(BUILDING_LAST_CHECK, memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).get());
                            intention.returnFactValueForGivenKey(IS_UNIT).get().move(intention.returnFactValueForGivenKey(BASE_TO_MOVE).get());
                        }
                        return true;
                    }
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> baseToMove
                                && !(memory.returnFactValueForGivenKey(BASE_TO_MOVE).isPresent()
                                && memory.returnFactValueForGivenKey(BASE_TO_MOVE).get().distanceTo(memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getPosition()) < 10))
                        .useFactsInMemory(true)
                        .build()
                )
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> true)
                        .build())
                .build();
        type.addConfiguration(GO_TO_BASE, reactOn, move);


        //find place
        ConfigurationWithCommand.WithReasoningCommandDesiredBySelf findPlaceReasoning = ConfigurationWithCommand.WithReasoningCommandDesiredBySelf
                .builder()
                .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                    @Override
                    public boolean act(WorkingMemory memory) {
                        AUnitWithCommands me = intention.returnFactValueForGivenKey(IS_UNIT).get();
                        if (me.getNearestBaseLocation().isPresent()) {
                            BotFacade.ADDITIONAL_OBSERVATIONS_PROCESSOR.requestObservation((mem, environment) -> {
                                Optional<ATilePosition> place = Util.getBuildTile(typeOfBuilding, me.getNearestBaseLocation().get().getTilePosition(), me, environment);
                                place.ifPresent(aTilePosition -> memory.updateFact(placeForBuilding, aTilePosition));
                                return true;
                            }, memory, DRONE);
                        }
                        return true;
                    }
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> !baseToMove
                                || (memory.returnFactValueForGivenKey(BASE_TO_MOVE).isPresent()
                                && memory.returnFactValueForGivenKey(BASE_TO_MOVE).get().distanceTo(memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getPosition()) < 10))
                        .useFactsInMemory(true)
                        .build())
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> true)
                        .build())
                .build();
        type.addConfiguration(findPlace, reactOn, findPlaceReasoning);

        //morph to building
        ConfigurationWithCommand.WithActingCommandDesiredBySelf build = ConfigurationWithCommand.
                WithActingCommandDesiredBySelf.builder()
                .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                    @Override
                    public boolean act(WorkingMemory memory) {
                        if (intention.returnFactValueForGivenKey(placeForBuilding).isPresent()) {
                            intention.returnFactValueForGivenKey(IS_UNIT).get().build(typeOfBuilding, intention.returnFactValueForGivenKey(placeForBuilding).get());
                        }
                        return true;
                    }
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> memory.returnFactValueForGivenKey(placeForBuilding).isPresent())
                        .useFactsInMemory(true)
                        .build()
                )
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(placeForBuilding).isPresent()
                                || memory.returnFactValueForGivenKey(IS_MORPHING_TO).isPresent())
                        .useFactsInMemory(true)
                        .build())
                .build();
        type.addConfiguration(buildCommand, reactOn, build);
    }

    //population and scout
    public static final AgentTypeUnit OVERLORD = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.OVERLORD)
            .usingTypesForFacts(new HashSet<>(Collections.singletonList(IS_MORPHING_TO)))
            .initializationStrategy(type -> {
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);

                //todo - maneuver, when under attack. do abstract plan
                //use reasoning command with observation to find suitable position
                //move to position
                //scouting - move to base scouted for last time. at start prefer unvisited base locations
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent goScouting = ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent.builder()
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueMadeCommitmentToType(VISIT) != 1.0)
                                .desiresToConsider(new HashSet<>(Collections.singletonList(VISIT)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> false)
                                .build())
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().move(intention.returnFactValueForGivenKeyInDesireParameters(IS_BASE_LOCATION).get());
                            }
                        })
                        .build();
                type.addConfiguration(VISIT, goScouting);

                //reason about morphing
                type.addConfiguration(MORPHING_TO, beliefsAboutMorphing);
            })
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(SURROUNDING_UNITS_AND_LOCATION, MORPHING_TO)))
            .build();

    //"barracks"
    public static final AgentTypeUnit EGG = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.EGG)
            .usingTypesForFacts(new HashSet<>(Collections.singletonList(IS_MORPHING_TO)))
            .initializationStrategy(type -> {

                //reason about morphing
                type.addConfiguration(MORPHING_TO, beliefsAboutMorphing);

            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(MORPHING_TO)))
            .build();
    public static final AgentTypeUnit LARVA = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.LARVA)
            .usingTypesForFacts(new HashSet<>(Collections.singletonList(IS_MORPHING_TO)))
            .initializationStrategy(type -> {

                //morph to overlord
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent morphToOverlord = ConfigurationWithCommand.
                        WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().morph(intention.getDesireKey().returnFactValueForGivenKey(MORPH_TO).get().returnType());
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_MORPHING_OVERLORDS) == 0
                                        && dataForDecision.getFeatureValueGlobalBeliefs(CURRENT_POPULATION)
                                        >= dataForDecision.getFeatureValueGlobalBeliefs(MAX_POPULATION)
                                        && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_SUPPLY_BY_OVERLORDS) + dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_HATCHERIES)
                                        == (dataForDecision.getFeatureValueGlobalBeliefs(MAX_POPULATION))
                                )
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(CURRENT_POPULATION, MAX_POPULATION, COUNT_OF_SUPPLY_BY_OVERLORDS, COUNT_OF_HATCHERIES)))
                                .globalBeliefTypes(new HashSet<>(Collections.singleton(COUNT_OF_MORPHING_OVERLORDS)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .build();
                type.addConfiguration(MORPH_TO_OVERLORD, morphToOverlord);

                //morph to zergling
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent morphToZergling = ConfigurationWithCommand.
                        WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().morph(intention.getDesireKey().returnFactValueForGivenKey(MORPH_TO).get().returnType());
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .build();
                type.addConfiguration(BOOST_GROUND_MELEE, morphToZergling);

                //TODO give conditions - morph in base where it makes sense
                //morph
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent mine = ConfigurationWithCommand.
                        WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().morph(intention.getDesireKey().returnFactValueForGivenKey(MORPH_TO).get().returnType());
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> memory.getReadOnlyMemoriesForAgentType(AgentTypes.BUILDING_ORDER_MANAGER)
                                        .anyMatch(readOnlyMemory -> !readOnlyMemory.collectKeysOfCommittedDesiresInTreeCounts().containsKey(ENABLE_GROUND_MELEE))
                                        || memory.getReadOnlyMemoriesForAgentType(AgentTypes.DRONE)
                                        .anyMatch(readOnlyMemory -> !readOnlyMemory.collectKeysOfCommittedDesiresInTreeCounts().containsKey(MORPH_TO_POOL))
                                )
                                .useFactsInMemory(true)
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .build();
                type.addConfiguration(MORPH_TO_DRONE, mine);

                //reason about morphing
                type.addConfiguration(MORPHING_TO, beliefsAboutMorphing);

            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(MORPHING_TO)))
            .build();
    //UNITS
}
