package cz.jan.maly.service;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.planing.CommandInterface;

/**
 * Contract for CommandManager
 * Created by Jan on 26-Feb-17.
 */
public interface CommandManager<T extends CommandInterface<K>, K extends Memory<?>> {

    /**
     * Execute command and returns result of operation
     *
     * @param memory
     * @param commandToExecute
     * @return
     */
    boolean executeCommand(T commandToExecute, K memory);
}
