package cz.jan.maly.model.planing;

import cz.jan.maly.model.DesireKeyIdentificationInterface;
import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.Optional;
import java.util.Set;

/**
 * Class describing template for desire. Desire instance represents high level abstraction of
 * what agent may want to achieve.
 * <p>
 * Created by Jan on 09-Feb-17.
 */
public abstract class Desire implements FactContainerInterface, DesireKeyIdentificationInterface {

    @Getter
    final DesireParameters desireParameters;

    Desire(DesireKey desireKey, Memory memory) {
        this.desireParameters = new DesireParameters(memory, desireKey);
    }

    Desire(DesireParameters desireParameters) {
        this.desireParameters = desireParameters;
    }

    public DesireKey getDesireKey() {
        return desireParameters.getDesireKey();
    }

    public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
        return desireParameters.returnFactValueForGivenKey(factKey);
    }

    public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
        return desireParameters.returnFactSetValueForGivenKey(factKey);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Desire)) return false;

        Desire desire = (Desire) o;

        return desireParameters.equals(desire.desireParameters);
    }

    @Override
    public int hashCode() {
        return desireParameters.hashCode();
    }
}
