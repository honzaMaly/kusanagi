package cz.jan.maly.model.watcher.agent_watcher_type_extension;

import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.watcher.AgentWatcherType;
import cz.jan.maly.model.watcher.updating_strategies.PlayerEnvironmentObservation;
import cz.jan.maly.model.watcher.updating_strategies.Reasoning;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Jan on 18-Apr-17.
 */
@Getter
public class AgentWatcherPlayerType extends AgentWatcherType {
    private final PlayerEnvironmentObservation playerEnvironmentObservation;

    @Builder
    private AgentWatcherPlayerType(String name, Set<FactKey<?>> factKeys, Set<FactKey<?>> factSetsKeys,
                                   List<PlanWatcherInitializationStrategy> planWatchers, Optional<Reasoning> reasoning, PlayerEnvironmentObservation playerEnvironmentObservation) {
        super(name, factKeys, factSetsKeys, planWatchers, reasoning);
        this.playerEnvironmentObservation = playerEnvironmentObservation;
    }

    /**
     * Builder with default values
     */
    public static class AgentWatcherPlayerTypeBuilder extends AgentWatcherTypeBuilder {
        private Set<FactKey<?>> factKeys = new HashSet<>();
        private Set<FactKey<?>> factSetsKeys = new HashSet<>();
        private Optional<Reasoning> reasoning = Optional.empty();
    }
}
