package cz.jan.maly.model.data;

import cz.jan.maly.model.agent.Agent;

import java.util.Map;
import java.util.Set;

/**
 * Common knowledge contains knowledge received by agents - it is kind of register
 * Created by Jan on 07-Dec-16.
 */
public abstract class CommonKnowledge {
    protected final Map<Agent, Map<KeyToFact, cz.jan.maly.model.metadata.Fact>> snapshotOfFactsByAgents;
    protected final Map<KeyToFact, Set<Agent>> agentsWithByTypeOfKnowledge;

    protected CommonKnowledge(Map<Agent, Map<KeyToFact, cz.jan.maly.model.metadata.Fact>> snapshotOfFactsByAgents, Map<KeyToFact, Set<Agent>> agentsWithByTypeOfKnowledge) {
        this.snapshotOfFactsByAgents = snapshotOfFactsByAgents;
        this.agentsWithByTypeOfKnowledge = agentsWithByTypeOfKnowledge;
    }
}
