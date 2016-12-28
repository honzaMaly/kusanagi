package cz.jan.maly.service;

import bwapi.Unit;

/**
 * Interface to be implemented by user to provide factory for creating agents for own units on their creation
 * Created by Jan on 28-Dec-16.
 */
public interface AgentUnitFactoryInterface {

    /**
     * Method to create agent from unit
     * @param unit
     */
    void createAgentForUnit(Unit unit);

}
