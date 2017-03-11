package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.metadata.DecisionContainerParameters;
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
    public DecisionContainerParameters getParametersToLoad() {
        return desire.getParametersToLoad();
    }

    /**
     * Implementation of top node with desire for other agents
     */
    static class ForOthers extends DesireNodeAtTopLevel<DesireForOthers> implements ResponseReceiverInterface<Boolean> {
        private final Object lockMonitor = new Object();
        private Boolean registered = false;

        ForOthers(Tree tree, DesireForOthers desire) {
            super(tree, desire);
        }

        @Override
        public Optional<IntentionNodeAtTopLevel<?, ?>> makeCommitment(DataForDecision dataForDecision) {
            if (desire.shouldCommit(dataForDecision)) {
                IntentionNodeAtTopLevel.WithDesireForOthers node = new IntentionNodeAtTopLevel.WithDesireForOthers(parent, desire);

                //share desire and wait for response of registration
                SharedDesireInRegister sharedDesire = node.intention.makeDesireToShare();
                if (Agent.DESIRE_MEDIATOR.registerDesire(sharedDesire, this)) {
                    synchronized (lockMonitor) {
                        try {
                            lockMonitor.wait();
                        } catch (InterruptedException e) {
                            MyLogger.getLogger().warning(this.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
                        }
                    }

                    //is desire register, if so, make intention out of it
                    if (registered) {
                        parent.addSharedDesireForOtherAgents(sharedDesire);
                        parent.replaceDesireByIntention(this, node);
                        return Optional.of(node);
                    }
                }
            }
            return Optional.empty();
        }

        @Override
        public void receiveResponse(Boolean response) {

            //update desire if registered is nonempty and notify waiting method to decide commitment
            synchronized (lockMonitor) {
                this.registered = response;
                lockMonitor.notify();
            }
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

                if (Agent.DESIRE_MEDIATOR.addCommitmentToDesire(getAgent(), desire.getDesireForAgents(), this)) {

                    //wait for registered
                    synchronized (lockMonitor) {
                        try {
                            lockMonitor.wait();
                        } catch (InterruptedException e) {
                            MyLogger.getLogger().warning(this.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
                        }
                    }

                    //if agent is committed according to system...
                    if (desire.getDesireForAgents().isAgentCommittedToDesire(getAgent())) {
                        IntentionNodeAtTopLevel<?, ?> node = formIntentionNode();
                        parent.replaceDesireByIntention(this, node);
                        parent.addCommittedSharedDesireByOtherAgents(desire.getDesireForAgents());
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
                return new IntentionNodeAtTopLevel.WithPlan.FromAnotherAgent(parent, desire);
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
         * Concrete implementation, desire forms intention with concrete plan (command)
         */
        static class WithIntentionWithPlan extends Own<OwnDesire.WithIntentionWithPlan> {
            WithIntentionWithPlan(Tree tree, OwnDesire.WithIntentionWithPlan desire) {
                super(tree, desire);
            }

            @Override
            IntentionNodeAtTopLevel<?, ?> formIntentionNode() {
                return new IntentionNodeAtTopLevel.WithPlan.Own(parent, desire);
            }
        }

    }

}
