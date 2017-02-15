package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Set;

/**
 * Template class for intention with plan of desires to commit to by agent himself
 * Created by Jan on 15-Feb-17.
 */
public abstract class PlanAsDesiresForSelfIntention extends AbstractPlanIntention<OwnDesire> {
    PlanAsDesiresForSelfIntention(Desire<OwnDesire> originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent) {
        super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
    }
}
