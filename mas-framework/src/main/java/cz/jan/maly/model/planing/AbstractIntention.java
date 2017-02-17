package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Set;

/**
 * Template class for intention with abstract plan - set of other intentions to commit to
 * Created by Jan on 15-Feb-17.
 */
abstract class AbstractIntention<T extends Desire, K extends Desire> extends Intention<T> {

    private AbstractIntention(T originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent) {
        super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
    }

    /**
     * Returns plan as set of desires to commit to - for self or for other agents
     *
     * @return
     */
    public abstract Set<K> returnPlanAsSetOfDesires();

    /**
     * Template class for intention with plan of desires to commit to by other agents
     */
    public abstract class WithDesiresForOtherAgents<V extends Desire> extends AbstractIntention<V, DesireForOthers> {

        WithDesiresForOtherAgents(V originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent) {
            super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
        }

        /**
         * Initiated by own desire...
         */
        public abstract class FromSelf extends WithDesiresForOtherAgents<OwnDesire> {
            protected FromSelf(OwnDesire originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent) {
                super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
            }
        }

        /**
         * Initiated by another agent's desire...
         */
        public abstract class FromAnotherAgent extends WithDesiresForOtherAgents<DesireFromAnotherAgent> {
            protected FromAnotherAgent(DesireFromAnotherAgent originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent) {
                super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
            }
        }

    }

    /**
     * Template class for intention with plan of desires to commit to by agent himself
     */
    public abstract class WithDesiresForSelf<V extends Desire> extends AbstractIntention<V, OwnDesire> {
        WithDesiresForSelf(V originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent) {
            super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
        }

        /**
         * Initiated by own desire...
         */
        public abstract class FromSelf extends WithDesiresForSelf<OwnDesire> {
            protected FromSelf(OwnDesire originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent) {
                super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
            }
        }

        /**
         * Initiated by another agent's desire...
         */
        public abstract class FromAnotherAgent extends WithDesiresForSelf<DesireFromAnotherAgent> {
            protected FromAnotherAgent(DesireFromAnotherAgent originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent) {
                super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
            }
        }

    }

}
