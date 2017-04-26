package cz.jan.maly.model.features;

import cz.jan.maly.model.metadata.DesireKeyID;
import cz.jan.maly.model.metadata.FactConverterID;
import cz.jan.maly.model.metadata.containers.*;
import cz.jan.maly.utils.MyLogger;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Contains map of fact keys and their feature values to initialize various instances of FeatureContainer types
 * Created by Jan on 22-Apr-17.
 */
@Getter
public class FeatureContainerHeader {
    private final Set<FactWithOptionalValue<?>> convertersForFacts;
    private final Set<FactWithOptionalValueSet<?>> convertersForFactSets;
    private final Set<FactWithSetOfOptionalValues<?>> convertersForFactsForGlobalBeliefs;
    private final Set<FactWithOptionalValueSets<?>> convertersForFactSetsForGlobalBeliefs;
    private final Set<FactWithSetOfOptionalValuesForAgentType<?>> convertersForFactsForGlobalBeliefsByAgentType;
    private final Set<FactWithOptionalValueSetsForAgentType<?>> convertersForFactSetsForGlobalBeliefsByAgentType;
    private final List<Integer> indexes;
    private final List<Integer> indexesForCommitment;
    private final int sizeOfFeatureVector;
    private final boolean trackCommittedOtherAgents;

    @Builder
    private FeatureContainerHeader(Set<FactWithOptionalValue<?>> convertersForFacts, Set<FactWithOptionalValueSet<?>> convertersForFactSets,
                                   Set<FactWithSetOfOptionalValues<?>> convertersForFactsForGlobalBeliefs,
                                   Set<FactWithOptionalValueSets<?>> convertersForFactSetsForGlobalBeliefs,
                                   Set<FactWithSetOfOptionalValuesForAgentType<?>> convertersForFactsForGlobalBeliefsByAgentType,
                                   Set<FactWithOptionalValueSetsForAgentType<?>> convertersForFactSetsForGlobalBeliefsByAgentType,
                                   Set<DesireKeyID> interestedInCommitments, boolean trackCommittedOtherAgents) {
        this.convertersForFacts = convertersForFacts;
        this.convertersForFactSets = convertersForFactSets;
        this.convertersForFactsForGlobalBeliefs = convertersForFactsForGlobalBeliefs;
        this.convertersForFactSetsForGlobalBeliefs = convertersForFactSetsForGlobalBeliefs;
        this.convertersForFactsForGlobalBeliefsByAgentType = convertersForFactsForGlobalBeliefsByAgentType;
        this.convertersForFactSetsForGlobalBeliefsByAgentType = convertersForFactSetsForGlobalBeliefsByAgentType;
        this.trackCommittedOtherAgents = trackCommittedOtherAgents;

        Set<Integer> indexesSet = new HashSet<>();

        //add indexes
        addIndexes(convertersForFacts.stream().map(FactConverterID::getID).collect(Collectors.toList()), indexesSet);
        addIndexes(convertersForFactSets.stream().map(FactConverterID::getID).collect(Collectors.toList()), indexesSet);
        addIndexes(convertersForFactsForGlobalBeliefs.stream().map(FactConverterID::getID).collect(Collectors.toList()), indexesSet);
        addIndexes(convertersForFactSetsForGlobalBeliefs.stream().map(FactConverterID::getID).collect(Collectors.toList()), indexesSet);
        addIndexes(convertersForFactsForGlobalBeliefsByAgentType.stream().map(FactConverterID::getID).collect(Collectors.toList()), indexesSet);
        addIndexes(convertersForFactSetsForGlobalBeliefsByAgentType.stream().map(FactConverterID::getID).collect(Collectors.toList()), indexesSet);
        indexes = indexesSet.stream().sorted().collect(Collectors.toList());

        //add commitments
        indexesSet.clear();
        addIndexes(interestedInCommitments.stream().map(DesireKeyID::getID).collect(Collectors.toList()), indexesSet);
        indexesForCommitment = indexesSet.stream().sorted().collect(Collectors.toList());

        //one additional dimension to track commitment by other agents
        if (trackCommittedOtherAgents) {
            this.sizeOfFeatureVector = indexes.size() + indexesForCommitment.size() + 1;
        } else {
            this.sizeOfFeatureVector = indexes.size() + indexesForCommitment.size();
        }
    }

    /**
     * Check for duplicity
     *
     * @param indexes
     * @param indexesSet
     */
    private void addIndexes(List<Integer> indexes, Set<Integer> indexesSet) {
        for (Integer integer : indexes) {
            if (indexesSet.contains(integer)) {
                MyLogger.getLogger().warning("Found duplicity index.");
            }
            indexesSet.add(integer);
        }
    }

    /**
     * Default values
     */
    public static class FeatureContainerHeaderBuilder {
        private Set<FactWithOptionalValue<?>> convertersForFacts = new HashSet<>();
        private Set<FactWithOptionalValueSet<?>> convertersForFactSets = new HashSet<>();
        private Set<FactWithSetOfOptionalValues<?>> convertersForFactsForGlobalBeliefs = new HashSet<>();
        private Set<FactWithOptionalValueSets<?>> convertersForFactSetsForGlobalBeliefs = new HashSet<>();
        private Set<FactWithSetOfOptionalValuesForAgentType<?>> convertersForFactsForGlobalBeliefsByAgentType = new HashSet<>();
        private Set<FactWithOptionalValueSetsForAgentType<?>> convertersForFactSetsForGlobalBeliefsByAgentType = new HashSet<>();
        private Set<DesireKeyID> interestedInCommitments = new HashSet<>();
        private boolean trackCommittedOtherAgents = false;
    }

}
