package cz.jan.maly.model.agent.action;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.AgentActionCycleAbstract;

/**
 * Abstract class to remove repetitive code which would appear in each concrete implementation of AgentActionCycleAbstract
 * referencing to action which should be executed next
 * Created by Jan on 14-Dec-16.
 */
public abstract class AgentActionCycleWithNextActionAbstract extends AgentActionCycleAbstract {
    protected final AgentActionCycleAbstract followingAction;

    public AgentActionCycleWithNextActionAbstract(Agent agent, AgentActionCycleAbstract followingAction) {
        super(agent);
        this.followingAction = followingAction;
    }

    @Override
    public boolean returnWorkflowExecutionBackToAgent() {
        return followingAction.returnWorkflowExecutionBackToAgent();
    }
}
