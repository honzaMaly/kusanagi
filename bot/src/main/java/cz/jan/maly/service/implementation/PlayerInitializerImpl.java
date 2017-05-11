package cz.jan.maly.service.implementation;

import bwapi.Race;
import cz.jan.maly.model.UnitTypeStatus;
import cz.jan.maly.model.agent.AgentPlayer;
import cz.jan.maly.model.agent.types.AgentTypePlayer;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.game.wrappers.APlayer;
import cz.jan.maly.model.game.wrappers.ARace;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.UnitWrapperFactory;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.service.PlayerInitializer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.bot.AgentTypes.BASE_LOCATION;
import static cz.jan.maly.model.bot.FactKeys.*;

/**
 * Strategy to initialize agent representing "player"
 * Created by Jan on 05-Apr-17.
 */
public class PlayerInitializerImpl implements PlayerInitializer {

    private static final AgentTypePlayer PLAYER = AgentTypePlayer.builder()
            .agentTypeID(AgentTypes.PLAYER)
            .usingTypesForFacts(new HashSet<>(Arrays.asList(AVAILABLE_MINERALS, ENEMY_RACE, AVAILABLE_GAS, POPULATION_LIMIT, POPULATION, IS_PLAYER)))
            .usingTypesForFactSets(new HashSet<>(Arrays.asList(UPGRADE_STATUS, TECH_TO_RESEARCH, OUR_BASE, ENEMY_BASE,
                    OWN_AIR_FORCE_STATUS, OWN_BUILDING_STATUS, OWN_GROUND_FORCE_STATUS, ENEMY_AIR_FORCE_STATUS, ENEMY_BUILDING_STATUS,
                    ENEMY_GROUND_FORCE_STATUS, LOCKED_UNITS, LOCKED_BUILDINGS, ENEMY_STATIC_AIR_FORCE_STATUS, ENEMY_STATIC_GROUND_FORCE_STATUS,
                    OWN_STATIC_AIR_FORCE_STATUS, OWN_STATIC_GROUND_FORCE_STATUS)))
            .initializationStrategy(type -> {

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
            .build();

    @Override
    public AgentPlayer createAgentForPlayer(APlayer player, BotFacade botFacade, Race enemyInitialRace) {
        return new AgentPlayer(PLAYER, botFacade, player, enemyInitialRace);
    }
}
