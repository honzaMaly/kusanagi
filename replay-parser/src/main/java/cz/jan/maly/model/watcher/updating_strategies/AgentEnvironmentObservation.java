package cz.jan.maly.model.watcher.updating_strategies;

import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
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
     * @param frame
     *
     * @return
     */
    AUnitWithCommands updateBeliefs(AUnitWithCommands aUnit, Beliefs beliefs, int frame);

}
