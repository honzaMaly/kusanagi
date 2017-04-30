package cz.jan.maly.model.watcher.agent_watcher_type_extension;

import cz.jan.maly.model.metadata.AgentTypeID;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.watcher.AgentWatcherType;
import cz.jan.maly.model.watcher.updating_strategies.BaseEnvironmentObservation;
import cz.jan.maly.model.watcher.updating_strategies.Reasoning;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Extension of AgentWatcherType to BaseWatcherType
 * Created by Jan on 28-Apr-17.
 */
@Getter
public class BaseWatcherType extends AgentWatcherType {
    private final BaseEnvironmentObservation baseEnvironmentObservation;

    @Builder
    private BaseWatcherType(AgentTypeID agentTypeID, Set<FactKey<?>> factKeys, Set<FactKey<?>> factSetsKeys,
                            List<PlanWatcherInitializationStrategy> planWatchers, Reasoning reasoning,
                            BaseEnvironmentObservation baseEnvironmentObservation) {
        super(agentTypeID, factKeys, factSetsKeys, planWatchers, reasoning);
        this.baseEnvironmentObservation = baseEnvironmentObservation;
    }

    /**
     * Builder with default values
     */
    public static class BaseWatcherTypeBuilder extends AgentWatcherTypeBuilder {
        private Set<FactKey<?>> factKeys = new HashSet<>();
        private Set<FactKey<?>> factSetsKeys = new HashSet<>();
    }
}
