package cz.jan.maly.model.planing.tree;

/**
 * Contract for intention nodes with childes
 * Created by Jan on 28-Feb-17.
 */
interface IntentionNodeWithChildes {

    /**
     * Replace desire by intention
     *
     * @param desireNode
     * @param intentionNode
     */
    void replaceDesireByIntention(DesireNodeNotTopLevel<?, ?> desireNode, IntentionNodeNotTopLevel<?, ?, ?> intentionNode);

    /**
     * Replace intention by desire
     *
     * @param desireNode
     * @param intentionNode
     */
    void replaceIntentionByDesire(IntentionNodeNotTopLevel<?, ?, ?> intentionNode, DesireNodeNotTopLevel<?, ?> desireNode);

    /**
     * Method to visit childes. Desires nodes are visited first.
     *
     * @param treeVisitorInterface
     */
    void payVisitToChildes(TreeVisitorInterface treeVisitorInterface);

}
