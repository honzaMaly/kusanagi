package cz.jan.maly.model.agent.action;

import bwapi.Game;
import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.AgentActionCycleAbstract;
import cz.jan.maly.model.GameObserver;
import cz.jan.maly.model.agent.AgentKnowledgeUpdateByGameObservationStrategy;
import cz.jan.maly.service.GameObserverManager;
import cz.jan.maly.service.MyLogger;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Class GetGameObservationAction add itself to queue of ObservationManager and waits until it can make observation of game to update agent's knowledge.
 * Knowledge is updated based on provided strategy.
 * Created by Jan on 14-Dec-16.
 */
public abstract class GetGameObservationAction extends AgentActionCycleWithNextActionAbstract implements GameObserver {
    //parameter of default time it takes to make observation of the game in milliseconds
    private static final long defaultTimeThatIsRequiredToMakeObservation = 40;

    private final GameObserverManager gameObserverManager = GameObserverManager.getInstance();
    private final AgentKnowledgeUpdateByGameObservationStrategy agentKnowledgeUpdateByGameObservationStrategy;
    private long timeThatWasRequiredToMakeObservation = defaultTimeThatIsRequiredToMakeObservation;

    public GetGameObservationAction(Agent agent, List<AgentActionCycleAbstract> followingActions, AgentKnowledgeUpdateByGameObservationStrategy agentKnowledgeUpdateByGameObservationStrategy) {
        super(agent, followingActions);
        this.agentKnowledgeUpdateByGameObservationStrategy = agentKnowledgeUpdateByGameObservationStrategy;
    }

    public long getTimeThatWasRequiredToMakeObservation() {
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
            agentKnowledgeUpdateByGameObservationStrategy.updateKnowledge(game, agent.getAgentsKnowledge());
            timeThatWasRequiredToMakeObservation = System.currentTimeMillis() - start;
            this.notifyAll();
        }
    }
}
