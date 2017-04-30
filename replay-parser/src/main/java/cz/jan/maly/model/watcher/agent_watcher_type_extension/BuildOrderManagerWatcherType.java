package cz.jan.maly.model.watcher.agent_watcher_type_extension;

import cz.jan.maly.model.metadata.AgentTypeID;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.watcher.AgentWatcherType;
import lombok.Builder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Extension of AgentWatcherType to BuildOrderManagerWatcherType
 * Created by Jan on 30-Apr-17.
 */
public class BuildOrderManagerWatcherType extends AgentWatcherType {

    @Builder
    private BuildOrderManagerWatcherType(AgentTypeID agentTypeID, Set<FactKey<?>> factKeys, Set<FactKey<?>> factSetsKeys,
                                         List<PlanWatcherInitializationStrategy> planWatchers) {
        super(agentTypeID, factKeys, factSetsKeys, planWatchers, null);
    }

    /**
     * Builder with default values
     */
    public static class BuildOrderManagerWatcherTypeBuilder extends AgentWatcherTypeBuilder {
        private Set<FactKey<?>> factKeys = new HashSet<>();
        private Set<FactKey<?>> factSetsKeys = new HashSet<>();
    }
}
