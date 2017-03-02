package cz.jan.maly.model.planing;

import cz.jan.maly.model.knowledge.DataForDecision;

/**
 * Interface to be implemented by intention instances to decide if agent
 * should remove its commitment to intention
 * Created by Jan on 26-Feb-17.
 */
interface RemoveCommitment {

    /**
     * Returns if agent should remove commitment to intention
     *
     * @param dataForDecision
     * @return
     */
    boolean shouldRemoveCommitment(DataForDecision dataForDecision);

}
