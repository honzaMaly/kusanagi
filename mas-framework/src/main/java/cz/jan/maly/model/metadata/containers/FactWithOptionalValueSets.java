package cz.jan.maly.model.metadata.containers;

import cz.jan.maly.model.FeatureRawValueObtainingStrategy;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Container for fact type and raw value obtaining strategy
 * Created by Jan on 14-Apr-17.
 */
@Getter
public class FactWithOptionalValueSets<V> {
    private final FactKey<V> factKey;
    private final FeatureRawValueObtainingStrategy<Stream<Optional<Stream<V>>>> strategyToObtainValue;

    public FactWithOptionalValueSets(FactKey<V> factKey, FeatureRawValueObtainingStrategy<Stream<Optional<Stream<V>>>> strategyToObtainValue) {
        this.factKey = factKey;
        this.strategyToObtainValue = strategyToObtainValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FactWithOptionalValueSets<?> that = (FactWithOptionalValueSets<?>) o;

        return factKey.equals(that.factKey);
    }

    @Override
    public int hashCode() {
        return factKey.hashCode();
    }
}
