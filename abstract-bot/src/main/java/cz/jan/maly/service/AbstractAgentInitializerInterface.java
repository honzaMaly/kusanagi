package cz.jan.maly.service;

import cz.jan.maly.model.agent.AbstractAgentInitializationStrategy;

import java.util.List;

/**
 * Interface to be implemented by user to define method which creat all abstract agents used in game at its start
 * Created by Jan on 28-Dec-16.
 */
public interface AbstractAgentInitializerInterface {

    /**
     * Method to return list of creation strategies (describe how to initialized one particular agent) for all abstract
     * game agents (tight to place, generally abstract). It is invoked on start of the game. Each element in list is
     * used for creating agent exactly once time.
     *
     * @return
     */
    List<AbstractAgentInitializationStrategy> initializeAbstractAgentOnStartOfTheGame();

}
