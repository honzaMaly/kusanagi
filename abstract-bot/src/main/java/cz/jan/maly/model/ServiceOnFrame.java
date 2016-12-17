package cz.jan.maly.model;

import bwapi.Game;

/**
 * Interface to be implemented by each service which act on frame. Each service should take in consideration remaining time
 * Created by Jan on 17-Dec-16.
 */
public interface ServiceOnFrame {

    void act(long remainingTime, Game game);

}
