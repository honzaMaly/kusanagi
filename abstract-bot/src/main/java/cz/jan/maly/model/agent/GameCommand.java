package cz.jan.maly.model.agent;

import bwapi.Game;

/**
 * Interface to be implemented by class which is interested in observing game, reading map or making action
 * Created by Jan on 28-Dec-16.
 */
public interface GameCommand {

    void commandGame(Game game);

    long getExecutionTime();

}
