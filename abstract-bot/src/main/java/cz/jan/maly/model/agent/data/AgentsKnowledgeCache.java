package cz.jan.maly.model.agent.data;

import cz.jan.maly.model.data.CommonKnowledge;
import cz.jan.maly.model.data.Fact;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.model.agent.Agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * This structure is used as cache (to store data from common knowledge) which can be then persist to agent's knowledge
 * Created by Jan on 28-Dec-16.
 */
public class AgentsKnowledgeCache {
    private final Map<KeyToFact, Map<Agent, Fact>> snapshotOfFactsByAgents = new HashMap<>();

    public AgentsKnowledgeCache(Set<KeyToFact> factsToCacheFromAgent) {
        factsToCacheFromAgent.forEach(keyToFact -> snapshotOfFactsByAgents.put(keyToFact, new HashMap<>()));
    }

    /**
     * Method which takes all interesting facts from knowledge and store them to cache
     * @param commonKnowledge
     */
    public void cacheUsefulPartsOfCommonKnowledge(CommonKnowledge commonKnowledge){
        snapshotOfFactsByAgents.keySet().forEach(keyToFact -> {
            Optional<Set<Agent>> agentsWithTypeInKnowledge = commonKnowledge.returnAgentsWithThisTypeOfKnowledge(keyToFact);
            Map<Agent, Fact> mapWithFactPerAgentOfThisFactType = new HashMap<>();
            agentsWithTypeInKnowledge.ifPresent(agents -> agentsWithTypeInKnowledge.get().forEach(agent -> mapWithFactPerAgentOfThisFactType.put(agent, commonKnowledge.getCloneOfFactOfAgentByKey(agent, keyToFact))));
            snapshotOfFactsByAgents.put(keyToFact, mapWithFactPerAgentOfThisFactType);
        });
    }

    /**
     * Method returns required map of facts
     *
     * @param keyToFact
     * @return
     */
    public Map<Agent, Fact> getMapOfFactsOfAgentsByKey(KeyToFact keyToFact) {
        if (!snapshotOfFactsByAgents.containsKey(keyToFact)) {
            throw new IllegalArgumentException("Agent does not work with this type of fact");
        }
        return snapshotOfFactsByAgents.get(keyToFact);
    }

}
