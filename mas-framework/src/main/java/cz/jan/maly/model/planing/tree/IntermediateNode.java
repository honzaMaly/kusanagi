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

    /**
     * Sends visitor to nodes with this node as parent
     *
     * @param treeVisitor
     */
    public void branch(TreeVisitorInterface treeVisitor) {
        childes.forEach(treeNode -> treeNode.accept(treeVisitor));
    }

    /**
     * Branch tree - create child nodes based on desires specified by intention
     */
    public void addChildes() {
        intention.get().returnPlanAsSetOfDesiresForOthers().forEach(desireForOthers -> childes.add(TreeNodeFactory.getTreeNode(desireForOthers)));
        intention.get().returnPlanAsSetOfDesiresWithAbstractIntention().forEach(withAbstractIntention -> childes.add(TreeNodeFactory.getTreeNode(withAbstractIntention)));
        intention.get().returnPlanAsSetOfDesiresWithIntentionWithPlan().forEach(withIntentionWithPlan -> childes.add(TreeNodeFactory.getTreeNode(withIntentionWithPlan)));
    }

    /**
     * Cut branches
     */
    public void cut() {
        childes.clear();
    }

}
