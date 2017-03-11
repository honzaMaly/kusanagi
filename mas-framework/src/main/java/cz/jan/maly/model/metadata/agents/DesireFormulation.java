package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.IntentionParameters;
import cz.jan.maly.model.planing.Commitment;
import cz.jan.maly.model.planing.RemoveCommitment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Template for desire formulations of specific type (child dependant), this contains only common structures and methods
 * Created by Jan on 11-Mar-17.
 */
abstract class DesireFormulation {
    //configurations to form desire
    private final Map<DesireKey, DecisionParameters> parametersForDecisionByDesire = new HashMap<>();
    private final Map<DesireKey, Commitment> decisionsByDesire = new HashMap<>();

    //configurations to form intention
    private final Map<DesireKey, DecisionParameters> parametersForDecisionByIntention = new HashMap<>();
    private final Map<DesireKey, RemoveCommitment> decisionsByIntention = new HashMap<>();
    private final Map<DesireKey, IntentionParameters> parametersOfIntentions = new HashMap<>();

    /**
     * Get facts types which needs to be in memory to support desires formulation
     *
     * @return
     */
    public Set<FactKey<?>> getRequiredFactsToSupportFormulation() {
        Set<FactKey<?>> facts = parametersForDecisionByDesire.keySet().stream()
                .flatMap(key -> key.getParametersTypesForFacts().stream())
                .collect(Collectors.toSet());
        facts.addAll(parametersForDecisionByDesire.values().stream()
                .flatMap(decisionContainerParameters -> decisionContainerParameters.getParametersTypesForFacts().stream())
                .collect(Collectors.toSet()));
        facts.addAll(parametersForDecisionByIntention.values().stream()
                .flatMap(decisionContainerParameters -> decisionContainerParameters.getParametersTypesForFacts().stream())
                .collect(Collectors.toSet()));
        facts.addAll(parametersOfIntentions.values().stream()
                .flatMap(intentionParameters -> intentionParameters.getParametersTypesForFacts().stream())
                .collect(Collectors.toSet()));
        return facts;
    }

    /**
     * Get facts sets types which needs to be in memory to support desires formulation
     *
     * @return
     */
    public Set<FactKey<?>> getRequiredFactsSetsToSupportFormulation() {
        Set<FactKey<?>> facts = parametersForDecisionByDesire.keySet().stream()
                .flatMap(key -> key.getParametersTypesForFactSets().stream())
                .collect(Collectors.toSet());
        facts.addAll(parametersForDecisionByDesire.values().stream()
                .flatMap(decisionContainerParameters -> decisionContainerParameters.getParametersTypesForFactSets().stream())
                .collect(Collectors.toSet()));
        facts.addAll(parametersForDecisionByIntention.values().stream()
                .flatMap(decisionContainerParameters -> decisionContainerParameters.getParametersTypesForFactSets().stream())
                .collect(Collectors.toSet()));
        facts.addAll(parametersOfIntentions.values().stream()
                .flatMap(intentionParameters -> intentionParameters.getParametersTypesForFactSets().stream())
                .collect(Collectors.toSet()));
        return facts;
    }

    /**
     * Returns set of supported desires types
     *
     * @return
     */
    public Set<DesireKey> supportedDesireTypes() {
        return parametersForDecisionByDesire.keySet();
    }

    protected IntentionParameters getIntentionParameters(DesireKey key) {
        return parametersOfIntentions.get(key);
    }

    protected RemoveCommitment getDecisionInIntention(DesireKey key) {
        return decisionsByIntention.get(key);
    }

    protected DecisionParameters getParametersForDecisionInIntention(DesireKey key) {
        return parametersForDecisionByIntention.get(key);
    }

    protected boolean supportsDesireType(DesireKey key) {
        return parametersForDecisionByDesire.containsKey(key);
    }

    protected DecisionParameters getParametersForDecisionInDesire(DesireKey key) {
        return parametersForDecisionByDesire.get(key);
    }

    protected Commitment getDecisionInDesire(DesireKey key) {
        return decisionsByDesire.get(key);
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
     */
    protected void addDesireFormulationConfiguration(DesireKey key, DecisionParameters decisionParametersForDesire,
                                                     Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                     RemoveCommitment decisionInIntention, IntentionParameters intentionParameters) {
        parametersForDecisionByDesire.put(key, decisionParametersForDesire);
        decisionsByDesire.put(key, decisionInDesire);
        parametersForDecisionByIntention.put(key, decisionParametersForIntention);
        decisionsByIntention.put(key, decisionInIntention);
        parametersOfIntentions.put(key, intentionParameters);
    }

}
