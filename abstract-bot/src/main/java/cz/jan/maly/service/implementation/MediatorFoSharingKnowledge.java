package cz.jan.maly.service.implementation;

import cz.jan.maly.model.agent.data.SnapshotOfAgentOwnKnowledge;
import cz.jan.maly.model.data.ReadOnlyCommonKnowledge;
import cz.jan.maly.model.data.WorkingCommonKnowledge;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static cz.jan.maly.utils.FrameworkUtils.getLengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge;

/**
 * MediatorFoSharingKnowledge stores all knowledge from agents to reduce coupling between agents (and communication) as agents ask mediator for knowledge instead.
 * Agents send theirs knowledge updates to MediatorFoSharingKnowledge to update common knowledge base. Those updates merge with previous common knowledge
 * to update working common knowledge after defined time interval.
 * Created by Jan on 07-Dec-16.
 */
@Getter
public class MediatorFoSharingKnowledge {
    private WorkingCommonKnowledge workingCommonKnowledge = new WorkingCommonKnowledge();
    private ReadOnlyCommonKnowledge readOnlyCommonKnowledge = workingCommonKnowledge.getCloneOfKnowledge();
    private final List<SnapshotOfAgentOwnKnowledge> updateKnowledgeByKnowledgeFromAgents = new ArrayList<>();
    private boolean shouldConsume = true;
    private final Object isAliveLockMonitor = new Object();
    private final Object isRegisterLockMonitor = new Object();

    public MediatorFoSharingKnowledge() {
        Consumer consumer = new Consumer();
        consumer.start();
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
     * Method to be called by agent to get snapshot of recent common knowledge
     *
     * @return
     */
    public ReadOnlyCommonKnowledge getCommonKnowledge() {
        synchronized (isRegisterLockMonitor) {
            return readOnlyCommonKnowledge;
        }
    }

    /**
     * Class describing consumer of agents' knowledge. It process received knowledge of agents and integrate it to common one
     */
    private class Consumer extends Thread {

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
                            if (!updateKnowledgeByKnowledgeFromAgents.isEmpty()) {
                                boolean foundNewestKnowledge = false;
                                for (int i = updateKnowledgeByKnowledgeFromAgents.size() - 1; i >= 0; i--) {
                                    if (updateKnowledgeByKnowledgeFromAgents.get(i).getAgent().equals(agentsKnowledge.getAgent())) {
                                        if (!foundNewestKnowledge) {
                                            foundNewestKnowledge = true;
                                            agentsKnowledge = updateKnowledgeByKnowledgeFromAgents.remove(i);
                                        } else {
                                            updateKnowledgeByKnowledgeFromAgents.remove(i);
                                        }
                                    }
                                }
                            }

                        }
                        workingCommonKnowledge.addSnapshot(agentsKnowledge);
                    }

                    //sleep until time is up or another message with knowledge was received
                    duration = System.currentTimeMillis() - start;
                    while (duration < getLengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge() || updateKnowledgeByKnowledgeFromAgents.isEmpty()) {
                        try {
                            Thread.sleep(Math.min(5, Math.max(0, getLengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge() - duration)));
                        } catch (InterruptedException e) {
                            MyLogger.getLogger().warning(e.getLocalizedMessage());
                        }
                        duration = System.currentTimeMillis() - start;
                    }
                    if (duration >= getLengthOfIntervalToSendUpdatesBeforeUpdatingCommonKnowledge()) {
                        break;
                    }
                }

                //update working knowledge by current one
                synchronized (isRegisterLockMonitor) {
                    readOnlyCommonKnowledge = workingCommonKnowledge.getCloneOfKnowledge();
                }
                synchronized (isAliveLockMonitor) {
                    if (!shouldConsume) {
                        break;
                    }
                }
            }
        }
    }

    public void terminate() {
        synchronized (isAliveLockMonitor){
            this.shouldConsume = false;
        }
    }

}
