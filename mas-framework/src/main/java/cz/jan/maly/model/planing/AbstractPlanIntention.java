package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Set;

/**
 * Template class for intention with abstract plan - set of other intentions to commit to
 * Created by Jan on 15-Feb-17.
 */
abstract class AbstractPlanIntention<V extends Desire> extends Intention<OwnDesire> {

    AbstractPlanIntention(Desire<OwnDesire> originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent) {
        super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
    }

    /**
     * Returns plan as set of desires to commit to - for self or for other agents
     * @return
     */
    public abstract Set<V> returnPlanAsSetOfDesires();

}
