package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.tree.TreeVisitorInterface;

/**
 * Interface for each element of tree to accept tree visitor instance
 * Created by Jan on 21-Feb-17.
 */
public interface VisitorAcceptor {
    /**
     * Accept instance of tree visitor
     *
     * @param treeVisitor
     */
    void accept(TreeVisitorInterface treeVisitor);
}
