package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.planing.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Template for intention not in top level
 * Created by Jan on 28-Feb-17.
 */
public abstract class IntentionNodeNotTopLevel<V extends Intention<? extends InternalDesire>, T extends InternalDesire<V>, K extends Node & IntentionNodeWithChildes & Parent> extends Node.NotTopLevel<K> implements IntentionNodeInterface, VisitorAcceptor {
    final V intention;

    private IntentionNodeNotTopLevel(K parent, T desire) {
        super(parent, desire.getDesireParameters());
        this.intention = desire.formIntention(getAgent());
    }

    @Override
    public DecisionParameters getParametersToLoad() {
        return intention.getParametersToLoad();
    }

    abstract DesireNodeNotTopLevel<?, ?> formDesireNode(Agent agent);

    @Override
    public boolean removeCommitment(DataForDecision dataForDecision, Agent agent) {
        if (intention.shouldRemoveCommitment(dataForDecision)) {
            parent.replaceIntentionByDesire(this, formDesireNode(agent));
            return true;
        }
        return false;
    }

    /**
     * Implementation of top node with desire for other agents
     */
    public static abstract class WithDesireForOthers<K extends Node & IntentionNodeWithChildes & Parent> extends IntentionNodeNotTopLevel<IntentionWithDesireForOtherAgents, DesireForOthers, K> {
        private WithDesireForOthers(K parent, DesireForOthers desire) {
            super(parent, desire);
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
            treeVisitor.visit(this, getAgent());
        }

        /**
         * Parent is in top level
         */
        static class TopLevelParent extends WithDesireForOthers<IntentionNodeAtTopLevel.WithAbstractPlan> {
            TopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, DesireForOthers desire) {
                super(parent, desire);
            }

            @Override
            DesireNodeNotTopLevel<?, ?> formDesireNode(Agent agent) {
                return new DesireNodeNotTopLevel.ForOthers.TopLevelParent(parent, agent.formDesireForOthers(getDesireKey(), parent.getDesireKey()));
            }
        }

        /**
         * Parent is in top level
         */
        static class NotTopLevelParent extends WithDesireForOthers<WithAbstractPlan> {
            NotTopLevelParent(WithAbstractPlan parent, DesireForOthers desire) {
                super(parent, desire);
            }

