package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.IntentionParameters;
import cz.jan.maly.model.planing.Commitment;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.planing.RemoveCommitment;
import cz.jan.maly.model.planing.SharedDesireForAgents;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Concrete implementation of another agent's desire with abstract plan formulation
 * Created by Jan on 11-Mar-17.
 */
public class AnotherAgentsDesireWithAbstractIntentionFormulation extends DesireFormulation implements AnotherAgentsInternalDesireFormulation<DesireFromAnotherAgent.WithAbstractIntention> {
    private final Map<DesireKey, Set<DesireKey>> desiresForOthersByKey = new HashMap<>();
    private final Map<DesireKey, Set<DesireKey>> desiresWithAbstractIntentionByKey = new HashMap<>();
    private final Map<DesireKey, Set<DesireKey>> desiresWithIntentionWithPlanByKey = new HashMap<>();

    @Override
    public Optional<DesireFromAnotherAgent.WithAbstractIntention> formDesire(SharedDesireForAgents desireForAgents) {
        if (supportsDesireType(desireForAgents.getDesireKey())) {
            DesireFromAnotherAgent.WithAbstractIntention withAbstractIntention = new DesireFromAnotherAgent.WithAbstractIntention(desireForAgents,
                    getDecisionInDesire(desireForAgents.getDesireKey()), getParametersForDecisionInDesire(desireForAgents.getDesireKey()),
                    getDecisionInIntention(desireForAgents.getDesireKey()), getParametersForDecisionInIntention(desireForAgents.getDesireKey()),
                    getIntentionParameters(desireForAgents.getDesireKey()), desiresForOthersByKey.get(desireForAgents.getDesireKey()),
                    desiresWithAbstractIntentionByKey.get(desireForAgents.getDesireKey()), desiresWithIntentionWithPlanByKey.get(desireForAgents.getDesireKey()));
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
