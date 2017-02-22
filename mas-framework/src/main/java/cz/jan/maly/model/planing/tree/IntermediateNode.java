package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.AbstractIntention;
import cz.jan.maly.model.planing.InternalDesire;

import java.util.HashSet;
import java.util.Set;

/**
 * Class for intermediate nodes to handle branching of tree
 * Created by Jan on 21-Feb-17.
 */
abstract class IntermediateNode<V extends InternalDesire, T extends AbstractIntention<V>> extends TreeNode<V, T> {
    protected final Set<TreeNode> childes = new HashSet<>();

    IntermediateNode(V desire) {
        super(desire);
    }
}
