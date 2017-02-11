package cz.jan.maly.model.data;

import cz.jan.maly.model.agent.Agent;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static cz.jan.maly.utils.FrameworkUtils.CLONER;

/**
 * Concrete implementation of CommonKnowledge. This class is intended as read only -
 * to ensure encapsulation and thread safety so data are returned in form of copy
 * Created by Jan on 29-Dec-16.
 */
public class ReadOnlyCommonKnowledge extends CommonKnowledge {

    public ReadOnlyCommonKnowledge(Map<Agent, Map<KeyToFact, cz.jan.maly.model.data.knowledge_representation.Fact>> snapshotOfFactsByAgents, Map<KeyToFact, Set<Agent>> agentsWithByTypeOfKnowledge) {
        super(snapshotOfFactsByAgents, agentsWithByTypeOfKnowledge);
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
    public cz.jan.maly.model.data.knowledge_representation.Fact getCloneOfFactOfAgentByKey(Agent agent, KeyToFact keyToFact) {
        Map<KeyToFact, cz.jan.maly.model.data.knowledge_representation.Fact> map = snapshotOfFactsByAgents.get(agent);
        if (map != null) {
            cz.jan.maly.model.data.knowledge_representation.Fact fact = map.get(keyToFact);
            if (fact != null) {
                return CLONER.deepClone(fact);
            }
        }
        return keyToFact.createEmptyFact();
    }
}
