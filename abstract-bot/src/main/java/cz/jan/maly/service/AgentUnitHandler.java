package cz.jan.maly.service;

import bwapi.Unit;
import cz.jan.maly.model.agent.AgentUnit;
import cz.jan.maly.service.implementation.BotFacade;

import java.util.Optional;

/**
 * Interface to be implemented by user to provide factory for creating agents for own units on their creation
 * Created by Jan on 28-Dec-16.
 */
public interface AgentUnitHandler {

    /**
     * Method to create agent from unit
     *
     * @param unit
     * @param botFacade
     * @param frameCount
     * @return
     */
    Optional<AgentUnit> createAgentForUnit(Unit unit, BotFacade botFacade, int frameCount);

}
