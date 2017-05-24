package cz.jan.maly.service.implementation;

import bwta.BaseLocation;
import cz.jan.maly.model.Decider;
import cz.jan.maly.model.UnitTypeStatus;
import cz.jan.maly.model.agent.AgentBaseLocation;
import cz.jan.maly.model.agent.types.AgentTypeBaseLocation;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.bot.DesireKeys;
import cz.jan.maly.model.bot.FactConverters;
import cz.jan.maly.model.game.wrappers.*;
import cz.jan.maly.model.knowledge.ReadOnlyMemory;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithAbstractPlan;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithSharedDesire;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.service.LocationInitializer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.bot.AgentTypes.*;
import static cz.jan.maly.model.bot.FactConverters.*;
import static cz.jan.maly.model.bot.FactKeys.*;
import static cz.jan.maly.model.bot.FactKeys.IS_BASE;
import static cz.jan.maly.model.bot.FactKeys.IS_ENEMY_BASE;
import static cz.jan.maly.model.bot.FactKeys.LAST_TIME_SCOUTED;
import static cz.jan.maly.model.bot.FeatureContainerHeaders.DEFENSE;
import static cz.jan.maly.model.bot.FeatureContainerHeaders.HOLDING;

/**
 * Strategy to initialize player
 * Created by Jan on 05-Apr-17.
 */
public class LocationInitializerImpl implements LocationInitializer {

