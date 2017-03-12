package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.IntentionParameters;
import cz.jan.maly.model.planing.Commitment;
import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.planing.RemoveCommitment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Concrete implementation of own desire with desire to share with others
 * Created by Jan on 12-Mar-17.
 */
public class OwnDesireWithSharedDesireFormulation extends DesireFormulation implements OwnInternalDesireFormulation<DesireForOthers> {
    final Map<DesireKey, DesireKey> sharedDesireKeyByKey = new HashMap<>();
    final Map<DesireKey, Integer> countOfAgentsToCommitByKey = new HashMap<>();

    @Override
    public Optional<DesireForOthers> formDesire(DesireKey key, Memory memory) {
        if (supportsDesireType(key)) {
            DesireForOthers forOthers = new DesireForOthers(key,
                    memory, getDecisionInDesire(key), getParametersForDecisionInDesire(key), getDecisionInIntention(key),
                    getParametersForDecisionInIntention(key), getIntentionParameters(key), sharedDesireKeyByKey.get(key),
                    countOfAgentsToCommitByKey.get(key));
            return Optional.of(forOthers);
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
     * @param sharedDesireKey
     * @param counts
     */
    public void addDesireFormulationConfiguration(DesireKey key, DecisionParameters decisionParametersForDesire,
                                                  Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                  RemoveCommitment decisionInIntention, IntentionParameters intentionParameters,
                                                  DesireKey sharedDesireKey, int counts) {
        addDesireFormulationConfiguration(key, decisionParametersForDesire, decisionInDesire,
                decisionParametersForIntention, decisionInIntention, intentionParameters);
        sharedDesireKeyByKey.put(key, sharedDesireKey);
        countOfAgentsToCommitByKey.put(key, counts);
    }

    /**
     * Concrete implementation of own desire with desire to share with others and possibility to create instance based on parent
     */
    public static class Stacked extends OwnDesireWithSharedDesireFormulation implements OwnInternalDesireFormulationStacked<DesireForOthers> {
        private final Map<DesireKey, OwnDesireWithSharedDesireFormulation> stack = new HashMap<>();

        @Override
        public Optional<DesireForOthers> formDesire(DesireKey parentKey, DesireKey key, Memory memory) {
            OwnDesireWithSharedDesireFormulation formulation = stack.get(parentKey);
            if (formulation != null) {
                if (formulation.supportsDesireType(key)) {
                    DesireForOthers forOthers = new DesireForOthers(key,
                            memory, formulation.getDecisionInDesire(key), formulation.getParametersForDecisionInDesire(key),
                            formulation.getDecisionInIntention(key), formulation.getParametersForDecisionInIntention(key),
                            formulation.getIntentionParameters(key), formulation.sharedDesireKeyByKey.get(key),
                            formulation.countOfAgentsToCommitByKey.get(key));
                    return Optional.of(forOthers);
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
         * @param sharedDesireKey
         * @param counts
         */
        public void addDesireFormulationConfiguration(DesireKey parent, DesireKey key, DecisionParameters decisionParametersForDesire,
                                                      Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                      RemoveCommitment decisionInIntention, IntentionParameters intentionParameters,
                                                      DesireKey sharedDesireKey, int counts) {
            OwnDesireWithSharedDesireFormulation formulation = stack.putIfAbsent(parent, new OwnDesireWithSharedDesireFormulation());
            formulation.addDesireFormulationConfiguration(key, decisionParametersForDesire,
                    decisionInDesire, decisionParametersForIntention, decisionInIntention, intentionParameters, sharedDesireKey, counts);
        }

    }

}
