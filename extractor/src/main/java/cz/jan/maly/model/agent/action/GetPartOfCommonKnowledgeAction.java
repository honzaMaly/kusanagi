package cz.jan.maly.model.agent.action;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.AgentActionCycleAbstract;
import cz.jan.maly.model.agent.AgentKnowledgeUpdateByCommonKnowledgeStrategy;
import cz.jan.maly.model.game.CommonKnowledge;
import cz.jan.maly.service.Mediator;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Class GetPartOfCommonKnowledgeAction get required knowledge from mediator to update agent's knowledge.
 * Knowledge is updated based on provided strategy.
 * Created by Jan on 14-Dec-16.
 */
public abstract class GetPartOfCommonKnowledgeAction extends AgentActionCycleWithNextActionAbstract {
    private final Mediator mediator = Mediator.getInstance();
    //strategy to update knowledge
    private final AgentKnowledgeUpdateByCommonKnowledgeStrategy knowledgeStrategy;

    public GetPartOfCommonKnowledgeAction(Agent agent, List<AgentActionCycleAbstract> followingActions, AgentKnowledgeUpdateByCommonKnowledgeStrategy knowledgeStrategy) {
        super(agent, followingActions);
        this.knowledgeStrategy = knowledgeStrategy;
    }

    public void updateAgentsKnowledge(CommonKnowledge workingCommonKnowledge){
        knowledgeStrategy.updateKnowledge(workingCommonKnowledge, agent.getAgentsKnowledge());
    }

    @Override
    public Optional<AgentActionCycleAbstract> executeAction(Set<Agent> agentsSentNotification) {
        mediator.updateAgentsKnowledge(this);
        return decideNextAction(agentsSentNotification);
    }
}
