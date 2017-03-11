package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DecisionContainerParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.IntentionParameters;
import cz.jan.maly.model.planing.Commitment;
import cz.jan.maly.model.planing.OwnDesire;
import cz.jan.maly.model.planing.RemoveCommitment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Concrete implementation of own desire with abstract plan formulation and possibility to create instance based on parent
 * Created by Jan on 11-Mar-17.
 */
public class OwnDesireWithAbstractIntentionStackedFormulation extends OwnDesireWithAbstractIntentionFormulation implements OwnInternalDesireFormulationStacked<OwnDesire.WithAbstractIntention> {
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
                        formulation.desiresWithAbstractIntentionByKey.get(key), formulation.desiresWithIntentionWithPlanByKey.get(key));
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
     * @param decisionContainerParametersForDesire
     * @param decisionInDesire
     * @param decisionContainerParametersForIntention
     * @param intentionParameters
     * @param decisionInIntention
     * @param desiresForOthers
     * @param desiresWithAbstractIntention
     * @param desiresWithIntentionWithPlan
     */
    public void addDesireFormulationConfiguration(DesireKey parent, DesireKey key, DecisionContainerParameters decisionContainerParametersForDesire,
                                                  Commitment decisionInDesire, DecisionContainerParameters decisionContainerParametersForIntention,
                                                  RemoveCommitment decisionInIntention, IntentionParameters intentionParameters,
                                                  Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention,
                                                  Set<DesireKey> desiresWithIntentionWithPlan) {
        OwnDesireWithAbstractIntentionFormulation formulation = stack.putIfAbsent(parent, new OwnDesireWithAbstractIntentionFormulation());
        formulation.addDesireFormulationConfiguration(key, decisionContainerParametersForDesire,
                decisionInDesire, decisionContainerParametersForIntention, decisionInIntention, intentionParameters,
                desiresForOthers, desiresWithAbstractIntention, desiresWithIntentionWithPlan);
    }

}
