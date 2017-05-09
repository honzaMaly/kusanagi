package cz.jan.maly.service.implementation;

import bwapi.Race;
import cz.jan.maly.model.agent.AgentPlayer;
import cz.jan.maly.model.agent.types.AgentTypePlayer;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.game.wrappers.APlayer;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.service.PlayerInitializer;

import java.util.Arrays;
import java.util.HashSet;

import static cz.jan.maly.model.DesiresKeys.READ_PLAYERS_DATA;
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

                //update beliefs based on resources fields presented in player object
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf readPlayersData = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                //read data from player
                                APlayer aPlayer = memory.returnFactValueForGivenKey(IS_PLAYER).get();
                                memory.updateFact(AVAILABLE_MINERALS, (double) aPlayer.getMinerals());
                                memory.updateFact(AVAILABLE_GAS, (double) aPlayer.getGas());
                                memory.updateFact(POPULATION_LIMIT, (double) aPlayer.getSupplyTotal());
                                memory.updateFact(POPULATION, (double) aPlayer.getSupplyUsed());
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> true)
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> true)
                                .build())
                        .build();
                type.addConfiguration(READ_PLAYERS_DATA, readPlayersData);

            })
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(READ_PLAYERS_DATA)))
            .build();

    @Override
    public AgentPlayer createAgentForPlayer(APlayer player, BotFacade botFacade, Race enemyInitialRace) {
        return new AgentPlayer(PLAYER, botFacade, player, enemyInitialRace);
    }
}
