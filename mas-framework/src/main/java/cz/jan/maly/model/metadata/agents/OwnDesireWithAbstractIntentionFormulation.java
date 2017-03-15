package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.IntentionParameters;
import cz.jan.maly.model.planing.Commitment;
import cz.jan.maly.model.planing.OwnDesire;
import cz.jan.maly.model.planing.RemoveCommitment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Concrete implementation of own desire with abstract plan formulation
 * Created by Jan on 11-Mar-17.
 */
public class OwnDesireWithAbstractIntentionFormulation extends DesireFormulation.WithAbstractPlan implements OwnInternalDesireFormulation<OwnDesire.WithAbstractIntention> {

    @Override
    public Optional<OwnDesire.WithAbstractIntention> formDesire(DesireKey key, Memory memory) {
        if (supportsDesireType(key)) {
            OwnDesire.WithAbstractIntention withAbstractIntention = new OwnDesire.WithAbstractIntention(key,
                    memory, getDecisionInDesire(key), getParametersForDecisionInDesire(key), getDecisionInIntention(key),
                    getParametersForDecisionInIntention(key), getIntentionParameters(key), desiresForOthersByKey.get(key),
                    desiresWithAbstractIntentionByKey.get(key), desiresWithIntentionToActByKey.get(key), desiresWithIntentionToReasonByKey.get(key));
            return Optional.of(withAbstractIntention);
        }
        return Optional.empty();
    }

    /**
     * Concrete implementation of own desire with abstract plan formulation and possibility to create instance based on parent
     */
    public static class Stacked extends OwnDesireWithAbstractIntentionFormulation implements OwnInternalDesireFormulationStacked<OwnDesire.WithAbstractIntention>, StackCommonGetters<OwnDesireWithAbstractIntentionFormulation> {
        private final Map<DesireKey, OwnDesireWithAbstractIntentionFormulation> stack = new HashMap<>();

        @Override
        public Optional<OwnDesire.WithAbstractIntention> formDesire(DesireKey parentKey, DesireKey key, Memory memory) {
            OwnDesireWithAbstractIntentionFormulation formulation = stack.get(parentKey);
            if (formulation != null) {
                if (formulation.supportsDesireType(key)) {
                    OwnDesire.WithAbstractIntention withAbstractIntention = new OwnDesire.WithAbstractIntention(key,
                            memory, formulation.getDecisionInDesire(key), formulation.getParametersForDecisionInDesire(key),
                            formulation.getDecisionInIntention(key), formulation.getParametersForDecisionInIntention(key),
                            formulation.getIntentionParameters(key), formulation.desiresForOthersByKey.get(key),
                            formulation.desiresWithAbstractIntentionByKey.get(key), formulation.desiresWithIntentionToActByKey.get(key),
                            formulation.desiresWithIntentionToReasonByKey.get(key));
                    return Optional.of(withAbstractIntention);
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
         * @param desiresForOthers
         * @param desiresWithAbstractIntention
         * @param desiresWithIntentionToAct
         * @param desiresWithIntentionToReason
         */
        public void addDesireFormulationConfiguration(DesireKey parent, DesireKey key, DecisionParameters decisionParametersForDesire,
                                                      Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                      RemoveCommitment decisionInIntention, IntentionParameters intentionParameters,
                                                      Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention,
                                                      Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason) {
            OwnDesireWithAbstractIntentionFormulation formulation = stack.putIfAbsent(parent, new OwnDesireWithAbstractIntentionFormulation());
            formulation.addDesireFormulationConfiguration(key, decisionParametersForDesire,
                    decisionInDesire, decisionParametersForIntention, decisionInIntention, intentionParameters,
                    desiresForOthers, desiresWithAbstractIntention, desiresWithIntentionToAct, desiresWithIntentionToReason);
        }

        @Override
        public Set<FactKey<?>> getRequiredFactsToSupportFormulationInStack() {
            return getRequiredFactsToSupportFormulation(stack.values());
        }

        @Override
        public Set<FactKey<?>> getRequiredFactsSetsToSupportFormulationInStack() {
            return getRequiredFactsSetsToSupportFormulation(stack.values());
        }
    }

}
