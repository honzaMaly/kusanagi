package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.PlanningTreeInterface;
import cz.jan.maly.model.metadata.AgentTypeKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.planing.SharedDesire;
import cz.jan.maly.model.planing.SharedDesireForAgents;
import cz.jan.maly.model.planing.SharedDesireInRegister;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Template class for memory - it stores facts, sets of facts, agent type and commitment process in form of tree, and provide
 * access to this data
 * Created by Jan on 16-Feb-17.
 */
public abstract class Memory<V extends PlanningTreeInterface> implements FactContainerInterface {
    final Map<FactKey, Fact> factParameterMap = new HashMap<>();
    final Map<FactKey, FactSet> factSetParameterMap = new HashMap<>();
    final V tree;
    final AgentTypeKey agentTypeKey;
    final Set<SharedDesireInRegister> sharedDesires;
    final Set<SharedDesireForAgents> sharedDesiresByOtherAgents;

    //todo wrappers around tree + agent type

    Memory(Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, V tree, AgentTypeKey agentTypeKey) {
        this.tree = tree;
        this.agentTypeKey = agentTypeKey;
        this.sharedDesires = new HashSet<>();
        this.sharedDesiresByOtherAgents = new HashSet<>();
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
     * @param sharedDesires
     * @param sharedDesiresByOtherAgents
     */
    Memory(Map<FactKey, Fact> factParameterMap, Map<FactKey, FactSet> factSetParameterMap, V tree, AgentTypeKey agentTypeKey, Set<SharedDesireInRegister> sharedDesires, Set<SharedDesireForAgents> sharedDesiresByOtherAgents) {
        this.tree = tree;
        this.agentTypeKey = agentTypeKey;
        this.sharedDesires = sharedDesires;
        this.sharedDesiresByOtherAgents = sharedDesiresByOtherAgents;
        factParameterMap.forEach((factKey, o) -> this.factParameterMap.put(factKey, o.copyFact()));
        factSetParameterMap.forEach((factKey, set) -> this.factSetParameterMap.put(factKey, set.copyFact()));
    }

    /**
     * Return count of shared desires by other agents
     *
     * @return
     */
    public int countOfSharedDesiresByOtherAgents() {
        return sharedDesires.size();
    }

    /**
     * Return count of shared desires
     *
     * @return
     */
    public int countOfSharedDesires() {
        return sharedDesires.size();
    }

    /**
     * Get set of desires type shared by other agents
     *
     * @return
     */
    public Set<DesireParameters> sharedDesiresParametersByOtherAgents() {
        return sharedDesiresByOtherAgents.stream()
                .map(SharedDesire::getDesireParameters)
                .collect(Collectors.toSet());
    }

    /**
     * Get set of desires type shared by this agent
     *
     * @return
     */
    public Set<DesireParameters> sharedDesiresParameters() {
        return sharedDesires.stream()
                .map(SharedDesire::getDesireParameters)
                .collect(Collectors.toSet());
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

