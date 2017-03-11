package cz.jan.maly.service;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.planing.Command;

/**
 * Contract for CommandManager
 * Created by Jan on 26-Feb-17.
 */
public interface CommandManager<T extends Command<?, K>, K extends Memory<?>> {

    /**
     * Execute command and returns result of operation
     *
     * @param memory
     * @param commandToExecute
     * @return
     */
    boolean executeCommand(T commandToExecute, K memory);
}
