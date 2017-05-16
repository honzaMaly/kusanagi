package cz.jan.maly.service.implementation;

import bwta.BaseLocation;
import cz.jan.maly.model.UnitTypeStatus;
import cz.jan.maly.model.agent.AgentBaseLocation;
import cz.jan.maly.model.agent.types.AgentTypeBaseLocation;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.bot.FactConverters;
import cz.jan.maly.model.game.wrappers.*;
import cz.jan.maly.model.knowledge.ReadOnlyMemory;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithSharedDesire;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.service.LocationInitializer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.bot.FactConverters.COUNT_OF_EXTRACTORS_ON_BASE;
import static cz.jan.maly.model.bot.FactConverters.COUNT_OF_MINERALS_ON_BASE;
import static cz.jan.maly.model.bot.FactKeys.*;

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
                                //todo stay if it is enemy base
                                .decisionStrategy((dataForDecision, memory) -> {

                                    //do not visit our base location
                                    if (dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 1.0) {
                                        return true;
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
                                .beliefTypes(new HashSet<>(Arrays.asList(FactConverters.IS_BASE, FactConverters.LAST_TIME_SCOUTED)))
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
                                .decisionStrategy((dataForDecision, memory) -> true)
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
                                memory.updateFactSetByFacts(STATIC_DEFENSE, playersUnits.stream()
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().equals(AUnitTypeWrapper.SUNKEN_COLONY_TYPE)
                                                || aUnitOfPlayer.getType().equals(AUnitTypeWrapper.CREEP_COLONY_TYPE)
                                                || aUnitOfPlayer.getType().equals(AUnitTypeWrapper.SPORE_COLONY_TYPE))
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
                type.addConfiguration(FRIENDLIES_IN_LOCATION, ourUnits);

                //estimate enemy force
                type.addConfiguration(ESTIMATE_ENEMY_FORCE_IN_LOCATION, formForceEstimator(ENEMY_UNIT, ENEMY_BUILDING_STATUS,
                        ENEMY_STATIC_AIR_FORCE_STATUS, ENEMY_STATIC_GROUND_FORCE_STATUS, ENEMY_AIR_FORCE_STATUS, ENEMY_GROUND_FORCE_STATUS));

                //estimate our force
                type.addConfiguration(ESTIMATE_OUR_FORCE_IN_LOCATION, formForceEstimator(OUR_UNIT, OWN_BUILDING_STATUS,
                        OWN_STATIC_AIR_FORCE_STATUS, OWN_STATIC_GROUND_FORCE_STATUS, OWN_AIR_FORCE_STATUS, OWN_GROUND_FORCE_STATUS));

                //eco concerns
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf ecoCorncerns = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();

                                //eco buildings
                                memory.updateFactSetByFacts(HAS_BASE, memory.returnFactSetValueForGivenKey(OWN_BUILDING).orElse(Stream.empty())
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().isBase())
                                        .collect(Collectors.toSet()));
                                memory.updateFactSetByFacts(HAS_EXTRACTOR, memory.returnFactSetValueForGivenKey(OWN_BUILDING).orElse(Stream.empty())
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().isGasBuilding())
                                        .collect(Collectors.toSet()));

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
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .build();
                type.addConfiguration(ECO_STATUS_IN_LOCATION, ecoCorncerns);

                //status concerns
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf baseStatus = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
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
                type.addConfiguration(BASE_STATUS, baseStatus);

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
                                )
                                .beliefTypes(new HashSet<>(Collections.singletonList(FactConverters.IS_BASE)))
                                .beliefSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                                .parameterValueSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                                .build()
                        )
                        .build();
                type.addConfiguration(MINE_MINERALS_IN_BASE, mineMinerals);

                //Make request to start mining gas. Remove request when there are no extractors or there is no hatchery to bring mineral in
                ConfigurationWithSharedDesire mineGas = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MINE_GAS_IN_BASE)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 1
                                        && dataForDecision.getFeatureValueDesireBeliefSets(COUNT_OF_EXTRACTORS_ON_BASE) > 0)
                                .beliefTypes(new HashSet<>(Collections.singletonList(FactConverters.IS_BASE)))
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

            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BASE, IS_ENEMY_BASE)))
            .usingTypesForFactSets(new HashSet<>(Arrays.asList(WORKER_ON_BASE, ENEMY_BUILDING, ENEMY_AIR,
                    ENEMY_GROUND, HAS_BASE, HAS_EXTRACTOR, OWN_BUILDING, OWN_AIR, OWN_GROUND,
                    WORKER_MINING_MINERALS, WORKER_MINING_GAS, OWN_AIR_FORCE_STATUS, OWN_BUILDING_STATUS,
                    OWN_GROUND_FORCE_STATUS, ENEMY_AIR_FORCE_STATUS, ENEMY_BUILDING_STATUS,
                    ENEMY_GROUND_FORCE_STATUS, LOCKED_UNITS, LOCKED_BUILDINGS, ENEMY_STATIC_AIR_FORCE_STATUS,
                    ENEMY_STATIC_GROUND_FORCE_STATUS, OWN_STATIC_AIR_FORCE_STATUS, OWN_STATIC_GROUND_FORCE_STATUS,
                    STATIC_DEFENSE, ENEMY_UNIT, OUR_UNIT)))
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(BASE_STATUS, ECO_STATUS_IN_LOCATION,
                    FRIENDLIES_IN_LOCATION, ENEMIES_IN_LOCATION, ESTIMATE_ENEMY_FORCE_IN_LOCATION, ESTIMATE_OUR_FORCE_IN_LOCATION,
                    VISIT)))
            .desiresForOthers(new HashSet<>(Arrays.asList(MINE_MINERALS_IN_BASE, VISIT, MINE_GAS_IN_BASE)))
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
