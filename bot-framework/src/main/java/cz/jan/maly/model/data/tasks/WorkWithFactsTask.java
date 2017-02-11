package cz.jan.maly.model.data.tasks;

import cz.jan.maly.model.data.RoutineTask;

/**
 * Created by Jan on 11-Feb-17.
 */
public class WorkWithFactsTask implements RoutineTask {
    @Override
    public int getOrder() {
        return RoutineTaskTypeEnumerations.WORK_WITH_FACTS.ordinal();
    }
}
