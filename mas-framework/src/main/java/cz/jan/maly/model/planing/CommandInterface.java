package cz.jan.maly.model.planing;

import cz.jan.maly.model.knowledge.Memory;

/**
 * Contract for command classes
 * Created by Jan on 14-Mar-17.
 */
public interface CommandInterface<K extends Memory<?, ?>> {

    /**
     * Method to be called by CommandForIntention Executor to execute command
     *
     * @param memory
     * @return
     */
    boolean act(K memory);

}
