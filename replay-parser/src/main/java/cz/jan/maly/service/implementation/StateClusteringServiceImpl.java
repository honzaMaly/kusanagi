package cz.jan.maly.service.implementation;

import cz.jan.maly.model.features.FeatureNormalizer;
import cz.jan.maly.model.tracking.State;
import cz.jan.maly.service.StateClusteringService;
import cz.jan.maly.utils.Configuration;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public List<Instance> computeStateRepresentatives(List<State> states, List<FeatureNormalizer> normalizers, int numberOfStates, int numberOfFeatures) throws Exception {

        // Declare the feature vector
        FastVector<Attribute> fastVector = new FastVector<>(numberOfFeatures);
        for (int i = 0; i < numberOfFeatures; i++) {
            fastVector.addElement(new Attribute(String.valueOf(i)));
        }
        Instances instances = new Instances("TestInstances", fastVector, states.size());
        instances.addAll(states.stream()
                .map(State::getFeatureVector)
                .map(doubles -> Configuration.normalizeFeatureVector(doubles, normalizers))
                .map(Configuration::convertVectorToInstance)
                .collect(Collectors.toList()));

        //do simple K-Means
        SimpleKMeans kMeans = new SimpleKMeans();
        kMeans.setNumClusters(numberOfStates);
        kMeans.setDistanceFunction(Configuration.DISTANCE_FUNCTION);
        kMeans.buildClusterer(instances);

        return kMeans.getClusterCentroids();
    }
}
