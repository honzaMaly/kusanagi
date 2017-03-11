package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.IntentionParameters;
import lombok.Getter;

import java.util.Set;

/**
 * Template for agent's desires transformation. Desire originated from another agent is transformed
 * Created by Jan on 16-Feb-17.
 */
public abstract class DesireFromAnotherAgent<T extends Intention<? extends DesireFromAnotherAgent<T>>> extends InternalDesire<T> {

    @Getter
    private final SharedDesireForAgents desireForAgents;

    DesireFromAnotherAgent(SharedDesireForAgents desireOriginatedFrom, Commitment commitment, DecisionParameters decisionDesire, RemoveCommitment removeCommitment, DecisionParameters decisionIntention, IntentionParameters intentionParameters, boolean isAbstract) {
        super(desireOriginatedFrom.desireParameters, commitment, decisionDesire, removeCommitment, decisionIntention, intentionParameters, isAbstract);
        this.desireForAgents = desireOriginatedFrom;
    }

    /**
     * Desire to initialize abstract intention
     */
    public static class WithAbstractIntention extends DesireFromAnotherAgent<AbstractIntention<WithAbstractIntention>> {
        private final Set<DesireKey> desiresForOthers;
        private final Set<DesireKey> desiresWithAbstractIntention;
        private final Set<DesireKey> desiresWithIntentionWithPlan;

        public WithAbstractIntention(SharedDesireForAgents desireOriginatedFrom, Commitment commitment, DecisionParameters decisionDesire, RemoveCommitment removeCommitment, DecisionParameters decisionIntention, IntentionParameters intentionParameters, Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention, Set<DesireKey> desiresWithIntentionWithPlan) {
            super(desireOriginatedFrom, commitment, decisionDesire, removeCommitment, decisionIntention, intentionParameters, true);
            this.desiresForOthers = desiresForOthers;
            this.desiresWithAbstractIntention = desiresWithAbstractIntention;
            this.desiresWithIntentionWithPlan = desiresWithIntentionWithPlan;
        }

        @Override
        public AbstractIntention<DesireFromAnotherAgent.WithAbstractIntention> formIntention(Agent agent) {
            return new AbstractIntention<>(this, intentionParameters, agent.getBeliefs(), removeCommitment, decisionIntention, desiresForOthers, desiresWithAbstractIntention, desiresWithIntentionWithPlan);
        }
    }

    /**
     * Desire to initialize intention with plan
     */
    public static class WithIntentionWithPlan extends DesireFromAnotherAgent<IntentionWithPlan<WithIntentionWithPlan>> {
        private final Command command;

        public WithIntentionWithPlan(SharedDesireForAgents desireOriginatedFrom, Commitment commitment, DecisionParameters decisionDesire, RemoveCommitment removeCommitment, DecisionParameters decisionIntention, IntentionParameters intentionParameters, Command command) {
            super(desireOriginatedFrom, commitment, decisionDesire, removeCommitment, decisionIntention, intentionParameters, false);
            this.command = command;
        }

        @Override
        public IntentionWithPlan<DesireFromAnotherAgent.WithIntentionWithPlan> formIntention(Agent agent) {
            return new IntentionWithPlan<>(this, intentionParameters, agent.getBeliefs(), removeCommitment, decisionIntention, command);
        }
    }

}
