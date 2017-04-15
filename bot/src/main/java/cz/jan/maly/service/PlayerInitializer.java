package cz.jan.maly.service;

import bwapi.Race;
import cz.jan.maly.model.agent.AgentPlayer;
import cz.jan.maly.model.game.wrappers.APlayer;
import cz.jan.maly.service.implementation.BotFacade;

/**
 * Interface to be implemented by user to create agent representing player. This agent make observation of player stats
 * Created by Jan on 05-Apr-17.
 */
public interface PlayerInitializer {

    /**
     * Method to create agent from player
     *
     * @param player
     * @param botFacade
     * @param enemyInitialRace
     * @return
     */
    AgentPlayer createAgentForPlayer(APlayer player, BotFacade botFacade, Race enemyInitialRace);

}
