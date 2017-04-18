package cz.jan.maly.model.watcher.updating_strategies;

import bwapi.Unit;
import cz.jan.maly.model.watcher.Beliefs;

/**
 * Template interface for AgentEnvironmentObservation
 * Created by Jan on 18-Apr-17.
 */
public interface AgentEnvironmentObservation {

    /**
     * Update beliefs by fields from unit
     * @param aUnit
     * @param beliefs
     */
    void updateBeliefs(Unit aUnit, Beliefs beliefs);

}
