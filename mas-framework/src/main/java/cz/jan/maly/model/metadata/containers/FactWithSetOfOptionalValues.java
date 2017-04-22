package cz.jan.maly.model.metadata.containers;

import cz.jan.maly.model.FeatureRawValueObtainingStrategy;
import cz.jan.maly.model.metadata.FactConverterID;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Container for fact type and raw value obtaining strategy
 * Created by Jan on 14-Apr-17.
 */
@Getter
public class FactWithSetOfOptionalValues<V> extends FactConverterID<V> {
    private final FeatureRawValueObtainingStrategy<Stream<Optional<V>>> strategyToObtainValue;

    public FactWithSetOfOptionalValues(FactConverterID<V> factConverterID, FeatureRawValueObtainingStrategy<Stream<Optional<V>>> strategyToObtainValue) {
        super(factConverterID.getID(), factConverterID.getFactKey());
        this.strategyToObtainValue = strategyToObtainValue;
    }
}
