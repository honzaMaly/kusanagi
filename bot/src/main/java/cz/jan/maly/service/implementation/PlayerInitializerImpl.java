package cz.jan.maly.service.implementation;

import bwapi.Race;
import cz.jan.maly.model.UnitTypeStatus;
import cz.jan.maly.model.agent.AgentPlayer;
import cz.jan.maly.model.agent.types.AgentTypePlayer;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.game.wrappers.*;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithAbstractPlan;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithSharedDesire;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.service.PlayerInitializer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.bot.AgentTypes.BASE_LOCATION;
import static cz.jan.maly.model.bot.FactConverters.*;
import static cz.jan.maly.model.bot.FactKeys.*;
import static cz.jan.maly.model.bot.FactKeys.IS_BASE;
import static cz.jan.maly.model.bot.FactKeys.IS_ENEMY_BASE;
import static cz.jan.maly.model.bot.FactKeys.IS_START_LOCATION;
import static cz.jan.maly.model.bot.FactKeys.LAST_TIME_SCOUTED;
import static cz.jan.maly.service.implementation.AbstractAgentsInitializerImpl.FIND_MAIN_BASE;

/**
 * Strategy to initialize agent representing "player"
 * Created by Jan on 05-Apr-17.
 */
public class PlayerInitializerImpl implements PlayerInitializer {

