package cz.jan.maly.service;

import bwapi.Unit;
import bwapi.UnitType;
import cz.jan.maly.model.AgentsUnitTypes;
import cz.jan.maly.model.agent.AgentUnit;
import cz.jan.maly.model.agent.types.AgentTypeUnit;
import cz.jan.maly.model.game.wrappers.AUnitTypeWrapper;
import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
import cz.jan.maly.model.game.wrappers.UnitWrapperFactory;
import cz.jan.maly.model.game.wrappers.WrapperTypeFactory;
import cz.jan.maly.service.implementation.BotFacade;
import cz.jan.maly.utils.MyLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Factory to create agent for given unit
 * Created by Jan on 17-Dec-16.
 */
public class AgentUnitFactory implements AgentUnitHandler {
    public static final AUnitTypeWrapper ZERGLING_TYPE = WrapperTypeFactory.createFrom(UnitType.Zerg_Zergling);
    public static final AUnitTypeWrapper DRONE_TYPE = WrapperTypeFactory.createFrom(UnitType.Zerg_Drone);
    public static final AUnitTypeWrapper HATCHERY_TYPE = WrapperTypeFactory.createFrom(UnitType.Zerg_Hatchery);
    public static final AUnitTypeWrapper LARVA_TYPE = WrapperTypeFactory.createFrom(UnitType.Zerg_Larva);
    public static final AUnitTypeWrapper EGG_TYPE = WrapperTypeFactory.createFrom(UnitType.Zerg_Egg);
    public static final AUnitTypeWrapper SPAWNING_POOL_TYPE = WrapperTypeFactory.createFrom(UnitType.Zerg_Spawning_Pool);

    private final Map<AUnitTypeWrapper, AgentTypeUnit> agentConfigurationForUnitType = new HashMap<>();

    {
        agentConfigurationForUnitType.put(DRONE_TYPE, AgentsUnitTypes.DRONE);
        agentConfigurationForUnitType.put(HATCHERY_TYPE, AgentsUnitTypes.HATCHERY);
        agentConfigurationForUnitType.put(LARVA_TYPE, AgentsUnitTypes.LARVA);
        agentConfigurationForUnitType.put(EGG_TYPE, AgentsUnitTypes.EGG);
        agentConfigurationForUnitType.put(SPAWNING_POOL_TYPE, AgentsUnitTypes.SPAWNING_POOL);
    }

    @Override
    public Optional<AgentUnit> createAgentForUnit(Unit unit, BotFacade botFacade, int frameCount) {
        Optional<AgentTypeUnit> agentTypeUnit = Optional.ofNullable(agentConfigurationForUnitType.get(WrapperTypeFactory.createFrom(unit.getType())));
        if (agentTypeUnit.isPresent()) {
            Optional<AUnitWithCommands> wrappedUnit = Optional.ofNullable(UnitWrapperFactory.getCurrentWrappedUnitToCommand(unit, frameCount, false));
            if (!wrappedUnit.isPresent()) {
                MyLogger.getLogger().warning("Could not initiate unit " + unit.getType());
                throw new RuntimeException("Could not initiate unit " + unit.getType());
            }
            AgentUnit agent = new AgentUnit(agentTypeUnit.get(), botFacade, wrappedUnit.get());
            return Optional.of(agent);
        }
        return Optional.empty();
    }


}
