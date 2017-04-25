package cz.jan.maly.utils;

import cz.jan.maly.model.features.FeatureNormalizer;
import jsat.linear.distancemetrics.DistanceMetric;
import jsat.linear.distancemetrics.EuclideanDistance;

import java.util.List;

/**
 * Contains configuration...
 * Created by Jan on 23-Apr-17.
 */
public class Configuration {
    public static final DistanceMetric DISTANCE_FUNCTION = new EuclideanDistance();

    /**
     * Standardize each part of feature vector according to normalizers
     *
     * @param featureVector
     * @param normalizers
     * @return
     */
    public static double[] normalizeFeatureVector(double[] featureVector, List<FeatureNormalizer> normalizers) {
        double[] normalizeFeatureVector = new double[featureVector.length];
        for (int i = 0; i < featureVector.length; i++) {
            normalizeFeatureVector[i] = normalizers.get(i).zScoreNormalization(featureVector[i]);
        }
        return normalizeFeatureVector;
    }

}
