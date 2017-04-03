package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.planing.IntentionCommand;
import cz.jan.maly.model.planing.OwnDesire;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.CommandFormulationStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Concrete implementation of own desire with acting command formulation
 * Created by Jan on 12-Mar-17.
 */
public class OwnDesireWithIntentionWithActingCommandFormulation extends DesireFormulation.WithCommand<CommandFormulationStrategy<ActCommand.Own, IntentionCommand.OwnActing>> implements OwnInternalDesireFormulation<OwnDesire.Acting> {
    @Override
    public Optional<OwnDesire.Acting> formDesire(DesireKey key, WorkingMemory memory) {
        if (supportsDesireType(key)) {
            OwnDesire.Acting acting = new OwnDesire.Acting(key,
                    memory, getDecisionInDesire(key), getDecisionInIntention(key), getTypesOfDesiresToConsiderWhenCommitting(key),
                    getTypesOfDesiresToConsiderWhenRemovingCommitment(key), commandsByKey.get(key));
            return Optional.of(acting);
        }
        return Optional.empty();
    }

    /**
     * Concrete implementation of own desire with intention with command formulation and possibility to create instance based on parent
     */
    public static class Stacked extends OwnDesireWithIntentionWithActingCommandFormulation implements OwnInternalDesireFormulationStacked<OwnDesire.Acting> {
        private final Map<DesireKey, OwnDesireWithIntentionWithActingCommandFormulation> stack = new HashMap<>();

        @Override
        public Optional<OwnDesire.Acting> formDesire(DesireKey parentKey, DesireKey key, WorkingMemory memory) {
            OwnDesireWithIntentionWithActingCommandFormulation formulation = stack.get(parentKey);
            if (formulation != null) {
                if (formulation.supportsDesireType(key)) {
                    OwnDesire.Acting acting = new OwnDesire.Acting(key,
                            memory, formulation.getDecisionInDesire(key), formulation.getDecisionInIntention(key),
                            formulation.getTypesOfDesiresToConsiderWhenCommitting(key),
                            formulation.getTypesOfDesiresToConsiderWhenRemovingCommitment(key),
                            formulation.commandsByKey.get(key));
                    return Optional.of(acting);
                }
            }
            return formDesire(key, memory);
        }

        /**
         * Add configuration for desire
         *
         * @param parent
         * @param key
         * @param configuration
         */
        public void addDesireFormulationConfiguration(DesireKey parent, DesireKey key,
                                                      ConfigurationWithCommand<CommandFormulationStrategy<ActCommand.Own, IntentionCommand.OwnActing>> configuration) {
            OwnDesireWithIntentionWithActingCommandFormulation formulation = stack.computeIfAbsent(parent, desireKey -> new OwnDesireWithIntentionWithActingCommandFormulation());
            formulation.addDesireFormulationConfiguration(key, configuration);
        }

    }
}
