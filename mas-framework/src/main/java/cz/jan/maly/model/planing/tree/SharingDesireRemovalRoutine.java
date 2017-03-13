package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.SharedDesireForAgents;
import cz.jan.maly.utils.MyLogger;

/**
 * Routine for sharing desire removal from mediator
 * Created by Jan on 13-Mar-17.
 */
class SharingDesireRemovalRoutine implements ResponseReceiverInterface<Boolean> {
    private final Object lockMonitor = new Object();
    private Boolean unregistered = false;

    boolean unregisterSharedDesire(SharedDesireForAgents sharedDesire, Tree tree) {

        //share desire and wait for response of registration
        if (Agent.DESIRE_MEDIATOR.unregisterDesire(sharedDesire, this)) {
            synchronized (lockMonitor) {
                try {
                    lockMonitor.wait();
                } catch (InterruptedException e) {
                    MyLogger.getLogger().warning(this.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
                }
            }

            //is desire register, if so, make intention out of it
            if (unregistered) {
                tree.removeSharedDesireForOtherAgents(sharedDesire);
                return true;
            } else {
                MyLogger.getLogger().warning(this.getClass().getSimpleName() + ": desire for others was not registered.");
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
