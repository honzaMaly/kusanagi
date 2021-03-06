package cz.jan.maly.service.implementation;

import cz.jan.maly.model.features.FeatureNormalizer;
import cz.jan.maly.model.tracking.State;
import cz.jan.maly.model.tracking.Trajectory;
import cz.jan.maly.service.StateClusteringService;
import cz.jan.maly.utils.Configuration;
import jsat.SimpleDataSet;
import jsat.classifiers.DataPoint;
import jsat.clustering.SeedSelectionMethods;
import jsat.clustering.kmeans.GMeans;
import jsat.clustering.kmeans.HamerlyKMeans;
import jsat.clustering.kmeans.MiniBatchKMeans;
import jsat.linear.DenseVector;
import jsat.linear.Vec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static cz.jan.maly.utils.Configuration.DISTANCE_FUNCTION;

/**
 * Concrete implementation of StateClusteringService
 * Created by Jan on 24-Apr-17.
 */
public class StateClusteringServiceImpl implements StateClusteringService {
    private static final int sampleStates = 20000;
    private static final SeedSelectionMethods.SeedSelection SEED_SELECTION_METHOD = SeedSelectionMethods.SeedSelection.MEAN_QUANTILES;

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
    public List<Vec> computeStateRepresentatives(List<Trajectory> trajectories, List<State> states, List<FeatureNormalizer> normalizers) {
        if (states.size() > sampleStates) {
//            int clusterNumberEstimation = estimateClusters(trajectories, normalizers);
//            MyLogger.getLogger().info("Estimated #" + clusterNumberEstimation + " clusters.");
            MiniBatchKMeans batchKMeans = new MiniBatchKMeans(DISTANCE_FUNCTION, 200, 250, SEED_SELECTION_METHOD);
            batchKMeans.cluster(createDataSet(states, normalizers), 1500);
            return batchKMeans.getMeans();
        }
        return computeStateRepresentatives(states, normalizers);
    }

    private int estimateClusters(List<Trajectory> trajectories, List<FeatureNormalizer> normalizers) {
        List<Trajectory> copy = new ArrayList<>(trajectories);
        Collections.shuffle(copy);
        List<List<Trajectory>> batches = trajectories.stream()
                .collect(splitByBatchSize(sampleStates));
        Collections.shuffle(batches);
        return ((int) batches.subList(0, Math.min(4, batches.size())).stream()
                .mapToInt(value -> computeStateRepresentatives(value.stream().flatMap(trajectory -> trajectory.getStates().stream()).collect(Collectors.toList()), normalizers).size())
                .average().orElse(1000));
//        return computeStateRepresentatives(trajectories.stream().flatMap(trajectory -> trajectory.getStates().stream()).limit(sampleStates).collect(Collectors.toList()), normalizers).size();
    }

    private static Collector<Trajectory, List<List<Trajectory>>, List<List<Trajectory>>> splitByBatchSize(int statesPerBatch) {
        final List<Trajectory> current = new ArrayList<>();
        return Collector.of(ArrayList::new,
                (lists, trajectory) -> {
                    current.add(trajectory);
                    if (current.stream().mapToInt(t -> t.getStates().size()).sum() > statesPerBatch) {
                        lists.add(new ArrayList<>(current));
                        current.clear();
                    }
                },
                (l1, l2) -> {
                    throw new RuntimeException("Should not run this in parallel");
                },
                l -> {
                    if (current.size() != 0) {
                        l.add(current);
                        return l;
                    }
                    return new ArrayList<>();
                }
        );
    }

    /**
     * Create data set
     *
     * @param states
     * @param normalizers
     * @return
     */

    private SimpleDataSet createDataSet(List<State> states, List<FeatureNormalizer> normalizers) {
        List<DataPoint> dataPoints = states.stream()
                .map(State::getFeatureVector)
                .map(doubles -> Configuration.normalizeFeatureVector(doubles, normalizers))
                .map(doubles -> new DataPoint(new DenseVector(doubles)))
                .collect(Collectors.toList());
        return new SimpleDataSet(dataPoints);
    }

    /**
     * Do clustering + cluster number estimation, returns means
     *
     * @param states
     * @param normalizers
     * @return
     */
    private List<Vec> computeStateRepresentatives(List<State> states, List<FeatureNormalizer> normalizers) {
        GMeans kMeans = new GMeans(new HamerlyKMeans(DISTANCE_FUNCTION, SEED_SELECTION_METHOD));

        //do clustering
        kMeans.cluster(createDataSet(states, normalizers));

        //get means and return them
        return kMeans.getMeans();
    }

}
