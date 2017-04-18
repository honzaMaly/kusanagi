package cz.jan.maly.model.watcher;

import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.Key;
import cz.jan.maly.model.watcher.updating_strategies.Reasoning;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * AgentWatcher type
 * Created by Jan on 17-Apr-17.
 */
@Getter
public class AgentWatcherType extends Key {
    private final Set<FactKey<?>> factKeys;
    private final Set<FactKey<?>> factSetsKeys;
    private final List<PlanWatcherInitializationStrategy> planWatchers;
    private final Optional<Reasoning> reasoning;

    protected AgentWatcherType(String name, Set<FactKey<?>> factKeys, Set<FactKey<?>> factSetsKeys, List<PlanWatcherInitializationStrategy> planWatchers,
                             Optional<Reasoning> reasoning) {
        super(name, AgentWatcherType.class);
        this.factKeys = factKeys;
        this.factSetsKeys = factSetsKeys;
        this.planWatchers = planWatchers;
        this.reasoning = reasoning;
    }

    /**
     * Builder with default values
     */
    public static class AgentWatcherTypeBuilder {
        private Set<FactKey<?>> factKeys = new HashSet<>();
        private Set<FactKey<?>> factSetsKeys = new HashSet<>();
        private Optional<Reasoning> reasoning = Optional.empty();
    }

    /**
     * Create instance of PlanWatcher
     */
    public interface PlanWatcherInitializationStrategy {
        PlanWatcher returnPlanWatcher();
    }

}
