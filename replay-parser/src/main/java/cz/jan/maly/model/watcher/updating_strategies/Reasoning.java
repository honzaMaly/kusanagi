package cz.jan.maly.model.watcher.updating_strategies;

import cz.jan.maly.model.watcher.Beliefs;
import cz.jan.maly.service.WatcherMediatorService;

/**
 * Template for belief updating strategy
 * Created by Jan on 18-Apr-17.
 */
public interface Reasoning {

    /**
     * Update beliefs
     * @param beliefs
     * @param mediatorService
     */
    void updateBeliefs(Beliefs beliefs, WatcherMediatorService mediatorService);

}