            @Override
            DesireNodeNotTopLevel<?, ?> formDesireNode(Agent agent) {
                return new DesireNodeNotTopLevel.ForOthers.NotTopLevelParent(parent, agent.formDesireForOthers(getDesireKey(), parent.getDesireKey()));
            }
        }

    }

    /**
     * Class to extend template - to define intention node without child
     */
    public abstract static class WithPlan<K extends Node & IntentionNodeWithChildes & Parent> extends IntentionNodeNotTopLevel<IntentionWithPlan<OwnDesire.WithIntentionWithPlan>, OwnDesire.WithIntentionWithPlan, K> implements NodeWithCommand {
        private WithPlan(K parent, OwnDesire.WithIntentionWithPlan desire) {
            super(parent, desire);
        }

        @Override
        public void accept(TreeVisitorInterface treeVisitor) {
            treeVisitor.visit(this, getAgent());
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
        public Command getCommand() {
            return intention.getCommand();
        }

        /**
         * Concrete implementation, intention's desire is formed anew
         */
        static class AtTopLevelParent extends WithPlan<IntentionNodeAtTopLevel.WithAbstractPlan> {
            AtTopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, OwnDesire.WithIntentionWithPlan desire) {
                super(parent, desire);
            }

            @Override
            DesireNodeNotTopLevel<?, ?> formDesireNode(Agent agent) {
                return new DesireNodeNotTopLevel.WithPlan.AtTopLevelParent(parent, agent.formOwnDesireWithIntentionWithPlan(getDesireKey(), parent.getDesireKey()));
            }
        }

        /**
         * Concrete implementation, intention's desire is formed anew
         */
        static class NotTopLevelParent extends WithPlan<IntentionNodeNotTopLevel.WithAbstractPlan> {
            NotTopLevelParent(WithAbstractPlan parent, OwnDesire.WithIntentionWithPlan desire) {
                super(parent, desire);
            }

            @Override
            DesireNodeNotTopLevel<?, ?> formDesireNode(Agent agent) {
                return new DesireNodeNotTopLevel.WithPlan.NotTopLevelParent(parent, agent.formOwnDesireWithIntentionWithPlan(getDesireKey(), parent.getDesireKey()));
            }
        }

    }

    /**
     * Class to extend template - to define intention node with childes
     */
    public abstract static class WithAbstractPlan<V extends AbstractIntention<? extends InternalDesire>, T extends InternalDesire<V>, K extends Node & IntentionNodeWithChildes & Parent> extends IntentionNodeNotTopLevel<V, T, K> implements IntentionNodeWithChildes, Parent<DesireNodeNotTopLevel<?, ?>, IntentionNodeNotTopLevel<?, ?, ?>> {
        private final Map<Intention<?>, IntentionNodeNotTopLevel<?, ?, ?>> intentions = new HashMap<>();
        final Map<InternalDesire<?>, DesireNodeNotTopLevel<?, ?>> desires = new HashMap<>();

        private WithAbstractPlan(K parent, T desire) {
            super(parent, desire);
            intention.returnPlanAsSetOfDesiresForOthers().forEach(desireForOthers -> desires.put(desireForOthers, new DesireNodeNotTopLevel.ForOthers.NotTopLevelParent(this, desireForOthers)));
            intention.returnPlanAsSetOfDesiresWithIntentionWithPlan().forEach(withIntentionWithPlan -> desires.put(withIntentionWithPlan, new DesireNodeNotTopLevel.WithPlan.NotTopLevelParent(this, withIntentionWithPlan)));
            intention.returnPlanAsSetOfDesiresWithAbstractIntention().forEach(withAbstractIntention -> desires.put(withAbstractIntention, new DesireNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent(this, withAbstractIntention)));
        }

        @Override
        public List<DesireNodeNotTopLevel<?, ?>> getNodesWithDesire() {
            return desires.values().stream()
                    .collect(Collectors.toList());
        }

        @Override
        public List<IntentionNodeNotTopLevel<?, ?, ?>> getNodesWithIntention() {
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
            treeVisitor.visit(this, getAgent());
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
         * Parent is in top level
         */
        static class TopLevelParent extends WithAbstractPlan<AbstractIntention<OwnDesire.WithAbstractIntention>, OwnDesire.WithAbstractIntention, IntentionNodeAtTopLevel.WithAbstractPlan> {
            TopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, OwnDesire.WithAbstractIntention desire) {
                super(parent, desire);
            }

            @Override
            DesireNodeNotTopLevel<?, ?> formDesireNode(Agent agent) {
                return new DesireNodeNotTopLevel.WithAbstractPlan.TopLevelParent(parent, agent.formOwnDesireWithAbstractIntention(getDesireKey(), parent.getDesireKey()));
            }

            @Override
            public Optional<DesireKey> getDesireKeyAssociatedWithParent() {
                return Optional.of(getDesireKey());
            }
        }

        /**
         * Parent is in top level
         */
        static class NotTopLevelParent extends WithAbstractPlan<AbstractIntention<OwnDesire.WithAbstractIntention>, OwnDesire.WithAbstractIntention, IntentionNodeNotTopLevel.WithAbstractPlan> {
            NotTopLevelParent(IntentionNodeNotTopLevel.WithAbstractPlan parent, OwnDesire.WithAbstractIntention desire) {
                super(parent, desire);
            }

            @Override
            DesireNodeNotTopLevel<?, ?> formDesireNode(Agent agent) {
                return new DesireNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent(parent, agent.formOwnDesireWithAbstractIntention(getDesireKey(), parent.getDesireKey()));
            }

            @Override
            public Optional<DesireKey> getDesireKeyAssociatedWithParent() {
                return Optional.of(getDesireKey());
            }
        }

    }

}
