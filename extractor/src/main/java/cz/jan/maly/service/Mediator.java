package cz.jan.maly.service;

import cz.jan.maly.model.ServiceInterface;
import cz.jan.maly.model.agent.AgentsKnowledge;
import cz.jan.maly.model.agent.action.GetPartOfCommonKnowledgeAction;
import cz.jan.maly.model.game.CommonKnowledge;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Mediator stores all knowledge from agents to reduce coupling between agents (and communication) as agents ask mediator for knowledge instead.
 * Agents send theirs knowledge updates to Mediator to update common knowledge base. Those updates merge with previous common knowledge
 * to update working common knowledge after defined time interval.
 * Created by Jan on 07-Dec-16.
 */
@Getter
public class Mediator implements ServiceInterface {
    //how long to save knowledge from agents before updating common knowledge with new data
    private static final long lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge = 100;

    private CommonKnowledge workingCommonKnowledge = new CommonKnowledge();
    private final List<AgentsKnowledge> updateKnowledgeByKnowledgeFromAgents = new ArrayList<>();
    private static Mediator instance = null;

    protected Mediator() {
        // Exists only to defeat instantiation.
    }

    public static Mediator getInstance() {
        if (instance == null) {
            instance = new Mediator();
        }
        return instance;
    }

    public synchronized boolean receiveAgentsKnowledge(AgentsKnowledge gameObservation) {
        return updateKnowledgeByKnowledgeFromAgents.add((AgentsKnowledge) gameObservation.getCopyOfKnowledge());
    }

    public void updateAgentsKnowledge(GetPartOfCommonKnowledgeAction updateKnowledgeAction) {
        synchronized (workingCommonKnowledge) {
            updateKnowledgeAction.updateAgentsKnowledge(workingCommonKnowledge);
        }
    }

    @Override
    public void reinitializedServiceForNewGame() {

        //todo reset common knowledge (in case of new game for same agent). kill thread, remove all observations and reinit thread

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
                        AgentsKnowledge agentsKnowledge;
                        synchronized (updateKnowledgeByKnowledgeFromAgents) {
                            agentsKnowledge = updateKnowledgeByKnowledgeFromAgents.remove(0);
                        }
                        updateCommonKnowledge(agentsKnowledge);
                    }

                    //sleep until time is up or another message with knowledge was received
                    duration = System.currentTimeMillis() - start;
                    while (duration < lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge || updateKnowledgeByKnowledgeFromAgents.isEmpty()) {
                        try {
                            Thread.sleep(Math.min(5, Math.max(0, lengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge - duration)));
                        } catch (InterruptedException e) {
                            MyLogger.getLogger().warning(e.getLocalizedMessage());
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

        private void updateCommonKnowledge(AgentsKnowledge agentsKnowledge) {
            //todo update knowledge
        }
    }

}
