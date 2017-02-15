package cz.jan.maly.model.data;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.data.SnapshotOfAgentOwnKnowledge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.jan.maly.utils.FrameworkUtils.CLONER;

/**
 * Concrete implementation of CommonKnowledge. This class is intended as working register -
 * register can be updated only once at time.
 * Created by Jan on 29-Dec-16.
 */
public class WorkingCommonKnowledge extends CommonKnowledge {

    public WorkingCommonKnowledge() {
        super(new HashMap<>(), new HashMap<>());
    }

    public void addSnapshot(SnapshotOfAgentOwnKnowledge snapshotOfAgentOwnKnowledge) {
        snapshotOfFactsByAgents.put(snapshotOfAgentOwnKnowledge.getAgent(), snapshotOfAgentOwnKnowledge.getSnapshotOfFacts());
        snapshotOfAgentOwnKnowledge.usedKeys().forEach(keyToFact -> agentsWithByTypeOfKnowledge.computeIfAbsent(keyToFact, keyToFact1 -> new HashSet<>()).add(snapshotOfAgentOwnKnowledge.getAgent()));
    }

    /**
     * Clone knowledge - content
     *
     * @return
     */
    public ReadOnlyCommonKnowledge getCloneOfKnowledge() {
        Map<Agent, Map<KeyToFact, cz.jan.maly.model.metadata.Fact>> map = new HashMap<>();
        snapshotOfFactsByAgents.forEach((agent, keyToFactFactMap) -> map.put(agent, CLONER.deepClone(keyToFactFactMap)));
        Map<KeyToFact, Set<Agent>> agentsByKnowledgeType = new HashMap<>();
        agentsWithByTypeOfKnowledge.forEach((keyToFact, agents) -> agentsByKnowledgeType.put(keyToFact, agents.stream().collect(Collectors.toSet())));
        return new ReadOnlyCommonKnowledge(map, agentsByKnowledgeType);
    }

}
