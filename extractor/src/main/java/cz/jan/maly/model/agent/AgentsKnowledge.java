package cz.jan.maly.model.agent;

import com.rits.cloning.Cloner;
import cz.jan.maly.model.Fact;
import cz.jan.maly.model.KeyToFact;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing knowledge of agent
 * Created by Jan on 09-Dec-16.
 */
public class AgentsKnowledge {
    private static final Cloner cloner = new Cloner();
    private final Agent agent;
    private final static Random RANDOM = new Random();
    private Map<Agent, Integer> receivedNotificationFromAgentWithAssociatedPassedActions = new HashMap<>();

    //this set of facts is send to mediator
    private final Map<KeyToFact, Fact> ownFacts = new HashMap<>();

    //this set of facts is not send to mediator. this set is aggregated common knowledge required by this agent to supplement its own knowledge
    private final Map<KeyToFact, Fact> aggregatedFactsFromCommonKnowledge = new HashMap<>();

    protected AgentsKnowledge(Agent agent, Set<KeyToFact> ownFactsToUse, Set<KeyToFact> factsByOtherAgentToUse) {
        this.agent = agent;
        ownFactsToUse.forEach(keyToFact -> ownFacts.put(keyToFact, keyToFact.createEmptyFact()));
        factsByOtherAgentToUse.forEach(keyToFact -> aggregatedFactsFromCommonKnowledge.put(keyToFact, keyToFact.createEmptyFact()));
    }

    public void propagateNewReceivedNotificationFromAgentsToKnowledge(Set<Agent> receivedNotificationFrom) {
        receivedNotificationFromAgentWithAssociatedPassedActions = receivedNotificationFromAgentWithAssociatedPassedActions.entrySet().stream()
                .filter(agentIntegerEntry -> agentIntegerEntry.getValue() + 1 < agent.getLengthOfLongestPath())
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() + 1));
        receivedNotificationFrom.forEach(agent1 -> receivedNotificationFromAgentWithAssociatedPassedActions.put(agent1, 0));
    }

    public boolean isKnowledgeActive() {
        return agent.isAgentAlive();
    }

    public double getRandomNumber() {
        return RANDOM.nextDouble();
    }

    /**
     * Get fact from own knowledge by key
     *
     * @param keyToFact
     * @return
     */
    public Optional<Fact> getAgentsOwnFactByKey(KeyToFact keyToFact) {
        return Optional.ofNullable(ownFacts.get(keyToFact));
    }

    /**
     * Get fact from own aggregated knowledge of common knowledge by key
     *
     * @param keyToFact
     * @return
     */
    public Optional<Fact> getAggregatedFactFromCommonKnowledgeByKey(KeyToFact keyToFact) {
        return Optional.ofNullable(aggregatedFactsFromCommonKnowledge.get(keyToFact));
    }

    /**
     * Method return snapshot of agent's current knowledge by cloning own facts (to be thread safe)
     *
     * @return
     */
    public SnapshotOfAgentOwnKnowledge createSnapshot() {
        Map<KeyToFact, Fact> cloneOfOwnFacts = cloner.deepClone(ownFacts);
        return new SnapshotOfAgentOwnKnowledge(cloneOfOwnFacts, agent);
    }

}
