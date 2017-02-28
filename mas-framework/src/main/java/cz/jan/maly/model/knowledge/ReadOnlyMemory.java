package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.metadata.AgentTypeKey;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Map;

/**
 * Represent another agent's memory - it is intended as read only
 * Created by Jan on 24-Feb-17.
 */
public class ReadOnlyMemory extends Memory<PlanningTreeOfAnotherAgent> {
    ReadOnlyMemory(Map<FactKey, Fact> factParameterMap, Map<FactKey, FactSet> factSetParameterMap, PlanningTreeOfAnotherAgent tree, AgentTypeKey agentTypeKey) {
        super(factParameterMap, factSetParameterMap, tree, agentTypeKey);
    }
}
