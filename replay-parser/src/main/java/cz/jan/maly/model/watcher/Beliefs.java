package cz.jan.maly.model.watcher;

import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.model.knowledge.FactSet;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValue;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValueSet;
import cz.jan.maly.utils.MyLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Class to represent agent's beliefs
 * Created by Jan on 17-Apr-17.
 */
public class Beliefs {

    //own beliefs
    private final Map<FactKey<?>, Fact<?>> facts = new HashMap<>();
    private final Map<FactKey<?>, FactSet<?>> factSets = new HashMap<>();

    Beliefs(AgentWatcherType type) {
        type.getFactKeys().forEach(factKey -> facts.put(factKey, factKey.returnEmptyFact()));
        type.getFactSetsKeys().forEach(factKey -> factSets.put(factKey, factKey.returnEmptyFactSet()));
    }

    public <K> Optional<K> returnFactValueForGivenKey(FactKey<K> factKey) {
        Fact<K> fact = (Fact<K>) facts.get(factKey);
        if (fact != null) {
            return Optional.ofNullable(fact.getContent());
        }
        MyLogger.getLogger().warning(factKey.getName() + " is not present.");
        return Optional.empty();
    }

    public <K, S extends Stream<?>> Optional<S> returnFactSetValueForGivenKey(FactKey<K> factKey) {
        FactSet<K> factSet = (FactSet<K>) factSets.get(factKey);
        if (factSet != null) {
            return Optional.ofNullable((S) factSet.getContent().stream());
        }
        MyLogger.getLogger().warning(factKey.getName() + " is not present.");
        return Optional.empty();
    }

    /**
     * Convert fact to feature value
     *
     * @param convertingStrategy
     * @param <V>
     * @return
     */
    public <V> double getFeatureValueOfFact(FactWithOptionalValue<V> convertingStrategy) {
        if (!facts.containsKey(convertingStrategy.getFactKey())) {
            MyLogger.getLogger().warning(convertingStrategy.getFactKey().getName() + " is not present in.");
            throw new RuntimeException(convertingStrategy.getFactKey().getName());
        }
        return convertingStrategy.getStrategyToObtainValue().returnRawValue(Optional.ofNullable((V) facts.get(convertingStrategy.getFactKey()).getContent()));
    }

    /**
     * Convert fact set to feature value
     *
     * @param convertingStrategy
     * @param <V>
     * @return
     */
    public <V> double getFeatureValueOfFactSet(FactWithOptionalValueSet<V> convertingStrategy) {
        if (!factSets.containsKey(convertingStrategy.getFactKey())) {
            MyLogger.getLogger().warning(convertingStrategy.getFactKey().getName() + " is not present in.");
            throw new RuntimeException(convertingStrategy.getFactKey().getName());
        }
        return convertingStrategy.getStrategyToObtainValue().returnRawValue(Optional.ofNullable(((Set<V>)factSets.get(convertingStrategy.getFactKey()).getContent()).stream()));
    }

    /**
     * Update fact value
     *
     * @param factKey
     * @param value
     * @param <V>
     */
    public <V> void updateFact(FactKey<V> factKey, V value) {
        Fact<V> fact = (Fact<V>) facts.get(factKey);
        if (fact != null) {
            fact.addFact(value);
        } else {
            MyLogger.getLogger().warning(factKey.getName() + " is not present in.");
            throw new RuntimeException(factKey.getName());
        }
    }

    /**
     * Erase fact value under given key
     *
     * @param factKey
     * @param <V>
     */
    public <V> void eraseFactValueForGivenKey(FactKey<V> factKey) {
        Fact<V> fact = (Fact<V>) facts.get(factKey);
        if (fact != null) {
            fact.removeFact();
        } else {
            MyLogger.getLogger().warning(factKey.getName() + " is not present in.");
            throw new RuntimeException(factKey.getName());
        }
    }

    /**
     * Update fact value
     *
     * @param factKey
     * @param value
     * @param <V>
     */
    public <V> void updateFactSetByFact(FactKey<V> factKey, V value) {
        FactSet<V> factSet = (FactSet<V>) factSets.get(factKey);
        if (factSet != null) {
            factSet.addFact(value);
        } else {
            MyLogger.getLogger().warning(factKey.getName() + " is not present in.");
            throw new RuntimeException(factKey.getName());
        }
    }

    /**
     * Update fact value
     *
     * @param factKey
     * @param values
     * @param <V>
     */
    public <V> void updateFactSetByFacts(FactKey<V> factKey, Set<V> values) {
        FactSet<V> factSet = (FactSet<V>) factSets.get(factKey);
        if (factSet != null) {
            factSet.eraseSet();
            values.forEach(factSet::addFact);
        } else {
            MyLogger.getLogger().warning(factKey.getName() + " is not present in.");
            throw new RuntimeException(factKey.getName());
        }
    }

    /**
     * Erase fact from set
     *
     * @param factKey
     * @param value
     * @param <V>
     */
    public <V> void eraseFactFromFactSet(FactKey<V> factKey, V value) {
        FactSet<V> factSet = (FactSet<V>) factSets.get(factKey);
        if (factSet != null) {
            factSet.removeFact(value);
        } else {
            MyLogger.getLogger().warning(factKey.getName() + " is not present in.");
            throw new RuntimeException(factKey.getName());
        }
    }

    /**
     * Erase fact set under given key
     *
     * @param factKey
     * @param <V>
     */
    public <V> void eraseFactSetForGivenKey(FactKey<V> factKey) {
        FactSet<V> factSet = (FactSet<V>) factSets.get(factKey);
        if (factSet != null) {
            factSet.eraseSet();
        } else {
            MyLogger.getLogger().warning(factKey.getName() + " is not present in.");
            throw new RuntimeException(factKey.getName());
        }
    }

    public boolean isFactKeyForValueInMemory(FactKey<?> factKey) {
        return facts.containsKey(factKey);
    }

    public boolean isFactKeyForSetInMemory(FactKey<?> factKey) {
        return factSets.containsKey(factKey);
    }

}
