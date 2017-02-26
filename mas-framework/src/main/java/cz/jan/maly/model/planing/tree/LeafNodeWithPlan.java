package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.*;

/**
 * Template for leaf node with command (plan)
 * Created by Jan on 21-Feb-17.
 */
public abstract class LeafNodeWithPlan<V extends InternalDesire, T extends IntentionWithPlan<V>> extends LeafNode<V, T> {
    private LeafNodeWithPlan(V desire) {
        super(desire);
    }

    /**
     * Return command associated with intention - new instance is created
     *
     * @return
     */
    public Command getCommand() {
        return intention.get().getCommand();
    }

    /**
     * Update intention with status of command execution - failed/succeed
     *
     * @param status
     */
    public void updateIntentionWithCommandExecutionStatus(boolean status) {
        intention.get().setExecuted(status);
    }

    /**
     * Leaf node containing another agent's desire
     */
    public static class WithAnotherAgentDesire extends LeafNodeWithPlan<DesireFromAnotherAgent.WithIntentionWithPlan, IntentionWithPlan<DesireFromAnotherAgent.WithIntentionWithPlan>> {
        WithAnotherAgentDesire(DesireFromAnotherAgent.WithIntentionWithPlan desire) {
            super(desire);
        }

        @Override
        IntentionWithPlan<DesireFromAnotherAgent.WithIntentionWithPlan> getIntention() {
            return desire.formIntention();
        }

        @Override
        public void accept(TreeVisitorInterface treeVisitor) {
            treeVisitor.visit(this);
        }
    }

    /**
     * Leaf node containing own desire
     */
    public static class WithOwnDesire extends LeafNodeWithPlan<OwnDesire.WithIntentionWithPlan, IntentionWithPlan<OwnDesire.WithIntentionWithPlan>> {

        WithOwnDesire(OwnDesire.WithIntentionWithPlan desire) {
            super(desire);
        }

        @Override
        IntentionWithPlan<OwnDesire.WithIntentionWithPlan> getIntention() {
            return desire.formIntention();
        }

        @Override
        public void accept(TreeVisitorInterface treeVisitor) {
            treeVisitor.visit(this);
        }
    }

}
