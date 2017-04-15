package cz.jan.maly.service.implementation;

import bwapi.Game;
import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.planing.command.ObservingCommand;
import cz.jan.maly.utils.MyLogger;

/**
 * Extra class to handle additional requests to observe game
 * Created by Jan on 05-Apr-17.
 */
public class AdditionalCommandToObserveGameProcessor {
    private final GameCommandExecutor commandExecutor;

    public AdditionalCommandToObserveGameProcessor(GameCommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    /**
     * Method to request extra game observation
     */
    public void requestObservation(ObservingCommand<Game> command, WorkingMemory memory, AgentType agentType) {
        GuardWaitingForResponse guardWaitingForResponse = new GuardWaitingForResponse();
        guardWaitingForResponse.requestObservation(command, memory, agentType);
    }

    /**
     * Class to represent object which waits on command execution
     */
    private class GuardWaitingForResponse implements ResponseReceiverInterface<Boolean> {
        final Object lockMonitor = new Object();

        /**
         * Send command to observe game
         *
         * @param command
         * @param memory
         * @param agentType
         */
        void requestObservation(ObservingCommand<Game> command, WorkingMemory memory,
                                AgentType agentType) {
            synchronized (lockMonitor) {
                if (commandExecutor.addCommandToObserve(command, memory, this, agentType)) {
                    try {
                        lockMonitor.wait();
                    } catch (InterruptedException e) {
                        MyLogger.getLogger().warning(this.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
                    }
                }
            }
        }

        @Override
        public void receiveResponse(Boolean response) {

            //notify waiting method
            synchronized (lockMonitor) {
                if (!response) {
                    MyLogger.getLogger().warning(this.getClass().getSimpleName() + " could not execute command");
                }
                lockMonitor.notify();
            }
        }
    }
}
