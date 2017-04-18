package cz.jan.maly.model.watcher.updating_strategies;

import bwapi.Player;
import cz.jan.maly.model.watcher.Beliefs;

/**
 * Template interface for PlayerEnvironmentObservation
 * Created by Jan on 18-Apr-17.
 */
public interface PlayerEnvironmentObservation {

    /**
     * Update beliefs by fields from unit
     * @param aPlayer
     * @param beliefs
     */
    void updateBeliefs(Player aPlayer, Beliefs beliefs);

}
