package cz.jan.maly.model.planing.tree;

import java.util.Optional;

/**
 * Contract for each node representing desire
 * Created by Jan on 28-Feb-17.
 */
interface DesireNodeInterface<V extends Node> {

    /**
     * Make commitment to this desire and replace itself by intention
     *
     * @return
     */
    Optional<V> makeCommitment();

}
