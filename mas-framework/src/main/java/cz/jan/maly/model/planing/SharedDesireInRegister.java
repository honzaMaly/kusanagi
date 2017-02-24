package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireParameters;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Concrete implementation of SharedDesire to be used in register
 * Created by Jan on 22-Feb-17.
 */
public class SharedDesireInRegister extends SharedDesire {

    SharedDesireInRegister(DesireParameters desireParameters, Agent originatedFromAgent, int limitOnNumberOfAgentsToCommit) {
        super(desireParameters, originatedFromAgent, limitOnNumberOfAgentsToCommit, new HashSet<>());
    }

    /**
     * Try to commit agent to desire and returns true if agent is committed to it
     *
     * @param agent
     * @return
     */
    public boolean commitToDesire(Agent agent) {
        if (committedAgents.size() + 1 <= limitOnNumberOfAgentsToCommit) {
            return committedAgents.contains(agent) || committedAgents.add(agent);

        }
        return committedAgents.contains(agent);
    }

    /**
     * Returns copy of set of committed agents
     *
     * @return
     */
    public Set<Agent> getCommittedAgents() {
        return committedAgents.stream().collect(Collectors.toSet());
    }

    /**
     * Removes commitment of agent to this desire
     *
     * @param agent
     * @return
     */
    public boolean removeCommitment(Agent agent) {
        return !committedAgents.contains(agent) || committedAgents.remove(agent);

    }

    /**
     * Returns clone of desire
     *
     * @return
     */
    public SharedDesireForAgents getCopyOfSharedDesireForAgents() {
        return new SharedDesireForAgents(desireParameters, originatedFromAgent, limitOnNumberOfAgentsToCommit, committedAgents);
    }

}
