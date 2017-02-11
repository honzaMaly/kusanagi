package cz.jan.maly.model.data.planing;

import cz.jan.maly.model.data.tasks.ActInGameTask;

/**
 * Created by Jan on 11-Feb-17.
 */
public abstract class ActInGameDesire extends Desire<ActInGameTask> {

    public ActInGameDesire(String name, int id, ActInGameTask ofRoutineTaskType, boolean isShared) {
        super(name, id, ofRoutineTaskType, isShared);
    }

}
