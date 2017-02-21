package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Template class for intention with desire for other agents - what this intention want them to achieve
 * Created by Jan on 21-Feb-17.
 */
public abstract class IntentionWithDesireForOtherAgents extends Intention<DesireForOthers> {

    @Getter
    private final Set<Agent> committedAgents = new HashSet<>();

    @Getter
    private final int limitOnNumberOfAgentsToCommit;

    @Getter
    private final Agent originatedFromAgent;

    protected IntentionWithDesireForOtherAgents(DesireForOthers originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, int limitOnNumberOfAgentsToCommit, Agent agent) {
        super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
        this.limitOnNumberOfAgentsToCommit = limitOnNumberOfAgentsToCommit;
        this.originatedFromAgent = agent;
    }

    protected IntentionWithDesireForOtherAgents(DesireForOthers originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent) {
        super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
        this.limitOnNumberOfAgentsToCommit = Integer.MAX_VALUE;
        this.originatedFromAgent = agent;
    }

    //TODO methods to handle sharing trough mediator

}
