package cz.jan.maly.model.watcher.updating_strategies;

import cz.jan.maly.model.game.wrappers.ABaseLocationWrapper;
import cz.jan.maly.model.watcher.Beliefs;

/**
 * Template interface for BaseEnvironmentObservation
 * Created by Jan on 28-Apr-17.
 */
public interface BaseEnvironmentObservation {

    /**
     * Update beliefs by fields from baseLocation
     *
     * @param baseLocation
     * @param beliefs
     */
    void updateBeliefs(ABaseLocationWrapper baseLocation, Beliefs beliefs);

}
