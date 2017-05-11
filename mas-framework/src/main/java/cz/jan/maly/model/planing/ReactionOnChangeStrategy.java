package cz.jan.maly.model.planing;

import cz.jan.maly.model.knowledge.WorkingMemory;

/**
 * Interface with template for reaction on change strategy
 * Created by Jan on 10-May-17.
 */
public interface ReactionOnChangeStrategy {

    /**
     * Strategy to update beliefs
     * @param memory
     */
    void updateBeliefs(WorkingMemory memory);

}
