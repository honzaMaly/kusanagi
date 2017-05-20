package cz.jan.maly.model.planing.heap;

import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.planing.SharedDesireForAgents;
import cz.jan.maly.utils.MyLogger;

import java.util.HashSet;
import java.util.Set;

/**
 * Routine for shared desires removal from mediator
 * Created by Jan on 13-Mar-17.
 */
public class SharingDesireRemovalInSubtreeRoutine implements ResponseReceiverInterface<Boolean> {
    private final Object lockMonitor = new Object();
    private Boolean unregistered = false;

    boolean unregisterSharedDesire(Set<SharedDesireForAgents> sharedDesires, HeapOfTrees heapOfTrees) {

        //share desire and wait for response of registration
        synchronized (lockMonitor) {
            if (heapOfTrees.getAgent().getDesireMediator().unregisterDesires(new HashSet<>(sharedDesires), this)) {
                try {
                    lockMonitor.wait();
                } catch (InterruptedException e) {
                    MyLogger.getLogger().warning(this.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
                }

                //is desire register, if so, make intention out of it
                if (unregistered) {
                    heapOfTrees.removeSharedDesiresForOtherAgents(sharedDesires);
                    return true;
                } else {
                    MyLogger.getLogger().warning(this.getClass().getSimpleName() + ": desire for others was not registered.");
                }
            }
        }
        return false;
    }

    @Override
    public void receiveResponse(Boolean response) {

        //notify waiting method to decide commitment
        synchronized (lockMonitor) {
            this.unregistered = response;
            lockMonitor.notify();
        }
    }

}
