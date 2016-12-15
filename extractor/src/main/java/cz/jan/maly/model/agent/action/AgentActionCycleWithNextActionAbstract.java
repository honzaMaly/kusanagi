package cz.jan.maly.model.agent.action;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.AgentActionCycleAbstract;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Abstract class to remove repetitive code which would appear in each concrete implementation of AgentActionCycleAbstract
 * referencing to actions which should be executed next
 * Created by Jan on 14-Dec-16.
 */
public abstract class AgentActionCycleWithNextActionAbstract extends AgentActionCycleAbstract {
    protected final List<AgentActionCycleAbstract> followingActions;

    public AgentActionCycleWithNextActionAbstract(Agent agent, List<AgentActionCycleAbstract> followingActions) {
        super(agent);
        this.followingActions = followingActions;
    }

    /**
     * Method to decide which action to choose next based on current state of knowledge and received notifications from agents
     * @param agentsSentNotification
     * @return
     */
    protected abstract Optional<AgentActionCycleAbstract> decideNextAction(Set<Agent> agentsSentNotification);

    @Override
    public boolean returnWorkflowExecutionBackToAgent() {
        return followingActions.stream()
                .allMatch(AgentActionCycleAbstract::returnWorkflowExecutionBackToAgent);
    }

    @Override
    public boolean isLeaf(){
        return false;
    }

}
