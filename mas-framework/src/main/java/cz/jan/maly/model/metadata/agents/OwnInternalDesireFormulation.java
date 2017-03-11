package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.Intention;
import cz.jan.maly.model.planing.InternalDesire;

import java.util.Optional;

/**
 * Contract for desire formulations - which forms desires from agent's memory
 * Created by Jan on 11-Mar-17.
 */
interface OwnInternalDesireFormulation<T extends InternalDesire<? extends Intention<T>>> {

    /**
     * Form desire of given key with data initialized from memory
     *
     * @param key
     * @param memory
     * @return
     */
    Optional<T> formDesire(DesireKey key, Memory memory);
}
