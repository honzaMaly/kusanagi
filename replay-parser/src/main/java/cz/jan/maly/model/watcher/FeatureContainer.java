package cz.jan.maly.model.watcher;

import com.rits.cloning.Cloner;
import cz.jan.maly.model.features.FeatureContainerHeader;
import cz.jan.maly.model.metadata.FactConverterID;
import cz.jan.maly.service.WatcherMediatorService;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

import java.util.Set;

/**
 * Template. Contains map of fact keys and their feature values
 * Created by Jan on 17-Apr-17.
 */
public class FeatureContainer {

    //cloning features...
    private static final Cloner CLONER = new Cloner();
    private final FeatureContainerHeader containerHeader;
    private double[] featureVector;
    private boolean hasChanged;
    private final Object syncLock = new Object();

    @Getter
    private final int numberOfFeatures;

    private final boolean trackCommitmentOfOtherAgents;
    private int committedAgents = 0;

    public FeatureContainer(FeatureContainerHeader containerHeader) {
        this.containerHeader = containerHeader;
        this.numberOfFeatures = containerHeader.getSizeOfFeatureVector();
        this.trackCommitmentOfOtherAgents = containerHeader.isTrackCommittedOtherAgents();

        //make feature vector
        featureVector = new double[numberOfFeatures];
    }

    public double[] getFeatureVector() {
        return CLONER.deepClone(featureVector);
    }

    public void addCommitment() {
        synchronized (syncLock) {
            committedAgents++;
        }
    }

    public void removeCommitment() {
        synchronized (syncLock) {
            committedAgents--;
            if (committedAgents < 0) {
                MyLogger.getLogger().warning("Commitment count is negative.");
            }
        }
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
        containerHeader.getConvertersForFactsForGlobalBeliefs().forEach(converter -> updatedFact(converter, mediatorService.getFeatureValueOfFact(converter)));
        containerHeader.getConvertersForFactSetsForGlobalBeliefs().forEach(converter -> updatedFact(converter, mediatorService.getFeatureValueOfFactSet(converter)));
        containerHeader.getConvertersForFacts().forEach(converter -> updatedFact(converter, beliefs.getFeatureValueOfFact(converter)));
        containerHeader.getConvertersForFactSets().forEach(converter -> updatedFact(converter, beliefs.getFeatureValueOfFactSet(converter)));
        containerHeader.getConvertersForFactsForGlobalBeliefsByAgentType().forEach(converter -> updatedFact(converter, mediatorService.getFeatureValueOfFact(converter)));
        containerHeader.getConvertersForFactSetsForGlobalBeliefsByAgentType().forEach(converter -> updatedFact(converter, mediatorService.getFeatureValueOfFactSet(converter)));

        //check commitment
        containerHeader.getIndexesForCommitment().forEach(integer -> {
            double commitment = 0;
            if (committedToIDs.contains(integer)) {
                commitment = 1;
            }
            if (featureVector[integer + containerHeader.getIndexes().size()] != commitment) {
                featureVector[integer + containerHeader.getIndexes().size()] = commitment;
                if (!hasChanged) {
                    hasChanged = true;
                }
            }
        });

        //check committed agents
        if (trackCommitmentOfOtherAgents) {
            synchronized (syncLock) {
                if (featureVector[featureVector.length - 1] != committedAgents) {
                    if (!hasChanged) {
                        hasChanged = true;
                    }
                    featureVector[featureVector.length - 1] = committedAgents;
                }
            }
        }

        return hasChanged;
    }

    /**
     * Compare values. If differ update value and set flag that value has changed
     *
     * @param converter
     * @param computedValue
     */
    private void updatedFact(FactConverterID<?> converter, double computedValue) {
        int index = containerHeader.getIndexes().indexOf(converter.getID());
        if (featureVector[index] != computedValue) {
            featureVector[index] = computedValue;
            if (!hasChanged) {
                hasChanged = true;
            }
        }
    }

}
