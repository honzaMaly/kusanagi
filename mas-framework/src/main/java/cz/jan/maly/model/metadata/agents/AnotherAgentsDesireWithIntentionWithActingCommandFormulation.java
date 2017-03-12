package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.planing.SharedDesireForAgents;
import cz.jan.maly.model.planing.command.ActCommand;

import java.util.Optional;

/**
 * Concrete implementation of another agent's desire with intention with plan formulation
 * Created by Jan on 12-Mar-17.
 */
public class AnotherAgentsDesireWithIntentionWithActingCommandFormulation extends DesireFormulation.WithCommand<ActCommand.DesiredByAnotherAgent> implements AnotherAgentsInternalDesireFormulation<DesireFromAnotherAgent.WithIntentionWithPlan> {
    @Override
    public Optional<DesireFromAnotherAgent.WithIntentionWithPlan> formDesire(SharedDesireForAgents desireForAgents) {
        if (supportsDesireType(desireForAgents.getDesireKey())) {
            DesireFromAnotherAgent.WithIntentionWithPlan withPlan = new DesireFromAnotherAgent.WithIntentionWithPlan(desireForAgents,
                    getDecisionInDesire(desireForAgents.getDesireKey()), getParametersForDecisionInDesire(desireForAgents.getDesireKey()),
                    getDecisionInIntention(desireForAgents.getDesireKey()), getParametersForDecisionInIntention(desireForAgents.getDesireKey()),
                    getIntentionParameters(desireForAgents.getDesireKey()), commandsByKey.get(desireForAgents.getDesireKey()));
            return Optional.of(withPlan);
        }
        return Optional.empty();
    }
}
