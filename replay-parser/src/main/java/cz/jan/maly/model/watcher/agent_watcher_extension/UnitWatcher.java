package cz.jan.maly.model.watcher.agent_watcher_extension;

import bwapi.Game;
import bwapi.Unit;
import cz.jan.maly.model.AgentMakingObservations;
import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
import cz.jan.maly.model.watcher.AgentWatcher;
import cz.jan.maly.model.watcher.agent_watcher_type_extension.UnitWatcherType;

/**
 * Created by Jan on 28-Apr-17.
 */
public class UnitWatcher extends AgentWatcher<UnitWatcherType> implements AgentMakingObservations {
    private AUnitWithCommands unitWithCommands;
    private final Game game;
    protected final Unit unit;

    public UnitWatcher(UnitWatcherType agentWatcherType, Game game, AUnitWithCommands aUnitWithCommands, Unit unit) {
        super(agentWatcherType);
        this.game = game;
        this.unit = unit;
        this.unitWithCommands = aUnitWithCommands;
    }

    @Override
    public void makeObservation() {
        unitWithCommands = UnitWatcherType.getAgentEnvironmentObservation().updateBeliefs(unitWithCommands, beliefs, game.getFrameCount());
    }
}
