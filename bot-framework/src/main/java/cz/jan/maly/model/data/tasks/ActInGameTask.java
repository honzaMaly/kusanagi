package cz.jan.maly.model.data.tasks;

import cz.jan.maly.model.data.RoutineTask;

/**
 * Created by Jan on 11-Feb-17.
 */
public abstract class ActInGameTask implements RoutineTask {

    @Override
    public int getOrder() {
        return RoutineTaskTypeEnumerations.ACT_IN_GAME.ordinal();
    }
}
