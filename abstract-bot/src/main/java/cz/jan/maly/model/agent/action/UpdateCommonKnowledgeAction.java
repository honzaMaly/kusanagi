package cz.jan.maly.model.agent.action;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.AgentActionCycleAbstract;
import cz.jan.maly.model.sflo.FormulaInterface;
import cz.jan.maly.service.implementation.MediatorFoSharingKnowledge;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Class UpdateCommonKnowledgeAction make snapshot of agent's knowledge to send it to mediator to update common knowledge.
 * Created by Jan on 14-Dec-16.
 */
public class UpdateCommonKnowledgeAction extends AgentActionCycleAbstract {

    public UpdateCommonKnowledgeAction(Agent agent, LinkedHashMap<FormulaInterface, AgentActionCycleAbstract> followingActionsWithConditions) {
        super(agent, followingActionsWithConditions, actionCycleEnum);
    }

    public UpdateCommonKnowledgeAction(Agent agent) {
        super(agent);
    }

    @Override
    public Optional<AgentActionCycleAbstract> executeAction() {
        MediatorFoSharingKnowledge.getInstance().receiveAgentsKnowledge(agent.getAgentsKnowledgeBase().createSnapshot());
        return decideNextAction();
    }
}
