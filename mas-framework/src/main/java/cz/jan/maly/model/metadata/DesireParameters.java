package cz.jan.maly.model.metadata;

import cz.jan.maly.model.DesireKeyIdentificationInterface;
import cz.jan.maly.model.FactContainerInterface;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Class to define container with parameters (facts) for desire classes to be accessed by it. As this class is read only
 * sharing it is thread safe
 * Created by Jan on 16-Feb-17.
 */
public class DesireParameters implements FactContainerInterface, DesireKeyIdentificationInterface {
    private final Map<FactKey, Object> factParameterMap;
    private final Map<FactKey, Set> factSetParameterMap;

    @Getter
    private final DesireKey desireKey;

    public DesireParameters(Map<FactKey, Object> factParameterMap, Map<FactKey, Set> factSetParameterMap, DesireKey desireKey) {
        this.factParameterMap = factParameterMap;
        this.factSetParameterMap = factSetParameterMap;
        this.desireKey = desireKey;
    }

    public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
        Object value = factParameterMap.get(factKey);
        if (value != null) {
            return Optional.of((V) value);
        }
        return Optional.empty();
    }

    public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
        Set values = factSetParameterMap.get(factKey);
        if (values != null) {
            return Optional.of((S) values);
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DesireParameters)) return false;

        DesireParameters that = (DesireParameters) o;

        if (!factParameterMap.equals(that.factParameterMap)) return false;
        if (!factSetParameterMap.equals(that.factSetParameterMap)) return false;
        return desireKey.equals(that.desireKey);
    }

    @Override
    public int hashCode() {
        int result = factParameterMap.hashCode();
        result = 31 * result + factSetParameterMap.hashCode();
        result = 31 * result + desireKey.hashCode();
        return result;
    }
}
