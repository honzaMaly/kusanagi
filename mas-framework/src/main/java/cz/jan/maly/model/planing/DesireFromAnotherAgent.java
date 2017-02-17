package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

/**
 * Template for agent's desires transformation. Desire originated from another agent is transformed
 * Created by Jan on 16-Feb-17.
 */
public class DesireFromAnotherAgent extends Desire {

    @Getter
    private final DesireForOthers desireOriginatedFrom;

    DesireFromAnotherAgent(Map<FactKey, Object> factParameterMap, Map<FactKey, Set> factSetParameterMap, DesireKey desireKey, DesireForOthers desireOriginatedFrom) {
        super(factParameterMap, factSetParameterMap, desireKey);
        this.desireOriginatedFrom = desireOriginatedFrom;
    }

    /**
     * Returns true if it makes sense to try to commit to this desire
     *
     * @return
     */
    public boolean mayTryToCommit() {
        return desireOriginatedFrom.getCommittedAgents().size() < desireOriginatedFrom.getLimitOnNumberOfAgentsToCommit();
    }

    /**
     * Returns true if agent is registered as committed to this desire
     *
     * @param agent
     * @return
     */
    public boolean isAgentCommittedToDesire(Agent agent) {
        return desireOriginatedFrom.isAgentCommittedToDesire(agent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DesireFromAnotherAgent)) return false;
        if (!super.equals(o)) return false;

        DesireFromAnotherAgent that = (DesireFromAnotherAgent) o;

        return desireOriginatedFrom.equals(that.desireOriginatedFrom);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + desireOriginatedFrom.hashCode();
        return result;
    }
}
