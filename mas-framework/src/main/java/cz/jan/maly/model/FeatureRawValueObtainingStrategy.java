package cz.jan.maly.model;

/**
 * Contract to convert object to feature value
 *
 * @param <V>
 */
public interface FeatureRawValueObtainingStrategy<V> {
    double returnRawValue(V v);
}
