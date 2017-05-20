package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.PlanningTreeInterface;
import cz.jan.maly.model.metadata.*;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Template class for memory - it stores facts, sets of facts, agent type and commitment process in form of heap, and provide
 * access to this data
 * Created by Jan on 16-Feb-17.
 */
public abstract class Memory<V extends PlanningTreeInterface> implements FactContainerInterface, PlanningTreeInterface {
    final Map<FactKey<?>, Fact<?>> factParameterMap = new HashMap<>();
    final Map<FactKey<?>, FactSet<?>> factSetParameterMap = new HashMap<>();
    final V tree;
    @Getter
    final AgentType agentType;
    @Getter
    final int agentId;
    final StrategyToGetSetOfMemoriesByAgentType strategyToGetSetOfMemoriesByAgentType;
    final StrategyToGetMemoryOfAgent strategyToGetMemoryOfAgent;
    final StrategyToGetAllMemories strategyToGetAllMemories;

    Memory(V tree, AgentType agentType, int agentId, StrategyToGetSetOfMemoriesByAgentType strategyToGetSetOfMemoriesByAgentType, StrategyToGetMemoryOfAgent strategyToGetMemoryOfAgent, StrategyToGetAllMemories strategyToGetAllMemories) {
        this.tree = tree;
        this.agentType = agentType;
        this.agentId = agentId;
        this.strategyToGetSetOfMemoriesByAgentType = strategyToGetSetOfMemoriesByAgentType;
        this.strategyToGetMemoryOfAgent = strategyToGetMemoryOfAgent;
        this.strategyToGetAllMemories = strategyToGetAllMemories;
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
     * @param strategyToGetSetOfMemoriesByAgentType
     * @param strategyToGetMemoryOfAgent
     * @param strategyToGetAllMemories
     */
    Memory(Map<FactKey, Fact> factParameterMap, Map<FactKey, FactSet> factSetParameterMap, V tree, AgentType agentType, int agentId,
           StrategyToGetSetOfMemoriesByAgentType strategyToGetSetOfMemoriesByAgentType, StrategyToGetMemoryOfAgent strategyToGetMemoryOfAgent,
           StrategyToGetAllMemories strategyToGetAllMemories) {
        this.tree = tree;
        this.agentType = agentType;
        this.agentId = agentId;
        this.strategyToGetSetOfMemoriesByAgentType = strategyToGetSetOfMemoriesByAgentType;
        this.strategyToGetMemoryOfAgent = strategyToGetMemoryOfAgent;
        this.strategyToGetAllMemories = strategyToGetAllMemories;
        factParameterMap.forEach((factKey, o) -> this.factParameterMap.put(factKey, o.copyFact()));
        factSetParameterMap.forEach((factKey, set) -> this.factSetParameterMap.put(factKey, set.copyFact()));
    }

    public boolean isFactKeyForValueInMemory(FactKey<?> factKey) {
        return factParameterMap.containsKey(factKey);
    }

    public boolean isFactKeyForSetInMemory(FactKey<?> factKey) {
        return factSetParameterMap.containsKey(factKey);
    }

    public Optional<ReadOnlyMemory> getReadOnlyMemoryForAgent(int agentId) {
        return strategyToGetMemoryOfAgent.getReadOnlyMemoryForAgent(agentId);
    }

    public Stream<ReadOnlyMemory> getReadOnlyMemoriesForAgentType(AgentTypeID agentType) {
        return strategyToGetSetOfMemoriesByAgentType.getReadOnlyMemoriesForAgentType(agentType);
    }

    public Stream<ReadOnlyMemory> getReadOnlyMemoriesForAgentType(AgentType agentType) {
        return strategyToGetSetOfMemoriesByAgentType.getReadOnlyMemoriesForAgentType(agentType.getAgentTypeID());
    }

    public Stream<ReadOnlyMemory> getReadOnlyMemories() {
        return strategyToGetAllMemories.getReadOnlyMemories();
    }

    @Override
    public <K> Optional<K> returnFactValueForGivenKey(FactKey<K> factKey) {
        Fact<K> fact = (Fact<K>) factParameterMap.get(factKey);
        if (fact != null) {
            return Optional.ofNullable(fact.getContent());
        }
        MyLogger.getLogger().warning(factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
        return Optional.empty();
    }

    /**
     * Returns copy of fact
     *
     * @param factKey
     * @param <K>
     * @return
     */
    public <K> Optional<Fact<K>> returnFactCopyForGivenKey(FactKey<K> factKey) {
        Fact<K> fact = (Fact<K>) factParameterMap.get(factKey);
        if (fact != null) {
            return Optional.ofNullable(fact.copyFact());
        }
        MyLogger.getLogger().warning(factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
        return Optional.empty();
    }

    @Override
    public <K, S extends Stream<K>> Optional<S> returnFactSetValueForGivenKey(FactKey<K> factKey) {
        FactSet<K> factSet = (FactSet<K>) factSetParameterMap.get(factKey);
        if (factSet != null) {
            return Optional.ofNullable((S) factSet.getContent().stream());
        }
        MyLogger.getLogger().warning(factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
        return Optional.empty();
    }

    /**
     * Returns copy of fact set
     *
     * @param factKey
     * @param <K>
     * @return
     */
    public <K> Optional<FactSet<K>> returnFactSetCopyForGivenKey(FactKey<K> factKey) {
        FactSet<K> factSet = (FactSet<K>) factSetParameterMap.get(factKey);
        if (factSet != null) {
            return Optional.ofNullable(factSet.copyFact());
        }
        MyLogger.getLogger().warning(factKey.getName() + " is not present in " + agentType.getName() + " type definition.");
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

    /**
     * Obtaining strategy
     */
    public interface StrategyToGetSetOfMemoriesByAgentType {
        /**
         * Get set of memories by agent type
         *
         * @param agentType
         * @return
         */
        Stream<ReadOnlyMemory> getReadOnlyMemoriesForAgentType(AgentTypeID agentType);
    }

    /**
     * Obtaining strategy
     */
    public interface StrategyToGetMemoryOfAgent {
        /**
         * Get ReadOnlyMemory by agent id
         *
         * @param agentId
         * @return
         */
        Optional<ReadOnlyMemory> getReadOnlyMemoryForAgent(int agentId);
    }

    /**
     * Obtaining strategy
     */
    public interface StrategyToGetAllMemories {
        /**
         * Get all beliefs in system
         *
         * @return
         */
        Stream<ReadOnlyMemory> getReadOnlyMemories();
    }

}

