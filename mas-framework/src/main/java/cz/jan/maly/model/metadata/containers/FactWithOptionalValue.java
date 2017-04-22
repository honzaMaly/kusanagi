package cz.jan.maly.model.metadata.containers;

import cz.jan.maly.model.FeatureRawValueObtainingStrategy;
import cz.jan.maly.model.metadata.FactConverterID;
import lombok.Getter;

import java.util.Optional;

/**
 * Container for fact type and raw value obtaining strategy
 * Created by Jan on 14-Apr-17.
 */
@Getter
public class FactWithOptionalValue<V> extends FactConverterID<V> {
    private final FeatureRawValueObtainingStrategy<Optional<V>> strategyToObtainValue;

    public FactWithOptionalValue(FactConverterID<V> factConverterID, FeatureRawValueObtainingStrategy<Optional<V>> strategyToObtainValue) {
        super(factConverterID.getID(), factConverterID.getFactKey());
        this.strategyToObtainValue = strategyToObtainValue;
    }
}
