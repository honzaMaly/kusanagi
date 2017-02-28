package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.PlanningTreeInterface;
import cz.jan.maly.model.metadata.AgentTypeKey;
import cz.jan.maly.model.metadata.FactKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Template class for memory - it stores facts, sets of facts, agent type and commitment process in form of tree, and provide
 * access to this data
 * Created by Jan on 16-Feb-17.
 */
abstract class Memory<V extends PlanningTreeInterface> implements FactContainerInterface {
    final Map<FactKey, Fact> factParameterMap = new HashMap<>();
    final Map<FactKey, FactSet> factSetParameterMap = new HashMap<>();
    final V tree;
    final AgentTypeKey agentTypeKey;

    //todo wrappers around tree + agent type

    Memory(Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, V tree, AgentTypeKey agentTypeKey) {
        this.tree = tree;
        this.agentTypeKey = agentTypeKey;
        parametersTypesForFact.forEach(factKey -> this.factParameterMap.put(factKey, factKey.returnEmptyFact()));
        parametersTypesForFactSets.forEach(factKey -> this.factSetParameterMap.put(factKey, factKey.returnEmptyFactSet()));
    }

    /**
     * To make read only copy...
     *
     * @param factParameterMap
     * @param factSetParameterMap
     * @param tree
     * @param agentTypeKey
     */
    Memory(Map<FactKey, Fact> factParameterMap, Map<FactKey, FactSet> factSetParameterMap, V tree, AgentTypeKey agentTypeKey) {
        this.tree = tree;
        this.agentTypeKey = agentTypeKey;
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
}

