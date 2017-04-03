package cz.jan.maly.service;

import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.planing.command.CommandInterface;

/**
 * Contract for CommandManager
 * Created by Jan on 26-Feb-17.
 */
public interface CommandManager<T extends CommandInterface> {

    /**
     * Execute command and returns result of operation
     *
     * @param commandToExecute
     * @return
     */
    default boolean executeCommand(T commandToExecute, WorkingMemory memory) {
        return commandToExecute.act(memory);
    }
}
