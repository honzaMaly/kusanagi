package cz.jan.maly.model;

import com.rits.cloning.Cloner;
import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.SnapshotOfAgentOwnKnowledge;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Common knowledge contains knowledge received by agents
 * Created by Jan on 07-Dec-16.
 */
public class CommonKnowledge {
    private static final Cloner cloner = new Cloner();
    private final Map<Agent, Map<KeyToFact, Fact>> snapshotOfFactsByAgents = new HashMap<>();

    public void addSnapshot(SnapshotOfAgentOwnKnowledge snapshotOfAgentOwnKnowledge){
        snapshotOfFactsByAgents.put(snapshotOfAgentOwnKnowledge.getAgent(), snapshotOfAgentOwnKnowledge.getSnapshotOfFacts());
    }

    /**
     * Method returns optional of required fact. If fact is present, clone of fact is return in wrapper
     * @param agent
     * @param keyToFact
     * @return
     */
    public Optional<Fact> getCloneOfFactOfAgentByKey(Agent agent, KeyToFact keyToFact){
        Map<KeyToFact, Fact> map = snapshotOfFactsByAgents.get(agent);
        if (map!=null){
            Fact fact = map.get(keyToFact);
            if (fact!=null){
                return Optional.ofNullable(cloner.deepClone(fact));
            }
        }
        return Optional.empty();
    }

    public CommonKnowledge getCloneOfKnowledge() {
        return cloner.deepClone(this);
    }
}
