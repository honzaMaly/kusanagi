package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.FactObtainingInterface;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Optional;
import java.util.Set;

/**
 * Created by Jan on 14-Feb-17.
 */
public class Beliefs implements FactObtainingInterface {

    public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
        return Optional.empty();
    }

    public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
        return Optional.empty();
    }

}
