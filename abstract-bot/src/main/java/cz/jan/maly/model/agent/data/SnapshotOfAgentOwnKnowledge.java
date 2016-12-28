package cz.jan.maly.model.agent.data;

import cz.jan.maly.model.data.Fact;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.model.agent.Agent;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

/**
 * Wrapper to transfer agent's own knowledge to common knowledge
 * Created by Jan on 17-Dec-16.
 */
@Getter
public class SnapshotOfAgentOwnKnowledge {
    private final Map<KeyToFact, Fact> snapshotOfFacts;
    private final Agent agent;

    public SnapshotOfAgentOwnKnowledge(Map<KeyToFact, Fact> snapshotOfFacts, Agent agent) {
        this.snapshotOfFacts = snapshotOfFacts;
        this.agent = agent;
    }

    public Set<KeyToFact> usedKeys() {
        return snapshotOfFacts.keySet();
    }
}
