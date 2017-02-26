package cz.jan.maly.model.planing;

/**
 * Template for agent's desires transformation. Desire originated from another agent is transformed
 * Created by Jan on 16-Feb-17.
 */
public abstract class DesireFromAnotherAgent<V extends DesireFromAnotherAgent, T extends Intention<V>> extends InternalDesire<V, T> {
    DesireFromAnotherAgent(SharedDesireForAgents desireOriginatedFrom, boolean isAbstract) {
        super(desireOriginatedFrom.desireParameters, isAbstract);
    }

    /**
     * Desire to initialize abstract intention
     */
    public abstract static class WithAbstractIntention extends DesireFromAnotherAgent<WithAbstractIntention, AbstractIntention<WithAbstractIntention>> {
        protected WithAbstractIntention(SharedDesireForAgents desireOriginatedFrom, boolean isAbstract) {
            super(desireOriginatedFrom, isAbstract);
        }
    }

    /**
     * Desire to initialize intention with plan
     */
    public abstract static class WithIntentionWithPlan extends DesireFromAnotherAgent<WithIntentionWithPlan, IntentionWithPlan<WithIntentionWithPlan>> {
        protected WithIntentionWithPlan(SharedDesireForAgents desireOriginatedFrom, boolean isAbstract) {
            super(desireOriginatedFrom, isAbstract);
        }
    }

}
