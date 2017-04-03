package cz.jan.maly.model.planing.command;

import cz.jan.maly.model.knowledge.WorkingMemory;

/**
 * Contract for command classes
 * Created by Jan on 14-Mar-17.
 */
public interface CommandInterface {
    /**
     * Method to be called by CommandForIntention Executor to execute command
     *
     * @param memory
     * @return
     */
    boolean act(WorkingMemory memory);
}
