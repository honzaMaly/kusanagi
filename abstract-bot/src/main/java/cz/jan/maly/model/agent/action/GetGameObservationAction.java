package cz.jan.maly.model.agent.action;

import bwapi.Game;
import cz.jan.maly.model.GameObserver;
import cz.jan.maly.model.agent.AgentActionCycleAbstract;
import cz.jan.maly.model.agent.AgentKnowledgeUpdateByGameObservationStrategy;
import cz.jan.maly.model.agent.AgentWithGameRepresentation;
import cz.jan.maly.model.sflo.TermInterface;
import cz.jan.maly.service.GameObserverManager;
import cz.jan.maly.service.MyLogger;

import java.util.LinkedHashMap;
import java.util.Optional;

import static cz.jan.maly.service.OnFrameExecutor.maxFrameExecutionTime;

/**
 * Class GetGameObservationAction add itself to queue of ObservationManager and waits until it can make observation of game to update agent's knowledge.
 * Knowledge is updated based on provided strategy.
 * Created by Jan on 14-Dec-16.
 */
public class GetGameObservationAction extends AgentActionCycleAbstract implements GameObserver {
    //parameter of default time it takes to make observation of the game in milliseconds
    private static final long defaultTimeThatIsRequiredToMakeObservation = 20;

    private final GameObserverManager gameObserverManager = GameObserverManager.getInstance();
    private final AgentKnowledgeUpdateByGameObservationStrategy agentKnowledgeUpdateByGameObservationStrategy;
    private long timeThatWasRequiredToMakeObservation = defaultTimeThatIsRequiredToMakeObservation;

    public GetGameObservationAction(AgentWithGameRepresentation agent, LinkedHashMap<TermInterface, AgentActionCycleAbstract> followingActionsWithConditions, AgentKnowledgeUpdateByGameObservationStrategy agentKnowledgeUpdateByGameObservationStrategy) {
        super(agent, followingActionsWithConditions);
        this.agentKnowledgeUpdateByGameObservationStrategy = agentKnowledgeUpdateByGameObservationStrategy;
    }

    public GetGameObservationAction(AgentWithGameRepresentation agent, AgentKnowledgeUpdateByGameObservationStrategy agentKnowledgeUpdateByGameObservationStrategy) {
        super(agent);
        this.agentKnowledgeUpdateByGameObservationStrategy = agentKnowledgeUpdateByGameObservationStrategy;
    }

    @Override
    public long getExecutionTime() {
        return timeThatWasRequiredToMakeObservation;
    }

    @Override
    public Optional<AgentActionCycleAbstract> executeAction() {
        synchronized (this) {
            if (gameObserverManager.isObserverOfGamePutInQueue(this)) {
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
    public void makeObservation(Game game) {
        synchronized (this) {
            long start = System.currentTimeMillis();
            agentKnowledgeUpdateByGameObservationStrategy.updateKnowledge(game, agent.getAgentsKnowledge(), ((AgentWithGameRepresentation) agent).getUnit());
            timeThatWasRequiredToMakeObservation = Math.min(System.currentTimeMillis() - start, maxFrameExecutionTime - 1);
            this.notifyAll();
        }
    }
}
