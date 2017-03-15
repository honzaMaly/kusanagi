package cz.jan.maly.service;

import bwapi.Unit;
import bwapi.UnitType;
import cz.jan.maly.model.AgentsTypes;
import cz.jan.maly.model.agent.BWAgentInGame;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.service.implementation.BotFacade;

import java.util.Optional;

/**
 * Factory to create agent for given unit
 * Created by Jan on 17-Dec-16.
 */
public class AgentUnitFactory implements AgentUnitFactoryInterface {

    @Override
    public Optional<BWAgentInGame> createAgentForUnit(Unit unit, BotFacade botFacade) {
        if (unit.getType().equals(UnitType.Zerg_Drone)) {
            return Optional.of(new BWAgentInGame(AgentsTypes.WORKER, botFacade, AUnit.createFrom(unit)));
        }
        return Optional.empty();
    }

}
