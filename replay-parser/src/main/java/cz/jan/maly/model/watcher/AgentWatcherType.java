package cz.jan.maly.model.watcher;

import cz.jan.maly.model.metadata.AgentTypeID;
import cz.jan.maly.model.metadata.FactKey;
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
public class AgentWatcherType extends AgentTypeID {
    private final Set<FactKey<?>> factKeys;
    private final Set<FactKey<?>> factSetsKeys;
    private final List<PlanWatcherInitializationStrategy> planWatchers;
    private final Optional<Reasoning> reasoning;

    protected AgentWatcherType(AgentTypeID agentTypeID, Set<FactKey<?>> factKeys, Set<FactKey<?>> factSetsKeys, List<PlanWatcherInitializationStrategy> planWatchers,
                               Reasoning reasoning) {
        super(agentTypeID.getName(), agentTypeID.getID());
        this.factKeys = factKeys;
        this.factSetsKeys = factSetsKeys;
        this.planWatchers = planWatchers;
        this.reasoning = Optional.ofNullable(reasoning);
    }

    /**
     * Builder with default values
     */
    public static class AgentWatcherTypeBuilder {
        private Set<FactKey<?>> factKeys = new HashSet<>();
        private Set<FactKey<?>> factSetsKeys = new HashSet<>();
    }

    /**
     * Create instance of PlanWatcher
     */
    public interface PlanWatcherInitializationStrategy {
        PlanWatcher returnPlanWatcher();
    }

}
