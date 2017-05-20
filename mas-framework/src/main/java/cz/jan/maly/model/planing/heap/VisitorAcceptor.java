package cz.jan.maly.model.planing.heap;

/**
 * Interface for each element of heapOfTrees to accept heapOfTrees visitor instance
 * Created by Jan on 21-Feb-17.
 */
public interface VisitorAcceptor {
    /**
     * Accept instance of heapOfTrees visitor
     *
     * @param treeVisitor
     */
    void accept(TreeVisitorInterface treeVisitor);
}
