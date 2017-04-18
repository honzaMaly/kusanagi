package cz.jan.maly.model.watcher;

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
    private final DesireID desireKey;

    protected PlanWatcher(FeatureContainerInitializationStrategy featureContainerInitializationStrategy, DesireID desireKey) {
        this.container = featureContainerInitializationStrategy.returnFeatureContainer();
        this.desireKey = desireKey;
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
     * Was status updated
     *
     * @param beliefs
     * @param mediatorService
     * @return
     */
    public boolean hasStatusChanged(Beliefs beliefs, WatcherMediatorService mediatorService, Set<Integer> committedToIDs) {
        boolean hasStatusChanged = container.isStatusUpdated(beliefs, mediatorService, committedToIDs);
        if (isCommitted != isAgentCommitted()) {
            isCommitted = !isCommitted;
            return true;
        }
        return hasStatusChanged;
    }

    /**
     * Get current feature vector
     *
     * @return
     */
    public double[] getFeatureVector() {
        return container.getFeatureVector();
    }

    /**
     * Get current feature vector
     *
     * @return
     */
    public double[] getFeatureVectorOfCommitment() {
        return container.getFeatureCommitmentVector();
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
