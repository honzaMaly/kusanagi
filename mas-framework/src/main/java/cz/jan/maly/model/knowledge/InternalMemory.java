package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.FactObtainingInterface;
import cz.jan.maly.model.metadata.FactKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Class represents agent's own memory - it stores facts and sets of facts
 * Created by Jan on 16-Feb-17.
 */
public class InternalMemory implements Memory<InternalMemory>, FactObtainingInterface {
    private final Map<FactKey, Fact> factParameterMap;
    private final Map<FactKey, FactSet> factSetParameterMap;

    public InternalMemory(Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets) {
        this.factParameterMap = new HashMap<>();
        parametersTypesForFact.forEach(factKey -> this.factParameterMap.put(factKey, factKey.returnEmptyFact()));
        this.factSetParameterMap = new HashMap<>();
        parametersTypesForFactSets.forEach(factKey -> this.factSetParameterMap.put(factKey, factKey.returnEmptyFactSet()));
    }

    private InternalMemory(Map<FactKey, Fact> factParameterMap, Map<FactKey, FactSet> factSetParameterMap) {
        this.factParameterMap = new HashMap<>();
        factParameterMap.forEach((factKey, o) -> this.factParameterMap.put(factKey, o.copyFact()));
        this.factSetParameterMap = new HashMap<>();
        factSetParameterMap.forEach((factKey, set) -> this.factSetParameterMap.put(factKey, set.copyFact()));
    }

    @Override
    public InternalMemory cloneMemory() {
        return new InternalMemory(factParameterMap, factSetParameterMap);
    }

    /**
     * Method erases no longer relevant information
     */
    public void forget() {
        factParameterMap.values().forEach(Fact::forget);
        factSetParameterMap.values().forEach(FactSet::forget);
    }

    /**
     * Update fact value
     *
     * @param factToUpdate
     * @param <V>
     */
    public <V> void updateFact(Fact<V> factToUpdate) {
        Fact<V> fact = factParameterMap.get(factToUpdate.getType());
        if (fact != null) {
            fact.addFact(factToUpdate.getContent());
        }
    }

    /**
     * Erase fact value under given key
     *
     * @param factKey
     * @param <V>
     */
    public <V> void eraseFactValueForGivenKey(FactKey<V> factKey) {
        Fact<V> fact = factParameterMap.get(factKey);
        if (fact != null) {
            fact.removeFact();
        }
    }

    /**
     * Update fact value
     *
     * @param factToAdd
     * @param <V>
     */
    public <V> void updateFactSetByFact(Fact<V> factToAdd) {
        FactSet<V> factSet = factSetParameterMap.get(factToAdd.getType());
        if (factSet != null) {
            factSet.addFact(factToAdd.getContent());
        }
    }

    /**
     * Erase fact from set
     *
     * @param factToRemove
     * @param <V>
     */
    public <V> void eraseFactFromFactSet(Fact<V> factToRemove) {
        FactSet<V> factSet = factSetParameterMap.get(factToRemove.getType());
        if (factSet != null) {
            factSet.removeFact(factToRemove.getContent());
        }
    }

    /**
     * Erase fact set under given key
     *
     * @param factKey
     * @param <V>
     */
    public <V> void eraseFactSetForGivenKey(FactKey<V> factKey) {
        FactSet<V> factSet = factSetParameterMap.get(factKey);
        if (factSet != null) {
            factSet.eraseSet();
        }
    }

    @Override
    public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
        Fact<V> fact = factParameterMap.get(factKey);
        if (fact != null) {
            return Optional.of(fact.getContent());
        }
        return Optional.empty();
    }

    @Override
    public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
        FactSet<V> factSet = factSetParameterMap.get(factKey);
        if (factSet != null) {
            return Optional.of((S) factSet.getContent());
        }
        return Optional.empty();
    }
}

