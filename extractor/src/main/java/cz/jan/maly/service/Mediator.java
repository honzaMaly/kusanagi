package cz.jan.maly.service;

import cz.jan.maly.model.ObtainingStrategyForPartOfCommonKnowledge;
import cz.jan.maly.model.agent.GameObservation;
import cz.jan.maly.model.agent.PartOfCommonKnowledgeRequestedByAgent;
import cz.jan.maly.model.game.CommonKnowledge;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Mediator stores all knowledge from agents to reduce coupling between agents as agents ask mediator for features instead.
 * Agents send theirs knowledge updates to Mediator to update common knowledge base. Those updates merge with previous common knowledge
 * to update working common knowledge after defined time interval.
 * Created by Jan on 07-Dec-16.
 */
@Getter
@Singleton
public class Mediator {
    private final long lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge;
    private CommonKnowledge workingCommonKnowledge;
    private final List<GameObservation> updateKnowledgeByKnowledgeFromAgents = new ArrayList<>();
    @Inject
    private Logger log;

    @Inject
    public Mediator(long lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge) {
        this.lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge = lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge;
    }

    public synchronized boolean receiveAgentsKnowledge(GameObservation gameObservation) {
        return updateKnowledgeByKnowledgeFromAgents.add((GameObservation) gameObservation.getCopyOfKnowledge());
    }

    public synchronized PartOfCommonKnowledgeRequestedByAgent constructKnowledgeRequiredByAgentAccordingToStrategy(ObtainingStrategyForPartOfCommonKnowledge obtainingStrategyForPartOfCommonKnowledge) {
        return obtainingStrategyForPartOfCommonKnowledge.composeKnowledge(workingCommonKnowledge);
    }

    @AllArgsConstructor
    private class Consumer implements Runnable {
        private final CommonKnowledge commonKnowledge;

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis(), duration;
                while (true) {

                    //update current knowledge by knowledge received from agents
                    if (!updateKnowledgeByKnowledgeFromAgents.isEmpty()) {
                        GameObservation gameObservation;
                        synchronized (updateKnowledgeByKnowledgeFromAgents) {
                            gameObservation = updateKnowledgeByKnowledgeFromAgents.remove(0);
                        }
                        updateCommonKnowledge(gameObservation);
                    }

                    //sleep until time is up or another message with knowledge was received
                    duration = System.currentTimeMillis() - start;
                    while (duration < lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge || updateKnowledgeByKnowledgeFromAgents.isEmpty()) {
                        try {
                            Thread.sleep(Math.min(5, Math.max(0, lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge - duration)));
                        } catch (InterruptedException e) {
                            log.warning(e.getLocalizedMessage());
                        }
                        duration = System.currentTimeMillis() - start;
                    }
                    if (duration >= lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge) {
                        break;
                    }
                }

                //update working knowledge by current one
                synchronized (workingCommonKnowledge) {
                    workingCommonKnowledge = (CommonKnowledge) commonKnowledge.getCopyOfKnowledge();
                }

            }
        }

        private void updateCommonKnowledge(GameObservation gameObservation) {
            //todo update knowledge
        }
    }

}
