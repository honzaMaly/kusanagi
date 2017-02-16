package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.FactObtainingInterface;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Class describing template for intention. Intention instance represents metadata and high level abstraction of
 * what agent has committed to achieve to. It may contain other desires related to this intention to be consider.
 * Created by Jan on 10-Feb-17.
 */
abstract class Intention<T extends Desire> implements FactObtainingInterface {
    private final Map<FactKey, Object> factParameterMap = new HashMap<>();
    private final Map<FactKey, Set> factSetParameterMap = new HashMap<>();

    @Getter
    private final Desire<T> originalDesire;

    Intention(Desire<T> originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent) {
        this.originalDesire = originalDesire;
        parametersTypesForFact.forEach(factKey -> {
            Optional<?> value = agent.getBeliefs().returnFactValueForGivenKey(factKey);
            value.ifPresent(o -> factParameterMap.put(factKey, o));
        });
        parametersTypesForFactSets.forEach(factKey -> {
            Optional<Set> value = agent.getBeliefs().returnFactSetValueForGivenKey(factKey);
            value.ifPresent(o -> factSetParameterMap.put(factKey, o));
        });
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

}