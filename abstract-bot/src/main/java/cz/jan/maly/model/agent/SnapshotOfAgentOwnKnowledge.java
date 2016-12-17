package cz.jan.maly.model.agent;

import cz.jan.maly.model.Fact;
import cz.jan.maly.model.KeyToFact;
import lombok.Getter;

import java.util.Map;

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
}
