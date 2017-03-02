package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.metadata.DesireKey;

import java.util.Optional;

/**
 * Contract for each node representing desire
 * Created by Jan on 28-Feb-17.
 */
public interface DesireNodeInterface<V extends Node & IntentionNodeInterface> {

    /**
     * Make commitment to this desire and replace itself by intention
     *
     * @param dataForDecision
     * @return
     */
    Optional<V> makeCommitment(DataForDecision dataForDecision);

    /**
     * Get desire key associated with desire
     *
     * @return
     */
    DesireKey getAssociatedDesireKey();

}
