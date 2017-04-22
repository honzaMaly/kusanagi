package cz.jan.maly.model.metadata.containers;

import cz.jan.maly.model.FeatureRawValueObtainingStrategy;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.FactConverterID;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Container for fact type and raw value obtaining strategy
 * Created by Jan on 14-Apr-17.
 */
@Getter
public class FactWithOptionalValueSetsForAgentType<V> extends FactWithOptionalValueSets<V> {
    private final AgentType agentType;

    public FactWithOptionalValueSetsForAgentType(FactConverterID<V> factConverterID, AgentType agentType,
                                                 FeatureRawValueObtainingStrategy<Stream<Optional<Stream<V>>>> strategyToObtainValue) {
        super(factConverterID, strategyToObtainValue);
        this.agentType = agentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FactWithOptionalValueSetsForAgentType<?> that = (FactWithOptionalValueSetsForAgentType<?>) o;

        return agentType.equals(that.agentType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + agentType.hashCode();
        return result;
    }
}
