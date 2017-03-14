package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.*;
import cz.jan.maly.model.planing.command.ActCommandForIntention;
import cz.jan.maly.model.planing.command.ReasoningCommandForIntention;

import java.util.Optional;

/**
 * Template for desire not in top level
 * Created by Jan on 28-Feb-17.
 */
public abstract class DesireNodeNotTopLevel<T extends InternalDesire<? extends Intention>, K extends Node & IntentionNodeWithChildes & Parent> extends Node.NotTopLevel<K> implements DesireNodeInterface<IntentionNodeNotTopLevel<?, ?, ?>> {
    final T desire;

    private DesireNodeNotTopLevel(K parent, T desire) {
        super(parent, desire.getDesireParameters());
        this.desire = desire;
    }

    abstract IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode();

    public Optional<IntentionNodeNotTopLevel<?, ?, ?>> makeCommitment(DataForDecision dataForDecision) {
        if (desire.shouldCommit(dataForDecision)) {
            IntentionNodeNotTopLevel<?, ?, ?> node = formIntentionNode();
            parent.replaceDesireByIntention(this, node);
            return Optional.of(node);
        }
        return Optional.empty();
    }

    @Override
    public DesireKey getAssociatedDesireKey() {
        return getDesireKey();
    }

    @Override
    public DecisionParameters getParametersToLoad() {
        return desire.getParametersToLoad();
    }

    /**
     * Implementation of top node with desire for other agents
     */
    static abstract class ForOthers<K extends Node & IntentionNodeWithChildes & Parent> extends DesireNodeNotTopLevel<DesireForOthers, K> {
        private final SharingDesireRoutine sharingDesireRoutine = new SharingDesireRoutine();

        private ForOthers(K parent, DesireForOthers desire) {
            super(parent, desire);
        }

        protected abstract IntentionNodeNotTopLevel.WithDesireForOthers<?> instantiate();

        @Override
        IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode() {
            return instantiate();
        }

        @Override
        public Optional<IntentionNodeNotTopLevel<?, ?, ?>> makeCommitment(DataForDecision dataForDecision) {
            if (desire.shouldCommit(dataForDecision)) {
                IntentionNodeNotTopLevel.WithDesireForOthers<?> node = instantiate();
                SharedDesireInRegister sharedDesire = node.intention.makeDesireToShare();
                if (sharingDesireRoutine.sharedDesire(sharedDesire, tree)) {
                    tree.addSharedDesireForOtherAgents(node.intention.getSharedDesire());
                    parent.replaceDesireByIntention(this, node);
                    return Optional.of(node);
                }
            }
            return Optional.empty();
        }

        /**
         * Parent is in top level
         */
        static class TopLevelParent extends ForOthers<IntentionNodeAtTopLevel.WithAbstractPlan> {
            TopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, DesireForOthers desire) {
                super(parent, desire);
            }

            @Override
            protected IntentionNodeNotTopLevel.WithDesireForOthers<?> instantiate() {
                return new IntentionNodeNotTopLevel.WithDesireForOthers.TopLevelParent(parent, desire);
            }
        }

        /**
         * Parent is not in top level
         */
        static class NotTopLevelParent extends ForOthers<IntentionNodeNotTopLevel.WithAbstractPlan> {
            NotTopLevelParent(IntentionNodeNotTopLevel.WithAbstractPlan parent, DesireForOthers desire) {
                super(parent, desire);
            }

