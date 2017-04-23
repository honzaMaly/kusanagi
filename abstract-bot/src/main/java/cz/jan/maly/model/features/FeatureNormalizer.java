package cz.jan.maly.model.features;

import java.io.Serializable;
import java.util.stream.DoubleStream;

/**
 * Transform value to z-score based on computed parameters
 * Created by Jan on 23-Apr-17.
 */
public class FeatureNormalizer implements Serializable {
    private final double mean;
    private final double std;

    public FeatureNormalizer(DoubleStream possibleValues) {
        this.mean = computeMean(possibleValues);
        this.std = computeStandardDeviation(possibleValues, this.mean);
    }

    private double computeMean(DoubleStream doubles) {
        return doubles.average().orElseGet(null);
    }

    private double computeStandardDeviation(DoubleStream doubles, double mean) {
        return Math.sqrt(doubles.map(d -> Math.pow(d - mean, 2)).sum());
    }

    /**
     * Transform value to z-score
     * @param value
     * @return
     */
    public double zScoreNormalization(double value) {
        return (value - mean) / std;
    }

}
