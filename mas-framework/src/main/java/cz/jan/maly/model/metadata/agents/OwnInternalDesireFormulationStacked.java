package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.Intention;
import cz.jan.maly.model.planing.InternalDesire;

import java.util.Optional;

/**
 * Contract for stacked desire formulations - which forms desires from agent's memory and based on parent's type
 * Created by Jan on 11-Mar-17.
 */
interface OwnInternalDesireFormulationStacked<T extends InternalDesire<? extends Intention<T>>> {

    /**
     * Form desire of given key with data initialized from memory depending on parent's key. If no parent is specified
     * method simple desire will be formed instead
     *
     * @param parentKey
     * @param key
     * @param memory
     * @return
     */
    Optional<T> formDesire(DesireKey parentKey, DesireKey key, WorkingMemory memory);

}
