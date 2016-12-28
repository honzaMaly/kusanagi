package cz.jan.maly.model.data;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.data.SnapshotOfAgentOwnKnowledge;

import java.util.*;
import java.util.stream.Collectors;

import static cz.jan.maly.utils.FrameworkUtils.CLONER;

/**
 * Common knowledge contains knowledge received by agents
 * Created by Jan on 07-Dec-16.
 */
public class CommonKnowledge {
    private final Map<Agent, Map<KeyToFact, Fact>> snapshotOfFactsByAgents;
    private final Map<KeyToFact, Set<Agent>> agentsWithByTypeOfKnowledge;

    public void addSnapshot(SnapshotOfAgentOwnKnowledge snapshotOfAgentOwnKnowledge) {
        snapshotOfFactsByAgents.put(snapshotOfAgentOwnKnowledge.getAgent(), snapshotOfAgentOwnKnowledge.getSnapshotOfFacts());
        snapshotOfAgentOwnKnowledge.usedKeys().forEach(keyToFact -> agentsWithByTypeOfKnowledge.computeIfAbsent(keyToFact, keyToFact1 -> new HashSet<>()).add(snapshotOfAgentOwnKnowledge.getAgent()));
    }

    public CommonKnowledge() {
        this.snapshotOfFactsByAgents = new HashMap<>();
        this.agentsWithByTypeOfKnowledge = new HashMap<>();
    }

    protected CommonKnowledge(Map<Agent, Map<KeyToFact, Fact>> snapshotOfFactsByAgents, Map<KeyToFact, Set<Agent>> agentsWithByTypeOfKnowledge) {
        this.snapshotOfFactsByAgents = snapshotOfFactsByAgents;
        this.agentsWithByTypeOfKnowledge = agentsWithByTypeOfKnowledge;
    }

    /**
     * Return set of agents with given type of knowledge if it exists
     *
     * @param keyToFact
     * @return
     */
    public Optional<Set<Agent>> returnAgentsWithThisTypeOfKnowledge(KeyToFact keyToFact) {
        return Optional.ofNullable(agentsWithByTypeOfKnowledge.get(keyToFact));
    }

    /**
     * Method returns required fact. If fact is present, clone of fact is return, else its initial value
     *
     * @param agent
     * @param keyToFact
     * @return
     */
    public Fact getCloneOfFactOfAgentByKey(Agent agent, KeyToFact keyToFact) {
        Map<KeyToFact, Fact> map = snapshotOfFactsByAgents.get(agent);
        if (map != null) {
            Fact fact = map.get(keyToFact);
            if (fact != null) {
                return CLONER.deepClone(fact);
            }
        }
        return keyToFact.createEmptyFact();
    }

    /**
     * Clone knowledge - content
     *
     * @return
     */
    public CommonKnowledge getCloneOfKnowledge() {
        Map<Agent, Map<KeyToFact, Fact>> map = new HashMap<>();
        snapshotOfFactsByAgents.forEach((agent, keyToFactFactMap) -> map.put(agent, CLONER.deepClone(keyToFactFactMap)));
        Map<KeyToFact, Set<Agent>> agentsByKnowledgeType = new HashMap<>();
        agentsWithByTypeOfKnowledge.forEach((keyToFact, agents) -> agentsByKnowledgeType.put(keyToFact, agents.stream().collect(Collectors.toSet())));
        return new CommonKnowledge(map, agentsByKnowledgeType);
    }
}
