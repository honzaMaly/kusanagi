package cz.jan.maly.service;

import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.planing.command.ObservingCommand;

/**
 * Contract for ObservingCommandManager
 * Created by Jan on 14-Mar-17.
 */
public interface ObservingCommandManager<E, T extends ObservingCommand<E>> {

    /**
     * Execute command and returns result of operation
     *
     * @param memory
     * @param commandToExecute
     * @return
     */
    boolean executeCommand(T commandToExecute, WorkingMemory memory, E environment);

}
