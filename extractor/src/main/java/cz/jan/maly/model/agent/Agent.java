package cz.jan.maly.model.agent;

import cz.jan.maly.model.ObtainingStrategyForPartOfCommonKnowledge;
import cz.jan.maly.service.Mediator;
import lombok.Getter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Jan on 09-Dec-16.
 */
@Getter
public abstract class Agent implements SubscriberOfGameObserver {
    private final GameObserver gameObserver;
    private final ObtainingStrategyForPartOfCommonKnowledge obtainingStrategyForPartOfCommonKnowledgeRequiredByThisAgent;
    private final AgentsKnowledge agentsKnowledge;
    @Inject
    private Logger log;
    @Inject
    private Mediator mediator;
    private List<GameObservation> gameObservations = new ArrayList<>();
    private boolean isAlive = true;

    protected Agent(GameObserver gameObserver, ObtainingStrategyForPartOfCommonKnowledge obtainingStrategyForPartOfCommonKnowledgeRequiredByThisAgent, AgentsKnowledge agentsKnowledge) {
        this.gameObserver = gameObserver;
        this.obtainingStrategyForPartOfCommonKnowledgeRequiredByThisAgent = obtainingStrategyForPartOfCommonKnowledgeRequiredByThisAgent;
        this.agentsKnowledge = agentsKnowledge;
        gameObserver.register(this);
    }

    @Override
    public void processObservation(GameObservation observation) {
        synchronized (gameObservations) {
            gameObservations.clear();
            gameObservations.add(observation);
            gameObservations.notifyAll();
        }
    }

    /**
     * Thread to consume new observations of game related to this agent. Upon receiving new observation agent acts
     */
    private class ObservationConsumer implements Runnable {
        @Override
        public void run() {
            while (isAlive) {

                //wait for agent to act, then update his knowledge and notify him to act again upon new knowledge
                synchronized (agentsKnowledge) {
                    GameObservation gameObservation;
                    synchronized (gameObservations) {
                        while (gameObservations.isEmpty()) {
                            try {
                                gameObservations.wait();
                            } catch (Exception ex) {
                                log.warning(ex.getLocalizedMessage());
                            }
                        }

                        //get latest observations
                        gameObservation = gameObservations.remove(gameObservations.size() - 1);
                    }
                    agentsKnowledge.updateKnowledge(gameObservation);
                    agentsKnowledge.notifyAll();
                }
            }
        }
    }


    //todo workflow -> read game, ask for features, make features in factory, act, add to queue for reading game

    //todo act based on content in knowledge

}
