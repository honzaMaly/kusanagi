package cz.jan.maly.service;

import bwapi.Game;
import bwapi.Unit;
import cz.jan.maly.model.watcher.agent_watcher_extension.UnitWatcher;

import java.util.Optional;

/**
 * Contract for factory to initiate agents for unit
 * Created by Jan on 28-Apr-17.
 */
public interface AgentUnitHandler {

    /**
     * Create agent watcher for unit if possible
     *
     * @param unit
     * @param game
     * @return
     */
    Optional<UnitWatcher> createAgentForUnit(Unit unit, Game game);

}
