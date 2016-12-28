package cz.jan.maly.service;

/**
 * Interface to be implemented by user to define method which creat all abstract agents used in game at its start
 * Created by Jan on 28-Dec-16.
 */
public interface AbstractAgentInitializerInterface {

    /**
     * Method is called on start of new game to create all abstract game agents (tight to place, generally abstract)
     */
    void initializeAbstractAgentOnStartOfTheGame();

}
