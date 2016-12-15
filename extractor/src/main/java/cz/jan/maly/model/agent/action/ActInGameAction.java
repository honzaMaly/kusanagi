package cz.jan.maly.model.agent.action;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.AgentActionCycleAbstract;
import cz.jan.maly.service.GameIssueCommandManager;

import java.util.List;

/**
 * Abstract class ActInGameAction issues command to game based on its implementation trough GameIssueCommandManager
 * Created by Jan on 14-Dec-16.
 */
public abstract class ActInGameAction extends AgentActionCycleWithNextActionAbstract {
    protected final GameIssueCommandManager issueCommandManager = GameIssueCommandManager.getInstance();

    public ActInGameAction(Agent agent, List<AgentActionCycleAbstract> followingActions) {
        super(agent, followingActions);
    }

}
