package cz.jan.maly.model.planing;

import cz.jan.maly.model.knowledge.DataForDecision;

/**
 * Interface to be implemented by desire to decide if agent
 * should commit to desire
 * Created by Jan on 21-Feb-17.
 */
public interface Commitment {

    /**
     * Returns if agent should commit to desire and make intention from it
     *
     * @param dataForDecision
     * @param desire
     * @return
     */
    boolean shouldCommit(InternalDesire<?> desire, DataForDecision dataForDecision);

}
