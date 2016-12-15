package cz.jan.maly.model.agent.action;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.AgentActionCycleAbstract;

import java.util.Optional;
import java.util.Set;

/**
 * This action is on end of the path. The purpose of this is to tell agent that leaf was reached so it can start again from root
 * Created by Jan on 14-Dec-16.
 */
public class LeafWasReachedAction extends AgentActionCycleAbstract {

    public LeafWasReachedAction(Agent agent) {
        super(agent);
    }

    @Override
    public Optional<AgentActionCycleAbstract> executeAction(Set<Agent> agentsSentNotification) {
        return Optional.empty();
    }

    @Override
    public boolean returnWorkflowExecutionBackToAgent() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
}
