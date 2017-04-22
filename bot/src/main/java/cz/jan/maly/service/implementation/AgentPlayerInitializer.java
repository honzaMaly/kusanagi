package cz.jan.maly.service.implementation;

import bwapi.Race;
import cz.jan.maly.model.agent.AgentPlayer;
import cz.jan.maly.model.agent.types.AgentTypePlayer;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.game.wrappers.APlayer;
import cz.jan.maly.service.PlayerInitializer;

/**
 * Strategy to initialize agent representing "player"
 * Created by Jan on 05-Apr-17.
 */
public class AgentPlayerInitializer implements PlayerInitializer {

    public static final AgentTypePlayer PLAYER = AgentTypePlayer.builder()
            .agentTypeID(AgentTypes.PLAYER)
            .initializationStrategy(type -> {
            })
            .build();

    @Override
    public AgentPlayer createAgentForPlayer(APlayer player, BotFacade botFacade, Race enemyInitialRace) {
        return new AgentPlayer(PLAYER, botFacade, player, enemyInitialRace);
    }
}
