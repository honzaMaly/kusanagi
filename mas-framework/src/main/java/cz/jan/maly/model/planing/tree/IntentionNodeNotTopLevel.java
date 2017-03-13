package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.planing.*;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Template for intention not in top level
 * Created by Jan on 28-Feb-17.
 */
public abstract class IntentionNodeNotTopLevel<V extends Intention<? extends InternalDesire<?>>, T extends InternalDesire<? extends V>, K extends Node & IntentionNodeWithChildes & Parent> extends Node.NotTopLevel<K> implements IntentionNodeInterface, VisitorAcceptor {
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
            treeVisitor.visit(this);
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

            @Override
            public void collectSharedDesiresForOtherAgentsInSubtree(Set<SharedDesire> sharedDesiresInSubtree) {
                sharedDesiresInSubtree.add(intention.getSharedDesire());
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

            @Override
            public void collectSharedDesiresForOtherAgentsInSubtree(Set<SharedDesire> sharedDesiresInSubtree) {
                sharedDesiresInSubtree.add(intention.getSharedDesire());
            }
        }

    }

    /**
     * Class to extend template - to define intention node without child
     */
    public abstract static class WithCommand<K extends Node & IntentionNodeWithChildes & Parent, V extends InternalDesire<? extends IntentionCommand<V, T>>, T extends Command<? extends IntentionCommand<V, T>, ?>> extends IntentionNodeNotTopLevel<IntentionCommand<V, T>, V, K> implements NodeWithCommand {
        private WithCommand(K parent, V desire) {
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
        public T getCommand() {
            return intention.getCommand();
        }

        @Override
        public void collectSharedDesiresForOtherAgentsInSubtree(Set<SharedDesire> sharedDesiresInSubtree) {
            //skip
        }

        /**
         * Concrete implementation, intention's desire is formed anew
         */
        static class ReasoningAtTopLevelParent extends WithCommand<IntentionNodeAtTopLevel.WithAbstractPlan, OwnDesire.Reasoning, ReasoningCommand> {
            ReasoningAtTopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, OwnDesire.Reasoning desire) {
                super(parent, desire);
            }

            @Override
            DesireNodeNotTopLevel<?, ?> formDesireNode(Agent agent) {
                return new DesireNodeNotTopLevel.WithCommand.ReasoningAtTopLevelParent(parent, agent.formOwnDesireWithReasoningCommand(getDesireKey(), parent.getDesireKey()));
            }

            @Override
            public void accept(TreeVisitorInterface treeVisitor) {
                treeVisitor.visitNodeWithReasoningCommand(this);
            }
        }

        /**
         * Concrete implementation, intention's desire is formed anew
         */
        static class ActingAtTopLevelParent extends WithCommand<IntentionNodeAtTopLevel.WithAbstractPlan, OwnDesire.Acting, ActCommand.Own> {
            ActingAtTopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, OwnDesire.Acting desire) {
                super(parent, desire);
            }

            @Override
            DesireNodeNotTopLevel<?, ?> formDesireNode(Agent agent) {
                return new DesireNodeNotTopLevel.WithCommand.ActingAtTopLevelParent(parent, agent.formOwnDesireWithActingCommand(getDesireKey(), parent.getDesireKey()));
            }

            @Override
            public void accept(TreeVisitorInterface treeVisitor) {
                treeVisitor.visitNodeWithActingCommand(this);
            }
        }

        /**
         * Concrete implementation, intention's desire is formed anew
         */
        static class ReasoningNotTopLevelParent extends WithCommand<IntentionNodeNotTopLevel.WithAbstractPlan, OwnDesire.Reasoning, ReasoningCommand> {
            ReasoningNotTopLevelParent(WithAbstractPlan parent, OwnDesire.Reasoning desire) {
                super(parent, desire);
            }

            @Override
            DesireNodeNotTopLevel<?, ?> formDesireNode(Agent agent) {
                return new DesireNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent(parent, agent.formOwnDesireWithReasoningCommand(getDesireKey(), parent.getDesireKey()));
            }

            @Override
            public void accept(TreeVisitorInterface treeVisitor) {
                treeVisitor.visitNodeWithReasoningCommand(this);
            }
        }

        /**
         * Concrete implementation, intention's desire is formed anew
         */
        static class ActingNotTopLevelParent extends WithCommand<IntentionNodeNotTopLevel.WithAbstractPlan, OwnDesire.Acting, ActCommand.Own> {
            ActingNotTopLevelParent(WithAbstractPlan parent, OwnDesire.Acting desire) {
                super(parent, desire);
            }

            @Override
            DesireNodeNotTopLevel<?, ?> formDesireNode(Agent agent) {
                return new DesireNodeNotTopLevel.WithCommand.ActingNotTopLevelParent(parent, agent.formOwnDesireWithActingCommand(getDesireKey(), parent.getDesireKey()));
            }

            @Override
            public void accept(TreeVisitorInterface treeVisitor) {
                treeVisitor.visitNodeWithActingCommand(this);
            }
        }

    }

    /**
     * Class to extend template - to define intention node with childes
     */
    public abstract static class WithAbstractPlan<V extends AbstractIntention<? extends InternalDesire<?>>, T extends InternalDesire<V>, K extends Node & IntentionNodeWithChildes & Parent> extends IntentionNodeNotTopLevel<V, T, K> implements IntentionNodeWithChildes, Parent<DesireNodeNotTopLevel<?, ?>, IntentionNodeNotTopLevel<?, ?, ?>> {
        private final Map<Intention<?>, IntentionNodeNotTopLevel<?, ?, ?>> intentions = new HashMap<>();
        final Map<InternalDesire<?>, DesireNodeNotTopLevel<?, ?>> desires = new HashMap<>();

        private WithAbstractPlan(K parent, T desire) {
            super(parent, desire);
            intention.returnPlanAsSetOfDesiresForOthers().stream()
                    .map(key -> getAgent().formDesireForOthers(key, intention.getDesireKey()))
                    .forEach(desireForOthers -> desires.put(desireForOthers, new DesireNodeNotTopLevel.ForOthers.NotTopLevelParent(this, desireForOthers)));
            intention.returnPlanAsSetOfDesiresWithIntentionToAct().stream()
                    .map(key -> getAgent().formOwnDesireWithActingCommand(key, intention.getDesireKey()))
                    .forEach(acting -> desires.put(acting, new DesireNodeNotTopLevel.WithCommand.ActingNotTopLevelParent(this, acting)));
            intention.returnPlanAsSetOfDesiresWithIntentionToReason().stream()
                    .map(key -> getAgent().formOwnDesireWithReasoningCommand(key, intention.getDesireKey()))
                    .forEach(reasoning -> desires.put(reasoning, new DesireNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent(this, reasoning)));
            intention.returnPlanAsSetOfDesiresWithAbstractIntention().stream()
                    .map(key -> getAgent().formOwnDesireWithAbstractIntention(key, intention.getDesireKey()))
                    .forEach(withAbstractIntention -> desires.put(withAbstractIntention, new DesireNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent(this, withAbstractIntention)));
        }

        @Override
        public void addSharedDesireForOtherAgents(SharedDesireInRegister sharedDesire) {
            parent.addSharedDesireForOtherAgents(sharedDesire);
        }

        @Override
        public void removeSharedDesireForOtherAgents(SharedDesireInRegister sharedDesire) {
            parent.removeSharedDesireForOtherAgents(sharedDesire);
        }

        @Override
        public void collectSharedDesiresForOtherAgentsInSubtree(Set<SharedDesire> sharedDesiresInSubtree) {
            intentions.values().forEach(node -> node.collectSharedDesiresForOtherAgentsInSubtree(sharedDesiresInSubtree));
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
