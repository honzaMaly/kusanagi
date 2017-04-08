package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.planing.IntentionCommand;
import cz.jan.maly.model.planing.OwnDesire;
import cz.jan.maly.model.planing.command.CommandFormulationStrategy;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.utils.MyLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Concrete implementation of own desire with reasoning command formulation
 * Created by Jan on 12-Mar-17.
 */
public class OwnDesireWithIntentionWithReasoningCommandFormulation extends DesireFormulation.WithCommand<CommandFormulationStrategy<ReasoningCommand, IntentionCommand.OwnReasoning>> implements OwnInternalDesireFormulation<OwnDesire.Reasoning> {
    @Override
    public Optional<OwnDesire.Reasoning> formDesire(DesireKey key, WorkingMemory memory) {
        if (supportsDesireType(key)) {
            OwnDesire.Reasoning reasoning = new OwnDesire.Reasoning(key,
                    memory, getDecisionInDesire(key), getDecisionInIntention(key),
                    getTypesOfDesiresToConsiderWhenCommitting(key), getTypesOfDesiresToConsiderWhenRemovingCommitment(key),
                    commandsByKey.get(key));
            return Optional.of(reasoning);
        }
        return Optional.empty();
    }

    @Override
    public boolean supportsDesireType(DesireKey desireKey) {
        return supportsType(desireKey);
    }

    /**
     * Concrete implementation of own desire with intention with command formulation and possibility to create instance based on parent
     */
    public static class Stacked extends OwnDesireWithIntentionWithReasoningCommandFormulation implements OwnInternalDesireFormulationStacked<OwnDesire.Reasoning> {
        private final Map<DesireKey, OwnDesireWithIntentionWithReasoningCommandFormulation> stack = new HashMap<>();

        @Override
        public Optional<OwnDesire.Reasoning> formDesire(DesireKey parentKey, DesireKey key, WorkingMemory memory, DesireParameters parentsDesireParameters) {
            OwnDesireWithIntentionWithReasoningCommandFormulation formulation = stack.get(parentKey);
            if (formulation != null) {
                if (formulation.supportsDesireType(key)) {
                    OwnDesire.Reasoning reasoning = new OwnDesire.Reasoning(key,
                            memory, formulation.getDecisionInDesire(key), formulation.getDecisionInIntention(key),
                            formulation.getTypesOfDesiresToConsiderWhenCommitting(key),
                            formulation.getTypesOfDesiresToConsiderWhenRemovingCommitment(key),
                            formulation.commandsByKey.get(key), parentsDesireParameters);
                    return Optional.of(reasoning);
                }
            }
            return formDesire(key, memory);
        }

        @Override
        public boolean supportsDesireType(DesireKey parent, DesireKey key) {
            if (stack.get(parent) == null || !stack.get(parent).supportsDesireType(key)) {
                MyLogger.getLogger().warning(parent.getName() + " is not associated with " + key.getName());
                return supportsType(key);
            }
            return true;
        }

        /**
         * Add configuration for desire
         *
         * @param parent
         * @param key
         * @param configuration
         */
        public void addDesireFormulationConfiguration(DesireKey parent, DesireKey key,
                                                      ConfigurationWithCommand<CommandFormulationStrategy<ReasoningCommand, IntentionCommand.OwnReasoning>> configuration) {
            OwnDesireWithIntentionWithReasoningCommandFormulation formulation = stack.computeIfAbsent(parent, desireKey -> new OwnDesireWithIntentionWithReasoningCommandFormulation());
            formulation.addDesireFormulationConfiguration(key, configuration);
        }

    }

}
