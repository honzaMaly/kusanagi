package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.metadata.*;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Container with data to be used to decide commitment
 * Created by Jan on 02-Mar-17.
 */
public class DataForDecision {
    //****beliefs for decision point****

    //what was already decided on same level - types of desires
    private final Map<DesireKey, FactConverter.BeliefFromKeyPresence> madeCommitmentToTypes = new HashMap<>();
    private final Map<DesireKey, FactConverter.BeliefFromKeyPresence> didNotMakeCommitmentToTypes = new HashMap<>();
    //desires/intention types to come
    private final Map<DesireKey, FactConverter.BeliefFromKeyPresence> typesAboutToMakeDecision = new HashMap<>();
    //static beliefs from desire key
    private final Map<FactKey<?>, FactConverter.BeliefFromKey<?>> staticBeliefs = new HashMap<>();
    private final Map<FactKey<?>, FactConverter.BeliefSetFromKey<?>> staticBeliefSets = new HashMap<>();
    //beliefs from desire parameters
    private final Map<FactKey<?>, FactConverter.BeliefFromDesire<?>> desireBeliefs = new HashMap<>();
    private final Map<FactKey<?>, FactConverter.BeliefSetFromDesire<?>> desireBeliefSets = new HashMap<>();
    //beliefs from agent beliefs
    private final Map<FactKey<?>, FactConverter.Belief<?>> beliefs = new HashMap<>();
    private final Map<FactKey<?>, FactConverter.BeliefSet<?>> beliefSets = new HashMap<>();
    //global beliefs
    private final Map<FactKey<?>, FactConverter.GlobalBelief<?>> globalBeliefs = new HashMap<>();
    private final Map<FactKey<?>, FactConverter.GlobalBeliefSet<?>> globalBeliefsSets = new HashMap<>();
    //global beliefs by agent type
    private final Map<FactKey<?>, Map<AgentType, FactConverter.GlobalBeliefForAgentType<?>>> globalBeliefsByAgentType = new HashMap<>();
    private final Map<FactKey<?>, Map<AgentType, FactConverter.GlobalBeliefSetForAgentType<?>>> globalBSetsByAgentType = new HashMap<>();
    @Getter
    private int numberOfCommittedAgents = 0;

    //****beliefs for decision point****
    @Getter
    @Setter
    private boolean beliefsChanged = true;

    /**
     * Constructor
     *
     * @param initializer
     * @param desireKey
     * @param desireParameters
     */
    public DataForDecision(DesireKey desireKey, DesireParameters desireParameters, CommitmentDeciderInitializer initializer) {

        initializer.getDesiresToConsider().forEach(key -> {
            madeCommitmentToTypes.put(key, new FactConverter.BeliefFromKeyPresence(this, key));
            didNotMakeCommitmentToTypes.put(key, new FactConverter.BeliefFromKeyPresence(this, key));
            typesAboutToMakeDecision.put(key, new FactConverter.BeliefFromKeyPresence(this, key));
        });

        //static values
        initializer.getStaticBeliefsTypes().forEach(factWithOptionalValue -> staticBeliefs.put(factWithOptionalValue.getFactKey(), new FactConverter.BeliefFromKey<>(this, desireKey, factWithOptionalValue)));
        initializer.getStaticBeliefsSetTypes().forEach(factWithOptionalValueSet -> staticBeliefSets.put(factWithOptionalValueSet.getFactKey(), new FactConverter.BeliefSetFromKey<>(this, desireKey, factWithOptionalValueSet)));

        //values from parameters
        initializer.getParameterValueTypes().forEach(factWithOptionalValue -> desireBeliefs.put(factWithOptionalValue.getFactKey(), new FactConverter.BeliefFromDesire<>(this, desireParameters, factWithOptionalValue)));
        initializer.getParameterValueSetTypes().forEach(factWithOptionalValueSet -> desireBeliefSets.put(factWithOptionalValueSet.getFactKey(), new FactConverter.BeliefSetFromDesire<>(this, desireParameters, factWithOptionalValueSet)));

        //values from beliefs
        initializer.getBeliefTypes().forEach(factWithOptionalValue -> beliefs.put(factWithOptionalValue.getFactKey(), new FactConverter.Belief<>(this, factWithOptionalValue)));
        initializer.getBeliefSetTypes().forEach(factWithOptionalValueSet -> beliefSets.put(factWithOptionalValueSet.getFactKey(), new FactConverter.BeliefSet<>(this, factWithOptionalValueSet)));

        //values from global beliefs
        initializer.getGlobalBeliefTypes().forEach(factWithSetOfOptionalValues -> globalBeliefs.put(factWithSetOfOptionalValues.getFactKey(), new FactConverter.GlobalBelief<>(this, factWithSetOfOptionalValues)));
        initializer.getGlobalBeliefSetTypes().forEach(factWithOptionalValueSets -> globalBeliefsSets.put(factWithOptionalValueSets.getFactKey(), new FactConverter.GlobalBeliefSet<>(this, factWithOptionalValueSets)));

        //values from global beliefs restricted to agent type
        initializer.getGlobalBeliefTypesByAgentType().forEach(factWithSetOfOptionalValuesForAgentType ->
                globalBeliefsByAgentType.computeIfAbsent(factWithSetOfOptionalValuesForAgentType.getFactKey(), factKey -> new HashMap<>())
                        .put(factWithSetOfOptionalValuesForAgentType.getAgentType(), new FactConverter.GlobalBeliefForAgentType<>(this, factWithSetOfOptionalValuesForAgentType)));
        initializer.getGlobalBeliefSetTypesByAgentType().forEach(factWithOptionalValueSetsForAgentType ->
                globalBSetsByAgentType.computeIfAbsent(factWithOptionalValueSetsForAgentType.getFactKey(), factKey -> new HashMap<>())
                        .put(factWithOptionalValueSetsForAgentType.getAgentType(), new FactConverter.GlobalBeliefSetForAgentType<>(this, factWithOptionalValueSetsForAgentType)));
    }

