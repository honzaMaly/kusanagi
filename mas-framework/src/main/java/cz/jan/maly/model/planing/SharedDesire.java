package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Template class for tracking commitmentDecider to desire which issues some agent to be achieved by other agents
 * Created by Jan on 22Feb17.
 */
public abstract class SharedDesire extends Desire {

    @Getter
    protected final Agent originatedFromAgent;
    @Getter
    protected final int limitOnNumberOfAgentsToCommit;
    final Set<Agent> committedAgents = new HashSet<>();

    SharedDesire(DesireKey desireKey, Agent originatedFromAgent, int limitOnNumberOfAgentsToCommit) {
        super(desireKey, originatedFromAgent.getBeliefs());
        this.originatedFromAgent = originatedFromAgent;
        this.limitOnNumberOfAgentsToCommit = limitOnNumberOfAgentsToCommit;
    }

    SharedDesire(DesireParameters desireParameters, Agent originatedFromAgent, int limitOnNumberOfAgentsToCommit, Set<Agent> committedAgents) {
        super(desireParameters, originatedFromAgent.getId());
        this.originatedFromAgent = originatedFromAgent;
        this.limitOnNumberOfAgentsToCommit = limitOnNumberOfAgentsToCommit;
        this.committedAgents.addAll(committedAgents);
    }

    /**
     * Returns copy of set of committed agents
     *
     * @return
     */
    public Set<Agent> getCommittedAgents() {
        return new HashSet<>(committedAgents);
    }

    public int countOfCommittedAgents() {
        return committedAgents.size();
    }

    /**
     * Returns if agent may try to commit to desire
     *
     * @return
     */
    public boolean mayTryToCommit() {
        if (committedAgents.size() + 1 <= limitOnNumberOfAgentsToCommit) {
            return true;
        }
        return false;
    }

    /**
     * Returns if agent is committed to desire
     *
     * @param agent
     * @return
     */
    public boolean isAgentCommittedToDesire(Agent agent) {
        return committedAgents.contains(agent);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SharedDesire)) return false;
        if (!super.equals(o)) return false;

        SharedDesire that = (SharedDesire) o;

        if (limitOnNumberOfAgentsToCommit != that.limitOnNumberOfAgentsToCommit) return false;
        return originatedFromAgent.equals(that.originatedFromAgent);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + originatedFromAgent.hashCode();
        result = 31 * result + limitOnNumberOfAgentsToCommit;
        return result;
    }
}
