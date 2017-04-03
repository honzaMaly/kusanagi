package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.planing.Intention;
import cz.jan.maly.model.planing.SharedDesireForAgents;

import java.util.Optional;

/**
 * Contract for desire formulations - which forms desires from shared desire by another agent
 * Created by Jan on 11-Mar-17.
 */
interface AnotherAgentsInternalDesireFormulation<T extends DesireFromAnotherAgent<? extends Intention<T>>> {

    /**
     * Form desire of given desire from another agent
     *
     * @param desireForAgents
     * @param memory
     * @return
     */
    Optional<T> formDesire(SharedDesireForAgents desireForAgents, WorkingMemory memory);

}
