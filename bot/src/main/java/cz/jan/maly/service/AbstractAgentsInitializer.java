package cz.jan.maly.service;

import cz.jan.maly.model.agent.AbstractAgent;
import cz.jan.maly.service.implementation.BotFacade;

import java.util.List;

/**
 * Interface to be implemented by user to create all additional abstract agents with no direct representation in game
 * Created by Jan on 09-May-17.
 */
public interface AbstractAgentsInitializer {

    /**
     * Method to create abstract agents
     *
     * @param botFacade
     * @return
     */
    List<AbstractAgent> initializeAbstractAgents(BotFacade botFacade);

}
