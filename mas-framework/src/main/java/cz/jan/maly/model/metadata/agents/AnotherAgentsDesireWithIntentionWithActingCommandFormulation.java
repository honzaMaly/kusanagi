package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.planing.IntentionCommand;
import cz.jan.maly.model.planing.SharedDesireForAgents;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.CommandFormulationStrategy;

import java.util.Optional;

/**
 * Concrete implementation of another agent's desire with intention with plan formulation
 * Created by Jan on 12-Mar-17.
 */
public class AnotherAgentsDesireWithIntentionWithActingCommandFormulation extends DesireFormulation.WithCommand<CommandFormulationStrategy<ActCommand.DesiredByAnotherAgent, IntentionCommand.FromAnotherAgent>> implements AnotherAgentsInternalDesireFormulation<DesireFromAnotherAgent.WithIntentionWithPlan> {
    @Override
    public Optional<DesireFromAnotherAgent.WithIntentionWithPlan> formDesire(SharedDesireForAgents desireForAgents, WorkingMemory memory) {
        if (supportsDesireType(desireForAgents.getDesireKey())) {
            DesireFromAnotherAgent.WithIntentionWithPlan withPlan = new DesireFromAnotherAgent.WithIntentionWithPlan(desireForAgents,
                    memory, getDecisionInDesire(desireForAgents.getDesireKey()), getDecisionInIntention(desireForAgents.getDesireKey()),
                    commandsByKey.get(desireForAgents.getDesireKey()),
                    getReactionInDesire(desireForAgents.getDesireKey()), getReactionInIntention(desireForAgents.getDesireKey()));
            return Optional.of(withPlan);
        }
        return Optional.empty();
    }

    @Override
    public boolean supportsDesireType(DesireKey desireKey) {
        return supportsType(desireKey);
    }
}
