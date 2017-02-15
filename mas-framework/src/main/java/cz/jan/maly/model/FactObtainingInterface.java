package cz.jan.maly.model;

import cz.jan.maly.model.metadata.FactKey;

import java.util.Optional;
import java.util.Set;

/**
 * Interface to be implemented by each class working with facts. Framework runs in multithreaded and is design to be
 * error prone so copy of value contained in fact is returned.
 * Created by Jan on 14-Feb-17.
 */
public interface FactObtainingInterface {

    /**
     * Returns fact value for given fact key if it exists
     *
     * @param factKey
     * @param <V>
     * @return
     */
    <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey);

    /**
     * Returns fact values as set for given fact key if it exists
     *
     * @param factKey
     * @param <V>
     * @param <S>
     * @return
     */
    <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey);

}
