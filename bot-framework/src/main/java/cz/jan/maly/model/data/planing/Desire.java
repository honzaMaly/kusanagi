package cz.jan.maly.model.data.planing;

import cz.jan.maly.model.data.Key;
import cz.jan.maly.model.data.RoutineTask;
import lombok.Getter;

/**
 * Class describing template for desire. Desire instance represents metadata and high level abstraction of
 * what agent may want to achieve. When agent commits to desire it is converted to intention.
 * <p>
 * Created by Jan on 09-Feb-17.
 */
abstract class Desire<T extends RoutineTask> extends Key {

    @Getter
    private final boolean isNumberOfAgentsCommittedToThisLimited;

    @Getter
    private final int limitOnAgentsToCommit;

    @Getter
    private final T ofRoutineTaskType;

    @Getter
    private final boolean isShared;

    public Desire(String name, int id, T ofRoutineTaskType, int limitOnAgentsToCommit, boolean isShared) {
        super(name, id);
        this.ofRoutineTaskType = ofRoutineTaskType;
        this.limitOnAgentsToCommit = limitOnAgentsToCommit;
        this.isShared = isShared;
        this.isNumberOfAgentsCommittedToThisLimited = true;
    }

    public Desire(String name, int id, T ofRoutineTaskType, boolean isShared) {
        super(name, id);
        this.ofRoutineTaskType = ofRoutineTaskType;
        this.isShared = isShared;
        this.isNumberOfAgentsCommittedToThisLimited = false;
        this.limitOnAgentsToCommit = 0;
    }

    //todo add parameters - specific for this desire (ex. where to move, what to build,...)

    //todo handle commitment. each agent has its own - factory?. optional, return empty intention when it is not possible to commit or intention for agent is not define. should be synchronized and keep track of commitment

    //todo specify when to take back for shared desire. How to synchronized... do it in same way as for request???K

}
