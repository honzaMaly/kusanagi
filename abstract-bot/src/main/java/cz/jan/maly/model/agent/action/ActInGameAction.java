package cz.jan.maly.model.agent.action;

import bwapi.Game;
import cz.jan.maly.model.agent.AgentActionCycleAbstract;
import cz.jan.maly.model.agent.action.game.Action;
import cz.jan.maly.model.agent.implementation.AgentWithGameRepresentation;
import cz.jan.maly.sfol.FormulaInterface;
import cz.jan.maly.utils.MyLogger;

import java.util.LinkedHashMap;
import java.util.Optional;

import static cz.jan.maly.utils.FrameworkUtils.getMaxFrameExecutionTime;

/**
 * Abstract class ActInGameAction issues command to game based on its implementation trough GameIssueCommandManager
 * Created by Jan on 14-Dec-16.
 */
public class ActInGameAction extends AgentActionCycleAbstract implements GameActionMaker {
    //parameter of default time it takes to make action in milliseconds
    private static final long defaultTimeThatIsRequiredToMakeAction = 5;

    protected final GameIssueCommandManager issueCommandManager = GameIssueCommandManager.getInstance();
    private final Action actionToExecute;
    private final boolean actionTerminateAgent;

    //parameter of default time it takes to make action in milliseconds
    private long timeThatWasRequiredToMakeAction = defaultTimeThatIsRequiredToMakeAction;

    public ActInGameAction(AgentWithGameRepresentation agent, LinkedHashMap<FormulaInterface, AgentActionCycleAbstract> followingActionsWithConditions, Action actionToExecute, boolean actionTerminateAgent) {
        super(agent, followingActionsWithConditions, actionCycleEnum);
        this.actionToExecute = actionToExecute;
        this.actionTerminateAgent = actionTerminateAgent;
    }

    public ActInGameAction(AgentWithGameRepresentation agent, Action actionToExecute, boolean actionTerminateAgent) {
        super(agent);
        this.actionToExecute = actionToExecute;
        this.actionTerminateAgent = actionTerminateAgent;
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
    public void executeActionInGame(Game game) {
        synchronized (this) {
            long start = System.currentTimeMillis();
            actionToExecute.executeAction(((AgentWithGameRepresentation) agent).getUnit(), game);
            if (actionTerminateAgent) {
                agent.terminateAgent();
            }
            timeThatWasRequiredToMakeAction = Math.min(System.currentTimeMillis() - start, getMaxFrameExecutionTime() - 1);
            this.notifyAll();
        }
    }

    @Override
    public long getExecutionTime() {
        return timeThatWasRequiredToMakeAction;
    }
}
