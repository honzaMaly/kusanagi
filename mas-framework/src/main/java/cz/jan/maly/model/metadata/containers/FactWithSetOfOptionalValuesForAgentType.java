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
public class FactWithSetOfOptionalValuesForAgentType<V> extends FactWithSetOfOptionalValues<V> {
    private final AgentType agentType;

    public FactWithSetOfOptionalValuesForAgentType(FactConverterID<V> factConverterID, FeatureRawValueObtainingStrategy<Stream<Optional<V>>> strategyToObtainValue, AgentType agentType) {
        super(factConverterID, strategyToObtainValue);
        this.agentType = agentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FactWithSetOfOptionalValuesForAgentType<?> that = (FactWithSetOfOptionalValuesForAgentType<?>) o;

        return agentType.equals(that.agentType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + agentType.hashCode();
        return result;
    }
}
