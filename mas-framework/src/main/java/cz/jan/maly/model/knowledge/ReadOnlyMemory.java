package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.AgentTypeKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.commitment.CommitmentTreeReadOnly;
import lombok.Getter;

import java.util.Map;

/**
 * Represent another agent's memory - it is intended as read only
 * Created by Jan on 24-Feb-17.
 */
public class ReadOnlyMemory extends Memory<CommitmentTreeReadOnly> {

    @Getter
    private final Agent owner;

    ReadOnlyMemory(Map<FactKey, Fact> factParameterMap, Map<FactKey, FactSet> factSetParameterMap, CommitmentTreeReadOnly commitmentTree, Agent owner, AgentTypeKey agentTypeKey) {
        super(factParameterMap, factSetParameterMap, commitmentTree, agentTypeKey);
        this.owner = owner;
    }
}
