package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Map;
import java.util.Set;

/**
 * Concrete implementation of SharedDesire to be used by agents
 * Created by Jan on 22-Feb-17.
 */
public class SharedDesireForAgents extends SharedDesire {

    SharedDesireForAgents(DesireKey desireKey, Agent originatedFromAgent, int limitOnNumberOfAgentsToCommit) {
        super(desireKey, originatedFromAgent, limitOnNumberOfAgentsToCommit);
    }

    SharedDesireForAgents(Map<FactKey, Object> factParameterMap, Map<FactKey, Set> factSetParameterMap, DesireKey desireKey, Agent originatedFromAgent, int limitOnNumberOfAgentsToCommit, Set<Agent> committedAgents) {
        super(factParameterMap, factSetParameterMap, desireKey, originatedFromAgent, limitOnNumberOfAgentsToCommit, committedAgents);
    }

    /**
     * Updates internal set of committed agents by current one
     *
     * @param currentlyCommittedAgents
     */
    public void updateCommittedAgentsSet(Set<Agent> currentlyCommittedAgents) {
        committedAgents.removeIf(agent -> !currentlyCommittedAgents.contains(agent));
        committedAgents.addAll(currentlyCommittedAgents);
    }
}
