package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.planing.*;
import cz.jan.maly.model.planing.command.ActCommandForIntention;
import cz.jan.maly.model.planing.command.ReasoningCommandForIntention;
import cz.jan.maly.utils.MyLogger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Template for intention in top level
 * Created by Jan on 28-Feb-17.
 */
public abstract class IntentionNodeAtTopLevel<V extends Intention<? extends InternalDesire<?>>, T extends InternalDesire<V>> extends Node.TopLevel implements IntentionNodeInterface, VisitorAcceptor {
    final V intention;

    private IntentionNodeAtTopLevel(Tree tree, T desire) {
        super(tree, desire.getDesireParameters());
        this.intention = desire.formIntention(tree.getAgent());
    }

    abstract void formDesireNodeAndReplaceIntentionNode(Agent agent);

    @Override
    public DecisionParameters getParametersToLoad() {
        return intention.getParametersToLoad();
    }

    /**
     * Class to extend template - to define intention node without child
     */
    public abstract static class WithCommand<V extends IntentionCommand<? extends InternalDesire<? extends IntentionCommand<?, ?>>, ?>, K extends CommandForIntention<? extends IntentionCommand<T, K>, ?>, T extends InternalDesire<V>> extends IntentionNodeAtTopLevel<V, T> implements NodeWithCommand<K> {
        private WithCommand(Tree tree, T desire) {
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
        public boolean removeCommitment(DataForDecision dataForDecision) {
            if (intention.shouldRemoveCommitment(dataForDecision)) {
                formDesireNodeAndReplaceIntentionNode(tree.getAgent());
                return true;
            }
            return false;
        }

        /**
         * Concrete implementation, intention's desire from another agent forms node
         */
        public static class FromAnotherAgent extends WithCommand<IntentionCommand.FromAnotherAgent, ActCommandForIntention.DesiredByAnotherAgent, DesireFromAnotherAgent.WithIntentionWithPlan> implements ResponseReceiverInterface<Boolean> {
            private final DesireFromAnotherAgent.WithIntentionWithPlan desire;
            private final Object lockMonitor = new Object();
            private Boolean registered = false;

            FromAnotherAgent(Tree tree, DesireFromAnotherAgent.WithIntentionWithPlan desire) {
                super(tree, desire);
                this.desire = desire;
            }

            @Override
            public boolean removeCommitment(DataForDecision dataForDecision) {
                if (intention.shouldRemoveCommitment(dataForDecision)) {

                    //share desire and wait for response of registration
                    SharedDesireForAgents sharedDesire = intention.getSharedDesireForAgents();
                    if (tree.getAgent().getDesireMediator().removeCommitmentToDesire(tree.getAgent(), sharedDesire, this)) {
                        synchronized (lockMonitor) {
                            try {
                                lockMonitor.wait();
                            } catch (InterruptedException e) {
                                MyLogger.getLogger().warning(this.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
                            }
                        }

                        //is desire register, if so, make intention out of it
                        if (registered) {
                            formDesireNodeAndReplaceIntentionNode(tree.getAgent());
                            return true;
                        } else {
                            MyLogger.getLogger().warning(this.getClass().getSimpleName() + ": desire for others was not registered.");
                        }
                    }
                }
                return false;
            }

            @Override
            void formDesireNodeAndReplaceIntentionNode(Agent agent) {
                parent.replaceIntentionByDesire(this, new DesireNodeAtTopLevel.FromAnotherAgent.WithIntentionWithPlan(parent, (DesireFromAnotherAgent.WithIntentionWithPlan) agent.formDesireFromOtherAgentWithIntentionWithPlan(desire.getDesireForAgents()).get()));
            }

            @Override
            public ActCommandForIntention.DesiredByAnotherAgent getCommand() {
                return intention.getCommand();
            }

            @Override
            public void collectSharedDesiresForOtherAgentsInSubtree(Set<SharedDesireForAgents> sharedDesiresInSubtree) {
                //skip
            }

            @Override
            public void accept(TreeVisitorInterface treeVisitor) {
                treeVisitor.visit(this);
            }

            @Override
            public void receiveResponse(Boolean response) {

                //notify waiting method to decide commitment
                synchronized (lockMonitor) {
                    this.registered = response;
                    lockMonitor.notify();
                }
            }
        }

        /**
         * Concrete implementation, intention's desire is formed anew for intention with reasoning command
         */
        public static class OwnReasoning extends WithCommand<IntentionCommand.OwnReasoning, ReasoningCommandForIntention, OwnDesire.Reasoning> {
            OwnReasoning(Tree tree, OwnDesire.Reasoning desire) {
                super(tree, desire);
            }

            @Override
            void formDesireNodeAndReplaceIntentionNode(Agent agent) {
                parent.replaceIntentionByDesire(this, new DesireNodeAtTopLevel.Own.WithReasoningCommand(parent, agent.formOwnDesireWithReasoningCommand(intention.getDesireKey())));
            }

            @Override
            public ReasoningCommandForIntention getCommand() {
                return intention.getCommand();
            }

            @Override
            public void collectSharedDesiresForOtherAgentsInSubtree(Set<SharedDesireForAgents> sharedDesiresInSubtree) {
                //skip
            }

            @Override
            public void accept(TreeVisitorInterface treeVisitor) {
                treeVisitor.visit(this);
            }
        }

        /**
         * Concrete implementation, intention's desire is formed anew for intention with acting command
         */
        public static class OwnActing extends WithCommand<IntentionCommand.OwnActing, ActCommandForIntention.Own, OwnDesire.Acting> {
            OwnActing(Tree tree, OwnDesire.Acting desire) {
                super(tree, desire);
            }

            @Override
            void formDesireNodeAndReplaceIntentionNode(Agent agent) {
                parent.replaceIntentionByDesire(this, new DesireNodeAtTopLevel.Own.WithActingCommand(parent, agent.formOwnDesireWithActingCommand(intention.getDesireKey())));
            }

            @Override
            public ActCommandForIntention.Own getCommand() {
                return intention.getCommand();
            }

            @Override
            public void collectSharedDesiresForOtherAgentsInSubtree(Set<SharedDesireForAgents> sharedDesiresInSubtree) {
                //skip
            }

            @Override
            public void accept(TreeVisitorInterface treeVisitor) {
                treeVisitor.visit(this);
            }
        }
    }

    /**
     * Concrete implementation, intention's desire for other agents is formed anew
     */
    public static class WithDesireForOthers extends IntentionNodeAtTopLevel<IntentionWithDesireForOtherAgents, DesireForOthers> {
        private final SharingDesireRemovalRoutine sharingDesireRemovalRoutine = new SharingDesireRemovalRoutine();

        WithDesireForOthers(Tree tree, DesireForOthers desire) {
            super(tree, desire);
        }

        @Override
        public boolean removeCommitment(DataForDecision dataForDecision) {
            if (intention.shouldRemoveCommitment(dataForDecision)) {

                //share desire and wait for response of registration
                if (sharingDesireRemovalRoutine.unregisterSharedDesire(intention.getSharedDesire(), tree)) {
                    formDesireNodeAndReplaceIntentionNode(tree.getAgent());
                    return true;
                }
            }
            return false;
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
        void formDesireNodeAndReplaceIntentionNode(Agent agent) {
            parent.replaceIntentionByDesire(this, new DesireNodeAtTopLevel.ForOthers(parent, agent.formDesireForOthers(getDesireKey())));
        }

        @Override
        public void accept(TreeVisitorInterface treeVisitor) {
            treeVisitor.visit(this);
        }

        @Override
        public void collectSharedDesiresForOtherAgentsInSubtree(Set<SharedDesireForAgents> sharedDesiresInSubtree) {
            sharedDesiresInSubtree.add(intention.getSharedDesire());
        }
    }

    /**
     * Class to extend template - to define intention node with childes
     */
    public abstract static class WithAbstractPlan<V extends AbstractIntention<? extends InternalDesire<?>>, T extends InternalDesire<V>> extends IntentionNodeAtTopLevel<V, T> implements IntentionNodeWithChildes, Parent<DesireNodeNotTopLevel<?, ?>, IntentionNodeNotTopLevel<?, ?, ?>> {
        private final Map<Intention<?>, IntentionNodeNotTopLevel<?, ?, ?>> intentions = new HashMap<>();
        private final Map<InternalDesire<?>, DesireNodeNotTopLevel<?, ?>> desires = new HashMap<>();
        private final SharingDesireRemovalInSubtreeRoutine sharingDesireRemovalInSubtreeRoutine = new SharingDesireRemovalInSubtreeRoutine();

        private WithAbstractPlan(Tree tree, T desire) {
            super(tree, desire);
            intention.returnPlanAsSetOfDesiresForOthers().stream()
                    .map(key -> tree.getAgent().formDesireForOthers(key, intention.getDesireKey()))
                    .forEach(desireForOthers -> desires.put(desireForOthers, new DesireNodeNotTopLevel.ForOthers.TopLevelParent(this, desireForOthers)));
            intention.returnPlanAsSetOfDesiresWithIntentionToAct().stream()
                    .map(key -> tree.getAgent().formOwnDesireWithActingCommand(key, intention.getDesireKey()))
                    .forEach(acting -> desires.put(acting, new DesireNodeNotTopLevel.WithCommand.ActingAtTopLevelParent(this, acting)));
            intention.returnPlanAsSetOfDesiresWithIntentionToReason().stream()
                    .map(key -> tree.getAgent().formOwnDesireWithReasoningCommand(key, intention.getDesireKey()))
                    .forEach(reasoning -> desires.put(reasoning, new DesireNodeNotTopLevel.WithCommand.ReasoningAtTopLevelParent(this, reasoning)));
            intention.returnPlanAsSetOfDesiresWithAbstractIntention().stream()
                    .map(key -> tree.getAgent().formOwnDesireWithAbstractIntention(key, intention.getDesireKey()))
                    .forEach(withAbstractIntention -> desires.put(withAbstractIntention, new DesireNodeNotTopLevel.WithAbstractPlan.TopLevelParent(this, withAbstractIntention)));
        }

        @Override
        public boolean removeCommitment(DataForDecision dataForDecision) {
            if (intention.shouldRemoveCommitment(dataForDecision)) {

                //share desire and wait for response of registration
                Set<SharedDesireForAgents> sharedDesires = new HashSet<>();
                collectSharedDesiresForOtherAgentsInSubtree(sharedDesires);
                if (!sharedDesires.isEmpty()) {
                    if (sharingDesireRemovalInSubtreeRoutine.unregisterSharedDesire(sharedDesires, tree)) {
                        formDesireNodeAndReplaceIntentionNode(tree.getAgent());
                        return true;
                    }
                } else {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void collectSharedDesiresForOtherAgentsInSubtree(Set<SharedDesireForAgents> sharedDesiresInSubtree) {
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
         * Concrete implementation, abstract intention's desire from another agent forms node
         */
        static class FromAnotherAgent extends WithAbstractPlan<AbstractIntention<DesireFromAnotherAgent.WithAbstractIntention>, DesireFromAnotherAgent.WithAbstractIntention> {
            private final DesireFromAnotherAgent.WithAbstractIntention desire;

            FromAnotherAgent(Tree tree, DesireFromAnotherAgent.WithAbstractIntention desire) {
                super(tree, desire);
                this.desire = desire;
            }

            @Override
            void formDesireNodeAndReplaceIntentionNode(Agent agent) {
                parent.replaceIntentionByDesire(this, new DesireNodeAtTopLevel.FromAnotherAgent.WithAbstractIntention(parent, desire));
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
            void formDesireNodeAndReplaceIntentionNode(Agent agent) {
                parent.replaceIntentionByDesire(this, new DesireNodeAtTopLevel.Own.WithAbstractIntention(parent, agent.formOwnDesireWithAbstractIntention(intention.getDesireKey())));
            }

            @Override
            public Optional<DesireKey> getDesireKeyAssociatedWithParent() {
                return Optional.of(getDesireKey());
            }
        }

    }

}
