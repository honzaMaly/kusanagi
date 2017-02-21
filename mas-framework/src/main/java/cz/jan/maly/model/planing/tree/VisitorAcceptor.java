package cz.jan.maly.model.planing.tree;

/**
 * Interface for each element of tree to accept tree visitor instance
 * Created by Jan on 21-Feb-17.
 */
interface VisitorAcceptor {
    /**
     * Accept instance of tree visitor
     *
     * @param treeVisitor
     */
    void accept(TreeVisitorInterface treeVisitor);
}
