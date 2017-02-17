package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.FactObtainingInterface;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.*;

import static cz.jan.maly.utils.FrameworkUtils.CLONER;

/**
 * Class describing template for desire. Desire instance represents high level abstraction of
 * what agent may want to achieve.
 * <p>
 * Created by Jan on 09-Feb-17.
 */
abstract class Desire implements FactObtainingInterface {
    final Map<FactKey, Object> factParameterMap;
    final Map<FactKey, Set> factSetParameterMap;

    @Getter
    final DesireKey desireKey;

    Desire(DesireKey desireKey, Agent agent) {
        this.desireKey = desireKey;

        //fill maps with actual parameters from beliefs
        factParameterMap = new HashMap<>();
        this.desireKey.getParametersTypesForFacts()
                .forEach(factKey -> {
                    Optional<?> value = agent.getBeliefs().returnFactValueForGivenKey(factKey);
                    value.ifPresent(o -> factParameterMap.put(factKey, o));
                });
        factSetParameterMap = new HashMap<>();
        this.desireKey.getParametersTypesForFactSets()
                .forEach(factKey -> {
                    Optional<Set> value = agent.getBeliefs().returnFactSetValueForGivenKey(factKey);
                    value.ifPresent(o -> factSetParameterMap.put(factKey, o));
                });
    }

    Desire(Map<FactKey, Object> factParameterMap, Map<FactKey, Set> factSetParameterMap, DesireKey desireKey) {
        this.factParameterMap = new HashMap<>();
        factParameterMap.forEach((factKey, o) -> this.factParameterMap.put(factKey, CLONER.deepClone(o)));
        this.factSetParameterMap = new HashMap<>();
        factSetParameterMap.forEach((factKey, set) -> this.factSetParameterMap.put(factKey, CLONER.deepClone(set)));
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
        if (!(o instanceof Desire)) return false;

        Desire desire = (Desire) o;

        if (!factParameterMap.equals(desire.factParameterMap)) return false;
        if (!factSetParameterMap.equals(desire.factSetParameterMap)) return false;
        return desireKey.equals(desire.desireKey);
    }

    @Override
    public int hashCode() {
        int result = factParameterMap.hashCode();
        result = 31 * result + factSetParameterMap.hashCode();
        result = 31 * result + desireKey.hashCode();
        return result;
    }
}
