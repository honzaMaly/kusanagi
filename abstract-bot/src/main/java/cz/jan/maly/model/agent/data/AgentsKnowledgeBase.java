package cz.jan.maly.model.agent.data;

import cz.jan.maly.model.data.Fact;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.model.agent.Agent;

import java.util.*;

import static cz.jan.maly.utils.FrameworkUtils.CLONER;

/**
 * Class representing knowledge of agent. Class is used as internal base for agent's knowledge. The data contained are shared
 * with other agents
 * Created by Jan on 09-Dec-16.
 */
public class AgentsKnowledgeBase {
    private final Map<KeyToFact, Fact> factBase = new HashMap<>();
    private final Agent agent;

    public AgentsKnowledgeBase(Set<KeyToFact> factsToUse, Agent agent) {
        factsToUse.forEach(keyToFact -> factBase.put(keyToFact, keyToFact.createEmptyFact()));
        this.agent = agent;
    }

    /**
     * Get fact from knowledge by key
     *
     * @param keyToFact
     * @return
     */
    public Fact getAgentsOwnFactByKey(KeyToFact keyToFact) {
        if (!factBase.containsKey(keyToFact)) {
            throw new IllegalArgumentException("Agent does not work with this type of fact");
        }
        return factBase.get(keyToFact);
    }

    /**
     * Method return snapshot of agent's current knowledge by cloning own facts (to be thread safe)
     *
     * @return
     */
    public SnapshotOfAgentOwnKnowledge createSnapshot() {
        return new SnapshotOfAgentOwnKnowledge(CLONER.deepClone(factBase), agent);
    }

}
