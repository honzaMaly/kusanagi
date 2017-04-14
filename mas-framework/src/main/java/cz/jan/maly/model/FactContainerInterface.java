package cz.jan.maly.model;

import cz.jan.maly.model.metadata.FactKey;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Interface to be implemented by each class working with facts. Returned values are references on actual object (not copy
 * of them) and is user responsibility to use them as read only as it can lead to great mess (concurrency exception, unpredictable
 * behaviour) as facts can be accessed by various threads (copy is not returned due to performance issues)
 * Created by Jan on 14-Feb-17.
 */
public interface FactContainerInterface {

    /**
     * Returns read only fact value for given fact key if it exists
     *
     * @param factKey
     * @param <V>
     * @return
     */
    <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey);

    /**
     * Returns read only fact values as set for given fact key if it exists
     *
     * @param factKey
     * @param <V>
     * @return
     */
    <V, S extends Stream<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey);

}
