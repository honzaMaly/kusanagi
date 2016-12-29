package cz.jan.maly.service.implementation;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.CommitmentCommand;
import cz.jan.maly.model.data.KeyToRequest;
import cz.jan.maly.model.data.Request;
import cz.jan.maly.model.data.RequestRegisterReadOnly;
import cz.jan.maly.model.data.WorkingRequestRegister;
import cz.jan.maly.utils.MyLogger;
import lombok.AllArgsConstructor;

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
public class MediatorForSharingRequests {
    private WorkingRequestRegister workingRequestRegister = new WorkingRequestRegister();
    private RequestRegisterReadOnly requestRegisterReadOnly = workingRequestRegister.getSnapshotOfRegister();
    //queue to update register
    private final List<QueuedItem> queue = new ArrayList<>();
    private boolean shouldConsume = true;
    private final Object isAliveLockMonitor = new Object();
    private final Object isRegisterLockMonitor = new Object();

    public MediatorForSharingRequests() {
        Consumer consumer = new Consumer();
        consumer.start();
    }

    /**
     * Method to add commitment to request
     *
     * @param keyToRequest
     * @param request
     * @param agentWhoWantsToCommit
     * @return
     */
    public boolean makeCommitmentToRequest(KeyToRequest keyToRequest, Request request, Agent agentWhoWantsToCommit, CommitmentCommand command) {
        synchronized (queue) {
            return queue.add(new QueuedItem(command) {
                @Override
                protected boolean executeItem() {
                    return workingRequestRegister.madeCommitmentToRequest(keyToRequest, request, agentWhoWantsToCommit, false);
                }
            });
        }
    }

    /**
     * Method to remove commitment to request
     *
     * @param keyToRequest
     * @param request
     * @param agentWhoWantsToCommit
     * @return
     */
    public boolean removeCommitmentToRequest(KeyToRequest keyToRequest, Request request, Agent agentWhoWantsToCommit, CommitmentCommand command) {
        synchronized (queue) {
            return queue.add(new QueuedItem(command) {
                @Override
                protected boolean executeItem() {
                    return workingRequestRegister.madeCommitmentToRequest(keyToRequest, request, agentWhoWantsToCommit, true);
                }
            });
        }
    }

    /**
     * Method to make request
     *
     * @param keyToRequest
     * @param request
     * @param agent
     * @return
     */
    public boolean addRequest(KeyToRequest keyToRequest, Request request, Agent agent, CommitmentCommand command) {
        synchronized (queue) {
            return queue.add(new QueuedItem(command) {
                @Override
                protected boolean executeItem() {
                    return workingRequestRegister.madeRequest(keyToRequest, request, agent, false);
                }
            });
        }
    }

    /**
     * Method to remove request
     *
     * @param keyToRequest
     * @param request
     * @param agent
     * @return
     */
    public boolean removeRequest(KeyToRequest keyToRequest, Request request, Agent agent, CommitmentCommand command) {
        synchronized (queue) {
            return queue.add(new QueuedItem(command) {
                @Override
                protected boolean executeItem() {
                    return workingRequestRegister.madeRequest(keyToRequest, request, agent, true);
                }
            });
        }
    }

    /**
     * Class describing consumer of agents' requests made on register of requests.
     * It process received requests of agents and integrate it to register
     */
    private class Consumer extends Thread {

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
                        queuedItem.handleRequest();
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
                synchronized (isRegisterLockMonitor) {
                    requestRegisterReadOnly = workingRequestRegister.getSnapshotOfRegister();
                }
                synchronized (isAliveLockMonitor) {
                    if (!shouldConsume) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Return current copy of requests in register. This copy is read only.
     *
     * @return
     */
    public RequestRegisterReadOnly returnCopyOfRegister() {
        synchronized (isRegisterLockMonitor) {
            return requestRegisterReadOnly;
        }
    }

    /**
     * Class represent one item in queue with operation to be done upon register (based on method implementation).
     * This ensure uniform interface for request handling
     */
    @AllArgsConstructor
    protected abstract class QueuedItem {
        private final CommitmentCommand commitmentCommand;

        public void handleRequest() {
            commitmentCommand.handleResultOfCommitmentRequest(executeItem());
        }

        protected abstract boolean executeItem();
    }

    public void terminate() {
        synchronized (isAliveLockMonitor) {
            this.shouldConsume = false;
        }
    }

}
