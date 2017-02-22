package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.Intention;
import cz.jan.maly.model.planing.InternalDesire;

import java.util.Optional;

/**
 * Template for node in tree. It defines common data structures - desire and optional intention (present intention represents
 * commitment to desire represented by this node). Derived classes should define other useful structures (childes) and further
 * bound desires and intentions types as well as define method to accept visitor which handles data structures.
 * Created by Jan on 21-Feb-17.
 */
abstract class TreeNode<T extends InternalDesire, V extends Intention<T>> implements VisitorAcceptor {
    protected final T desire;
    protected Optional<V> intention = Optional.empty();

    TreeNode(T desire) {
        this.desire = desire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeNode)) return false;

        TreeNode<?, ?> treeNode = (TreeNode<?, ?>) o;

        if (!desire.equals(treeNode.desire)) return false;
        return intention.equals(treeNode.intention);
    }

    @Override
    public int hashCode() {
        int result = desire.hashCode();
        result = 31 * result + intention.hashCode();
        return result;
    }
}
