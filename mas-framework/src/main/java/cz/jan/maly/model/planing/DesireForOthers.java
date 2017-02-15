package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

/**
 * Template for agent's desires for others to achieve
 * Created by Jan on 15-Feb-17.
 */
public class DesireForOthers extends Desire<DesireForOthers> {

    @Getter
    private final int limitOnNumberOfAgentsToCommit;

    public DesireForOthers(DesireKey desireKey, Agent agent, int limitOnNumberOfAgentsToCommit) {
        super(desireKey, agent);
        this.limitOnNumberOfAgentsToCommit = limitOnNumberOfAgentsToCommit;
    }

    public DesireForOthers(DesireKey desireKey, Agent agent) {
        super(desireKey, agent);
        this.limitOnNumberOfAgentsToCommit = Integer.MAX_VALUE;
    }

    private DesireForOthers(Map<FactKey, Object> factParameterMap, Map<FactKey, Set> factSetParameterMap, DesireKey desireKey, int limitOnNumberOfAgentsToCommit) {
        super(factParameterMap, factSetParameterMap, desireKey);
        this.limitOnNumberOfAgentsToCommit = limitOnNumberOfAgentsToCommit;
    }

    @Override
    public DesireForOthers copyDesire() {
        return new DesireForOthers(factParameterMap, factSetParameterMap, desireKey, limitOnNumberOfAgentsToCommit);
    }
}
