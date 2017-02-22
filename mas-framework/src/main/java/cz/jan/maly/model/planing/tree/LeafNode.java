package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.Intention;
import cz.jan.maly.model.planing.InternalDesire;

/**
 * Template for leaf node
 * Created by Jan on 21-Feb-17.
 */
abstract class LeafNode<V extends InternalDesire, T extends Intention<V>> extends TreeNode<V, T> {
    LeafNode(V desire) {
        super(desire);
    }
}
