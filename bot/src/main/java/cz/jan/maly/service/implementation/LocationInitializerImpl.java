package cz.jan.maly.service.implementation;

import bwta.BaseLocation;
import cz.jan.maly.model.UnitTypeStatus;
import cz.jan.maly.model.agent.AgentBaseLocation;
import cz.jan.maly.model.agent.types.AgentTypeBaseLocation;
import cz.jan.maly.model.bot.FactConverters;
import cz.jan.maly.model.game.wrappers.*;
import cz.jan.maly.model.knowledge.ReadOnlyMemory;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithSharedDesire;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.service.LocationInitializer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.bot.FactConverters.COUNT_OF_MINERALS_ON_BASE;
import static cz.jan.maly.model.bot.FactKeys.*;

/**
 * Strategy to initialize player
 * Created by Jan on 05-Apr-17.
 */
public class LocationInitializerImpl implements LocationInitializer {

    public static final AgentTypeBaseLocation BASE_LOCATION = AgentTypeBaseLocation.builder()
            .initializationStrategy(type -> {

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
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf enemyForce = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                Set<UnitTypeStatus> enemyBuildingsTypes = memory.returnFactSetValueForGivenKey(ENEMY_UNIT)
                                        .orElse(Stream.empty())
                                        .filter(enemy -> enemy.getType().isBuilding())
                                        .collect(Collectors.groupingBy(AUnit::getType)).entrySet().stream()
                                        .map(entry -> new UnitTypeStatus(entry.getKey(), entry.getValue().stream()))
                                        .collect(Collectors.toSet());
                                memory.updateFactSetByFacts(ENEMY_BUILDING_STATUS, enemyBuildingsTypes);
                                memory.updateFactSetByFacts(ENEMY_STATIC_AIR_FORCE_STATUS, enemyBuildingsTypes.stream()
                                        .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().isMilitaryBuildingAntiAir())
                                        .collect(Collectors.toSet()));
                                memory.updateFactSetByFacts(ENEMY_STATIC_GROUND_FORCE_STATUS, enemyBuildingsTypes.stream()
                                        .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().isMilitaryBuildingAntiGround())
                                        .collect(Collectors.toSet()));

                                Set<UnitTypeStatus> enemyUnitsTypes = memory.returnFactSetValueForGivenKey(ENEMY_UNIT)
                                        .orElse(Stream.empty())
                                        .filter(enemy -> !enemy.getType().isNotActuallyUnit() && !enemy.getType().isBuilding())
                                        .collect(Collectors.groupingBy(AUnit::getType)).entrySet().stream()
                                        .map(entry -> new UnitTypeStatus(entry.getKey(), entry.getValue().stream()))
                                        .collect(Collectors.toSet());
                                memory.updateFactSetByFacts(ENEMY_AIR_FORCE_STATUS, enemyUnitsTypes.stream()
                                        .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().canAttackAirUnits())
                                        .collect(Collectors.toSet()));
                                memory.updateFactSetByFacts(ENEMY_GROUND_FORCE_STATUS, enemyUnitsTypes.stream()
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
                type.addConfiguration(ESTIMATE_ENEMY_FORCE_IN_LOCATION, enemyForce);

                //estimate our force
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf ourForce = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                Set<UnitTypeStatus> ownBuildingsTypes = memory.returnFactSetValueForGivenKey(OUR_UNIT)
                                        .orElse(Stream.empty())
                                        .filter(enemy -> enemy.getType().isBuilding())
                                        .collect(Collectors.groupingBy(AUnit::getType)).entrySet().stream()
                                        .map(entry -> new UnitTypeStatus(entry.getKey(), entry.getValue().stream()))
                                        .collect(Collectors.toSet());
                                memory.updateFactSetByFacts(OWN_BUILDING_STATUS, ownBuildingsTypes);
                                memory.updateFactSetByFacts(OWN_STATIC_AIR_FORCE_STATUS, ownBuildingsTypes.stream()
                                        .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().isMilitaryBuildingAntiAir())
                                        .collect(Collectors.toSet()));
                                memory.updateFactSetByFacts(OWN_STATIC_GROUND_FORCE_STATUS, ownBuildingsTypes.stream()
                                        .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().isMilitaryBuildingAntiGround())
                                        .collect(Collectors.toSet()));

                                Set<UnitTypeStatus> ownUnitsTypes = memory.returnFactSetValueForGivenKey(OUR_UNIT)
                                        .orElse(Stream.empty())
                                        .filter(enemy -> !enemy.getType().isNotActuallyUnit() && !enemy.getType().isBuilding())
                                        .collect(Collectors.groupingBy(AUnit::getType)).entrySet().stream()
                                        .map(entry -> new UnitTypeStatus(entry.getKey(), entry.getValue().stream()))
                                        .collect(Collectors.toSet());
                                memory.updateFactSetByFacts(OWN_AIR_FORCE_STATUS, ownUnitsTypes.stream()
                                        .filter(unitTypeStatus -> unitTypeStatus.getUnitTypeWrapper().canAttackAirUnits())
                                        .collect(Collectors.toSet()));
                                memory.updateFactSetByFacts(OWN_GROUND_FORCE_STATUS, ownUnitsTypes.stream()
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
                type.addConfiguration(ESTIMATE_OUR_FORCE_IN_LOCATION, ourForce);

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
                                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(LOCATION).isPresent() &&
                                                readOnlyMemory.returnFactValueForGivenKey(LOCATION).get().equals(base))
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
                                memory.updateFact(IS_ENEMY_BASE, !isBase && memory.returnFactSetValueForGivenKey(ENEMY_BUILDING).map(Stream::count).orElse(0L) > 0);
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
                    FRIENDLIES_IN_LOCATION, ENEMIES_IN_LOCATION, ESTIMATE_ENEMY_FORCE_IN_LOCATION, ESTIMATE_OUR_FORCE_IN_LOCATION)))
            .desiresForOthers(new HashSet<>(Collections.singletonList(MINE_MINERALS_IN_BASE)))
            .build();

    @Override
    public Optional<AgentBaseLocation> createAgent(BaseLocation baseLocation, BotFacade botFacade) {
        return Optional.of(new AgentBaseLocation(BASE_LOCATION, botFacade, baseLocation));
    }
}
