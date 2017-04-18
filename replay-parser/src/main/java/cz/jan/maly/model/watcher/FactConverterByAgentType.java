package cz.jan.maly.model.watcher;

import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

/**
 * Extension of FactConverter to add filter AgentWatcherType
 * Created by Jan on 18-Apr-17.
 */
public class FactConverterByAgentType<V> extends FactConverter<V> {

    @Getter
    private final AgentWatcherType type;

    public FactConverterByAgentType(FactKey<V> factKey, FactConvertingStrategy<V> convertingStrategy, int order, AgentWatcherType type) {
        super(factKey, convertingStrategy, order);
        this.type = type;
    }
}
