package cz.jan.maly.model;

import bwapi.Game;

/**
 * Interface to be implemented by class which is interested in observing game
 * Created by Jan on 09-Dec-16.
 */
public interface GameObserver {

    void makeObservation(Game game);

}
