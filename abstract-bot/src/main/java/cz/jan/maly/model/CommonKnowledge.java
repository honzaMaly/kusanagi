package cz.jan.maly.model;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.SnapshotOfAgentOwnKnowledge;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static cz.jan.maly.utils.FrameworkUtils.CLONER;

/**
 * Common knowledge contains knowledge received by agents
 * Created by Jan on 07-Dec-16.
 */
public class CommonKnowledge {
    private final Map<Agent, Map<KeyToFact, Fact>> snapshotOfFactsByAgents;

    public void addSnapshot(SnapshotOfAgentOwnKnowledge snapshotOfAgentOwnKnowledge) {
        snapshotOfFactsByAgents.put(snapshotOfAgentOwnKnowledge.getAgent(), snapshotOfAgentOwnKnowledge.getSnapshotOfFacts());
    }

    public CommonKnowledge() {
        this.snapshotOfFactsByAgents = new HashMap<>();
    }

    protected CommonKnowledge(Map<Agent, Map<KeyToFact, Fact>> snapshotOfFactsByAgents) {
        this.snapshotOfFactsByAgents = snapshotOfFactsByAgents;
    }

    /**
     * Method returns optional of required fact. If fact is present, clone of fact is return in wrapper
     *
     * @param agent
     * @param keyToFact
     * @return
     */
    public <V> Optional<Fact<V>> getCloneOfFactOfAgentByKey(Agent agent, KeyToFact<V> keyToFact) {
        Map<KeyToFact, Fact> map = snapshotOfFactsByAgents.get(agent);
        if (map != null) {
            Fact<V> fact = map.get(keyToFact);
            if (fact != null) {
                return Optional.ofNullable(CLONER.deepClone(fact));
            }
        }
        return Optional.empty();
    }

    public CommonKnowledge getCloneOfKnowledge() {
        Map<Agent, Map<KeyToFact, Fact>> map = new HashMap<>();
        snapshotOfFactsByAgents.forEach((agent, keyToFactFactMap) -> map.put(agent, CLONER.deepClone(keyToFactFactMap)));
        return new CommonKnowledge(map);
    }
}
