package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.AgentTypeKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.commitment.CommitmentTreeInWorkingMemory;

import java.util.Set;

/**
 * Represents agent's own memory
 * Created by Jan on 24-Feb-17.
 */
public class WorkingMemory extends Memory<CommitmentTreeInWorkingMemory> {
    public WorkingMemory(Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, CommitmentTreeInWorkingMemory commitmentTree, AgentTypeKey agentTypeKey) {
        super(parametersTypesForFact, parametersTypesForFactSets, commitmentTree, agentTypeKey);
    }

    /**
     * Return read only copy of working memory to be shared
     *
     * @param owner
     * @return
     */
    public ReadOnlyMemory cloneMemory(Agent owner) {
        forget();
        return new ReadOnlyMemory(factParameterMap, factSetParameterMap, commitmentTree.getReadOnlyCopy(), owner, agentTypeKey);
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
}
