package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.planing.*;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.CommandForIntention;
import cz.jan.maly.model.planing.command.ReasoningCommand;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Template for intention not in top level
 * Created by Jan on 28-Feb-17.
 */
public abstract class IntentionNodeNotTopLevel<V extends Intention<? extends InternalDesire<?>>, T extends InternalDesire<? extends V>, K extends Node & IntentionNodeWithChildes & Parent<?, ?>> extends Node.NotTopLevel<K> implements IntentionNodeInterface, VisitorAcceptor {
    final V intention;

    private IntentionNodeNotTopLevel(K parent, T desire) {
        super(parent, desire.getDesireParameters());
        this.intention = desire.formIntention(tree.getAgent());
    }

    @Override
    public Set<DesireKey> getParametersToLoad() {
        return intention.getParametersToLoad();
    }

    abstract void replaceIntentionByDesireInParent();

    /**
     * Implementation of top node with desire for other agents
     */
    public static abstract class WithDesireForOthers<K extends Node & IntentionNodeWithChildes & Parent<?, ?>> extends IntentionNodeNotTopLevel<IntentionWithDesireForOtherAgents, DesireForOthers, K> {
        private final SharingDesireRemovalRoutine sharingDesireRemovalRoutine = new SharingDesireRemovalRoutine();

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
        public boolean removeCommitment(DataForDecision dataForDecision) {
            if (intention.shouldRemoveCommitment(dataForDecision)) {

                //share desire and wait for response of registration
                if (sharingDesireRemovalRoutine.unregisterSharedDesire(intention.getSharedDesire(), tree)) {
                    replaceIntentionByDesireInParent();
                    return true;
                }
            }
            return false;
        }

        @Override
        public void accept(TreeVisitorInterface treeVisitor) {
            treeVisitor.visit(this);
        }

        /**
         * Parent is in top level
         */
        static class TopLevelParent extends WithDesireForOthers<IntentionNodeAtTopLevel.WithAbstractPlan<?, ?>> {
            TopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, DesireForOthers desire) {
                super(parent, desire);
            }

            @Override
            public void collectSharedDesiresForOtherAgentsInSubtree(Set<SharedDesireForAgents> sharedDesiresInSubtree) {
                sharedDesiresInSubtree.add(intention.getSharedDesire());
            }

            @Override
            void replaceIntentionByDesireInParent() {
                parent.replaceIntentionByDesireWithDesireForOthers(this, new DesireNodeNotTopLevel.ForOthers.TopLevelParent(parent, tree.getAgent().formDesireForOthers(getDesireKey(), parent.getDesireKey(), parent.desireParameters)));
            }
        }

        /**
         * Parent is not in top level
         */
        static class NotTopLevelParent extends WithDesireForOthers<WithAbstractPlan<?, ?, ?>> {
            NotTopLevelParent(WithAbstractPlan parent, DesireForOthers desire) {
                super(parent, desire);
            }

            @Override
            public void collectSharedDesiresForOtherAgentsInSubtree(Set<SharedDesireForAgents> sharedDesiresInSubtree) {
                sharedDesiresInSubtree.add(intention.getSharedDesire());
            }

