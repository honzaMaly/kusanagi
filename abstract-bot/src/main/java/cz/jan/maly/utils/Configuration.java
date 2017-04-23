package cz.jan.maly.utils;

import weka.core.DenseInstance;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;

/**
 * Contains configuration...
 * Created by Jan on 23-Apr-17.
 */
public class Configuration {
    public static final DistanceFunction DISTANCE_FUNCTION = new EuclideanDistance();

    /**
     * Converts featureVector to Instance
     *
     * @return
     */
    public static Instance convertVectorToInstance(double[] featureVector) {
        Instance inst = new DenseInstance(featureVector.length);
        for (int i = 0; i < featureVector.length; i++) {
            inst.setValue(i, featureVector[i]);
        }
        return inst;
    }
}
