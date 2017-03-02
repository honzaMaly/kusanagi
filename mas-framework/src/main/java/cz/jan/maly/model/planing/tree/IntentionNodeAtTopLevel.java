package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.metadata.DecisionContainerParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.planing.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Template for intention in top level
 * Created by Jan on 28-Feb-17.
 */
public abstract class IntentionNodeAtTopLevel<V extends Intention<? extends InternalDesire>, T extends InternalDesire<V>> extends Node.TopLevel implements IntentionNodeInterface {
    final V intention;

    private IntentionNodeAtTopLevel(Tree tree, T desire) {
        super(tree, desire.getDesireParameters());
        this.intention = desire.formIntention();
    }

    abstract DesireNodeAtTopLevel<?> formDesireNode(Agent agent);

    @Override
    public boolean removeCommitment(DataForDecision dataForDecision, Agent agent) {
        if (intention.shouldRemoveCommitment(dataForDecision)) {
            parent.replaceIntentionByDesire(this, formDesireNode(agent));
            return true;
        }
        return false;
    }

    @Override
    public DecisionContainerParameters getParametersToLoad() {
        return intention.getParametersToLoad();
    }

    /**
     * Class to extend template - to define intention node without child
     */
    public abstract static class WithPlan<V extends IntentionWithPlan<? extends InternalDesire>, T extends InternalDesire<V>> extends IntentionNodeAtTopLevel<V, T> {
        private WithPlan(Tree tree, T desire) {
            super(tree, desire);
        }

        @Override
        public void collectKeysOfCommittedDesiresInSubtree(List<DesireKey> list) {
            list.add(getDesireKey());
        }

        @Override
        public void collectKeysOfDesiresInSubtree(List<DesireKey> list) {
            //skip
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
            DesireNodeAtTopLevel<?> formDesireNode(Agent agent) {
                return new DesireNodeAtTopLevel.FromAnotherAgent.WithIntentionWithPlan(parent, desire);
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
            DesireNodeAtTopLevel<?> formDesireNode(Agent agent) {
                return new DesireNodeAtTopLevel.Own.WithIntentionWithPlan(parent, agent.formOwnDesireWithIntentionWithPlan(intention.getDesireKey()));
            }
        }
    }

    /**
     * Concrete implementation, intention's desire for other agents is formed anew
     */
    public static class WithDesireForOthers extends IntentionNodeAtTopLevel<IntentionWithDesireForOtherAgents, DesireForOthers> {
        WithDesireForOthers(Tree tree, DesireForOthers desire) {
            super(tree, desire);
        }

        @Override
        public void collectKeysOfCommittedDesiresInSubtree(List<DesireKey> list) {
            list.add(getDesireKey());
        }

        @Override
        public void collectKeysOfDesiresInSubtree(List<DesireKey> list) {
            //skip
        }

        @Override
        DesireNodeAtTopLevel<?> formDesireNode(Agent agent) {
            return new DesireNodeAtTopLevel.ForOthers(parent, agent.formDesireForOthers(getDesireKey()));
        }

        @Override
        public void accept(TreeVisitorInterface treeVisitor) {
            treeVisitor.visit(this);
        }
    }

    /**
     * Class to extend template - to define intention node with childes
     */
    public abstract static class WithAbstractPlan<V extends AbstractIntention<? extends InternalDesire>, T extends InternalDesire<V>> extends IntentionNodeAtTopLevel<V, T> implements IntentionNodeWithChildes, Parent {
        private final Map<Intention<?>, IntentionNodeNotTopLevel<?, ?, ?>> intentions = new HashMap<>();
        private final Map<InternalDesire<?>, DesireNodeNotTopLevel<?, ?>> desires = new HashMap<>();

        private WithAbstractPlan(Tree tree, T desire) {
            super(tree, desire);
            intention.returnPlanAsSetOfDesiresForOthers().forEach(desireForOthers -> desires.put(desireForOthers, new DesireNodeNotTopLevel.ForOthers.TopLevelParent(this, desireForOthers)));
            intention.returnPlanAsSetOfDesiresWithIntentionWithPlan().forEach(withIntentionWithPlan -> desires.put(withIntentionWithPlan, new DesireNodeNotTopLevel.WithPlan.AtTopLevelParent(this, withIntentionWithPlan)));
            intention.returnPlanAsSetOfDesiresWithAbstractIntention().forEach(withAbstractIntention -> desires.put(withAbstractIntention, new DesireNodeNotTopLevel.WithAbstractPlan.TopLevelParent(this, withAbstractIntention)));
        }

        @Override
        public List<Node> getNodesWithDesire() {
            return desires.values().stream()
                    .collect(Collectors.toList());
        }

        @Override
        public List<Node> getNodesWithIntention() {
            return intentions.values().stream()
                    .collect(Collectors.toList());
        }

        @Override
        public Set<DesireParameters> getParametersOfCommittedDesires() {
            return intentions.values().stream()
                    .map(intentionNode -> intentionNode.desireParameters)
                    .collect(Collectors.toSet());
        }

        @Override
        public Set<DesireParameters> getParametersOfDesires() {
            return desires.values().stream()
                    .map(desiresNode -> desiresNode.desireParameters)
                    .collect(Collectors.toSet());
        }

        @Override
        public void collectKeysOfCommittedDesiresInSubtree(List<DesireKey> list) {
            list.add(intention.getDesireKey());
            intentions.values().forEach(intentionNode -> intentionNode.collectKeysOfCommittedDesiresInSubtree(list));
        }

        @Override
        public void collectKeysOfDesiresInSubtree(List<DesireKey> list) {
            desires.values().forEach(desireNode -> list.add(desireNode.getDesireKey()));
            intentions.values().forEach(intentionNode -> intentionNode.collectKeysOfDesiresInSubtree(list));
        }

        @Override
        public void accept(TreeVisitorInterface treeVisitor) {
            treeVisitor.visit(this);
        }

        @Override
        public void replaceDesireByIntention(DesireNodeNotTopLevel<?, ?> desireNode, IntentionNodeNotTopLevel<?, ?, ?> intentionNode) {
            if (desires.containsKey(desireNode.desire)) {
                desires.remove(desireNode.desire);
                intentions.put(intentionNode.intention, intentionNode);
            } else {
                throw new RuntimeException("Could not replace desire by intention, desire node is missing.");
            }
        }

        @Override
        public void replaceIntentionByDesire(IntentionNodeNotTopLevel<?, ?, ?> intentionNode, DesireNodeNotTopLevel<?, ?> desireNode) {
            if (intentions.containsKey(intentionNode.intention)) {
                intentions.remove(intentionNode.intention);
                desires.put(desireNode.desire, desireNode);
            } else {
                throw new RuntimeException("Could not replace intention by desire, intention node is missing.");
            }
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
            DesireNodeAtTopLevel<?> formDesireNode(Agent agent) {
                return new DesireNodeAtTopLevel.FromAnotherAgent.WithAbstractIntention(parent, desire);
            }

            @Override
            public Optional<DesireKey> getDesireKeyAssociatedWithParent() {
                return Optional.of(getDesireKey());
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
            DesireNodeAtTopLevel<?> formDesireNode(Agent agent) {
                return new DesireNodeAtTopLevel.Own.WithAbstractIntention(parent, agent.formOwnDesireWithAbstractIntention(intention.getDesireKey()));
            }

            @Override
            public Optional<DesireKey> getDesireKeyAssociatedWithParent() {
                return Optional.of(getDesireKey());
            }
        }

    }

}
