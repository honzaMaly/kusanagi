package cz.jan.maly.service.implementation;

import bwapi.Unit;
import cz.jan.maly.model.AgentsUnitTypes;
import cz.jan.maly.model.agent.AgentUnit;
import cz.jan.maly.model.agent.types.AgentTypeUnit;
import cz.jan.maly.model.game.wrappers.AUnitTypeWrapper;
import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
import cz.jan.maly.model.game.wrappers.UnitWrapperFactory;
import cz.jan.maly.model.game.wrappers.WrapperTypeFactory;
import cz.jan.maly.service.AgentUnitHandler;
import cz.jan.maly.utils.MyLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static cz.jan.maly.model.game.wrappers.AUnitTypeWrapper.*;

/**
 * Factory to create agent for given unit
 * Created by Jan on 17-Dec-16.
 */
public class AgentUnitHandlerImpl implements AgentUnitHandler {

    private final Map<AUnitTypeWrapper, AgentTypeUnit> agentConfigurationForUnitType = new HashMap<>();

    {
        agentConfigurationForUnitType.put(DRONE_TYPE, AgentsUnitTypes.DRONE);
        agentConfigurationForUnitType.put(HATCHERY_TYPE, AgentsUnitTypes.HATCHERY);
        agentConfigurationForUnitType.put(LARVA_TYPE, AgentsUnitTypes.LARVA);
        agentConfigurationForUnitType.put(EGG_TYPE, AgentsUnitTypes.EGG);
        agentConfigurationForUnitType.put(SPAWNING_POOL_TYPE, AgentsUnitTypes.SPAWNING_POOL);
        agentConfigurationForUnitType.put(OVERLORD_TYPE, AgentsUnitTypes.OVERLORD);
        agentConfigurationForUnitType.put(ZERGLING_TYPE, AgentsUnitTypes.ZERGLING);
        agentConfigurationForUnitType.put(EXTRACTOR_TYPE, AgentsUnitTypes.EXTRACTOR);
        agentConfigurationForUnitType.put(LAIR_TYPE, AgentsUnitTypes.LAIR);
        agentConfigurationForUnitType.put(SPIRE_TYPE, AgentsUnitTypes.SPIRE);
        agentConfigurationForUnitType.put(EVOLUTION_CHAMBER_TYPE, AgentsUnitTypes.EVOLUTION_CHAMBER);
        agentConfigurationForUnitType.put(HYDRALISK_DEN_TYPE, AgentsUnitTypes.HYDRALISK_DEN);
        agentConfigurationForUnitType.put(SUNKEN_COLONY_TYPE, AgentsUnitTypes.SUNKEN_COLONY);
        agentConfigurationForUnitType.put(CREEP_COLONY_TYPE, AgentsUnitTypes.CREEP_COLONY);
        agentConfigurationForUnitType.put(SPORE_COLONY_TYPE, AgentsUnitTypes.SPORE_COLONY);
        agentConfigurationForUnitType.put(MUTALISK_TYPE, AgentsUnitTypes.MUTALISK);
        agentConfigurationForUnitType.put(HYDRALISK_TYPE, AgentsUnitTypes.HYDRALISK);
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