            @Override
            protected IntentionNodeNotTopLevel.WithDesireForOthers<?> instantiate() {
                return new IntentionNodeNotTopLevel.WithDesireForOthers.NotTopLevelParent(parent, desire);
            }
        }

    }

    /**
     * Implementation of top node with desire for other agents
     */
    static abstract class WithAbstractPlan<T extends InternalDesire<? extends Intention>, K extends Node & IntentionNodeWithChildes & Parent> extends DesireNodeNotTopLevel<T, K> {
        private WithAbstractPlan(K parent, T desire) {
            super(parent, desire);
        }

        /**
         * Parent is in top level
         */
        static class TopLevelParent extends WithAbstractPlan<OwnDesire.WithAbstractIntention, IntentionNodeAtTopLevel.WithAbstractPlan> {
            TopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, OwnDesire.WithAbstractIntention desire) {
                super(parent, desire);
            }

            @Override
            IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode() {
                return new IntentionNodeNotTopLevel.WithAbstractPlan.TopLevelParent(parent, desire);
            }
        }

        /**
         * Parent is in top level
         */
        static class NotTopLevelParent extends WithAbstractPlan<OwnDesire.WithAbstractIntention, IntentionNodeNotTopLevel.WithAbstractPlan> {
            NotTopLevelParent(IntentionNodeNotTopLevel.WithAbstractPlan parent, OwnDesire.WithAbstractIntention desire) {
                super(parent, desire);
            }

            @Override
            IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode() {
                return new IntentionNodeNotTopLevel.WithAbstractPlan.NotTopLevelParent(parent, desire);
            }
        }

    }

    /**
     * Class to extend template - to define desire node without child
     */
    abstract static class WithCommand<K extends Node & IntentionNodeWithChildes & Parent, V extends InternalDesire<? extends IntentionCommand<V, T>>, T extends CommandForIntention<? extends IntentionCommand<V, T>, ?>> extends WithAbstractPlan<V, K> {
        private WithCommand(K parent, V desire) {
            super(parent, desire);
        }

        /**
         * Concrete implementation, intention's desire is formed anew with acting command
         */
        static class ActingAtTopLevelParent extends WithCommand<IntentionNodeAtTopLevel.WithAbstractPlan, OwnDesire.Acting, ActCommandForIntention.Own> {
            ActingAtTopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, OwnDesire.Acting desire) {
                super(parent, desire);
            }

            @Override
            IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode() {
                return new IntentionNodeNotTopLevel.WithCommand.ActingAtTopLevelParent(parent, desire);
            }
        }

        /**
         * Concrete implementation, intention's desire is formed anew with acting command
         */
        static class ReasoningAtTopLevelParent extends WithCommand<IntentionNodeAtTopLevel.WithAbstractPlan, OwnDesire.Reasoning, ReasoningCommandForIntention> {
            ReasoningAtTopLevelParent(IntentionNodeAtTopLevel.WithAbstractPlan parent, OwnDesire.Reasoning desire) {
                super(parent, desire);
            }

            @Override
            IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode() {
                return new IntentionNodeNotTopLevel.WithCommand.ReasoningAtTopLevelParent(parent, desire);
            }
        }

        /**
         * Concrete implementation, intention's desire is formed anew with acting command
         */
        static class ActingNotTopLevelParent extends WithCommand<IntentionNodeNotTopLevel.WithAbstractPlan, OwnDesire.Acting, ActCommandForIntention.Own> {
            ActingNotTopLevelParent(IntentionNodeNotTopLevel.WithAbstractPlan parent, OwnDesire.Acting desire) {
                super(parent, desire);
            }

            @Override
            IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode() {
                return new IntentionNodeNotTopLevel.WithCommand.ActingNotTopLevelParent(parent, desire);
            }
        }

        /**
         * Concrete implementation, intention's desire is formed anew with reasoning command
         */
        static class ReasoningNotTopLevelParent extends WithCommand<IntentionNodeNotTopLevel.WithAbstractPlan, OwnDesire.Reasoning, ReasoningCommandForIntention> {
            ReasoningNotTopLevelParent(IntentionNodeNotTopLevel.WithAbstractPlan parent, OwnDesire.Reasoning desire) {
                super(parent, desire);
            }

            @Override
            IntentionNodeNotTopLevel<?, ?, ?> formIntentionNode() {
                return new IntentionNodeNotTopLevel.WithCommand.ReasoningNotTopLevelParent(parent, desire);
            }
        }

    }

}
