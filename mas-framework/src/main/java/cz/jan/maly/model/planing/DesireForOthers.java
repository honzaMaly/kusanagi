package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Template for agent's desires for others to achieve
 * Created by Jan on 15-Feb-17.
 */
public class DesireForOthers extends Desire {

    @Getter
    private final Set<Agent> committedAgents = new HashSet<>();

    @Getter
    private final int limitOnNumberOfAgentsToCommit;

    @Getter
    private final Agent originatedFromAgent;

    public DesireForOthers(DesireKey desireKey, Agent agent, int limitOnNumberOfAgentsToCommit) {
        super(desireKey, agent);
        this.limitOnNumberOfAgentsToCommit = limitOnNumberOfAgentsToCommit;
        this.originatedFromAgent = agent;
    }

    public DesireForOthers(DesireKey desireKey, Agent agent) {
        super(desireKey, agent);
        this.limitOnNumberOfAgentsToCommit = Integer.MAX_VALUE;
        this.originatedFromAgent = agent;
    }

    private DesireForOthers(Map<FactKey, Object> factParameterMap, Map<FactKey, Set> factSetParameterMap, DesireKey desireKey, Set<Agent> committedAgents, int limitOnNumberOfAgentsToCommit, Agent originatedFromAgent) {
        super(factParameterMap, factSetParameterMap, desireKey);
        this.committedAgents.addAll(committedAgents);
        this.limitOnNumberOfAgentsToCommit = limitOnNumberOfAgentsToCommit;
        this.originatedFromAgent = originatedFromAgent;
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
     * Removes commitment of agent to this desire
     *
     * @param agent
     * @return
     */
    public boolean removeCommitment(Agent agent) {
        return !committedAgents.contains(agent) || committedAgents.remove(agent);
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

    /**
     * Returns clone of desire
     *
     * @return
     */
    public DesireForOthers cloneDesire() {
        return new DesireForOthers(factParameterMap, factSetParameterMap, desireKey, committedAgents, limitOnNumberOfAgentsToCommit, originatedFromAgent);
    }

    /**
     * Return desire to which can another agent commit to
     *
     * @return
     */
    public DesireFromAnotherAgent getDesireToCommitTo() {
        return new DesireFromAnotherAgent(factParameterMap, factSetParameterMap, desireKey, cloneDesire());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DesireForOthers)) return false;
        if (!super.equals(o)) return false;

        DesireForOthers that = (DesireForOthers) o;

        if (limitOnNumberOfAgentsToCommit != that.limitOnNumberOfAgentsToCommit) return false;
        return originatedFromAgent.equals(that.originatedFromAgent);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + limitOnNumberOfAgentsToCommit;
        result = 31 * result + originatedFromAgent.hashCode();
        return result;
    }
}
