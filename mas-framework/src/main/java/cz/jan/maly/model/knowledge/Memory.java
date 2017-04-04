package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.PlanningTreeInterface;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Template class for memory - it stores facts, sets of facts, agent type and commitment process in form of tree, and provide
 * access to this data
 * Created by Jan on 16-Feb-17.
 */
public abstract class Memory<V extends PlanningTreeInterface> implements FactContainerInterface, PlanningTreeInterface {
    final Map<FactKey<?>, Fact<?>> factParameterMap = new HashMap<>();
    final Map<FactKey<?>, FactSet<?>> factSetParameterMap = new HashMap<>();
    Map<AgentType, Set<ReadOnlyMemory>> sharedKnowledgeByOtherAgentsTypes = new HashMap<>();
    Map<Integer, ReadOnlyMemory> sharedKnowledgeByOtherAgents = new HashMap<>();
    final V tree;

    @Getter
    final AgentType agentType;

    @Getter
    final int agentId;

    Memory(V tree, AgentType agentType, int agentId) {
        this.tree = tree;
        this.agentType = agentType;
        this.agentId = agentId;
        agentType.getUsingTypesForFacts().forEach(factKey -> this.factParameterMap.put(factKey, factKey.returnEmptyFact()));
        agentType.getUsingTypesForFactSets().forEach(factKey -> this.factSetParameterMap.put(factKey, factKey.returnEmptyFactSet()));
    }

    /**
     * To make read only copy...
     *
     * @param factParameterMap
     * @param factSetParameterMap
     * @param tree
     * @param agentType
     * @param agentId
     */
    Memory(Map<FactKey, Fact> factParameterMap, Map<FactKey, FactSet> factSetParameterMap, V tree, AgentType agentType, int agentId, Map<AgentType, Set<ReadOnlyMemory>> sharedKnowledgeByOtherAgentsTypes, Map<Integer, ReadOnlyMemory> sharedKnowledgeByOtherAgents) {
        this.tree = tree;
        this.agentType = agentType;
        this.agentId = agentId;
        factParameterMap.forEach((factKey, o) -> this.factParameterMap.put(factKey, o.copyFact()));
        factSetParameterMap.forEach((factKey, set) -> this.factSetParameterMap.put(factKey, set.copyFact()));
        sharedKnowledgeByOtherAgentsTypes.forEach((agentT, readOnlyMemories) -> this.sharedKnowledgeByOtherAgentsTypes.put(agentT, readOnlyMemories.stream().collect(Collectors.toSet())));
        sharedKnowledgeByOtherAgents.forEach((integer, readOnlyMemory) -> this.sharedKnowledgeByOtherAgents.put(integer, readOnlyMemory));
    }

    public Optional<ReadOnlyMemory> getReadOnlyMemoryForAgent(int agentId) {
        return Optional.ofNullable(sharedKnowledgeByOtherAgents.get(agentId));
    }

    public Set<ReadOnlyMemory> getReadOnlyMemoriesForAgentType(AgentType agentType) {
        return sharedKnowledgeByOtherAgentsTypes.getOrDefault(agentType, new HashSet<>());
    }

    public Set<ReadOnlyMemory> getReadOnlyMemories() {
        return new HashSet<>(sharedKnowledgeByOtherAgents.values());
    }

    @Override
    public <K> Optional<K> returnFactValueForGivenKey(FactKey<K> factKey) {
        Fact<K> fact = (Fact<K>) factParameterMap.get(factKey);
        if (fact != null) {
            return Optional.ofNullable(fact.getContent());
        }
        MyLogger.getLogger().warning("Given key is not present!");
        return Optional.empty();
    }

    /**
     * Returns copy of fact
     * @param factKey
     * @param <K>
     * @return
     */
    public <K> Optional<Fact<K>> returnFactCopyForGivenKey(FactKey<K> factKey) {
        Fact<K> fact = (Fact<K>) factParameterMap.get(factKey);
        if (fact != null) {
            return Optional.ofNullable(fact.copyFact());
        }
        MyLogger.getLogger().warning("Given key is not present!");
        return Optional.empty();
    }

    @Override
    public <K, S extends Set<K>> Optional<S> returnFactSetValueForGivenKey(FactKey<K> factKey) {
        FactSet<K> factSet = (FactSet<K>) factSetParameterMap.get(factKey);
        if (factSet != null) {
            return Optional.ofNullable((S) factSet.getContent());
        }
        MyLogger.getLogger().warning("Given key is not present!");
        return Optional.empty();
    }

    /**
     * Returns copy of fact set
     * @param factKey
     * @param <K>
     * @return
     */
    public <K> Optional<FactSet<K>> returnFactSetCopyForGivenKey(FactKey<K> factKey) {
        FactSet<K> factSet = (FactSet<K>) factSetParameterMap.get(factKey);
        if (factSet != null) {
            return Optional.ofNullable(factSet.copyFact());
        }
        MyLogger.getLogger().warning("Given key is not present!");
        return Optional.empty();
    }

    @Override
    public Set<DesireParameters> committedSharedDesiresParametersByOtherAgents() {
        return tree.committedSharedDesiresParametersByOtherAgents();
    }

    @Override
    public Set<DesireParameters> sharedDesiresParameters() {
        return tree.sharedDesiresParameters();
    }

    @Override
    public int countOfCommittedSharedDesiresByOtherAgents() {
        return tree.countOfCommittedSharedDesiresByOtherAgents();
    }

    @Override
    public int countOfSharedDesires() {
        return tree.countOfSharedDesires();
    }

    @Override
    public Map<DesireKey, Long> collectKeysOfCommittedDesiresInTreeCounts() {
        return tree.collectKeysOfCommittedDesiresInTreeCounts();
    }

    @Override
    public Map<DesireKey, Long> collectKeysOfDesiresInTreeCounts() {
        return tree.collectKeysOfDesiresInTreeCounts();
    }

    @Override
    public Set<DesireParameters> getParametersOfCommittedDesiresOnTopLevel() {
        return tree.getParametersOfCommittedDesiresOnTopLevel();
    }

    @Override
    public Set<DesireParameters> getParametersOfDesiresOnTopLevel() {
        return tree.getParametersOfDesiresOnTopLevel();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Memory)) return false;

        Memory<?> memory = (Memory<?>) o;

        if (agentId != memory.agentId) return false;
        return agentType.equals(memory.agentType);
    }

    @Override
    public int hashCode() {
        int result = agentType.hashCode();
        result = 31 * result + agentId;
        return result;
    }
}

