package cz.jan.maly.service;

import cz.jan.maly.model.ObtainingStrategyForPartOfCommonKnowledge;
import cz.jan.maly.model.agent.AgentsOwnKnowledge;
import cz.jan.maly.model.agent.PartOfCommonKnowledgeRequiredByAgent;
import cz.jan.maly.model.game.CommonKnowledge;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
public class Mediator {
    private final long lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge;
    private CommonKnowledge workingCommonKnowledge;
    private final List<AgentsOwnKnowledge> updateKnowledgeByKnowledgeFromAgents = new ArrayList<>();
    private final Logger log;

    public Mediator(long lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge, Logger log) {
        this.lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge = lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge;
        this.log = log;
    }

    public synchronized boolean receiveAgentsKnowledge(AgentsOwnKnowledge agentsOwnKnowledge) {
        return updateKnowledgeByKnowledgeFromAgents.add((AgentsOwnKnowledge) agentsOwnKnowledge.getCopyOfKnowledge());
    }

    public synchronized PartOfCommonKnowledgeRequiredByAgent constructKnowledgeRequiredByAgentAccordingToStrategy(ObtainingStrategyForPartOfCommonKnowledge obtainingStrategyForPartOfCommonKnowledge) {
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
                        AgentsOwnKnowledge agentsOwnKnowledge;
                        synchronized (updateKnowledgeByKnowledgeFromAgents) {
                            agentsOwnKnowledge = updateKnowledgeByKnowledgeFromAgents.remove(0);
                        }
                        updateCommonKnowledge(agentsOwnKnowledge);
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

        private void updateCommonKnowledge(AgentsOwnKnowledge agentsOwnKnowledge) {
            //todo update knowledge
        }
    }

}
