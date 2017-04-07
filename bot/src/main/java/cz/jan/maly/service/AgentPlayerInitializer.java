package cz.jan.maly.service;

import bwapi.Race;
import cz.jan.maly.model.agent.AgentPlayer;
import cz.jan.maly.model.agent.types.AgentTypePlayer;
import cz.jan.maly.model.game.wrappers.APlayer;
import cz.jan.maly.service.implementation.BotFacade;

/**
 * Strategy to initialize agent representing "player"
 * Created by Jan on 05-Apr-17.
 */
public class AgentPlayerInitializer implements PlayerInitializer {

    @Override
    public AgentPlayer createAgentForPlayer(APlayer player, BotFacade botFacade, Race enemyInitialRace) {
        return new AgentPlayer(PLAYER, botFacade, player, enemyInitialRace);
    }

    public static final AgentTypePlayer PLAYER = AgentTypePlayer.builder()
            .name("PLAYER")
            .initializationStrategy(type -> {

                //todo

            })
            .build();
}