    public double getFeatureValueMadeCommitmentToType(DesireKey desireKey) {
        return madeCommitmentToTypes.get(desireKey).getValue();
    }

    public double getFeatureValueDidNotMakeCommitmentToType(DesireKey desireKey) {
        return didNotMakeCommitmentToTypes.get(desireKey).getValue();
    }

    public double getFeatureValueTypesAboutToMakeDecision(DesireKey desireKey) {
        return typesAboutToMakeDecision.get(desireKey).getValue();
    }

    public double getFeatureValueStaticBeliefs(FactKey<?> desireKey) {
        return staticBeliefs.get(desireKey).getValue();
    }

    public double getFeatureValueStaticBeliefSets(FactKey<?> desireKey) {
        return staticBeliefSets.get(desireKey).getValue();
    }

    public double getFeatureValueDesireBeliefs(FactKey<?> desireKey) {
        return desireBeliefs.get(desireKey).getValue();
    }

    public double getFeatureValueDesireBeliefSets(FactKey<?> desireKey) {
        return desireBeliefSets.get(desireKey).getValue();
    }

    public double getFeatureValueBeliefs(FactKey<?> desireKey) {
        return beliefs.get(desireKey).getValue();
    }

    public double getFeatureValueBeliefSets(FactKey<?> desireKey) {
        return beliefSets.get(desireKey).getValue();
    }

    public double getFeatureValueGlobalBeliefs(FactKey<?> desireKey) {
        return globalBeliefs.get(desireKey).getValue();
    }

    public double getFeatureValueGlobalBeliefSets(FactKey<?> desireKey) {
        return globalBeliefsSets.get(desireKey).getValue();
    }

    public double getFeatureValueGlobalBeliefs(FactKey<?> desireKey, AgentType agentType) {
        return globalBeliefsByAgentType.get(desireKey).get(agentType).getValue();
    }

    public double getFeatureValueGlobalBeliefSets(FactKey<?> desireKey, AgentType agentType) {
        return globalBSetsByAgentType.get(desireKey).get(agentType).getValue();
    }

    /**
     * Update beliefs needed to make decision and set status of update - was any value changed?
     *
     * @param madeCommitmentToTypes
     * @param didNotMakeCommitmentToTypes
     * @param typesAboutToMakeDecision
     * @param memory
     */
    public void updateBeliefs(List<DesireKey> madeCommitmentToTypes, List<DesireKey> didNotMakeCommitmentToTypes,
                              List<DesireKey> typesAboutToMakeDecision, WorkingMemory memory) {
        this.madeCommitmentToTypes.values().forEach(beliefFromKeyPresence -> beliefFromKeyPresence.hasUpdatedValueFromRegisterChanged(madeCommitmentToTypes));
        this.didNotMakeCommitmentToTypes.values().forEach(beliefFromKeyPresence -> beliefFromKeyPresence.hasUpdatedValueFromRegisterChanged(didNotMakeCommitmentToTypes));
        this.typesAboutToMakeDecision.values().forEach(beliefFromKeyPresence -> beliefFromKeyPresence.hasUpdatedValueFromRegisterChanged(typesAboutToMakeDecision));

        this.beliefs.values().forEach(belief -> belief.hasUpdatedValueFromRegisterChanged(memory));
        this.beliefSets.values().forEach(belief -> belief.hasUpdatedValueFromRegisterChanged(memory));

        this.globalBeliefs.values().forEach(belief -> belief.hasUpdatedValueFromRegisterChanged(memory));
        this.globalBeliefsSets.values().forEach(belief -> belief.hasUpdatedValueFromRegisterChanged(memory));

        this.globalBeliefsByAgentType.values().stream()
                .flatMap(agentTypeGlobalBeliefMap -> agentTypeGlobalBeliefMap.values().stream())
                .forEach(globalBelief -> globalBelief.hasUpdatedValueFromRegisterChanged(memory));
        this.globalBSetsByAgentType.values().stream()
                .flatMap(agentTypeGlobalBeliefSetMap -> agentTypeGlobalBeliefSetMap.values().stream())
                .forEach(globalBeliefSet -> globalBeliefSet.hasUpdatedValueFromRegisterChanged(memory));
    }

    /**
     * Update beliefs needed to make decision and set status of update - was any value changed?
     *
     * @param madeCommitmentToTypes
     * @param didNotMakeCommitmentToTypes
     * @param typesAboutToMakeDecision
     * @param memory
     */
    public void updateBeliefs(List<DesireKey> madeCommitmentToTypes, List<DesireKey> didNotMakeCommitmentToTypes,
                              List<DesireKey> typesAboutToMakeDecision, WorkingMemory memory, int numberOfCommittedAgents) {
        updateBeliefs(madeCommitmentToTypes, didNotMakeCommitmentToTypes, typesAboutToMakeDecision, memory);
        if (numberOfCommittedAgents != this.numberOfCommittedAgents) {
            beliefsChanged = true;
            this.numberOfCommittedAgents = numberOfCommittedAgents;
        }
    }

}
