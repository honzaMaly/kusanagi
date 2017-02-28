package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Set;

/**
 * Template class for intention with abstract plan - set of other intentions to commit to. 3 sets a
 * Created by Jan on 15-Feb-17.
 */
public abstract class AbstractIntention<T extends InternalDesire> extends Intention<T> {

    protected AbstractIntention(T originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent) {
        super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
    }

    /**
     * Returns plan as set of desires for others to commit to
     *
     * @return
     */
    public abstract Set<DesireForOthers> returnPlanAsSetOfDesiresForOthers();

    /**
     * Returns plan as set of own desires with abstract intention
     *
     * @return
     */
    public abstract Set<OwnDesire.WithAbstractIntention> returnPlanAsSetOfDesiresWithAbstractIntention();

    /**
     * Returns plan as set of own desires with intention with command
     *
     * @return
     */
    public abstract Set<OwnDesire.WithIntentionWithPlan> returnPlanAsSetOfDesiresWithIntentionWithPlan();

}
