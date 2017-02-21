package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.Desire;
import cz.jan.maly.model.planing.Intention;

/**
 * Template for leaf node
 * Created by Jan on 21-Feb-17.
 */
abstract class LeafNode<V extends Desire, T extends Intention<V>> extends TreeNode<V, T> {
    LeafNode(V desire) {
        super(desire);
    }
}
