package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;

import java.util.Set;

/**
 * Concrete implementation of SharedDesire to be used by agents
 * Created by Jan on 22-Feb-17.
 */
public class SharedDesireForAgents extends SharedDesire {

    SharedDesireForAgents(DesireKey desireKey, Agent originatedFromAgent, int limitOnNumberOfAgentsToCommit) {
        super(desireKey, originatedFromAgent, limitOnNumberOfAgentsToCommit);
    }

    SharedDesireForAgents(DesireParameters desireParameters, Agent originatedFromAgent, int limitOnNumberOfAgentsToCommit, Set<Agent> committedAgents) {
        super(desireParameters, originatedFromAgent, limitOnNumberOfAgentsToCommit, committedAgents);
    }

    /**
     * Updates internal set of committed agents by current one
     *
     * @param sharedDesireForAgentsInSystem
     */
    public void updateCommittedAgentsSet(SharedDesireForAgents sharedDesireForAgentsInSystem) {
        committedAgents.removeIf(agent -> !sharedDesireForAgentsInSystem.committedAgents.contains(agent));
        committedAgents.addAll(sharedDesireForAgentsInSystem.committedAgents);
    }
}