            @Override
            void replaceIntentionByDesireInParent() {
                parent.replaceIntentionByDesireWithDesireForOthers(this, new DesireNodeNotTopLevel.ForOthers.NotTopLevelParent(parent, tree.getAgent().formDesireForOthers(getDesireKey(), parent.getDesireKey(), parent.desireParameters)));
            }
        }

    }

    /**
     * Class to extend template - to define intention node without child
     */
    public abstract static class WithCommand<K extends Node & IntentionNodeWithChildes & Parent<?, ?>, V extends InternalDesire<? extends IntentionCommand<V, T>>, T extends CommandForIntention<? extends IntentionCommand<V, T>>> extends IntentionNodeNotTopLevel<IntentionCommand<V, T>, V, K> implements NodeWithCommand {
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
        public void collectSharedDesiresForOtherAgentsInSubtree(Set<SharedDesireForAgents> sharedDesiresInSubtree) {
            //skip
        }

        @Override
        public boolean removeCommitment(DataForDecision dataForDecision) {
            if (intention.shouldRemoveCommitment(dataForDecision)) {
                replaceIntentionByDesireInParent();
                return true;
            }
            return false;
        }

        /**
         * Concrete implementation, intention's desire is formed anew
         */
        static class ReasoningAtTopLevelParent extends WithCommand<IntentionNodeAtTopLevel.WithAbstractPlan<?, ?>, OwnDesire.Reasoning, ReasoningCommand> {
            ReasoningAtTopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, OwnDesire.Reasoning desire) {
                super(parent, desire);
            }

            @Override
            public void accept(TreeVisitorInterface treeVisitor) {
                treeVisitor.visitNodeWithReasoningCommand(this);
            }

            @Override
            void replaceIntentionByDesireInParent() {
                parent.replaceIntentionByDesireReasoning(this, new DesireNodeNotTopLevel.WithCommand.ReasoningAtTopLevelParent(parent, tree.getAgent().formOwnDesireWithReasoningCommand(getDesireKey(), parent.getDesireKey(), parent.desireParameters)));
            }
        }

        /**
         * Concrete implementation, intention's desire is formed anew
         */
        static class ActingAtTopLevelParent extends WithCommand<IntentionNodeAtTopLevel.WithAbstractPlan<?, ?>, OwnDesire.Acting, ActCommand.Own> {
            ActingAtTopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, OwnDesire.Acting desire) {
                super(parent, desire);
            }

            @Override
            public void accept(TreeVisitorInterface treeVisitor) {
                treeVisitor.visitNodeWithActingCommand(this);
            }

            @Override
            void replaceIntentionByDesireInParent() {
                parent.replaceIntentionByDesireActing(this, new DesireNodeNotTopLevel.WithCommand.ActingAtTopLevelParent(parent, tree.getAgent().formOwnDesireWithActingCommand(getDesireKey(), parent.getDesireKey(), parent.desireParameters)));
            }
        }

        /**
         * Concrete implementation, intention's desire is formed anew
         */
        static class ReasoningNotTopLevelParent extends WithCommand<IntentionNodeNotTopLevel.WithAbstractPlan<?, ?, ?>, OwnDesire.Reasoning, ReasoningCommand> {
            ReasoningNotTopLevelParent(WithAbstractPlan parent, OwnDesire.Reasoning desire) {
                super(parent, desire);
            }

            @Override
            void replaceIntentionByDesireInParent() {
                parent.replaceIntentionByDesireReasoning(this, new DesireNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent(parent, tree.getAgent().formOwnDesireWithReasoningCommand(getDesireKey(), parent.getDesireKey(), parent.desireParameters)));
            }

            @Override
            public void accept(TreeVisitorInterface treeVisitor) {
                treeVisitor.visitNodeWithReasoningCommand(this);
            }
        }

        /**
         * Concrete implementation, intention's desire is formed anew
         */
        static class ActingNotTopLevelParent extends WithCommand<IntentionNodeNotTopLevel.WithAbstractPlan<?, ?, ?>, OwnDesire.Acting, ActCommand.Own> {
            ActingNotTopLevelParent(WithAbstractPlan parent, OwnDesire.Acting desire) {
                super(parent, desire);
            }

            @Override
            void replaceIntentionByDesireInParent() {
                parent.replaceIntentionByDesireActing(this, new DesireNodeNotTopLevel.WithCommand.ActingNotTopLevelParent(parent, tree.getAgent().formOwnDesireWithActingCommand(getDesireKey(), parent.getDesireKey(), parent.desireParameters)));
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
    public abstract static class WithAbstractPlan<V extends AbstractIntention<? extends InternalDesire<?>>, T extends InternalDesire<V>,
            K extends Node & IntentionNodeWithChildes & Parent<?, ?>> extends IntentionNodeNotTopLevel<V, T, K> implements
            IntentionNodeWithChildes<IntentionNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent,
                    IntentionNodeNotTopLevel.WithCommand.ActingNotTopLevelParent, IntentionNodeNotTopLevel.WithDesireForOthers.NotTopLevelParent,
                    IntentionNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent, DesireNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent,
                    DesireNodeNotTopLevel.WithCommand.ActingNotTopLevelParent, DesireNodeNotTopLevel.ForOthers.NotTopLevelParent,
                    DesireNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent>, Parent<DesireNodeNotTopLevel<?, ?>, IntentionNodeNotTopLevel<?, ?, ?>> {
        private final SharingDesireRemovalInSubtreeRoutine sharingDesireRemovalInSubtreeRoutine = new SharingDesireRemovalInSubtreeRoutine();

        private WithAbstractPlan(K parent, T desire) {
            super(parent, desire);
        }

        @Override
        public boolean removeCommitment(DataForDecision dataForDecision) {
            if (intention.shouldRemoveCommitment(dataForDecision)) {

                //share desire and wait for response of registration
                Set<SharedDesireForAgents> sharedDesires = new HashSet<>();
                collectSharedDesiresForOtherAgentsInSubtree(sharedDesires);
                if (!sharedDesires.isEmpty()) {
                    if (sharingDesireRemovalInSubtreeRoutine.unregisterSharedDesire(sharedDesires, tree)) {
                        replaceIntentionByDesireInParent();
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
            getNodesWithIntention().forEach(node -> node.collectSharedDesiresForOtherAgentsInSubtree(sharedDesiresInSubtree));
        }

        @Override
        public List<DesireNodeNotTopLevel<?, ?>> getNodesWithDesire() {
            return getDesireUpdater().getNodesWithDesire();
        }

        @Override
        public List<IntentionNodeNotTopLevel<?, ?, ?>> getNodesWithIntention() {
            return getDesireUpdater().getNodesWithIntention();
        }

        @Override
        public Set<DesireParameters> getParametersOfCommittedDesires() {
            return getNodesWithIntention().stream()
                    .map(intentionNode -> intentionNode.desireParameters)
                    .collect(Collectors.toSet());
        }

        @Override
        public Set<DesireParameters> getParametersOfDesires() {
            return getNodesWithDesire().stream()
                    .map(desiresNode -> desiresNode.desireParameters)
                    .collect(Collectors.toSet());
        }

        @Override
        public void collectKeysOfCommittedDesiresInSubtree(List<DesireKey> list) {
            list.add(intention.getDesireKey());
            getNodesWithIntention().forEach(intentionNode -> intentionNode.collectKeysOfCommittedDesiresInSubtree(list));
        }

        @Override
        public void collectKeysOfDesiresInSubtree(List<DesireKey> list) {
            getNodesWithDesire().forEach(desireNode -> list.add(desireNode.getDesireKey()));
            getNodesWithIntention().forEach(intentionNode -> intentionNode.collectKeysOfDesiresInSubtree(list));
        }

        @Override
        public void accept(TreeVisitorInterface treeVisitor) {
            treeVisitor.visit(this);
        }

        /**
         * Parent is in top level
         */
        static class TopLevelParent extends WithAbstractPlan<AbstractIntention<OwnDesire.WithAbstractIntention>, OwnDesire.WithAbstractIntention, IntentionNodeAtTopLevel.WithAbstractPlan<?, ?>> {
            private final DesireUpdater<IntentionNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent,
                    IntentionNodeNotTopLevel.WithCommand.ActingNotTopLevelParent, IntentionNodeNotTopLevel.WithDesireForOthers.NotTopLevelParent,
                    IntentionNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent, DesireNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent,
                    DesireNodeNotTopLevel.WithCommand.ActingNotTopLevelParent, DesireNodeNotTopLevel.ForOthers.NotTopLevelParent,
                    DesireNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent, TopLevelParent> desireUpdater = new DesireUpdater<WithCommand.ReasoningNotTopLevelParent,
                    WithCommand.ActingNotTopLevelParent, WithDesireForOthers.NotTopLevelParent, NotTopLevelParent,
                    DesireNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent, DesireNodeNotTopLevel.WithCommand.ActingNotTopLevelParent,
                    DesireNodeNotTopLevel.ForOthers.NotTopLevelParent, DesireNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent, TopLevelParent>() {
                @Override
                protected void updateDesires(Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention,
                                             Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason, TopLevelParent forNode) {
                    desiresForOthers.stream()
                            .map(key -> tree.getAgent().formDesireForOthers(key, intention.getDesireKey(), intention.getParametersOfDesire()))
                            .forEach(desireForOthers -> this.addDesireWithDesireForOthers(new DesireNodeNotTopLevel.ForOthers.NotTopLevelParent(forNode, desireForOthers)));
                    desiresWithIntentionToAct.stream()
                            .map(key -> tree.getAgent().formOwnDesireWithActingCommand(key, intention.getDesireKey(), intention.getParametersOfDesire()))
                            .forEach(acting -> this.addDesireActing(new DesireNodeNotTopLevel.WithCommand.ActingNotTopLevelParent(forNode, acting)));
                    desiresWithIntentionToReason.stream()
                            .map(key -> tree.getAgent().formOwnDesireWithReasoningCommand(key, intention.getDesireKey(), intention.getParametersOfDesire()))
                            .forEach(reasoning -> this.addDesireReasoning(new DesireNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent(forNode, reasoning)));
                    desiresWithAbstractIntention.stream()
                            .map(key -> tree.getAgent().formOwnDesireWithAbstractIntention(key, intention.getDesireKey(), intention.getParametersOfDesire()))
                            .forEach(withAbstractIntention -> this.addDesireWithAbstractPlan(new DesireNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent(forNode, withAbstractIntention)));
                }
            };

            TopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, OwnDesire.WithAbstractIntention desire) {
                super(parent, desire);
                desireUpdater.initDesires(intention, this);
            }

            @Override
            public Optional<DesireKey> getDesireKeyAssociatedWithParent() {
                return Optional.of(getDesireKey());
            }

            @Override
            public void updateDesires() {
                desireUpdater.updateDesires(this);
            }

            @Override
            public DesireUpdater<WithCommand.ReasoningNotTopLevelParent, WithCommand.ActingNotTopLevelParent, WithDesireForOthers.NotTopLevelParent, NotTopLevelParent, DesireNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent, DesireNodeNotTopLevel.WithCommand.ActingNotTopLevelParent, DesireNodeNotTopLevel.ForOthers.NotTopLevelParent, DesireNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent, ?> getDesireUpdater() {
                return desireUpdater;
            }

            @Override
            void replaceIntentionByDesireInParent() {
                parent.replaceIntentionByDesireWithAbstractPlan(this, new DesireNodeNotTopLevel.WithAbstractPlan.TopLevelParent(parent, tree.getAgent().formOwnDesireWithAbstractIntention(getDesireKey(), parent.getDesireKey(), parent.desireParameters)));
            }
        }

        /**
         * Parent is in top level
         */
        static class NotTopLevelParent extends WithAbstractPlan<AbstractIntention<OwnDesire.WithAbstractIntention>, OwnDesire.WithAbstractIntention, IntentionNodeNotTopLevel.WithAbstractPlan<?, ?, ?>> {
            private final DesireUpdater<IntentionNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent,
                    IntentionNodeNotTopLevel.WithCommand.ActingNotTopLevelParent, IntentionNodeNotTopLevel.WithDesireForOthers.NotTopLevelParent,
                    IntentionNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent, DesireNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent,
                    DesireNodeNotTopLevel.WithCommand.ActingNotTopLevelParent, DesireNodeNotTopLevel.ForOthers.NotTopLevelParent,
                    DesireNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent, NotTopLevelParent> desireUpdater = new DesireUpdater<WithCommand.ReasoningNotTopLevelParent,
                    WithCommand.ActingNotTopLevelParent, WithDesireForOthers.NotTopLevelParent, NotTopLevelParent,
                    DesireNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent, DesireNodeNotTopLevel.WithCommand.ActingNotTopLevelParent,
                    DesireNodeNotTopLevel.ForOthers.NotTopLevelParent, DesireNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent, NotTopLevelParent>() {
                @Override
                protected void updateDesires(Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention,
                                             Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason, NotTopLevelParent forNode) {
                    desiresForOthers.stream()
                            .map(key -> tree.getAgent().formDesireForOthers(key, intention.getDesireKey(), intention.getParametersOfDesire()))
                            .forEach(desireForOthers -> this.addDesireWithDesireForOthers(new DesireNodeNotTopLevel.ForOthers.NotTopLevelParent(forNode, desireForOthers)));
                    desiresWithIntentionToAct.stream()
                            .map(key -> tree.getAgent().formOwnDesireWithActingCommand(key, intention.getDesireKey(), intention.getParametersOfDesire()))
                            .forEach(acting -> this.addDesireActing(new DesireNodeNotTopLevel.WithCommand.ActingNotTopLevelParent(forNode, acting)));
                    desiresWithIntentionToReason.stream()
                            .map(key -> tree.getAgent().formOwnDesireWithReasoningCommand(key, intention.getDesireKey(), intention.getParametersOfDesire()))
                            .forEach(reasoning -> this.addDesireReasoning(new DesireNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent(forNode, reasoning)));
                    desiresWithAbstractIntention.stream()
                            .map(key -> tree.getAgent().formOwnDesireWithAbstractIntention(key, intention.getDesireKey(), intention.getParametersOfDesire()))
                            .forEach(withAbstractIntention -> this.addDesireWithAbstractPlan(new DesireNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent(forNode, withAbstractIntention)));
                }
            };

            NotTopLevelParent(IntentionNodeNotTopLevel.WithAbstractPlan parent, OwnDesire.WithAbstractIntention desire) {
                super(parent, desire);
                desireUpdater.initDesires(intention, this);
            }

            @Override
            public void updateDesires() {
                desireUpdater.updateDesires(this);
            }

            @Override
            public Optional<DesireKey> getDesireKeyAssociatedWithParent() {
                return Optional.of(getDesireKey());
            }

            @Override
            public DesireUpdater<WithCommand.ReasoningNotTopLevelParent, WithCommand.ActingNotTopLevelParent, WithDesireForOthers.NotTopLevelParent, NotTopLevelParent, DesireNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent, DesireNodeNotTopLevel.WithCommand.ActingNotTopLevelParent, DesireNodeNotTopLevel.ForOthers.NotTopLevelParent, DesireNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent, ?> getDesireUpdater() {
                return desireUpdater;
            }

            @Override
            void replaceIntentionByDesireInParent() {
                parent.replaceIntentionByDesireWithAbstractPlan(this, new DesireNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent(parent, tree.getAgent().formOwnDesireWithAbstractIntention(getDesireKey(), parent.getDesireKey(), parent.desireParameters)));
            }
        }

    }

}
