package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.planing.IntentionWithPlan;
import cz.jan.maly.model.planing.InternalDesire;
import cz.jan.maly.model.planing.OwnDesire;

/**
 * Template for leaf node with command (plan)
 * Created by Jan on 21-Feb-17.
 */
abstract class LeafNodeWithPlan<V extends InternalDesire, T extends IntentionWithPlan<V>> extends LeafNode<V, T> {
    private LeafNodeWithPlan(V desire) {
        super(desire);
    }

    /**
     * Leaf node containing another agent's desire
     */
    public static class WithAnotherAgentDesire extends LeafNodeWithPlan<DesireFromAnotherAgent, IntentionWithPlan<DesireFromAnotherAgent>> {
        WithAnotherAgentDesire(DesireFromAnotherAgent desire) {
            super(desire);
        }

        @Override
        public void accept(TreeVisitorInterface treeVisitor) {
            treeVisitor.visit(this);
        }
    }

    /**
     * Leaf node containing own desire
     */
    public static class WithOwnDesire extends LeafNodeWithPlan<OwnDesire, IntentionWithPlan<OwnDesire>> {

        WithOwnDesire(OwnDesire desire) {
            super(desire);
        }

        @Override
        public void accept(TreeVisitorInterface treeVisitor) {
            treeVisitor.visit(this);
        }
    }

}
