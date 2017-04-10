package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.planing.tree.Tree;
import cz.jan.maly.utils.MyLogger;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents agent's own memory
 * Created by Jan on 24-Feb-17.
 */
public class WorkingMemory extends Memory<Tree> {
    public WorkingMemory(Tree tree, AgentType agentType, int agentId,
                         StrategyToGetSetOfMemoriesByAgentType strategyToGetSetOfMemoriesByAgentType,
                         StrategyToGetMemoryOfAgent strategyToGetMemoryOfAgent, StrategyToGetAllMemories strategyToGetAllMemories) {
        super(tree, agentType, agentId, strategyToGetSetOfMemoriesByAgentType, strategyToGetMemoryOfAgent, strategyToGetAllMemories);
    }


    /**
     * Return read only copy of working memory to be shared
     *
     * @return
     */
    public ReadOnlyMemory cloneMemory() {
        forget();
        return new ReadOnlyMemory(factParameterMap.entrySet().stream()
                .filter(factKeyFactEntry -> !factKeyFactEntry.getKey().isPrivate())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                factSetParameterMap.entrySet().stream()
                        .filter(factKeyFactEntry -> !factKeyFactEntry.getKey().isPrivate())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                tree.getReadOnlyCopy(), agentType, agentId, strategyToGetSetOfMemoriesByAgentType, strategyToGetMemoryOfAgent, strategyToGetAllMemories);
    }

    /**
     * Method erases no longer relevant information
     */
    private void forget() {
        factParameterMap.values().forEach(Fact::forget);
        factSetParameterMap.values().forEach(FactSet::forget);
    }

    /**
     * Update fact value
     *
     * @param factKey
     * @param value
     * @param <V>
     */
    public <V> void updateFact(FactKey<V> factKey, V value) {
        Fact<V> fact = (Fact<V>) factParameterMap.get(factKey);
        if (fact != null) {
            fact.addFact(value);
        } else {
            MyLogger.getLogger().warning(factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
        }
    }

    /**
     * Erase fact value under given key
     *
     * @param factKey
     * @param <V>
     */
    public <V> void eraseFactValueForGivenKey(FactKey<V> factKey) {
        Fact<V> fact = (Fact<V>) factParameterMap.get(factKey);
        if (fact != null) {
            fact.removeFact();
        } else {
            MyLogger.getLogger().warning(factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
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
        FactSet<V> factSet = (FactSet<V>) factSetParameterMap.get(factKey);
        if (factSet != null) {
            factSet.addFact(value);
        } else {
            MyLogger.getLogger().warning(factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
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
        FactSet<V> factSet = (FactSet<V>) factSetParameterMap.get(factKey);
        if (factSet != null) {
            factSet.eraseSet();
            values.forEach(factSet::addFact);
        } else {
            MyLogger.getLogger().warning(factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
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
        FactSet<V> factSet = (FactSet<V>) factSetParameterMap.get(factKey);
        if (factSet != null) {
            factSet.removeFact(value);
        } else {
            MyLogger.getLogger().warning(factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
        }
    }

    /**
     * Erase fact set under given key
     *
     * @param factKey
     * @param <V>
     */
    public <V> void eraseFactSetForGivenKey(FactKey<V> factKey) {
        FactSet<V> factSet = (FactSet<V>) factSetParameterMap.get(factKey);
        if (factSet != null) {
            factSet.eraseSet();
        } else {
            MyLogger.getLogger().warning(factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
        }
    }
}
