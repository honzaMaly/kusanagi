package cz.jan.maly.model.agent;

import bwapi.Unit;
import bwapi.UnitType;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.service.AgentUnitFactoryInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory to create agent for given unit
 * Created by Jan on 17-Dec-16.
 */
public class AgentUnitFactory implements AgentUnitFactoryInterface {
    private static Map<UnitType, AgentCreationStrategy> UNIT_TYPE_AGENT_CREATION_STRATEGY_MAP = new HashMap<>();

    {
        UNIT_TYPE_AGENT_CREATION_STRATEGY_MAP.put(UnitType.Zerg_Drone, unit -> new Worker(0, AUnit.createFrom(unit)));
        UNIT_TYPE_AGENT_CREATION_STRATEGY_MAP.put(UnitType.Zerg_Hatchery, unit -> new Base(0, AUnit.createFrom(unit)));
        UNIT_TYPE_AGENT_CREATION_STRATEGY_MAP.put(UnitType.Zerg_Larva, unit -> new Larva(0, AUnit.createFrom(unit)));
    }

    @Override
    public void createAgentForUnit(Unit unit) {
        AgentCreationStrategy agentCreationStrategy = UNIT_TYPE_AGENT_CREATION_STRATEGY_MAP.get(unit.getType());
        if (agentCreationStrategy != null) {
            agentCreationStrategy.createAgent(unit);
        }
    }

    private interface AgentCreationStrategy {
        void createAgent(Unit unit);
    }

}
