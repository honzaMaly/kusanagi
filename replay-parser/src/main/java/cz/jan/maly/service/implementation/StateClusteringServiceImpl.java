package cz.jan.maly.service.implementation;

import cz.jan.maly.model.features.FeatureNormalizer;
import cz.jan.maly.model.tracking.State;
import cz.jan.maly.service.StateClusteringService;
import cz.jan.maly.utils.Configuration;
import jsat.SimpleDataSet;
import jsat.classifiers.DataPoint;
import jsat.clustering.SeedSelectionMethods;
import jsat.clustering.kmeans.HamerlyKMeans;
import jsat.clustering.kmeans.KMeans;
import jsat.linear.DenseVector;
import jsat.linear.Vec;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.jan.maly.utils.Configuration.DISTANCE_FUNCTION;

/**
 * Concrete implementation of StateClusteringService
 * Created by Jan on 24-Apr-17.
 */
public class StateClusteringServiceImpl implements StateClusteringService {

    @Override
    public List<FeatureNormalizer> computeFeatureNormalizersBasedOnStates(List<State> states, int cardinality) {
        List<FeatureNormalizer> normalizers = new ArrayList<>();
        for (int i = 0; i < cardinality; i++) {
            final int index = i;
            Set<Double> possibleValues = states.stream()
                    .map(State::getFeatureVector)
                    .mapToDouble(doubles -> doubles[index])
                    .boxed()
                    .collect(Collectors.toSet());
            normalizers.add(new FeatureNormalizer(possibleValues));
        }
        return normalizers;
    }

    @Override
    public List<Vec> computeStateRepresentatives(List<State> states, List<FeatureNormalizer> normalizers, int maxNumberOfClusters, int numberOfFeatures) throws Exception {

        //one of the fastest k-means
        KMeans simpleKMeans = new HamerlyKMeans(DISTANCE_FUNCTION, SeedSelectionMethods.SeedSelection.FARTHEST_FIRST);

        //create data set
        List<DataPoint> dataPoints = states.stream()
                .map(State::getFeatureVector)
                .map(doubles -> Configuration.normalizeFeatureVector(doubles, normalizers))
                .map(doubles -> new DataPoint(new DenseVector(doubles)))
                .collect(Collectors.toList());
        SimpleDataSet dataSet = new SimpleDataSet(dataPoints);

        //do clustering
        simpleKMeans.cluster(dataSet, maxNumberOfClusters);

        //get means and return them
        return simpleKMeans.getMeans();
    }
}
