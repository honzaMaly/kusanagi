package cz.jan.maly.model;

/**
 * Interface to be implemented by class which is interested in making action in game
 * Created by Jan on 17-Dec-16.
 */
public interface GameActionMaker {

    void executeActionInGame();

    long getExecutionTime();

}
