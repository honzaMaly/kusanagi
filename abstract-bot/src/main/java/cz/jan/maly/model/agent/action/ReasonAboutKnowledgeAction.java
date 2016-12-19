package cz.jan.maly.model.agent.action;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.AgentActionCycleAbstract;
import cz.jan.maly.model.agent.AgentKnowledgeUpdateByReasoning;
import cz.jan.maly.model.sflo.TermInterface;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Action to reason about knowledge using facts contained in it based on strategy
 * Created by Jan on 14-Dec-16.
 */
public class ReasonAboutKnowledgeAction extends AgentActionCycleAbstract {
    private final AgentKnowledgeUpdateByReasoning reasoningStrategy;

    public ReasonAboutKnowledgeAction(Agent agent, AgentKnowledgeUpdateByReasoning reasoningStrategy) {
        super(agent);
        this.reasoningStrategy = reasoningStrategy;
    }

    public ReasonAboutKnowledgeAction(Agent agent, LinkedHashMap<TermInterface, AgentActionCycleAbstract> followingActionsWithConditions, AgentKnowledgeUpdateByReasoning reasoningStrategy) {
        super(agent, followingActionsWithConditions);
        this.reasoningStrategy = reasoningStrategy;
    }

    @Override
    public Optional<AgentActionCycleAbstract> executeAction() {
        reasoningStrategy.updateKnowledge(agent.getAgentsKnowledge());
        return decideNextAction();
    }
}
