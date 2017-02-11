package cz.jan.maly.model.data.planing;

import cz.jan.maly.model.data.knowledge_representation.FactKey;
import cz.jan.maly.model.data.RoutineTask;
import lombok.Getter;

import java.util.List;

/**
 * Class describing template for intention. Intention instance represents metadata and high level abstraction of
 * what agent has committed to achieve to. It may contain other desires related to this intention to be consider.
 * Created by Jan on 10-Feb-17.
 */
abstract class Intention<T extends RoutineTask> {

    @Getter
    final List<Desire> relatedDesiresToHave;

    @Getter
    final Desire<T> fromDesire;

    @Getter
    final List<FactKey> factKeysUsed;

    public Intention(Desire<T> desire, List<Desire> relatedDesiresToHave, List<FactKey> factKeysUsed) {
        this.relatedDesiresToHave = relatedDesiresToHave;
        this.fromDesire = desire;
        this.factKeysUsed = factKeysUsed;
    }

    public T getRoutineTaskType() {
        return fromDesire.getOfRoutineTaskType();
    }

    //todo factory of plan - based on race - atomic = execution/ or can be abstract without plan - only to add other desires - as plan

}
