package cz.jan.maly.model.agent.action;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.AgentActionCycleAbstract;
import cz.jan.maly.model.sflo.TermInterface;
import cz.jan.maly.service.Mediator;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Class UpdateCommonKnowledgeAction make snapshot of agent's knowledge to send it to mediator to update common knowledge.
 * Created by Jan on 14-Dec-16.
 */
public class UpdateCommonKnowledgeAction extends AgentActionCycleAbstract {

    public UpdateCommonKnowledgeAction(Agent agent, LinkedHashMap<TermInterface, AgentActionCycleAbstract> followingActionsWithConditions) {
        super(agent, followingActionsWithConditions);
    }

    public UpdateCommonKnowledgeAction(Agent agent) {
        super(agent);
    }

    @Override
    public Optional<AgentActionCycleAbstract> executeAction() {
        Mediator.getInstance().receiveAgentsKnowledge(agent.getAgentsKnowledge().createSnapshot());
        return decideNextAction();
    }
}
