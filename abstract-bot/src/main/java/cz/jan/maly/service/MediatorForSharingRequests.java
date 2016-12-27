package cz.jan.maly.service;

import cz.jan.maly.model.*;
import cz.jan.maly.model.agent.Agent;

import java.util.ArrayList;
import java.util.List;

import static cz.jan.maly.utils.FrameworkUtils.getLengthOfIntervalToSendUpdatesBeforeUpdatingRegister;

/**
 * MediatorForSharingRequests handles all requests (proposals) by agents. In this framework each agents can make proposal
 * (request) for other agents what to follow. This kind of architecture enables one to have opened minded agents with
 * no central control. Agents can commit to whatever proposal they want as nobody can send them request directly.
 * Only author of proposal can remove this proposal. Agents can remove/make commitments on requests which enables them to do so.
 * Created by Jan on 21-Dec-16.
 */
public class MediatorForSharingRequests implements ServiceInterface {
    private static MediatorForSharingRequests instance = null;
    private WorkingRequestRegister workingRequestRegister = new WorkingRequestRegister();
    private RequestRegisterReadOnly requestRegisterReadOnly = workingRequestRegister.getSnapshotOfRegister();
    //queue to update register
    private final List<QueuedItem> queue = new ArrayList<>();
    private Consumer consumer = new Consumer();

    private MediatorForSharingRequests() {
        // Exists only to defeat instantiation.
        consumer.start();
    }

    public static MediatorForSharingRequests getInstance() {
        if (instance == null) {
            instance = new MediatorForSharingRequests();
        }
        return instance;
    }

    /**
     * Method to add commitment to request
     *
     * @param keyToRequest
     * @param request
     * @param agentWhoWantsToCommit
     * @return
     */
    public synchronized boolean makeCommitmentToRequest(KeyToRequest keyToRequest, Request request, Agent agentWhoWantsToCommit) {
        return queue.add(new QueuedItem() {
            @Override
            protected boolean executeItem() {
                return workingRequestRegister.madeCommitmentToRequest(keyToRequest, request, agentWhoWantsToCommit, false);
            }
        });
    }

    /**
     * Method to remove commitment to request
     *
     * @param keyToRequest
     * @param request
     * @param agentWhoWantsToCommit
     * @return
     */
    public synchronized boolean removeCommitmentToRequest(KeyToRequest keyToRequest, Request request, Agent agentWhoWantsToCommit) {
        return queue.add(new QueuedItem() {
            @Override
            protected boolean executeItem() {
                return workingRequestRegister.madeCommitmentToRequest(keyToRequest, request, agentWhoWantsToCommit, true);
            }
        });
    }

    /**
     * Method to make request
     *
     * @param keyToRequest
     * @param request
     * @param agent
     * @return
     */
    public synchronized boolean addRequest(KeyToRequest keyToRequest, Request request, Agent agent) {
        return queue.add(new QueuedItem() {
            @Override
            protected boolean executeItem() {
                return workingRequestRegister.madeRequest(keyToRequest, request, agent, false);
            }
        });
    }

    /**
     * Method to remove request
     *
     * @param keyToRequest
     * @param request
     * @param agent
     * @return
     */
    public synchronized boolean removeRequest(KeyToRequest keyToRequest, Request request, Agent agent) {
        return queue.add(new QueuedItem() {
            @Override
            protected boolean executeItem() {
                return workingRequestRegister.madeRequest(keyToRequest, request, agent, true);
            }
        });
    }

    /**
     * Class describing consumer of agents' requests made on register of requests.
     * It process received requests of agents and integrate it to register
     */
    private class Consumer extends Thread {
        private Boolean shouldConsume = true;
        protected boolean isTerminated = false;

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis(), duration;
                while (true) {

                    //execute requests from queue
                    if (!queue.isEmpty()) {
                        QueuedItem queuedItem;
                        synchronized (queue) {
                            queuedItem = queue.remove(0);
                        }

                        //todo return result of operation to action
                        queuedItem.executeItem();
                    }

                    //sleep until time is up or another request was received
                    duration = System.currentTimeMillis() - start;
                    while (duration < getLengthOfIntervalToSendUpdatesBeforeUpdatingRegister() || queue.isEmpty()) {
                        try {
                            Thread.sleep(Math.min(5, Math.max(0, getLengthOfIntervalToSendUpdatesBeforeUpdatingRegister() - duration)));
                        } catch (InterruptedException e) {
                            MyLogger.getLogger().warning(e.getLocalizedMessage());
                        }
                        duration = System.currentTimeMillis() - start;
                    }
                    if (duration >= getLengthOfIntervalToSendUpdatesBeforeUpdatingRegister()) {
                        break;
                    }
                }

                //update read only register by copy of current one
                synchronized (requestRegisterReadOnly) {
                    requestRegisterReadOnly = workingRequestRegister.getSnapshotOfRegister();
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

    /**
     * Return current copy of requests in register. This copy is read only.
     *
     * @return
     */
    public synchronized RequestRegisterReadOnly returnCopyOfRegister() {
        return requestRegisterReadOnly;
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
        workingRequestRegister = new WorkingRequestRegister();
        queue.clear();
        consumer = new Consumer();
        consumer.run();
    }

    /**
     * Class represent one item in queue with operation to be done upon register (based on method implementation).
     * This ensure uniform interface for request handling
     */
    protected abstract class QueuedItem {

        //todo action to call to return it result of operation. Action should implement new interface with method to resume

        protected abstract boolean executeItem();
    }

}
