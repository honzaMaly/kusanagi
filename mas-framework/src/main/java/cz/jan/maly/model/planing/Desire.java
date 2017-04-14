package cz.jan.maly.model.planing;

import cz.jan.maly.model.DesireKeyIdentificationInterface;
import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Class describing template for desire. Desire instance represents high level abstraction of
 * what agent may want to achieve.
 * <p>
 * Created by Jan on 09-Feb-17.
 */
public abstract class Desire implements DesireKeyIdentificationInterface {

    @Getter
    final DesireParameters desireParameters;

    final int originatorId;

    Desire(DesireKey desireKey, Memory memory) {
        this.desireParameters = new DesireParameters(memory, desireKey);
        this.originatorId = memory.getAgentId();
    }

    Desire(DesireParameters desireParameters, int originatorId) {
        this.desireParameters = desireParameters;
        this.originatorId = originatorId;
    }

    public DesireKey getDesireKey() {
        return desireParameters.getDesireKey();
    }

    public <V> Optional<V> returnFactValueForGivenKeyInParameters(FactKey<V> factKey) {
        return desireParameters.returnFactValueForGivenKey(factKey);
    }

    public <V, S extends Stream<V>> Optional<S> returnFactSetValueForGivenKeyInParameters(FactKey<V> factKey) {
        return desireParameters.returnFactSetValueForGivenKey(factKey);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Desire)) return false;

        Desire desire = (Desire) o;

        if (originatorId != desire.originatorId) return false;
        return desireParameters.equals(desire.desireParameters);
    }

    @Override
    public int hashCode() {
        int result = desireParameters.hashCode();
        result = 31 * result + originatorId;
        return result;
    }
}
