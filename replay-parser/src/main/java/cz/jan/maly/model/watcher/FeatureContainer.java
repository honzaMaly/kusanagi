package cz.jan.maly.model.watcher;

import cz.jan.maly.service.WatcherMediatorService;
import lombok.Builder;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * Template. Contains map of fact keys and their feature values
 * Created by Jan on 17-Apr-17.
 */
public class FeatureContainer {
    private final Set<FactConverter<?>> convertersForFacts;
    private final Set<FactConverter<Stream<?>>> convertersForFactSets;
    private final Set<FactConverter<Stream<Optional<?>>>> convertersForFactsForGlobalBeliefs;
    private final Set<FactConverter<Stream<Optional<Stream<?>>>>> convertersForFactSetsForGlobalBeliefs;
    private final Set<FactConverterByAgentType<Stream<Optional<?>>>> convertersForFactsForGlobalBeliefsByAgentType;
    private final Set<FactConverterByAgentType<Stream<Optional<Stream<?>>>>> convertersForFactSetsForGlobalBeliefsByAgentType;
    private final List<Integer> indexes = new ArrayList<>();
    @Getter
    private double[] featureVector;
    private boolean hasChanged;
    @Getter
    private double[] featureCommitmentVector;
    private final List<Integer> indexesForCommitment = new ArrayList<>();

    @Builder
    private FeatureContainer(Set<FactConverter<?>> convertersForFacts, Set<FactConverter<Stream<?>>> convertersForFactSets,
                             Set<FactConverter<Stream<Optional<?>>>> convertersForFactsForGlobalBeliefs,
                             Set<FactConverter<Stream<Optional<Stream<?>>>>> convertersForFactSetsForGlobalBeliefs,
                             Set<FactConverterByAgentType<Stream<Optional<?>>>> convertersForFactsForGlobalBeliefsByAgentType,
                             Set<FactConverterByAgentType<Stream<Optional<Stream<?>>>>> convertersForFactSetsForGlobalBeliefsByAgentType, Set<DesireID> interestedInCommitments) {
        this.convertersForFacts = convertersForFacts;
        this.convertersForFactSets = convertersForFactSets;
        this.convertersForFactsForGlobalBeliefs = convertersForFactsForGlobalBeliefs;
        this.convertersForFactSetsForGlobalBeliefs = convertersForFactSetsForGlobalBeliefs;
        this.convertersForFactsForGlobalBeliefsByAgentType = convertersForFactsForGlobalBeliefsByAgentType;
        this.convertersForFactSetsForGlobalBeliefsByAgentType = convertersForFactSetsForGlobalBeliefsByAgentType;

        //add indexes
        indexes.addAll(convertersForFacts.stream().map(FactConverter::getOrder).collect(Collectors.toList()));
        indexes.addAll(convertersForFactSets.stream().map(FactConverter::getOrder).collect(Collectors.toList()));
        indexes.addAll(convertersForFactsForGlobalBeliefs.stream().map(FactConverter::getOrder).collect(Collectors.toList()));
        indexes.addAll(convertersForFactSetsForGlobalBeliefs.stream().map(FactConverter::getOrder).collect(Collectors.toList()));
        indexes.addAll(convertersForFactsForGlobalBeliefsByAgentType.stream().map(FactConverter::getOrder).collect(Collectors.toList()));
        indexes.addAll(convertersForFactSetsForGlobalBeliefsByAgentType.stream().map(FactConverter::getOrder).collect(Collectors.toList()));

        featureVector = new double[indexes.size()];
        Collections.sort(indexes);

        //add commitments
        indexesForCommitment.addAll(interestedInCommitments.stream().map(DesireID::getID).collect(Collectors.toList()));
        featureCommitmentVector = new double[indexesForCommitment.size()];
        Collections.sort(indexesForCommitment);
    }

    /**
     * Default values
     */
    public static class FeatureContainerBuilder {
        private Set<FactConverter<?>> convertersForFacts = new HashSet<>();
        private Set<FactConverter<Stream<?>>> convertersForFactSets = new HashSet<>();
        private Set<FactConverter<Stream<Optional<?>>>> convertersForFactsForGlobalBeliefs = new HashSet<>();
        private Set<FactConverter<Stream<Optional<Stream<?>>>>> convertersForFactSetsForGlobalBeliefs = new HashSet<>();
        private Set<FactConverterByAgentType<Stream<Optional<?>>>> convertersForFactsForGlobalBeliefsByAgentType = new HashSet<>();
        private Set<FactConverterByAgentType<Stream<Optional<Stream<?>>>>> convertersForFactSetsForGlobalBeliefsByAgentType = new HashSet<>();
        private Set<DesireID> interestedInCommitments = new HashSet<>();
    }

    /**
     * Update features if values differ. If so return true to indicate that values has changed
     *
     * @param beliefs
     * @param mediatorService
     * @return
     */
    public boolean isStatusUpdated(Beliefs beliefs, WatcherMediatorService mediatorService, Set<Integer> committedToIDs) {
        hasChanged = false;

        //update features values
        convertersForFactsForGlobalBeliefs.forEach(converter -> updatedFact(converter, mediatorService.getFeatureValueOfFact(converter)));
        convertersForFactSetsForGlobalBeliefs.forEach(converter -> updatedFact(converter, mediatorService.getFeatureValueOfFactSet(converter)));
        convertersForFacts.forEach(converter -> updatedFact(converter, beliefs.getFeatureValueOfFact(converter)));
        convertersForFactSets.forEach(converter -> updatedFact(converter, beliefs.getFeatureValueOfFactSet(converter)));
        convertersForFactsForGlobalBeliefsByAgentType.forEach(converter -> updatedFact(converter, mediatorService.getFeatureValueOfFact(converter)));
        convertersForFactSetsForGlobalBeliefsByAgentType.forEach(converter -> updatedFact(converter, mediatorService.getFeatureValueOfFactSet(converter)));

        //check commitment
        indexesForCommitment.forEach(integer -> {
            double commitment = 0;
            if (committedToIDs.contains(integer)){
                commitment = 1;
            }
            if (featureCommitmentVector[integer]!=commitment){
                featureVector[integer] = commitment;
                if (!hasChanged){
                    hasChanged = true;
                }
            }
        });

        return hasChanged;
    }

    /**
     * Compare values. If differ update value and set flag that value has changed
     * @param converter
     * @param computedValue
     */
    private void updatedFact(FactConverter<?> converter, double computedValue){
        int index = indexes.indexOf(converter.getOrder());
        if (featureVector[index] != computedValue) {
            featureVector[index] = computedValue;
            if (!hasChanged){
                hasChanged = true;
            }
        }
    }

}
