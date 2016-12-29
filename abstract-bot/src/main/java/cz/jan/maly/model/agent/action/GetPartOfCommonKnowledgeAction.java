package cz.jan.maly.model.agent.action;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.AgentActionCycleAbstract;
import cz.jan.maly.model.data.CommonKnowledge;
import cz.jan.maly.model.sflo.FormulaInterface;
import cz.jan.maly.service.implementation.MediatorFoSharingKnowledge;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Class GetPartOfCommonKnowledgeAction get required knowledge from mediator to update agent's knowledge.
 * Knowledge is updated based on provided strategy.
 * Created by Jan on 14-Dec-16.
 */
public class GetPartOfCommonKnowledgeAction extends AgentActionCycleAbstract {
    //strategy to update knowledge
    private final AgentKnowledgeUpdateByCommonKnowledgeStrategy knowledgeStrategy;

    public GetPartOfCommonKnowledgeAction(Agent agent, LinkedHashMap<FormulaInterface, AgentActionCycleAbstract> followingActionsWithConditions, AgentKnowledgeUpdateByCommonKnowledgeStrategy knowledgeStrategy) {
        super(agent, followingActionsWithConditions, actionCycleEnum);
        this.knowledgeStrategy = knowledgeStrategy;
    }

    public GetPartOfCommonKnowledgeAction(Agent agent, AgentKnowledgeUpdateByCommonKnowledgeStrategy knowledgeStrategy) {
        super(agent);
        this.knowledgeStrategy = knowledgeStrategy;
    }

    public void updateAgentsKnowledge(CommonKnowledge workingCommonKnowledge){
        knowledgeStrategy.updateKnowledge(workingCommonKnowledge, agent.getAgentsKnowledgeBase());
    }

    @Override
    public Optional<AgentActionCycleAbstract> executeAction() {
        MediatorFoSharingKnowledge.getInstance().updateAgentsKnowledge(this);
        return decideNextAction();
    }
}
