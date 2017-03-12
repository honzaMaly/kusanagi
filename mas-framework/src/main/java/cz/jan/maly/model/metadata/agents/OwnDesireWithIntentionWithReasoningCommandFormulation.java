package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.IntentionParameters;
import cz.jan.maly.model.planing.Commitment;
import cz.jan.maly.model.planing.OwnDesire;
import cz.jan.maly.model.planing.RemoveCommitment;
import cz.jan.maly.model.planing.command.ReasoningCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Concrete implementation of own desire with reasoning command formulation
 * Created by Jan on 12-Mar-17.
 */
public class OwnDesireWithIntentionWithReasoningCommandFormulation extends DesireFormulation.WithCommand<ReasoningCommand> implements OwnInternalDesireFormulation<OwnDesire.Reasoning> {
    @Override
    public Optional<OwnDesire.Reasoning> formDesire(DesireKey key, Memory memory) {
        if (supportsDesireType(key)) {
            OwnDesire.Reasoning reasoning = new OwnDesire.Reasoning(key,
                    memory, getDecisionInDesire(key), getParametersForDecisionInDesire(key), getDecisionInIntention(key),
                    getParametersForDecisionInIntention(key), getIntentionParameters(key), commandsByKey.get(key));
            return Optional.of(reasoning);
        }
        return Optional.empty();
    }

    /**
     * Concrete implementation of own desire with intention with command formulation and possibility to create instance based on parent
     */
    public static class Stacked extends OwnDesireWithIntentionWithReasoningCommandFormulation implements OwnInternalDesireFormulationStacked<OwnDesire.Reasoning> {
        private final Map<DesireKey, OwnDesireWithIntentionWithReasoningCommandFormulation> stack = new HashMap<>();

        @Override
        public Optional<OwnDesire.Reasoning> formDesire(DesireKey parentKey, DesireKey key, Memory memory) {
            OwnDesireWithIntentionWithReasoningCommandFormulation formulation = stack.get(parentKey);
            if (formulation != null) {
                if (formulation.supportsDesireType(key)) {
                    OwnDesire.Reasoning reasoning = new OwnDesire.Reasoning(key,
                            memory, formulation.getDecisionInDesire(key), formulation.getParametersForDecisionInDesire(key),
                            formulation.getDecisionInIntention(key), formulation.getParametersForDecisionInIntention(key),
                            formulation.getIntentionParameters(key), formulation.commandsByKey.get(key));
                    return Optional.of(reasoning);
                }
            }
            return formDesire(key, memory);
        }

        /**
         * Add configuration for desire
         *
         * @param key
         * @param parent
         * @param decisionParametersForDesire
         * @param decisionInDesire
         * @param decisionParametersForIntention
         * @param intentionParameters
         * @param decisionInIntention
         * @param command
         */
        public void addDesireFormulationConfiguration(DesireKey parent, DesireKey key, DecisionParameters decisionParametersForDesire,
                                                      Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                      RemoveCommitment decisionInIntention, IntentionParameters intentionParameters, ReasoningCommand command) {
            OwnDesireWithIntentionWithReasoningCommandFormulation formulation = stack.putIfAbsent(parent, new OwnDesireWithIntentionWithReasoningCommandFormulation());
            formulation.addDesireFormulationConfiguration(key, decisionParametersForDesire,
                    decisionInDesire, decisionParametersForIntention, decisionInIntention, intentionParameters, command);
        }

    }

}
