package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.SharedDesireInRegister;
import cz.jan.maly.utils.MyLogger;

/**
 * Routine for sharing desire with mediator
 * Created by Jan on 13-Mar-17.
 */
class SharingDesireRoutine implements ResponseReceiverInterface<Boolean> {
    private final Object lockMonitor = new Object();
    private Boolean registered = false;

    boolean sharedDesire(SharedDesireInRegister sharedDesire) {
        if (Agent.DESIRE_MEDIATOR.registerDesire(sharedDesire, this)) {
            synchronized (lockMonitor) {
                try {
                    lockMonitor.wait();
                } catch (InterruptedException e) {
                    MyLogger.getLogger().warning(this.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
                }
            }

            //is desire register, if so, make intention out of it
            if (registered) {
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
            this.registered = response;
            lockMonitor.notify();
        }
    }
}
