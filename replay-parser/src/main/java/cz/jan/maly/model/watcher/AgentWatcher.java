package cz.jan.maly.model.watcher;

import cz.jan.maly.model.metadata.DesireKeyID;
import cz.jan.maly.model.tracking.Trajectory;
import cz.jan.maly.service.WatcherMediatorService;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Template for AgentWatcher
 * Created by Jan on 17-Apr-17.
 */
public class AgentWatcher<T extends AgentWatcherType> {

    @Getter
    protected final T agentWatcherType;

    private static int idCounter = 0;

    @Getter
    protected final Beliefs beliefs;

    @Getter
    private final int ID;

    @Getter
    private final List<PlanWatcher> plansToWatch;

    public AgentWatcher(T agentWatcherType) {
        this.agentWatcherType = agentWatcherType;
        beliefs = new Beliefs(agentWatcherType);
        this.ID = idCounter++;
        this.plansToWatch = agentWatcherType.getPlanWatchers().stream()
                .map(AgentWatcherType.PlanWatcherInitializationStrategy::returnPlanWatcher)
                .collect(Collectors.toList());
    }

    /**
     * Get stream of entries of list of trajectories for desire
     *
     * @return
     */
    public Stream<Map.Entry<DesireKeyID, List<Trajectory>>> getTrajectories() {
        return plansToWatch.stream()
                .collect(Collectors.groupingBy(PlanWatcher::getDesireKey,
                        Collectors.mapping(PlanWatcher::getTrajectory, Collectors.toList())))
                .entrySet().stream();
    }

    /**
     * Do reasoning
     *
     * @param mediatorService
     */
    public void reason(WatcherMediatorService mediatorService) {
        if (agentWatcherType.getReasoning().isPresent()) {
            agentWatcherType.getReasoning().get().updateBeliefs(beliefs, mediatorService);
        }
    }

    /**
     * Handle trajectories of plans
     *
     * @param mediatorService
     * @return
     */
    public void handleTrajectoriesOfPlans(WatcherMediatorService mediatorService) {
        Set<Integer> committedIDs = plansToWatch.stream()
                .filter(PlanWatcher::isCommitted)
                .map(planWatcher -> planWatcher.getDesireKey().getID())
                .collect(Collectors.toSet());
        plansToWatch.forEach(planWatcher -> planWatcher.addNewStateIfAgentHasTransitedToOne(beliefs, mediatorService, committedIDs));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AgentWatcher that = (AgentWatcher) o;

        return ID == that.ID && agentWatcherType.equals(that.agentWatcherType);
    }

    @Override
    public int hashCode() {
        int result = agentWatcherType.hashCode();
        result = 31 * result + ID;
        return result;
    }
}
