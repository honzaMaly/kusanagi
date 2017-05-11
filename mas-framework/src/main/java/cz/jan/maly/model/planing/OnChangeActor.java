package cz.jan.maly.model.planing;

import cz.jan.maly.model.knowledge.WorkingMemory;

import java.util.Optional;

/**
 * Interface with default method to add functionality to react on commitment change
 * Created by Jan on 10-May-17.
 */
interface OnChangeActor {
    ReactionOnChangeStrategy DEFAULT_REACTION_DO_NOTHING = memory -> {};

    Optional<ReactionOnChangeStrategy> getReactionOnChangeStrategy();

    /**
     * React on commitment
     * @param memory
     * @return always true
     */
    default boolean actOnChange(WorkingMemory memory){

        //execute provided strategy or the default one
        getReactionOnChangeStrategy().orElse(DEFAULT_REACTION_DO_NOTHING).updateBeliefs(memory);

        //TODO - !!!HACK!!! - always return true as it is used in condition
        return true;
    }

}
