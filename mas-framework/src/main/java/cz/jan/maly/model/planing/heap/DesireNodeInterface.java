package cz.jan.maly.model.planing.heap;

import cz.jan.maly.model.metadata.DesireKey;

import java.util.List;
import java.util.Optional;

/**
 * Contract for each node representing desire
 * Created by Jan on 28-Feb-17.
 */
public interface DesireNodeInterface<V extends Node<?> & IntentionNodeInterface> {

    /**
     * Make commitment to this desire and replace itself by intention
     *
     * @return
     */
    Optional<V> makeCommitment(List<DesireKey> madeCommitmentToTypes, List<DesireKey> didNotMakeCommitmentToTypes,
                               List<DesireKey> typesAboutToMakeDecision);

    /**
     * Get desire key associated with desire
     *
     * @return
     */
    DesireKey getAssociatedDesireKey();

}
