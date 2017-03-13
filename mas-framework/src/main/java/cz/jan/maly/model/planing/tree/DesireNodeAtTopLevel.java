package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.*;
import cz.jan.maly.utils.MyLogger;

import java.util.Optional;

/**
 * Template for desire in top level
 * Created by Jan on 28-Feb-17.
 */
public abstract class DesireNodeAtTopLevel<T extends InternalDesire<? extends Intention>> extends Node.TopLevel implements DesireNodeInterface<IntentionNodeAtTopLevel<?, ?>> {
    final T desire;

    private DesireNodeAtTopLevel(Tree tree, T desire) {
        super(tree, desire.getDesireParameters());
        this.desire = desire;
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
    static class ForOthers extends DesireNodeAtTopLevel<DesireForOthers> {
        private final SharingDesireRoutine sharingDesireRoutine = new SharingDesireRoutine();

        ForOthers(Tree tree, DesireForOthers desire) {
            super(tree, desire);
        }

        @Override
        public Optional<IntentionNodeAtTopLevel<?, ?>> makeCommitment(DataForDecision dataForDecision) {
            if (desire.shouldCommit(dataForDecision)) {
                IntentionNodeAtTopLevel.WithDesireForOthers node = new IntentionNodeAtTopLevel.WithDesireForOthers(parent, desire);
                SharedDesireInRegister sharedDesire = node.intention.makeDesireToShare();
                if (sharingDesireRoutine.sharedDesire(sharedDesire)) {
                    tree.addSharedDesireForOtherAgents(node.intention.getSharedDesire());
                    parent.replaceDesireByIntention(this, node);
                    return Optional.of(node);
                }
            }
            return Optional.empty();
        }
    }

    /**
     * Template for nodes with desires from another agent
     *
     * @param <V>
     */
    abstract static class FromAnotherAgent<V extends DesireFromAnotherAgent<? extends Intention>> extends DesireNodeAtTopLevel<V> implements ResponseReceiverInterface<Optional<SharedDesireForAgents>> {
        private final Object lockMonitor = new Object();

        FromAnotherAgent(Tree tree, V desire) {
            super(tree, desire);
        }

        abstract IntentionNodeAtTopLevel<?, ?> formIntentionNode();

        @Override
        public Optional<IntentionNodeAtTopLevel<?, ?>> makeCommitment(DataForDecision dataForDecision) {
            if (desire.shouldCommit(dataForDecision)) {

                if (Agent.DESIRE_MEDIATOR.addCommitmentToDesire(parent.getAgent(), desire.getDesireForAgents(), this)) {

                    //wait for registered
                    synchronized (lockMonitor) {
                        try {
                            lockMonitor.wait();
                        } catch (InterruptedException e) {
                            MyLogger.getLogger().warning(this.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
                        }
                    }

                    //if agent is committed according to system...
                    if (desire.getDesireForAgents().isAgentCommittedToDesire(parent.getAgent())) {
                        IntentionNodeAtTopLevel<?, ?> node = formIntentionNode();
                        parent.replaceDesireByIntention(this, node);
                        tree.addSharedDesireFromAnotherAgents(desire.getDesireForAgents());
                        return Optional.of(node);
                    }
                }
            }
            return Optional.empty();
        }

        @Override
        public void receiveResponse(Optional<SharedDesireForAgents> response) {

            //update desire if registered is nonempty and notify waiting method to decide commitment
            synchronized (lockMonitor) {
                response.ifPresent(sharedDesireForAgents -> desire.getDesireForAgents().updateCommittedAgentsSet(sharedDesireForAgents));
                lockMonitor.notify();
            }
        }

        /**
         * Concrete implementation, desire forms intention with abstract plan
         */
        static class WithAbstractIntention extends FromAnotherAgent<DesireFromAnotherAgent.WithAbstractIntention> {
            WithAbstractIntention(Tree tree, DesireFromAnotherAgent.WithAbstractIntention desire) {
                super(tree, desire);
            }

            @Override
            IntentionNodeAtTopLevel<?, ?> formIntentionNode() {
                return new IntentionNodeAtTopLevel.WithAbstractPlan.FromAnotherAgent(parent, desire);
            }
        }

        /**
         * Concrete implementation, desire forms intention with concrete plan (command)
         */
        static class WithIntentionWithPlan extends FromAnotherAgent<DesireFromAnotherAgent.WithIntentionWithPlan> {
            WithIntentionWithPlan(Tree tree, DesireFromAnotherAgent.WithIntentionWithPlan desire) {
                super(tree, desire);
            }

            @Override
            IntentionNodeAtTopLevel<?, ?> formIntentionNode() {
                return new IntentionNodeAtTopLevel.WithCommand.FromAnotherAgent(parent, desire);
            }
        }

    }

    /**
     * Template for nodes with own desires
     *
     * @param <V>
     */
    abstract static class Own<V extends OwnDesire<? extends Intention>> extends DesireNodeAtTopLevel<V> {
        Own(Tree tree, V desire) {
            super(tree, desire);
        }

        @Override
        public Optional<IntentionNodeAtTopLevel<?, ?>> makeCommitment(DataForDecision dataForDecision) {
            if (desire.shouldCommit(dataForDecision)) {
                IntentionNodeAtTopLevel<?, ?> node = formIntentionNode();
                parent.replaceDesireByIntention(this, node);
                return Optional.of(node);
            }
            return Optional.empty();
        }

        abstract IntentionNodeAtTopLevel<?, ?> formIntentionNode();

        /**
         * Concrete implementation, desire forms intention with abstract plan
         */
        static class WithAbstractIntention extends Own<OwnDesire.WithAbstractIntention> {
            WithAbstractIntention(Tree tree, OwnDesire.WithAbstractIntention desire) {
                super(tree, desire);
            }

            @Override
            IntentionNodeAtTopLevel<?, ?> formIntentionNode() {
                return new IntentionNodeAtTopLevel.WithAbstractPlan.Own(parent, desire);
            }
        }

        /**
         * Concrete implementation, desire forms intention with acting command
         */
        static class WithActingCommand extends Own<OwnDesire.Acting> {
            WithActingCommand(Tree tree, OwnDesire.Acting desire) {
                super(tree, desire);
            }

            @Override
            IntentionNodeAtTopLevel<?, ?> formIntentionNode() {
                return new IntentionNodeAtTopLevel.WithCommand.OwnActing(parent, desire);
            }
        }

        /**
         * Concrete implementation, desire forms intention with reasoning command
         */
        static class WithReasoningCommand extends Own<OwnDesire.Reasoning> {
            WithReasoningCommand(Tree tree, OwnDesire.Reasoning desire) {
                super(tree, desire);
            }

            @Override
            IntentionNodeAtTopLevel<?, ?> formIntentionNode() {
                return new IntentionNodeAtTopLevel.WithCommand.OwnReasoning(parent, desire);
            }
        }

    }

}
