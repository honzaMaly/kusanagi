package cz.jan.maly.model.agent.action;

import cz.jan.maly.model.GameActionMaker;
import cz.jan.maly.model.agent.AgentActionCycleAbstract;
import cz.jan.maly.model.agent.AgentWithGameRepresentation;
import cz.jan.maly.model.agent.action.game.Action;
import cz.jan.maly.model.sflo.TermInterface;
import cz.jan.maly.service.GameIssueCommandManager;
import cz.jan.maly.service.MyLogger;

import java.util.LinkedHashMap;
import java.util.Optional;

import static cz.jan.maly.service.OnFrameExecutor.maxFrameExecutionTime;

/**
 * Abstract class ActInGameAction issues command to game based on its implementation trough GameIssueCommandManager
 * Created by Jan on 14-Dec-16.
 */
public class ActInGameAction extends AgentActionCycleAbstract implements GameActionMaker {
    //parameter of default time it takes to make action in milliseconds
    private static final long defaultTimeThatIsRequiredToMakeAction = 5;

    protected final GameIssueCommandManager issueCommandManager = GameIssueCommandManager.getInstance();
    private final Action actionToExecute;

    //parameter of default time it takes to make action in milliseconds
    private long timeThatWasRequiredToMakeAction = defaultTimeThatIsRequiredToMakeAction;

    public ActInGameAction(AgentWithGameRepresentation agent, LinkedHashMap<TermInterface, AgentActionCycleAbstract> followingActionsWithConditions, Action actionToExecute) {
        super(agent, followingActionsWithConditions);
        this.actionToExecute = actionToExecute;
    }

    public ActInGameAction(AgentWithGameRepresentation agent, Action actionToExecute) {
        super(agent);
        this.actionToExecute = actionToExecute;
    }

    @Override
    public Optional<AgentActionCycleAbstract> executeAction() {
        synchronized (this) {
            if (issueCommandManager.isActionToExecuteInGamePutInQueue(this)) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    MyLogger.getLogger().warning(e.getLocalizedMessage());
                }
            }
        }
        return decideNextAction();
    }

    @Override
    public void executeActionInGame() {
        synchronized (this) {
            long start = System.currentTimeMillis();
            actionToExecute.executeAction();
            timeThatWasRequiredToMakeAction = Math.min(System.currentTimeMillis() - start, maxFrameExecutionTime - 1);
            this.notifyAll();
        }
    }

    @Override
    public long getExecutionTime() {
        return timeThatWasRequiredToMakeAction;
    }
}
