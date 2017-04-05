package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.planing.tree.Tree;
import cz.jan.maly.model.servicies.beliefs.ReadOnlyMemoryRegister;
import cz.jan.maly.utils.MyLogger;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents agent's own memory
 * Created by Jan on 24-Feb-17.
 */
public class WorkingMemory extends Memory<Tree> {

    public WorkingMemory(Tree tree, AgentType agentType, int agentId) {
        super(tree, agentType, agentId);
    }

    /**
     * Adds knowledge
     *
     * @param readOnlyMemoryRegister
     */
    public void addKnowledge(ReadOnlyMemoryRegister readOnlyMemoryRegister) {
        Map<AgentType, Map<Integer, ReadOnlyMemory>> sharedKnowledgeByOtherAgents = readOnlyMemoryRegister.formKnowledge();
        this.sharedKnowledgeByOtherAgents = sharedKnowledgeByOtherAgents.values().stream()
                .flatMap(integerReadOnlyMemoryMap -> integerReadOnlyMemoryMap.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.sharedKnowledgeByOtherAgentsTypes = sharedKnowledgeByOtherAgents.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(ReadOnlyMemory::getAgentType, Collectors.toSet()));
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
                tree.getReadOnlyCopy(), agentType, agentId, sharedKnowledgeByOtherAgentsTypes, sharedKnowledgeByOtherAgents);
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
            MyLogger.getLogger().warning("Given key is not present!");
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
            MyLogger.getLogger().warning("Given key is not present!");
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
            MyLogger.getLogger().warning("Given key is not present!");
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
            MyLogger.getLogger().warning("Given key is not present!");
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
            MyLogger.getLogger().warning("Given key is not present!");
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
            MyLogger.getLogger().warning("Given key is not present!");
        }
    }
}
