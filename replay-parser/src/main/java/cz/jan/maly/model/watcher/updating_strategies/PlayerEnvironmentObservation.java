package cz.jan.maly.model.watcher.updating_strategies;

import cz.jan.maly.model.game.wrappers.APlayer;
import cz.jan.maly.model.watcher.Beliefs;

/**
 * Template interface for PlayerEnvironmentObservation
 * Created by Jan on 18-Apr-17.
 */
public interface PlayerEnvironmentObservation {

    /**
     * Update beliefs by fields from unit
     *
     * @param aPlayer
     * @param beliefs
     * @return
     */
    APlayer updateBeliefs(APlayer aPlayer, Beliefs beliefs);

}
