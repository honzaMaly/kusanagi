package cz.jan.maly.model.watcher;

import com.rits.cloning.Cloner;
import cz.jan.maly.service.WatcherMediatorService;
import cz.jan.maly.utils.MyLogger;
import lombok.Builder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Template. Contains map of fact keys and their feature values
 * Created by Jan on 17-Apr-17.
 */
public class FeatureContainer {

    //cloning features...
    private static final Cloner CLONER = new Cloner();

    private final Set<FactConverter<?>> convertersForFacts;
    private final Set<FactConverter<Stream<?>>> convertersForFactSets;
    private final Set<FactConverter<Stream<Optional<?>>>> convertersForFactsForGlobalBeliefs;
    private final Set<FactConverter<Stream<Optional<Stream<?>>>>> convertersForFactSetsForGlobalBeliefs;
    private final Set<FactConverterByAgentType<Stream<Optional<?>>>> convertersForFactsForGlobalBeliefsByAgentType;
    private final Set<FactConverterByAgentType<Stream<Optional<Stream<?>>>>> convertersForFactSetsForGlobalBeliefsByAgentType;
    private final List<Integer> indexes;
    private final Set<Integer> indexesSet = new HashSet<>();
    private double[] featureVector;
    private boolean hasChanged;
    private final List<Integer> indexesForCommitment;

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
        addIndexes(convertersForFacts.stream().map(FactConverter::getOrder).collect(Collectors.toList()));
        addIndexes(convertersForFactSets.stream().map(FactConverter::getOrder).collect(Collectors.toList()));
        addIndexes(convertersForFactsForGlobalBeliefs.stream().map(FactConverter::getOrder).collect(Collectors.toList()));
        addIndexes(convertersForFactSetsForGlobalBeliefs.stream().map(FactConverter::getOrder).collect(Collectors.toList()));
        addIndexes(convertersForFactsForGlobalBeliefsByAgentType.stream().map(FactConverter::getOrder).collect(Collectors.toList()));
        addIndexes(convertersForFactSetsForGlobalBeliefsByAgentType.stream().map(FactConverter::getOrder).collect(Collectors.toList()));
        indexes = indexesSet.stream().sorted().collect(Collectors.toList());

        //add commitments
        indexesSet.clear();
        addIndexes(interestedInCommitments.stream().map(DesireID::getID).collect(Collectors.toList()));
        indexesForCommitment = indexesSet.stream().sorted().collect(Collectors.toList());

        //make feature vector
        featureVector = new double[indexes.size() + indexesForCommitment.size()];
    }

    public double[] getFeatureVector() {
        return CLONER.deepClone(featureVector);
    }

    private void addIndexes(List<Integer> indexes) {
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
            if (committedToIDs.contains(integer)) {
                commitment = 1;
            }
            if (featureVector[integer + indexes.size()] != commitment) {
                featureVector[integer + indexes.size()] = commitment;
                if (!hasChanged) {
                    hasChanged = true;
                }
            }
        });

        return hasChanged;
    }

    /**
     * Compare values. If differ update value and set flag that value has changed
     *
     * @param converter
     * @param computedValue
     */
    private void updatedFact(FactConverter<?> converter, double computedValue) {
        int index = indexes.indexOf(converter.getOrder());
        if (featureVector[index] != computedValue) {
            featureVector[index] = computedValue;
            if (!hasChanged) {
                hasChanged = true;
            }
        }
    }

}
