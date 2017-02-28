package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Template for intention in top level
 * Created by Jan on 28-Feb-17.
 */
abstract class IntentionNodeAtTopLevel<V extends Intention<? extends InternalDesire>, T extends InternalDesire<V>> extends Node.TopLevel implements IntentionNodeInterface {
    final V intention;

    private IntentionNodeAtTopLevel(Tree tree, T desire) {
        super(tree, desire.getDesireParameters());
        this.intention = desire.formIntention();
    }

    abstract DesireNodeAtTopLevel<?> formDesireNode();

    @Override
    public boolean removeCommitment() {
        if (intention.shouldRemoveCommitment()) {
            tree.replaceIntentionByDesire(this, formDesireNode());
            return true;
        }
        return false;
    }

    /**
     * Class to extend template - to define intention node without child
     */
    abstract static class WithPlan<V extends IntentionWithPlan<? extends InternalDesire>, T extends InternalDesire<V>> extends IntentionNodeAtTopLevel<V, T> {
        private WithPlan(Tree tree, T desire) {
            super(tree, desire);
        }

        @Override
        public void accept(TreeVisitorInterface treeVisitor) {
            treeVisitor.visit(this);
        }

        /**
         * Concrete implementation, intention's desire from another agent forms node
         */
        static class FromAnotherAgent extends WithPlan<IntentionWithPlan<DesireFromAnotherAgent.WithIntentionWithPlan>, DesireFromAnotherAgent.WithIntentionWithPlan> {
            private final DesireFromAnotherAgent.WithIntentionWithPlan desire;

            FromAnotherAgent(Tree tree, DesireFromAnotherAgent.WithIntentionWithPlan desire) {
                super(tree, desire);
                this.desire = desire;
            }

            @Override
            DesireNodeAtTopLevel<?> formDesireNode() {
                return new DesireNodeAtTopLevel.FromAnotherAgent.WithIntentionWithPlan(tree, desire);
            }
        }

        /**
         * Concrete implementation, intention's desire is formed anew
         */
        static class Own extends WithPlan<IntentionWithPlan<OwnDesire.WithIntentionWithPlan>, OwnDesire.WithIntentionWithPlan> {
            Own(Tree tree, OwnDesire.WithIntentionWithPlan desire) {
                super(tree, desire);
            }

            @Override
            DesireNodeAtTopLevel<?> formDesireNode() {
                return new DesireNodeAtTopLevel.Own.WithIntentionWithPlan(tree, tree.agent.formOwnDesireWithIntentionWithPlan(intention.getDesireKey()));
            }
        }
    }

    /**
     * Concrete implementation, intention's desire for other agents is formed anew
     */
    static class WithDesireForOthers extends IntentionNodeAtTopLevel<IntentionWithDesireForOtherAgents, DesireForOthers> {
        WithDesireForOthers(Tree tree, DesireForOthers desire) {
            super(tree, desire);
        }

        @Override
        DesireNodeAtTopLevel<?> formDesireNode() {
            return new DesireNodeAtTopLevel.ForOthers(tree, tree.agent.formDesireForOthers(getDesireKey()));
        }

        @Override
        public void accept(TreeVisitorInterface treeVisitor) {
            treeVisitor.visit(this);
        }
    }

    /**
     * Class to extend template - to define intention node with childes
     */
    abstract static class WithAbstractPlan<V extends AbstractIntention<? extends InternalDesire>, T extends InternalDesire<V>> extends IntentionNodeAtTopLevel<V, T> implements IntentionNodeWithChildes {
        private final Map<Intention<?>, IntentionNodeNotTopLevel<?, ?, ?>> intentionsInTopLevel = new HashMap<>();
        private final Map<InternalDesire<?>, DesireNodeNotTopLevel<?, ?>> desiresInTopLevel = new HashMap<>();

        private WithAbstractPlan(Tree tree, T desire) {
            super(tree, desire);

            //todo init intermediate nodes

        }

        @Override
        public void accept(TreeVisitorInterface treeVisitor) {
            treeVisitor.visit(this);
        }

        @Override
        public void replaceDesireByIntention(DesireNodeNotTopLevel<?, ?> desireNode, IntentionNodeNotTopLevel<?, ?, ?> intentionNode) {
            if (desiresInTopLevel.containsKey(desireNode.desire)) {
                desiresInTopLevel.remove(desireNode.desire);
                intentionsInTopLevel.put(intentionNode.intention, intentionNode);
            } else {
                throw new RuntimeException("Could not replace desire by intention, desire node is missing.");
            }
        }

        @Override
        public void replaceIntentionByDesire(IntentionNodeNotTopLevel<?, ?, ?> intentionNode, DesireNodeNotTopLevel<?, ?> desireNode) {
            if (intentionsInTopLevel.containsKey(intentionNode.intention)) {
                intentionsInTopLevel.remove(intentionNode.intention);
                desiresInTopLevel.put(desireNode.desire, desireNode);
            } else {
                throw new RuntimeException("Could not replace intention by desire, intention node is missing.");
            }
        }

        @Override
        public void payVisitToChildes(TreeVisitorInterface treeVisitorInterface) {
            desiresInTopLevel.values().forEach(node -> node.accept(treeVisitorInterface));
            intentionsInTopLevel.values().forEach(node -> node.accept(treeVisitorInterface));
        }

        /**
         * Concrete implementation, abstract intention's desire from another agent forms node
         */
        static class FromAnotherAgent extends WithAbstractPlan<AbstractIntention<DesireFromAnotherAgent.WithAbstractIntention>, DesireFromAnotherAgent.WithAbstractIntention> {
            private final DesireFromAnotherAgent.WithAbstractIntention desire;

            FromAnotherAgent(Tree tree, DesireFromAnotherAgent.WithAbstractIntention desire) {
                super(tree, desire);
                this.desire = desire;
            }

            @Override
            DesireNodeAtTopLevel<?> formDesireNode() {
                return new DesireNodeAtTopLevel.FromAnotherAgent.WithAbstractIntention(tree, desire);
            }
        }

        /**
         * Concrete implementation, abstract intention's desire is formed anew
         */
        static class Own extends WithAbstractPlan<AbstractIntention<OwnDesire.WithAbstractIntention>, OwnDesire.WithAbstractIntention> {
            Own(Tree tree, OwnDesire.WithAbstractIntention desire) {
                super(tree, desire);
            }

            @Override
            DesireNodeAtTopLevel<?> formDesireNode() {
                return new DesireNodeAtTopLevel.Own.WithAbstractIntention(tree, tree.agent.formOwnDesireWithAbstractIntention(intention.getDesireKey()));
            }
        }

    }

}
