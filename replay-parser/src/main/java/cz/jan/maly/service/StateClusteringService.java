package cz.jan.maly.service;

import cz.jan.maly.model.features.FeatureNormalizer;
import cz.jan.maly.model.tracking.State;
import cz.jan.maly.model.tracking.Trajectory;
import jsat.linear.Vec;

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
     * @param trajectories
     * @param states
     * @param normalizers
     * @return
     */
    List<Vec> computeStateRepresentatives(List<Trajectory> trajectories, List<State> states, List<FeatureNormalizer> normalizers);

}
