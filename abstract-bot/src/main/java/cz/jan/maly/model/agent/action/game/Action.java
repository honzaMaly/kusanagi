package cz.jan.maly.model.agent.action.game;

import bwapi.Game;
import cz.jan.maly.model.game.wrappers.AUnit;

/**
 * Interface prescribes strategy of action to be executed in game
 * Created by Jan on 17-Dec-16.
 */
public interface Action {

    /**
     * Command unit to execute action
     */
    void executeAction(AUnit unit, Game game);
}
