package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.PlanningTreeInterface;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Template class for memory - it stores facts, sets of facts, agent type and commitment process in form of tree, and provide
 * access to this data
 * Created by Jan on 16-Feb-17.
 */
public abstract class Memory<V extends PlanningTreeInterface> implements FactContainerInterface, PlanningTreeInterface {
    final Map<FactKey, Fact> factParameterMap = new HashMap<>();
    final Map<FactKey, FactSet> factSetParameterMap = new HashMap<>();
    final V tree;

    @Getter
    final AgentType agentType;

    @Getter
    final int agentId;

    Memory(Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, V tree, AgentType agentType, int agentId) {
        this.tree = tree;
        this.agentType = agentType;
        this.agentId = agentId;
        parametersTypesForFact.forEach(factKey -> this.factParameterMap.put(factKey, factKey.returnEmptyFact()));
        parametersTypesForFactSets.forEach(factKey -> this.factSetParameterMap.put(factKey, factKey.returnEmptyFactSet()));
    }

    /**
     * To make read only copy...
     *  @param factParameterMap
     * @param factSetParameterMap
     * @param tree
     * @param agentType
     * @param agentId
     */
    Memory(Map<FactKey, Fact> factParameterMap, Map<FactKey, FactSet> factSetParameterMap, V tree, AgentType agentType, int agentId) {
        this.tree = tree;
        this.agentType = agentType;
        this.agentId = agentId;
        factParameterMap.forEach((factKey, o) -> this.factParameterMap.put(factKey, o.copyFact()));
        factSetParameterMap.forEach((factKey, set) -> this.factSetParameterMap.put(factKey, set.copyFact()));
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
}

