package cz.jan.maly.service;

import cz.jan.maly.model.QueuedItemInterfaceWithResponse;
import cz.jan.maly.model.servicies.Register;
import cz.jan.maly.model.servicies.WorkingRegister;
import cz.jan.maly.utils.MyLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Template for each mediator to implement. Mediator instance enables agents to share information through it. Instead of direct
 * communication agent to agent mediator is used to reduce communication required. At time, only snapshot of container with
 * shared information is available to agents (to reduce synchronization required). This snapshot is periodically
 * updated however data contained may not be actual when snapshot is downloaded by agent.
 * Created by Jan on 17-Feb-17.
 */
public abstract class MediatorTemplate<V extends Register<?>, T extends Register<?> & WorkingRegister<V>> implements TerminableService {
    protected final T workingRegister;
    private V readOnlyRegister;
    private final List<QueuedItemInterfaceWithResponse<?>> queuedItems = new ArrayList<>();
    private final LengthOfIntervalObtainingStrategy lengthOfIntervalObtainingStrategy;
    private boolean shouldConsume = true;
    private final Object isAliveLockMonitor = new Object();
    private long updates = 0;

    protected MediatorTemplate(T workingRegister, LengthOfIntervalObtainingStrategy lengthOfIntervalObtainingStrategy) {
        this.workingRegister = workingRegister;
        this.lengthOfIntervalObtainingStrategy = lengthOfIntervalObtainingStrategy;
        this.readOnlyRegister = this.workingRegister.makeSnapshot();

        //main code for mediator to handle queue and update shared register
        Consumer consumer = new Consumer();
        consumer.start();
    }

    /**
     * Interface for strategy describing method to return interval to wait before updating read only register
     */
    protected interface LengthOfIntervalObtainingStrategy {
        int getLengthOfInterval();
    }

    /**
     * Add request to internal queue
     *
     * @param request
     * @return
     */
    protected boolean addToQueue(QueuedItemInterfaceWithResponse<?> request) {
        synchronized (queuedItems) {
            return queuedItems.add(request);
        }
    }

    /**
     * Get number of updates of mediator
     *
     * @return
     */
    public long getUpdateCount() {
        return updates;
    }

    /**
     * Returns snapshot of register
     *
     * @return
     */
    public V getSnapshotOfRegister() {
        return readOnlyRegister;
    }

    public void terminate() {
        synchronized (isAliveLockMonitor) {
            this.shouldConsume = false;
            MyLogger.getLogger().info(this.getClass().getSimpleName() + " is being terminated.");
        }
    }

    /**
     * Class describing consumer of queued item. Consumer executes items and every once in a while update read only
     * register
     */
    private class Consumer extends Thread {

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis(), duration;
                while (true) {

                    //execute requests from queue
                    if (!queuedItems.isEmpty()) {
                        QueuedItemInterfaceWithResponse queuedItem;
                        synchronized (queuedItems) {
                            queuedItem = queuedItems.remove(0);
                        }
                        queuedItem.executeItem();
                    }

                    //sleep until time is up or another request was received
                    duration = System.currentTimeMillis() - start;
                    while (duration < lengthOfIntervalObtainingStrategy.getLengthOfInterval() && queuedItems.isEmpty()) {
                        try {
                            Thread.sleep(Math.min(5, Math.max(0, lengthOfIntervalObtainingStrategy.getLengthOfInterval() - duration)));
                        } catch (InterruptedException e) {
                            MyLogger.getLogger().warning(e.getLocalizedMessage());
                        }
                        duration = System.currentTimeMillis() - start;
                    }
                    if (duration >= lengthOfIntervalObtainingStrategy.getLengthOfInterval()) {
                        break;
                    }
                }

                //update read only register by copy of current one
                readOnlyRegister = workingRegister.makeSnapshot();
                //does not need to be synchronized as this is only increased and agents check if their local update is less then update of this class
                updates++;

                //check for termination condition
                synchronized (isAliveLockMonitor) {
                    if (!shouldConsume) {
                        break;
                    }
                }
            }
        }
    }

}
