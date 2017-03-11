package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.IntentionParameters;

import java.util.Set;

/**
 * Template for agent's own desires
 * Created by Jan on 15-Feb-17.
 */
public abstract class OwnDesire<T extends Intention<? extends OwnDesire<T>>> extends InternalDesire<T> {
    OwnDesire(DesireKey desireKey, Memory memory, Commitment commitment, DecisionParameters decisionDesire, RemoveCommitment removeCommitment, DecisionParameters decisionIntention, IntentionParameters intentionParameters, boolean isAbstract) {
        super(desireKey, memory, commitment, decisionDesire, removeCommitment, decisionIntention, intentionParameters, isAbstract);
    }

    /**
     * Desire to initialize abstract intention
     */
    public static class WithAbstractIntention extends OwnDesire<AbstractIntention<WithAbstractIntention>> {
        private final Set<DesireKey> desiresForOthers;
        private final Set<DesireKey> desiresWithAbstractIntention;
        private final Set<DesireKey> desiresWithIntentionWithPlan;

        public WithAbstractIntention(DesireKey desireKey, Memory memory, Commitment commitment, DecisionParameters decisionDesire, RemoveCommitment removeCommitment, DecisionParameters decisionIntention, IntentionParameters intentionParameters, Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention, Set<DesireKey> desiresWithIntentionWithPlan) {
            super(desireKey, memory, commitment, decisionDesire, removeCommitment, decisionIntention, intentionParameters, true);
            this.desiresForOthers = desiresForOthers;
            this.desiresWithAbstractIntention = desiresWithAbstractIntention;
            this.desiresWithIntentionWithPlan = desiresWithIntentionWithPlan;
        }

        @Override
        public AbstractIntention<OwnDesire.WithAbstractIntention> formIntention(Agent agent) {
            return new AbstractIntention<>(this, intentionParameters, agent.getBeliefs(), removeCommitment, decisionIntention, desiresForOthers, desiresWithAbstractIntention, desiresWithIntentionWithPlan);
        }
    }

    /**
     * Desire to initialize intention with plan
     */
    public static class WithIntentionWithPlan extends OwnDesire<IntentionWithPlan<WithIntentionWithPlan>> {
        private final Command command;

        public WithIntentionWithPlan(DesireKey desireKey, Memory memory, Commitment commitment, DecisionParameters decisionDesire, RemoveCommitment removeCommitment, DecisionParameters decisionIntention, IntentionParameters intentionParameters, Command command) {
            super(desireKey, memory, commitment, decisionDesire, removeCommitment, decisionIntention, intentionParameters, false);
            this.command = command;
        }

        @Override
        public IntentionWithPlan<OwnDesire.WithIntentionWithPlan> formIntention(Agent agent) {
            return new IntentionWithPlan<>(this, intentionParameters, agent.getBeliefs(), removeCommitment, decisionIntention, command);
        }
    }

}
