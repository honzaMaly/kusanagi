package cz.jan.maly.model.planing;

import lombok.Getter;

/**
 * Template for agent's desires transformation. Desire originated from another agent is transformed
 * Created by Jan on 16-Feb-17.
 */
public abstract class DesireFromAnotherAgent<T extends Intention<? extends DesireFromAnotherAgent>> extends InternalDesire<T> {

    @Getter
    private final SharedDesireForAgents desireForAgents;

    DesireFromAnotherAgent(SharedDesireForAgents desireOriginatedFrom, boolean isAbstract) {
        super(desireOriginatedFrom.desireParameters, isAbstract);
        this.desireForAgents = desireOriginatedFrom;
    }

    /**
     * Desire to initialize abstract intention
     */
    public abstract static class WithAbstractIntention extends DesireFromAnotherAgent<AbstractIntention<WithAbstractIntention>> {
        protected WithAbstractIntention(SharedDesireForAgents desireOriginatedFrom, boolean isAbstract) {
            super(desireOriginatedFrom, isAbstract);
        }
    }

    /**
     * Desire to initialize intention with plan
     */
    public abstract static class WithIntentionWithPlan extends DesireFromAnotherAgent<IntentionWithPlan<WithIntentionWithPlan>> {
        protected WithIntentionWithPlan(SharedDesireForAgents desireOriginatedFrom, boolean isAbstract) {
            super(desireOriginatedFrom, isAbstract);
        }
    }

}
