package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.planing.SharedDesireForAgents;

import java.util.Optional;

/**
 * Concrete implementation of another agent's desire with abstract plan formulation
 * Created by Jan on 11-Mar-17.
 */
public class AnotherAgentsDesireWithAbstractIntentionFormulation extends DesireFormulation.WithAbstractPlan implements AnotherAgentsInternalDesireFormulation<DesireFromAnotherAgent.WithAbstractIntention> {

    @Override
    public Optional<DesireFromAnotherAgent.WithAbstractIntention> formDesire(SharedDesireForAgents desireForAgents) {
        if (supportsDesireType(desireForAgents.getDesireKey())) {
            DesireFromAnotherAgent.WithAbstractIntention withAbstractIntention = new DesireFromAnotherAgent.WithAbstractIntention(desireForAgents,
                    getDecisionInDesire(desireForAgents.getDesireKey()), getParametersForDecisionInDesire(desireForAgents.getDesireKey()),
                    getDecisionInIntention(desireForAgents.getDesireKey()), getParametersForDecisionInIntention(desireForAgents.getDesireKey()),
                    getIntentionParameters(desireForAgents.getDesireKey()), desiresForOthersByKey.get(desireForAgents.getDesireKey()),
                    desiresWithAbstractIntentionByKey.get(desireForAgents.getDesireKey()), desiresWithIntentionToActByKey.get(desireForAgents.getDesireKey()),
                    desiresWithIntentionToReasonByKey.get(desireForAgents.getDesireKey()));
            return Optional.of(withAbstractIntention);
        }
        return Optional.empty();
    }
}
