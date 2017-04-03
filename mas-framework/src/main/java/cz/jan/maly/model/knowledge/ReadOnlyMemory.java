package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Map;
import java.util.Set;

/**
 * Represent another agent's memory - it is intended as read only
 * Created by Jan on 24-Feb-17.
 */
public class ReadOnlyMemory extends Memory<PlanningTreeOfAnotherAgent> {
    ReadOnlyMemory(Map<FactKey, Fact> factParameterMap, Map<FactKey, FactSet> factSetParameterMap, PlanningTreeOfAnotherAgent tree, AgentType agentType, int agentId, Map<AgentType, Set<ReadOnlyMemory>> sharedKnowledgeByOtherAgentsTypes, Map<Integer, ReadOnlyMemory> sharedKnowledgeByOtherAgents) {
        super(factParameterMap, factSetParameterMap, tree, agentType, agentId, sharedKnowledgeByOtherAgentsTypes, sharedKnowledgeByOtherAgents);
    }
}