    private static final AgentTypePlayer PLAYER = AgentTypePlayer.builder()
            .agentTypeID(AgentTypes.PLAYER)
            .usingTypesForFacts(new HashSet<>(Arrays.asList(AVAILABLE_MINERALS, ENEMY_RACE, AVAILABLE_GAS, POPULATION_LIMIT, POPULATION, IS_PLAYER,
                    MORPHED_WORKERS, TRANSIT_FROM_5_POOL, WERE_6_LINGS_MORPHED, BASE_TO_MOVE, IS_BASE_LOCATION, TIME_OF_HOLD_COMMAND, TIME_OF_LAST_DRONE)))
            .usingTypesForFactSets(new HashSet<>(Arrays.asList(UPGRADE_STATUS, TECH_TO_RESEARCH, OUR_BASE, ENEMY_BASE,
                    OWN_AIR_FORCE_STATUS, OWN_BUILDING_STATUS, OWN_GROUND_FORCE_STATUS, ENEMY_AIR_FORCE_STATUS, ENEMY_BUILDING_STATUS,
                    ENEMY_GROUND_FORCE_STATUS, LOCKED_UNITS, LOCKED_BUILDINGS, ENEMY_STATIC_AIR_FORCE_STATUS, ENEMY_STATIC_GROUND_FORCE_STATUS,
                    OWN_STATIC_AIR_FORCE_STATUS, OWN_STATIC_GROUND_FORCE_STATUS, LING)))
            .initializationStrategy(type -> {

                /////////5 pool for start
                //abstract plan to build units based on position requests
                ConfigurationWithAbstractPlan startWith5Pool = ConfigurationWithAbstractPlan.builder()
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) ->
                                        dataForDecision.getFeatureValueGlobalBeliefs(CAN_TRANSIT_FROM_5_POOL) == 0
                                )
                                .globalBeliefTypes(new HashSet<>(Collections.singletonList(CAN_TRANSIT_FROM_5_POOL)))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(CAN_TRANSIT_FROM_5_POOL) != 0)
                                .globalBeliefTypes(new HashSet<>(Collections.singletonList(CAN_TRANSIT_FROM_5_POOL)))
                                .build())
                        .desiresForOthers(new HashSet<>(Arrays.asList(MORPH_TO_DRONE, MORPH_TO_POOL, BOOST_GROUND_MELEE, HOLD_GROUND)))
                        .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(REASON_ABOUT_TRANSITION, FIND_ENEMY_BASE)))
                        .build();
                type.addConfiguration(DO_5_POOL, startWith5Pool, true);

                //build workers - 1, than wait for pool - is build or is in construction to build other 2
                ConfigurationWithSharedDesire trainDrone = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MORPH_TO_DRONE)
                        .counts(1)
                        .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.updateFact(TIME_OF_LAST_DRONE, memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).get()))
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) ->
                                        memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).get() > memory.returnFactValueForGivenKey(TIME_OF_LAST_DRONE).get() + 25
                                                && (dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_WORKERS) < 5
                                                || (dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_WORKERS) < 6 &&
                                                dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS) + dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS_IN_CONSTRUCTION) > 0)))
                                .globalBeliefTypes(new HashSet<>(Arrays.asList(COUNT_OF_WORKERS)))
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(COUNT_OF_POOLS, COUNT_OF_POOLS_IN_CONSTRUCTION)))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) ->
                                        (dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_WORKERS) >= 5 &&
                                                (dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS)
                                                        + dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS_IN_CONSTRUCTION)) == 0)
                                                || (dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_WORKERS) >= 6))
                                .globalBeliefTypes(new HashSet<>(Arrays.asList(COUNT_OF_WORKERS)))
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(COUNT_OF_POOLS, COUNT_OF_POOLS_IN_CONSTRUCTION)))
                                .build())
                        .build();
                type.addConfiguration(MORPH_TO_DRONE, DO_5_POOL, trainDrone);

                //build pool
                ConfigurationWithSharedDesire buildPool = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MORPH_TO_POOL)
                        .counts(1)
                        .reactionOnChangeStrategy(FIND_MAIN_BASE)
                        .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.eraseFactValueForGivenKey(BASE_TO_MOVE))
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) ->
                                        dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS) == 0
                                                && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS_IN_CONSTRUCTION) == 0
                                )
                                .globalBeliefTypesByAgentType(Stream.of(COUNT_OF_POOLS_IN_CONSTRUCTION, COUNT_OF_POOLS, COUNT_OF_MINERALS).collect(Collectors.toSet()))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS) > 0
                                        || dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS_IN_CONSTRUCTION) > 0
                                        || !memory.returnFactValueForGivenKey(BASE_TO_MOVE).isPresent())
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(COUNT_OF_POOLS_IN_CONSTRUCTION, COUNT_OF_POOLS)))
                                .build())
                        .build();
                type.addConfiguration(MORPH_TO_POOL, DO_5_POOL, buildPool);

                //build lings
                ConfigurationWithSharedDesire buildLings = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(BOOST_GROUND_MELEE)
                        .counts(1)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS) > 0)
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(COUNT_OF_POOLS)))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> false)
                                .build())
                        .build();
                type.addConfiguration(BOOST_GROUND_MELEE, DO_5_POOL, buildLings);

                //find enemy location
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf reasonAboutEnemyLocation = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {

                                //is any base enemy's
                                Optional<ABaseLocationWrapper> enemyBase = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                        .filter(readOnlyMemory -> readOnlyMemory.returnFactSetValueForGivenKey(ENEMY_BUILDING).orElse(Stream.empty()).count() > 0)
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get())
                                        .findAny();
                                enemyBase.ifPresent(aBaseLocationWrapper -> memory.updateFact(IS_BASE_LOCATION, aBaseLocationWrapper));

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
                type.addConfiguration(FIND_ENEMY_BASE, DO_5_POOL, reasonAboutEnemyLocation);

                //attack with lings
                ConfigurationWithSharedDesire holdGround = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(HOLD_GROUND)
                        .reactionOnChangeStrategy((memory, desireParameters) -> memory.updateFact(TIME_OF_HOLD_COMMAND, memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).get()))
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> memory.returnFactValueForGivenKey(WERE_6_LINGS_MORPHED).get()
                                        && memory.returnFactValueForGivenKey(IS_BASE_LOCATION).isPresent())
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(IS_BASE_LOCATION).isPresent())
                                .build())
                        .build();
                type.addConfiguration(HOLD_GROUND, DO_5_POOL, holdGround);

                //reason about end of pool phase
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf reasonAboutTransition = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {

                                //if we have build 6 zerglings for first time save them to set
                                if (!memory.returnFactValueForGivenKey(WERE_6_LINGS_MORPHED).get()) {
                                    Set<AUnitOfPlayer> zerglings = UnitWrapperFactory.getStreamOfAllAlivePlayersUnits()
                                            .filter(aUnitOfPlayer -> aUnitOfPlayer.getType().equals(AUnitTypeWrapper.ZERGLING_TYPE))
                                            .collect(Collectors.toSet());
                                    memory.updateFact(WERE_6_LINGS_MORPHED, zerglings.size() >= 6);
                                } else {

                                    //if all of these units are dead or we have 20 lings- transit to next phase
                                    memory.updateFact(TRANSIT_FROM_5_POOL, memory.returnFactValueForGivenKey(IS_PLAYER).get().getMinerals() > 100);
                                }
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
                type.addConfiguration(REASON_ABOUT_TRANSITION, DO_5_POOL, reasonAboutTransition);

                ////////5 pool for start

                //send worker to scout
                ConfigurationWithSharedDesire sendWorkerScouting = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(WORKER_SCOUT)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_START_LOCATION).get())
                                        .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(LAST_TIME_SCOUTED).isPresent())
                                        .count() > 0
                                        && memory.getReadOnlyMemoriesForAgentType(AgentTypes.DRONE).count() > 8)
                                .useFactsInMemory(true)
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_START_LOCATION).get())
                                        .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(LAST_TIME_SCOUTED).isPresent())
                                        .count() == 0)
                                .useFactsInMemory(true)
                                .build())
                        .counts(1)
                        .build();
                type.addConfiguration(WORKER_SCOUT, sendWorkerScouting);

                //read data from player
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf readPlayersData = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                APlayer aPlayer = memory.returnFactValueForGivenKey(IS_PLAYER).get();
                                memory.updateFact(AVAILABLE_MINERALS, (double) aPlayer.getMinerals());
                                memory.updateFact(AVAILABLE_GAS, (double) aPlayer.getGas());
                                memory.updateFact(POPULATION_LIMIT, (double) aPlayer.getSupplyTotal());
                                memory.updateFact(POPULATION, (double) aPlayer.getSupplyUsed());
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
                type.addConfiguration(READ_PLAYERS_DATA, readPlayersData);

                //estimate enemy force
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf estimateEnemyForce = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                Set<UnitTypeStatus> enemyBuildingsTypes = UnitWrapperFactory.getStreamOfAllAliveEnemyUnits()
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
                                Set<UnitTypeStatus> enemyUnitsTypes = UnitWrapperFactory.getStreamOfAllAliveEnemyUnits()
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
                type.addConfiguration(ESTIMATE_ENEMY_FORCE, estimateEnemyForce);

                //estimate our force
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf estimateOurForce = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                Set<UnitTypeStatus> ownBuildingsTypes = UnitWrapperFactory.getStreamOfAllAlivePlayersUnits()
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
                                Set<UnitTypeStatus> ownUnitsTypes = UnitWrapperFactory.getStreamOfAllAlivePlayersUnits()
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
                type.addConfiguration(ESTIMATE_OUR_FORCE, estimateOurForce);

                //enemy race
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf updateEnemyRace = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                Optional<Race> enemyRace = UnitWrapperFactory.getStreamOfAllAliveEnemyUnits().map(enemy -> enemy.getType().getRace()).findAny();
                                enemyRace.ifPresent(race -> memory.updateFact(ENEMY_RACE, ARace.getRace(race)));
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> UnitWrapperFactory.getStreamOfAllAliveEnemyUnits().count() > 0)
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .build();
                type.addConfiguration(UPDATE_ENEMY_RACE, updateEnemyRace);

                //bases
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf reasonAboutBases = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                memory.updateFactSetByFacts(OUR_BASE, memory.getReadOnlyMemoriesForAgentType(BASE_LOCATION)
                                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE).orElse(false))
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get())
                                        .collect(Collectors.toSet()));
                                memory.updateFactSetByFacts(ENEMY_BASE, memory.getReadOnlyMemoriesForAgentType(BASE_LOCATION)
                                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_ENEMY_BASE).orElse(false))
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get())
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
                type.addConfiguration(REASON_ABOUT_BASES, reasonAboutBases);

            })
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(READ_PLAYERS_DATA, ESTIMATE_ENEMY_FORCE,
                    ESTIMATE_OUR_FORCE, UPDATE_ENEMY_RACE, REASON_ABOUT_BASES)))
            .desiresForOthers(new HashSet<>(Collections.singletonList(WORKER_SCOUT)))
            .desiresWithAbstractIntention(new HashSet<>(Collections.singleton(DO_5_POOL)))
            .build();

    @Override
    public AgentPlayer createAgentForPlayer(APlayer player, BotFacade botFacade, Race enemyInitialRace) {
        return new AgentPlayer(PLAYER, botFacade, player, enemyInitialRace);
    }
}
