package cz.jan.maly.service;

import bwapi.Unit;
import bwapi.UnitType;
import cz.jan.maly.model.AgentsUnitTypes;
import cz.jan.maly.model.agent.AgentUnit;
import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
import cz.jan.maly.model.game.wrappers.UnitWrapperFactory;
import cz.jan.maly.service.implementation.BotFacade;
import cz.jan.maly.utils.MyLogger;

import java.util.Optional;

/**
 * Factory to create agent for given unit
 * Created by Jan on 17-Dec-16.
 */
public class AgentUnitFactory implements AgentUnitHandler {

    @Override
    public Optional<AgentUnit> createAgentForUnit(Unit unit, BotFacade botFacade, int frameCount) {
        if (unit.getType().equals(UnitType.Zerg_Drone)) {
            Optional<AUnitWithCommands> wrappedUnit = Optional.ofNullable(UnitWrapperFactory.getCurrentWrappedUnitToCommand(unit, frameCount, false));
            if (!wrappedUnit.isPresent()) {
                MyLogger.getLogger().warning("Could not initiate unit " + UnitType.Zerg_Drone);
                throw new RuntimeException("Could not initiate unit " + UnitType.Zerg_Drone);
            }
            AgentUnit agent = new AgentUnit(AgentsUnitTypes.WORKER, botFacade, wrappedUnit.get());
            return Optional.of(agent);
        }
        return Optional.empty();
    }

}
