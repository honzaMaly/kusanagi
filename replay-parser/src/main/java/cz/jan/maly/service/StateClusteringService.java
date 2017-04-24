package cz.jan.maly.service;

import cz.jan.maly.model.features.FeatureNormalizer;
import cz.jan.maly.model.tracking.State;
import weka.core.Instance;

import java.util.List;

/**
 * Contract for clustering service
 * Created by Jan on 24-Apr-17.
 */
public interface StateClusteringService {

    /**
     * Returns feature normalizers
     *
     * @param states
     * @param cardinality
     * @return
     */
    List<FeatureNormalizer> computeFeatureNormalizersBasedOnStates(List<State> states, int cardinality);

    /**
     * Compute states representative (do compression)
     *
     * @param states
     * @param normalizers
     * @param numberOfStates
     * @param numberOfFeatures
     * @return
     */
    List<Instance> computeStateRepresentatives(List<State> states, List<FeatureNormalizer> normalizers, int numberOfStates, int numberOfFeatures) throws Exception;

}
