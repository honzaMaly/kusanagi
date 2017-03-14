package cz.jan.maly.model.planing.command;

import cz.jan.maly.model.knowledge.WorkingMemory;

/**
 * Contract for observing command classes
 * Created by Jan on 14-Mar-17.
 */
public interface ObservingCommand<E> {

    /**
     * Method to be called by Executor to execute observation command by updating memory by data from environment
     *
     * @param memory
     * @return
     */
    boolean observe(WorkingMemory memory, E environment);
}
