package cz.jan.maly.model.agent.action;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.AgentActionCycleAbstract;

import java.util.Optional;

/**
 * This action is on end of the cycle. The purpose of this is to tell agent that one life cycle has just ended
 * Created by Jan on 14-Dec-16.
 */
public class ReturnExecutionToAgentAction extends AgentActionCycleAbstract {

    public ReturnExecutionToAgentAction(Agent agent) {
        super(agent);
    }

    @Override
    public Optional<AgentActionCycleAbstract> executeAction() {
        return Optional.empty();
    }

    @Override
    public boolean returnWorkflowExecutionBackToAgent() {
        return true;
    }
}
