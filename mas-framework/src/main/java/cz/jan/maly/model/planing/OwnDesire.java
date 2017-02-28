package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireKey;

/**
 * Template for agent's own desires
 * Created by Jan on 15-Feb-17.
 */
public abstract class OwnDesire<T extends Intention<? extends OwnDesire>> extends InternalDesire<T> {
    OwnDesire(DesireKey desireKey, Agent agent, boolean isAbstract) {
        super(desireKey, agent, isAbstract);
    }

    /**
     * Desire to initialize abstract intention
     */
    public abstract static class WithAbstractIntention extends OwnDesire<AbstractIntention<WithAbstractIntention>> {
        protected WithAbstractIntention(DesireKey desireKey, Agent agent, boolean isAbstract) {
            super(desireKey, agent, isAbstract);
        }
    }

    /**
     * Desire to initialize intention with plan
     */
    public abstract static class WithIntentionWithPlan extends OwnDesire<IntentionWithPlan<WithIntentionWithPlan>> {
        protected WithIntentionWithPlan(DesireKey desireKey, Agent agent, boolean isAbstract) {
            super(desireKey, agent, isAbstract);
        }
    }

}
