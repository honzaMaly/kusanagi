package cz.jan.maly.model;

/**
 * Interface to be implemented by each service in this package. It describes one method which should be called on start of new game
 * to reinitialized service that implements it
 * Created by Jan on 14-Dec-16.
 */
public interface ServiceInterface {

    void reinitializedServiceForNewGame();

}
