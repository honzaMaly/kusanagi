package cz.jan.maly.service;

import cz.jan.maly.model.ServiceInterface;
import cz.jan.maly.model.agent.SnapshotOfAgentOwnKnowledge;
import cz.jan.maly.model.agent.action.GetPartOfCommonKnowledgeAction;
import cz.jan.maly.model.CommonKnowledge;
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
    private final List<SnapshotOfAgentOwnKnowledge> updateKnowledgeByKnowledgeFromAgents = new ArrayList<>();

    private static Mediator instance = null;
    private Consumer consumer = new Consumer(workingCommonKnowledge);

    protected Mediator() {
        // Exists only to defeat instantiation.
        consumer.run();
    }

    public static Mediator getInstance() {
        if (instance == null) {
            instance = new Mediator();
        }
        return instance;
    }

    /**
     * Method to register new knowledge from agent
     *
     * @param agentsKnowledge
     * @return
     */
    public synchronized boolean receiveAgentsKnowledge(SnapshotOfAgentOwnKnowledge agentsKnowledge) {
        return updateKnowledgeByKnowledgeFromAgents.add(agentsKnowledge);
    }

    /**
     * Method to be called by agent to update his knowledge by common knowledge in a way that is described by strategy implemented by him
     *
     * @param updateKnowledgeAction
     */
    public void updateAgentsKnowledge(GetPartOfCommonKnowledgeAction updateKnowledgeAction) {
        synchronized (workingCommonKnowledge) {
            updateKnowledgeAction.updateAgentsKnowledge(workingCommonKnowledge);
        }
    }

    @Override
    public void reinitializedServiceForNewGame() {
        consumer.terminate();
        while (!consumer.isTerminated) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                MyLogger.getLogger().warning(e.getLocalizedMessage());
            }
        }
        updateKnowledgeByKnowledgeFromAgents.clear();
        workingCommonKnowledge = new CommonKnowledge();
        consumer = new Consumer(workingCommonKnowledge);
        consumer.run();
    }

    /**
     * Class describing consumer of agents' knowledge. It process received knowledge of agents and integrate it to common one
     */
    private class Consumer implements Runnable {
        private final CommonKnowledge commonKnowledge;
        private Boolean shouldConsume = true;
        protected boolean isTerminated = false;

        private Consumer(CommonKnowledge commonKnowledge) {
            this.commonKnowledge = commonKnowledge.getCloneOfKnowledge();
        }

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis(), duration;
                while (true) {

                    //update current knowledge by knowledge received from agents
                    if (!updateKnowledgeByKnowledgeFromAgents.isEmpty()) {
                        SnapshotOfAgentOwnKnowledge agentsKnowledge;
                        synchronized (updateKnowledgeByKnowledgeFromAgents) {
                            agentsKnowledge = updateKnowledgeByKnowledgeFromAgents.remove(0);

                            //check if there is more recent knowledge from agent. if so, take it and remove all older
                            boolean foundNewestKnowledge = false;
                            for (int i = updateKnowledgeByKnowledgeFromAgents.size()-1; i >= 0; i--) {
                                if (updateKnowledgeByKnowledgeFromAgents.get(i).getAgent().equals(agentsKnowledge.getAgent())){
                                    if (!foundNewestKnowledge){
                                        foundNewestKnowledge = true;
                                        agentsKnowledge = updateKnowledgeByKnowledgeFromAgents.remove(i);
                                    } else {
                                        updateKnowledgeByKnowledgeFromAgents.remove(i);
                                    }
                                }
                            }

                        }
                        commonKnowledge.addSnapshot(agentsKnowledge);
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
                    workingCommonKnowledge = commonKnowledge.getCloneOfKnowledge();
                }
                synchronized (shouldConsume) {
                    if (!shouldConsume) {
                        break;
                    }
                }
            }
            isTerminated = true;
        }

        public synchronized void terminate() {
            this.shouldConsume = false;
        }
    }

}
