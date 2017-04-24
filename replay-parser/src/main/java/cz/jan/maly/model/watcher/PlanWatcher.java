package cz.jan.maly.model.watcher;

import cz.jan.maly.model.metadata.DesireKeyID;
import cz.jan.maly.model.tracking.State;
import cz.jan.maly.model.tracking.Trajectory;
import cz.jan.maly.service.WatcherMediatorService;
import lombok.Getter;

import java.util.Set;

/**
 * Class PlanWatcher track agent commitment to desire given beliefs
 * Created by Jan on 18-Apr-17.
 */
public abstract class PlanWatcher {
    private final FeatureContainer container;

    @Getter
    private boolean isCommitted = false;

    @Getter
    private final Trajectory trajectory;

    @Getter
    private final DesireKeyID desireKey;

    protected PlanWatcher(FeatureContainerInitializationStrategy featureContainerInitializationStrategy, DesireKeyID desireKey) {
        this.container = featureContainerInitializationStrategy.returnFeatureContainer();
        this.desireKey = desireKey;
        this.trajectory = new Trajectory(new State(this.container.getFeatureVector(), isCommitted), this.container.getNumberOfFeatures());
    }

    /**
     * Create instance of FeatureContainer
     */
    public interface FeatureContainerInitializationStrategy {
        FeatureContainer returnFeatureContainer();
    }

    /**
     * Decide if agent is committed based on handcrafted rules
     *
     * @return
     */
    protected abstract boolean isAgentCommitted();

    /**
     * If agent transit to new state (either feature vector has changed or commitment) add new state to trajectory
     *
     * @param beliefs
     * @param mediatorService
     * @return
     */
    public void addNewStateIfAgentHasTransitedToOne(Beliefs beliefs, WatcherMediatorService mediatorService, Set<Integer> committedToIDs) {
        boolean hasStatusChanged = false;
        if (isCommitted != isAgentCommitted()) {
            isCommitted = !isCommitted;
            hasStatusChanged = true;
        }
        if (container.isStatusUpdated(beliefs, mediatorService, committedToIDs) || hasStatusChanged) {
            trajectory.addNewState(new State(container.getFeatureVector(), isCommitted));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlanWatcher that = (PlanWatcher) o;

        return desireKey.equals(that.desireKey);
    }

    @Override
    public int hashCode() {
        return desireKey.hashCode();
    }
}
