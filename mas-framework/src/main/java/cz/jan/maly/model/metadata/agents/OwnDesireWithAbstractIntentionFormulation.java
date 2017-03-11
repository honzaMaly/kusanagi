package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DecisionParameters;
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
 * Concrete implementation of own desire with abstract plan formulation
 * Created by Jan on 11-Mar-17.
 */
public class OwnDesireWithAbstractIntentionFormulation extends DesireFormulation implements OwnInternalDesireFormulation<OwnDesire.WithAbstractIntention> {
    final Map<DesireKey, Set<DesireKey>> desiresForOthersByKey = new HashMap<>();
    final Map<DesireKey, Set<DesireKey>> desiresWithAbstractIntentionByKey = new HashMap<>();
    final Map<DesireKey, Set<DesireKey>> desiresWithIntentionWithPlanByKey = new HashMap<>();

    @Override
    public Optional<OwnDesire.WithAbstractIntention> formDesire(DesireKey key, Memory memory) {
        if (supportsDesireType(key)) {
            OwnDesire.WithAbstractIntention withAbstractIntention = new OwnDesire.WithAbstractIntention(key,
                    memory, getDecisionInDesire(key), getParametersForDecisionInDesire(key), getDecisionInIntention(key),
                    getParametersForDecisionInIntention(key), getIntentionParameters(key), desiresForOthersByKey.get(key),
                    desiresWithAbstractIntentionByKey.get(key), desiresWithIntentionWithPlanByKey.get(key));
            return Optional.of(withAbstractIntention);
        }
        return Optional.empty();
    }

    /**
     * Add configuration for desire
     *
     * @param key
     * @param decisionParametersForDesire
     * @param decisionInDesire
     * @param decisionParametersForIntention
     * @param intentionParameters
     * @param decisionInIntention
     * @param desiresForOthers
     * @param desiresWithAbstractIntention
     * @param desiresWithIntentionWithPlan
     */
    public void addDesireFormulationConfiguration(DesireKey key, DecisionParameters decisionParametersForDesire,
                                                  Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                  RemoveCommitment decisionInIntention, IntentionParameters intentionParameters,
                                                  Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention,
                                                  Set<DesireKey> desiresWithIntentionWithPlan) {
        addDesireFormulationConfiguration(key, decisionParametersForDesire, decisionInDesire,
                decisionParametersForIntention, decisionInIntention, intentionParameters);
        desiresForOthersByKey.put(key, desiresForOthers);
        desiresWithAbstractIntentionByKey.put(key, desiresWithAbstractIntention);
        desiresWithIntentionWithPlanByKey.put(key, desiresWithIntentionWithPlan);
    }
}