    public static final AgentTypeBaseLocation BASE_LOCATION = AgentTypeBaseLocation.builder()
            .initializationStrategy(type -> {

                //reason about last visit
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf reasonAboutVisit = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();
                                OptionalInt frameWhenLastVisited = UnitWrapperFactory.getStreamOfAllAlivePlayersUnits()
                                        .filter(aUnitOfPlayer -> {
                                            APosition aPosition = aUnitOfPlayer.getPosition();
                                            return aPosition.distanceTo(base) < 5;
                                        })
                                        .mapToInt(AUnit::getFrameCount)
                                        .max();
                                frameWhenLastVisited.ifPresent(integer -> memory.updateFact(LAST_TIME_SCOUTED, integer));

                                //base status
                                boolean isBase = memory.returnFactSetValueForGivenKey(HAS_BASE).map(Stream::count).orElse(0L) > 0;
                                memory.updateFact(IS_BASE, isBase);

                                //todo it is not our base and (it has enemy buildings on it || we haven't visited all base locations and this one is unvisited base location)
                                memory.updateFact(IS_ENEMY_BASE, !isBase
                                        && memory.returnFactSetValueForGivenKey(ENEMY_BUILDING).map(Stream::count).orElse(0L) > 0);
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .build();
                type.addConfiguration(VISIT, reasonAboutVisit);

                //tell system to visit me
                ConfigurationWithSharedDesire visitMe = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(VISIT)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {

                                    //do not visit our base location
                                    if (dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 1.0) {
                                        return false;
                                    }

                                    //if everything is visited desire visit
                                    if (dataForDecision.getFeatureValueGlobalBeliefs(FactConverters.COUNT_OF_VISITED_BASES)
                                            == dataForDecision.getFeatureValueGlobalBeliefs(FactConverters.AVAILABLE_BASES)) {
                                        return true;
                                    }

                                    //visit bases first
                                    long countOfUnvisitedStartingPositions = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                            .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(IS_BASE))
                                            .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(IS_BASE).get())
                                            .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get().isStartLocation())
                                            .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(LAST_TIME_SCOUTED))
                                            .filter(integer -> !integer.isPresent())
                                            .count();

                                    //visit bases first
                                    if (countOfUnvisitedStartingPositions > 0) {
                                        return memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get().isStartLocation();
                                    }

                                    //all starting positions have been visit so desire to visit everything from now on
                                    return true;
                                })
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(FactConverters.COUNT_OF_VISITED_BASES, FactConverters.AVAILABLE_BASES)))
                                .beliefTypes(new HashSet<>(Collections.singleton(FactConverters.IS_BASE)))
                                .useFactsInMemory(true)
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {

                                    //do not visit our base location
                                    if (dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 1.0) {
                                        return true;
                                    }

                                    //stay if it is enemy base
                                    if (dataForDecision.getFeatureValueBeliefs(FactConverters.IS_ENEMY_BASE) == 1.0) {
                                        return false;
                                    }

                                    //not visit anything
                                    if (dataForDecision.getFeatureValueDesireBeliefs(FactConverters.LAST_TIME_SCOUTED) == -1
                                            && dataForDecision.getFeatureValueBeliefs(FactConverters.LAST_TIME_SCOUTED) == -1) {
                                        return false;
                                    }

                                    ///we made first visit
                                    if (dataForDecision.getFeatureValueDesireBeliefs(FactConverters.LAST_TIME_SCOUTED) == -1
                                            && dataForDecision.getFeatureValueBeliefs(FactConverters.LAST_TIME_SCOUTED) > 0) {
                                        return true;
                                    }

                                    //new visit
                                    return (dataForDecision.getFeatureValueDesireBeliefs(FactConverters.LAST_TIME_SCOUTED) < dataForDecision.getFeatureValueBeliefs(FactConverters.LAST_TIME_SCOUTED));
                                })
                                .beliefTypes(new HashSet<>(Arrays.asList(FactConverters.IS_BASE, FactConverters.LAST_TIME_SCOUTED, FactConverters.IS_ENEMY_BASE)))
                                .parameterValueTypes(new HashSet<>(Collections.singleton(FactConverters.LAST_TIME_SCOUTED)))
                                .build())
                        .counts(1)
                        .build();
                type.addConfiguration(VISIT, visitMe);

                //enemy's units
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf enemyUnits = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();

                                Set<AUnit.Enemy> enemies = UnitWrapperFactory.getStreamOfAllAliveEnemyUnits().filter(enemy -> {
                                    Optional<ABaseLocationWrapper> bL = enemy.getNearestBaseLocation();
                                    return bL.isPresent() && bL.get().equals(base);
                                }).collect(Collectors.toSet());

                                memory.updateFactSetByFacts(ENEMY_UNIT, enemies);
                                memory.updateFactSetByFacts(ENEMY_BUILDING, enemies.stream().filter(enemy -> enemy.getType().isBuilding()).collect(Collectors.toSet()));
                                memory.updateFactSetByFacts(ENEMY_GROUND, enemies.stream().filter(enemy -> !enemy.getType().isBuilding() && !enemy.getType().isFlyer()).collect(Collectors.toSet()));
                                memory.updateFactSetByFacts(ENEMY_AIR, enemies.stream().filter(enemy -> !enemy.getType().isBuilding() && enemy.getType().isFlyer()).collect(Collectors.toSet()));

                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {
                                    ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();
                                    return UnitWrapperFactory.getStreamOfAllAliveEnemyUnits().filter(enemy -> {
                                        Optional<ABaseLocationWrapper> bL = enemy.getNearestBaseLocation();
                                        return bL.isPresent() && bL.get().equals(base);
                                    }).count() > 0;
                                })
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .build();
                type.addConfiguration(ENEMIES_IN_LOCATION, enemyUnits);

                //player's units
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf ourUnits = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();

                                Set<AUnitOfPlayer> playersUnits = UnitWrapperFactory.getStreamOfAllAlivePlayersUnits().filter(enemy -> {
                                    Optional<ABaseLocationWrapper> bL = enemy.getNearestBaseLocation();
                                    return bL.isPresent() && bL.get().equals(base);
                                }).collect(Collectors.toSet());

                                memory.updateFactSetByFacts(OUR_UNIT, playersUnits);
                                memory.updateFactSetByFacts(OWN_BUILDING, playersUnits.stream().filter(own -> own.getType().isBuilding()).collect(Collectors.toSet()));
                                memory.updateFactSetByFacts(OWN_GROUND, playersUnits.stream().filter(own -> !own.getType().isBuilding() && !own.getType().isFlyer()).collect(Collectors.toSet()));
                                memory.updateFactSetByFacts(OWN_AIR, playersUnits.stream().filter(own -> !own.getType().isBuilding() && own.getType().isFlyer()).collect(Collectors.toSet()));

                                //find new static defense buildings
                                if (memory.returnFactSetValueForGivenKey(OWN_BUILDING)
                                        .orElse(Stream.empty())
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().isMilitaryBuilding())
                                        .count() > 0) {
                                    memory.updateFactSetByFacts(STATIC_DEFENSE, memory.returnFactSetValueForGivenKey(OWN_BUILDING).orElse(Stream.empty())
                                            .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().isMilitaryBuilding())
                                            .collect(Collectors.toSet()));
                                }

                                //eco buildings
                                memory.updateFactSetByFacts(HAS_BASE, Stream.concat(memory.getReadOnlyMemoriesForAgentType(HATCHERY)
                                                .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get())
                                                .filter(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().isPresent())
                                                .filter(aUnitOfPlayer -> base.equals(aUnitOfPlayer.getNearestBaseLocation().orElse(null))),
                                        memory.getReadOnlyMemoriesForAgentType(LAIR)
                                                .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get())
                                                .filter(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().isPresent())
                                                .filter(aUnitOfPlayer -> base.equals(aUnitOfPlayer.getNearestBaseLocation().orElse(null)))
                                ).collect(Collectors.toSet()));
                                memory.updateFactSetByFacts(HAS_EXTRACTOR, memory.returnFactSetValueForGivenKey(OWN_BUILDING).orElse(Stream.empty())
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().isGasBuilding())
                                        .collect(Collectors.toSet()));


                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {
                                    ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();
                                    return UnitWrapperFactory.getStreamOfAllAlivePlayersUnits().filter(enemy -> {
                                        Optional<ABaseLocationWrapper> bL = enemy.getNearestBaseLocation();
                                        return bL.isPresent() && bL.get().equals(base);
                                    }).count() > 0;
                                })
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .build();
                type.addConfiguration(FRIENDLIES_IN_LOCATION, ourUnits);

                //estimate enemy force
                type.addConfiguration(ESTIMATE_ENEMY_FORCE_IN_LOCATION, formForceEstimator(ENEMY_UNIT, ENEMY_BUILDING_STATUS,
                        ENEMY_STATIC_AIR_FORCE_STATUS, ENEMY_STATIC_GROUND_FORCE_STATUS, ENEMY_AIR_FORCE_STATUS, ENEMY_GROUND_FORCE_STATUS));

                //estimate our force
                type.addConfiguration(ESTIMATE_OUR_FORCE_IN_LOCATION, formForceEstimator(OUR_UNIT, OWN_BUILDING_STATUS,
                        OWN_STATIC_AIR_FORCE_STATUS, OWN_STATIC_GROUND_FORCE_STATUS, OWN_AIR_FORCE_STATUS, OWN_GROUND_FORCE_STATUS));

                //eco concerns
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf ecoConcerns = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();

                                //workers
                                Set<ReadOnlyMemory> workersAroundBase = memory.getReadOnlyMemories()
                                        .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(LOCATION) &&
                                                readOnlyMemory.returnFactValueForGivenKey(LOCATION).isPresent()
                                                && readOnlyMemory.returnFactValueForGivenKey(LOCATION).get().equals(base))
                                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getType().isWorker()
                                                || (!readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getTrainingQueue().isEmpty()
                                                && readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getTrainingQueue().get(0).isWorker()))
                                        .collect(Collectors.toSet());

                                memory.updateFactSetByFacts(WORKER_ON_BASE, workersAroundBase.stream()
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get())
                                        .collect(Collectors.toSet()));
                                workersAroundBase = workersAroundBase.stream()
                                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getType().isWorker())
                                        .collect(Collectors.toSet());
                                memory.updateFactSetByFacts(WORKER_MINING_MINERALS, workersAroundBase.stream()
                                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_GATHERING_MINERALS).get())
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get())
                                        .collect(Collectors.toSet()));
                                memory.updateFactSetByFacts(WORKER_MINING_GAS, workersAroundBase.stream()
                                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_GATHERING_GAS).get())
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get())
                                        .collect(Collectors.toSet()));

                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 1.0)
                                .beliefTypes(new HashSet<>(Collections.singleton(FactConverters.IS_BASE)))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .build();
                type.addConfiguration(ECO_STATUS_IN_LOCATION, ecoConcerns);

                //Make request to start mining. Remove request when there are no more minerals to mine or there is no hatchery to bring mineral in
                ConfigurationWithSharedDesire mineMinerals = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MINE_MINERALS_IN_BASE)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 1
                                        && dataForDecision.getFeatureValueDesireBeliefSets(COUNT_OF_MINERALS_ON_BASE) > 0)
                                .beliefTypes(new HashSet<>(Collections.singletonList(FactConverters.IS_BASE)))
                                .parameterValueSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 0
                                        || dataForDecision.getFeatureValueBeliefSets(COUNT_OF_MINERALS_ON_BASE) == 0
                                        || dataForDecision.getFeatureValueBeliefSets(COUNT_OF_MINERALS_ON_BASE) != dataForDecision.getFeatureValueDesireBeliefSets(COUNT_OF_MINERALS_ON_BASE)
                                        || (dataForDecision.madeDecisionToAny() && memory.returnFactSetValueForGivenKey(WORKER_MINING_GAS).orElse(Stream.empty()).count() == 0)
                                )
                                .beliefTypes(new HashSet<>(Collections.singletonList(FactConverters.IS_BASE)))
                                .beliefSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                                .parameterValueSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                                .desiresToConsider(new HashSet<>(Collections.singleton(MINE_GAS_IN_BASE)))
                                .build()
                        )
                        .build();
                type.addConfiguration(MINE_MINERALS_IN_BASE, mineMinerals);

                //Make request to start mining gas. Remove request when there are no extractors or there is no hatchery to bring mineral in
                ConfigurationWithSharedDesire mineGas = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MINE_GAS_IN_BASE)
                        .counts(2)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(CAN_TRANSIT_FROM_5_POOL) != 0
                                        && dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 1
                                        && dataForDecision.getFeatureValueDesireBeliefSets(COUNT_OF_EXTRACTORS_ON_BASE) > 0
                                        && memory.returnFactSetValueForGivenKey(OWN_BUILDING).orElse(Stream.empty())
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().isGasBuilding())
                                        .anyMatch(aUnitOfPlayer -> !aUnitOfPlayer.isBeingConstructed() && !aUnitOfPlayer.isMorphing()))
                                .beliefTypes(new HashSet<>(Collections.singletonList(FactConverters.IS_BASE)))
                                .globalBeliefTypes(new HashSet<>(Collections.singletonList(CAN_TRANSIT_FROM_5_POOL)))
                                .parameterValueSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_EXTRACTORS_ON_BASE)))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 0
                                        || dataForDecision.getFeatureValueBeliefSets(COUNT_OF_EXTRACTORS_ON_BASE) == 0
                                )
                                .beliefTypes(new HashSet<>(Collections.singletonList(FactConverters.IS_BASE)))
                                .beliefSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_EXTRACTORS_ON_BASE)))
                                .build()
                        )
                        .build();
                type.addConfiguration(MINE_GAS_IN_BASE, mineGas);

                //build creep colony
                ConfigurationWithSharedDesire buildCreepColony = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MORPH_TO_CREEP_COLONY)
                        .counts(1)
                        .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.updateFact(LAST_CREEP_COLONY_BUILDING_TIME,
                                memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).orElse(null)))
                        .reactionOnChangeStrategy((memory, desireParameters) -> {
                            long countOfCreepColonies = memory.returnFactSetValueForGivenKey(STATIC_DEFENSE).orElse(Stream.empty())
                                    .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().equals(AUnitTypeWrapper.CREEP_COLONY_TYPE))
                                    .count()
                                    //workers building creep colony
                                    + memory.returnFactSetValueForGivenKey(WORKER_ON_BASE).orElse(Stream.empty())
                                    .filter(AUnit::isMorphing)
                                    .filter(aUnitOfPlayer -> !aUnitOfPlayer.getTrainingQueue().isEmpty())
                                    .map(aUnitOfPlayer -> aUnitOfPlayer.getTrainingQueue().get(0))
                                    .filter(typeWrapper -> typeWrapper.equals(AUnitTypeWrapper.CREEP_COLONY_TYPE))
                                    .count();
                            memory.updateFact(CREEP_COLONY_COUNT, (int) countOfCreepColonies);
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(CAN_TRANSIT_FROM_5_POOL) != 0
                                        && dataForDecision.getFeatureValueBeliefSets(BASE_IS_COMPLETED) == 1.0
                                        && (memory.returnFactValueForGivenKey(LAST_CREEP_COLONY_BUILDING_TIME).orElse(0) + 100
                                        < memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).orElse(0))
                                        && (dataForDecision.getFeatureValueBeliefSets(COUNT_OF_CREEP_COLONIES_AT_BASE_IN_CONSTRUCTION)
                                        + dataForDecision.getFeatureValueBeliefSets(COUNT_OF_CREEP_COLONIES_AT_BASE)) == 0
                                        && !dataForDecision.madeDecisionToAny()
                                        && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS) > 0
                                        && Decider.getDecision(AgentTypes.BASE_LOCATION, DesireKeys.BUILD_CREEP_COLONY, dataForDecision, DEFENSE))
                                .globalBeliefTypes(Stream.concat(DEFENSE.getConvertersForFactsForGlobalBeliefs().stream(),
                                        Stream.of(CAN_TRANSIT_FROM_5_POOL)).collect(Collectors.toSet()))
                                .globalBeliefSetTypes(DEFENSE.getConvertersForFactSetsForGlobalBeliefs())
                                .globalBeliefTypesByAgentType(Stream.concat(DEFENSE.getConvertersForFactsForGlobalBeliefsByAgentType().stream(),
                                        Stream.of(COUNT_OF_POOLS)).collect(Collectors.toSet()))
                                .globalBeliefSetTypesByAgentType(DEFENSE.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .beliefTypes(DEFENSE.getConvertersForFacts())
                                .beliefSetTypes(Stream.concat(DEFENSE.getConvertersForFactSets().stream(),
                                        Stream.of(BASE_IS_COMPLETED, COUNT_OF_CREEP_COLONIES_AT_BASE_IN_CONSTRUCTION,
                                                COUNT_OF_CREEP_COLONIES_AT_BASE)).collect(Collectors.toSet()))
                                .desiresToConsider(new HashSet<>(Collections.singleton(BUILD_CREEP_COLONY)))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(IS_BASE).get()
                                        || memory.returnFactValueForGivenKey(CREEP_COLONY_COUNT).orElse(0) !=
                                        (dataForDecision.getFeatureValueBeliefSets(COUNT_OF_CREEP_COLONIES_AT_BASE_IN_CONSTRUCTION)
                                                + dataForDecision.getFeatureValueBeliefSets(COUNT_OF_CREEP_COLONIES_AT_BASE))
                                )
                                .beliefSetTypes(new HashSet<>(Arrays.asList(COUNT_OF_CREEP_COLONIES_AT_BASE_IN_CONSTRUCTION,
                                        COUNT_OF_CREEP_COLONIES_AT_BASE)))
                                .useFactsInMemory(true)
                                .build())
                        .build();
                type.addConfiguration(BUILD_CREEP_COLONY, buildCreepColony);

                //common plan to build creep colony for sunken/spore colony
                ConfigurationWithSharedDesire buildCreepColonyCommon = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MORPH_TO_CREEP_COLONY)
                        .counts(1)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> (dataForDecision.getFeatureValueBeliefSets(COUNT_OF_CREEP_COLONIES_AT_BASE_IN_CONSTRUCTION)
                                        + dataForDecision.getFeatureValueBeliefSets(COUNT_OF_CREEP_COLONIES_AT_BASE)) == 0
                                        && !dataForDecision.madeDecisionToAny())
                                .beliefSetTypes(Stream.of(BASE_IS_COMPLETED, COUNT_OF_CREEP_COLONIES_AT_BASE_IN_CONSTRUCTION,
                                        COUNT_OF_CREEP_COLONIES_AT_BASE).collect(Collectors.toSet()))
                                .desiresToConsider(new HashSet<>(Collections.singleton(BUILD_CREEP_COLONY)))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> (dataForDecision.getFeatureValueBeliefSets(COUNT_OF_CREEP_COLONIES_AT_BASE_IN_CONSTRUCTION)
                                        + dataForDecision.getFeatureValueBeliefSets(COUNT_OF_CREEP_COLONIES_AT_BASE)) > 0
                                )
                                .beliefSetTypes(new HashSet<>(Arrays.asList(COUNT_OF_CREEP_COLONIES_AT_BASE_IN_CONSTRUCTION,
                                        COUNT_OF_CREEP_COLONIES_AT_BASE)))
                                .useFactsInMemory(true)
                                .build())
                        .build();

                //build sunken as abstract plan
                ConfigurationWithAbstractPlan buildSunkenAbstract = ConfigurationWithAbstractPlan.builder()
                        .reactionOnChangeStrategy((memory, desireParameters) -> {
                            long countOfSunkenColonies = memory.returnFactSetValueForGivenKey(STATIC_DEFENSE).orElse(Stream.empty())
                                    .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().equals(AUnitTypeWrapper.SUNKEN_COLONY_TYPE))
                                    .count()
                                    //creep colonies morphing to sunken
                                    + memory.returnFactSetValueForGivenKey(STATIC_DEFENSE).orElse(Stream.empty())
                                    .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().equals(AUnitTypeWrapper.CREEP_COLONY_TYPE))
                                    .filter(aUnitOfPlayer -> !aUnitOfPlayer.getTrainingQueue().isEmpty())
                                    .map(aUnitOfPlayer -> aUnitOfPlayer.getTrainingQueue().get(0))
                                    .filter(typeWrapper -> typeWrapper.equals(AUnitTypeWrapper.SUNKEN_COLONY_TYPE))
                                    .count();
                            memory.updateFact(SUNKEN_COLONY_COUNT, (int) countOfSunkenColonies);
                        })
                        .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.updateFact(LAST_SUNKEN_COLONY_BUILDING_TIME,
                                memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).orElse(null)))
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(CAN_TRANSIT_FROM_5_POOL) != 0
                                        && dataForDecision.getFeatureValueBeliefSets(BASE_IS_COMPLETED) == 1.0
                                        && (memory.returnFactValueForGivenKey(LAST_SUNKEN_COLONY_BUILDING_TIME).orElse(0) + 100
                                        < memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).orElse(0))
                                        && (dataForDecision.getFeatureValueBeliefSets(COUNT_OF_SUNKEN_COLONIES_AT_BASE_IN_CONSTRUCTION)
                                        + dataForDecision.getFeatureValueBeliefSets(COUNT_OF_SUNKEN_COLONIES_AT_BASE)) <= 4
                                        && Decider.getDecision(AgentTypes.BASE_LOCATION, DesireKeys.BUILD_SUNKEN_COLONY, dataForDecision, DEFENSE))
                                .globalBeliefTypes(Stream.concat(DEFENSE.getConvertersForFactsForGlobalBeliefs().stream(),
                                        Stream.of(CAN_TRANSIT_FROM_5_POOL)).collect(Collectors.toSet()))
                                .globalBeliefSetTypes(DEFENSE.getConvertersForFactSetsForGlobalBeliefs())
                                .globalBeliefTypesByAgentType(DEFENSE.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(DEFENSE.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .beliefTypes(DEFENSE.getConvertersForFacts())
                                .beliefSetTypes(Stream.concat(DEFENSE.getConvertersForFactSets().stream(),
                                        Stream.of(BASE_IS_COMPLETED, COUNT_OF_SUNKEN_COLONIES_AT_BASE_IN_CONSTRUCTION,
                                                COUNT_OF_SUNKEN_COLONIES_AT_BASE)).collect(Collectors.toSet()))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(IS_BASE).get()
                                        || memory.returnFactValueForGivenKey(SUNKEN_COLONY_COUNT).orElse(0) !=
                                        (dataForDecision.getFeatureValueBeliefSets(COUNT_OF_SUNKEN_COLONIES_AT_BASE_IN_CONSTRUCTION)
                                                + dataForDecision.getFeatureValueBeliefSets(COUNT_OF_SUNKEN_COLONIES_AT_BASE))
                                )
                                .beliefSetTypes(new HashSet<>(Arrays.asList(COUNT_OF_SUNKEN_COLONIES_AT_BASE_IN_CONSTRUCTION,
                                        COUNT_OF_SUNKEN_COLONIES_AT_BASE)))
                                .useFactsInMemory(true)
                                .build())
                        .desiresForOthers(new HashSet<>(Arrays.asList(BUILD_CREEP_COLONY, BUILD_SUNKEN_COLONY)))
                        .build();
                type.addConfiguration(BUILD_SUNKEN_COLONY, buildSunkenAbstract, true);
                type.addConfiguration(BUILD_CREEP_COLONY, BUILD_SUNKEN_COLONY, buildCreepColonyCommon);
                ConfigurationWithSharedDesire buildSunken = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MORPH_TO_SUNKEN_COLONY)
                        .counts(1)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> false)
                                .build())
                        .build();
                type.addConfiguration(BUILD_SUNKEN_COLONY, BUILD_SUNKEN_COLONY, buildSunken);

                //spore colony as abstract plan
                ConfigurationWithAbstractPlan buildSporeColonyAbstract = ConfigurationWithAbstractPlan.builder()
                        .reactionOnChangeStrategy((memory, desireParameters) -> {
                            long countOfSporeColonies = memory.returnFactSetValueForGivenKey(STATIC_DEFENSE).orElse(Stream.empty())
                                    .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().equals(AUnitTypeWrapper.SPORE_COLONY_TYPE))
                                    .count()
                                    //creep colonies morphing to sunken
                                    + memory.returnFactSetValueForGivenKey(STATIC_DEFENSE).orElse(Stream.empty())
                                    .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().equals(AUnitTypeWrapper.CREEP_COLONY_TYPE))
                                    .filter(aUnitOfPlayer -> !aUnitOfPlayer.getTrainingQueue().isEmpty())
                                    .map(aUnitOfPlayer -> aUnitOfPlayer.getTrainingQueue().get(0))
                                    .filter(typeWrapper -> typeWrapper.equals(AUnitTypeWrapper.SPORE_COLONY_TYPE))
                                    .count();
                            memory.updateFact(SPORE_COLONY_COUNT, (int) countOfSporeColonies);
                        })
                        .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.updateFact(LAST_SPORE_COLONY_BUILDING_TIME,
                                memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).orElse(null)))
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(CAN_TRANSIT_FROM_5_POOL) != 0
                                        && dataForDecision.getFeatureValueBeliefSets(BASE_IS_COMPLETED) == 1.0
                                        && (memory.returnFactValueForGivenKey(LAST_SPORE_COLONY_BUILDING_TIME).orElse(0) + 100
                                        < memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).orElse(0))
                                        && (dataForDecision.getFeatureValueBeliefSets(COUNT_OF_SPORE_COLONIES_AT_BASE_IN_CONSTRUCTION)
                                        + dataForDecision.getFeatureValueBeliefSets(COUNT_OF_SPORE_COLONIES_AT_BASE)) <= 4
                                        && Decider.getDecision(AgentTypes.BASE_LOCATION, DesireKeys.BUILD_SPORE_COLONY, dataForDecision, DEFENSE))
                                .globalBeliefTypes(Stream.concat(DEFENSE.getConvertersForFactsForGlobalBeliefs().stream(),
                                        Stream.of(CAN_TRANSIT_FROM_5_POOL)).collect(Collectors.toSet()))
                                .globalBeliefSetTypes(DEFENSE.getConvertersForFactSetsForGlobalBeliefs())
                                .globalBeliefTypesByAgentType(DEFENSE.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(DEFENSE.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .beliefTypes(DEFENSE.getConvertersForFacts())
                                .beliefSetTypes(Stream.concat(DEFENSE.getConvertersForFactSets().stream(),
                                        Stream.of(BASE_IS_COMPLETED, COUNT_OF_SPORE_COLONIES_AT_BASE,
                                                COUNT_OF_SPORE_COLONIES_AT_BASE_IN_CONSTRUCTION)).collect(Collectors.toSet()))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(IS_BASE).get()
                                        || memory.returnFactValueForGivenKey(SPORE_COLONY_COUNT).orElse(0) !=
                                        (dataForDecision.getFeatureValueBeliefSets(COUNT_OF_SPORE_COLONIES_AT_BASE_IN_CONSTRUCTION)
                                                + dataForDecision.getFeatureValueBeliefSets(COUNT_OF_SPORE_COLONIES_AT_BASE))
                                )
                                .beliefSetTypes(new HashSet<>(Arrays.asList(COUNT_OF_SPORE_COLONIES_AT_BASE_IN_CONSTRUCTION,
                                        COUNT_OF_SPORE_COLONIES_AT_BASE)))
                                .useFactsInMemory(true)
                                .build())
                        .desiresForOthers(new HashSet<>(Arrays.asList(BUILD_CREEP_COLONY, BUILD_SPORE_COLONY)))
                        .build();
                type.addConfiguration(BUILD_SPORE_COLONY, buildSporeColonyAbstract, true);
                type.addConfiguration(BUILD_CREEP_COLONY, BUILD_SPORE_COLONY, buildCreepColonyCommon);
                ConfigurationWithSharedDesire buildSporeColony = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MORPH_TO_SPORE_COLONY)
                        .counts(1)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> false)
                                .build())
                        .build();
                type.addConfiguration(BUILD_SPORE_COLONY, BUILD_SPORE_COLONY, buildSporeColony);


                //hold ground
                ConfigurationWithSharedDesire holdGround = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(HOLD_GROUND)
                        .reactionOnChangeStrategy((memory, desireParameters) -> memory.updateFact(TIME_OF_HOLD_COMMAND,
                                memory.getReadOnlyMemoriesForAgentType(PLAYER)
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .findAny().orElse(null)))
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) ->
                                        dataForDecision.getFeatureValueGlobalBeliefs(CAN_TRANSIT_FROM_5_POOL) != 0
                                                && memory.returnFactValueForGivenKey(IS_ENEMY_BASE).get()
                                                && Decider.getDecision(AgentTypes.BASE_LOCATION, DesireKeys.HOLD_GROUND, dataForDecision, HOLDING))
                                .globalBeliefTypes(Stream.concat(HOLDING.getConvertersForFactsForGlobalBeliefs().stream(),
                                        Stream.of(CAN_TRANSIT_FROM_5_POOL)).collect(Collectors.toSet()))
                                .globalBeliefSetTypes(HOLDING.getConvertersForFactSetsForGlobalBeliefs())
                                .globalBeliefTypesByAgentType(HOLDING.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(HOLDING.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .beliefTypes(HOLDING.getConvertersForFacts())
                                .beliefSetTypes(HOLDING.getConvertersForFactSets())
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(IS_ENEMY_BASE).get()
                                        || !Decider.getDecision(AgentTypes.BASE_LOCATION, DesireKeys.HOLD_GROUND, dataForDecision, HOLDING)
                                )
                                .globalBeliefTypes(HOLDING.getConvertersForFactsForGlobalBeliefs())
                                .globalBeliefSetTypes(HOLDING.getConvertersForFactSetsForGlobalBeliefs())
                                .globalBeliefTypesByAgentType(HOLDING.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(HOLDING.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .beliefTypes(HOLDING.getConvertersForFacts())
                                .beliefSetTypes(HOLDING.getConvertersForFactSets())
                                .build())
                        .build();
                type.addConfiguration(HOLD_GROUND, holdGround);

                //hold air
                ConfigurationWithSharedDesire holdAir = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(HOLD_AIR)
                        .reactionOnChangeStrategy((memory, desireParameters) -> memory.updateFact(TIME_OF_HOLD_COMMAND,
                                memory.getReadOnlyMemoriesForAgentType(PLAYER)
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .findAny().orElse(null)))
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(CAN_TRANSIT_FROM_5_POOL) != 0
                                        && memory.returnFactValueForGivenKey(IS_ENEMY_BASE).get()
                                        && Decider.getDecision(AgentTypes.BASE_LOCATION, DesireKeys.HOLD_AIR, dataForDecision, HOLDING))
                                .globalBeliefTypes(Stream.concat(HOLDING.getConvertersForFactsForGlobalBeliefs().stream(),
                                        Stream.of(CAN_TRANSIT_FROM_5_POOL)).collect(Collectors.toSet()))
                                .globalBeliefSetTypes(HOLDING.getConvertersForFactSetsForGlobalBeliefs())
                                .globalBeliefTypesByAgentType(HOLDING.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(HOLDING.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .beliefTypes(HOLDING.getConvertersForFacts())
                                .beliefSetTypes(HOLDING.getConvertersForFactSets())
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(IS_ENEMY_BASE).get()
                                        || !Decider.getDecision(AgentTypes.BASE_LOCATION, DesireKeys.HOLD_AIR, dataForDecision, HOLDING))
                                .globalBeliefTypes(HOLDING.getConvertersForFactsForGlobalBeliefs())
                                .globalBeliefSetTypes(HOLDING.getConvertersForFactSetsForGlobalBeliefs())
                                .globalBeliefTypesByAgentType(HOLDING.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(HOLDING.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .beliefTypes(HOLDING.getConvertersForFacts())
                                .beliefSetTypes(HOLDING.getConvertersForFactSets())
                                .build())
                        .build();
                type.addConfiguration(HOLD_AIR, holdAir);

                //defend base
                ConfigurationWithSharedDesire defend = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(DEFEND)
                        .reactionOnChangeStrategy((memory, desireParameters) -> memory.updateFact(TIME_OF_HOLD_COMMAND,
                                memory.getReadOnlyMemoriesForAgentType(PLAYER)
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .findAny().orElse(null)))
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> memory.returnFactValueForGivenKey(IS_BASE).get()
                                        && memory.returnFactSetValueForGivenKey(ENEMY_UNIT).orElse(Stream.empty()).count() > 0)
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(IS_BASE).get()
                                        || memory.returnFactSetValueForGivenKey(ENEMY_UNIT).orElse(Stream.empty()).count() == 0)
                                .build())
                        .build();
                type.addConfiguration(DEFEND, defend);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BASE, IS_ENEMY_BASE, BASE_TO_MOVE, SUNKEN_COLONY_COUNT,
                    SPORE_COLONY_COUNT, CREEP_COLONY_COUNT, LAST_SUNKEN_COLONY_BUILDING_TIME, LAST_CREEP_COLONY_BUILDING_TIME,
                    LAST_SPORE_COLONY_BUILDING_TIME, TIME_OF_HOLD_COMMAND)))
            .usingTypesForFactSets(new HashSet<>(Arrays.asList(WORKER_ON_BASE, ENEMY_BUILDING, ENEMY_AIR,
                    ENEMY_GROUND, HAS_BASE, HAS_EXTRACTOR, OWN_BUILDING, OWN_AIR, OWN_GROUND,
                    WORKER_MINING_MINERALS, WORKER_MINING_GAS, OWN_AIR_FORCE_STATUS, OWN_BUILDING_STATUS,
                    OWN_GROUND_FORCE_STATUS, ENEMY_AIR_FORCE_STATUS, ENEMY_BUILDING_STATUS,
                    ENEMY_GROUND_FORCE_STATUS, LOCKED_UNITS, LOCKED_BUILDINGS, ENEMY_STATIC_AIR_FORCE_STATUS,
                    ENEMY_STATIC_GROUND_FORCE_STATUS, OWN_STATIC_AIR_FORCE_STATUS, OWN_STATIC_GROUND_FORCE_STATUS,
                    STATIC_DEFENSE, ENEMY_UNIT, OUR_UNIT)))
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(ECO_STATUS_IN_LOCATION,
                    FRIENDLIES_IN_LOCATION, ENEMIES_IN_LOCATION, ESTIMATE_ENEMY_FORCE_IN_LOCATION, ESTIMATE_OUR_FORCE_IN_LOCATION,
                    VISIT)))
            .desiresForOthers(new HashSet<>(Arrays.asList(VISIT, MINE_GAS_IN_BASE, MINE_MINERALS_IN_BASE,
                    BUILD_CREEP_COLONY, HOLD_GROUND, HOLD_AIR, DEFEND)))
            .desiresWithAbstractIntention(new HashSet<>(Arrays.asList(BUILD_SUNKEN_COLONY, BUILD_SPORE_COLONY)))
            .build();

    @Override
    public Optional<AgentBaseLocation> createAgent(BaseLocation baseLocation, BotFacade botFacade) {
        return Optional.of(new AgentBaseLocation(BASE_LOCATION, botFacade, baseLocation));
    }

    /**
     * Template to create configuration of force estimation reasoning
     *
     * @param factToSelectUnits
     * @param buildings
     * @param staticAir
     * @param staticGround
     * @param air
     * @param ground
     * @return
     */
    private static ConfigurationWithCommand.WithReasoningCommandDesiredBySelf formForceEstimator(FactKey<? extends AUnit> factToSelectUnits,
                                                                                                 FactKey<UnitTypeStatus> buildings,
                                                                                                 FactKey<UnitTypeStatus> staticAir,
                                                                                                 FactKey<UnitTypeStatus> staticGround,
                                                                                                 FactKey<UnitTypeStatus> air,
                                                                                                 FactKey<UnitTypeStatus> ground) {
        return ConfigurationWithCommand.
                WithReasoningCommandDesiredBySelf.builder()
                .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                    @Override
                    public boolean act(WorkingMemory memory) {
                        Set<UnitTypeStatus> unitTypes = memory.returnFactSetValueForGivenKey(factToSelectUnits)
                                .orElse(Stream.empty())
                                .filter(enemy -> enemy.getType().isBuilding())
                                .collect(Collectors.groupingBy(AUnit::getType)).entrySet().stream()
                                .map(entry -> new UnitTypeStatus(entry.getKey(), entry.getValue().stream()))
                                .collect(Collectors.toSet());

                        memory.updateFactSetByFacts(buildings, unitTypes);
                        memory.updateFactSetByFacts(staticAir, unitTypes.stream()
                                .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().isMilitaryBuildingAntiAir())
                                .collect(Collectors.toSet()));
                        memory.updateFactSetByFacts(staticGround, unitTypes.stream()
                                .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().isMilitaryBuildingAntiGround())
                                .collect(Collectors.toSet()));

                        Set<UnitTypeStatus> ownUnitsTypes = memory.returnFactSetValueForGivenKey(factToSelectUnits)
                                .orElse(Stream.empty())
                                .filter(enemy -> !enemy.getType().isNotActuallyUnit() && !enemy.getType().isBuilding())
                                .collect(Collectors.groupingBy(AUnit::getType)).entrySet().stream()
                                .map(entry -> new UnitTypeStatus(entry.getKey(), entry.getValue().stream()))
                                .collect(Collectors.toSet());
                        memory.updateFactSetByFacts(air, ownUnitsTypes.stream()
                                .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().canAttackAirUnits())
                                .collect(Collectors.toSet()));
                        memory.updateFactSetByFacts(ground, ownUnitsTypes.stream()
                                .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().canAttackGroundUnits())
                                .collect(Collectors.toSet()));

                        return true;
                    }
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> true)
                        .build())
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> true)
                        .build())
                .build();
    }
}