package cz.jan.maly.model.agent.action.game;

import cz.jan.maly.model.game.wrappers.AUnit;

/**
 * Abstract class defines strategy describing action to be executed in game
 * Created by Jan on 17-Dec-16.
 */
public abstract class Action {
    protected final AUnit unit;

    protected Action(AUnit unit) {
        this.unit = unit;
    }

    /**
     * Command unit to execute action
     */
    public abstract void executeAction();
}
