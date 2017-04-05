package cz.jan.maly.service;

import bwta.BaseLocation;
import bwta.Region;
import cz.jan.maly.model.agent.AgentBaseLocation;
import cz.jan.maly.model.agent.AgentRegion;
import cz.jan.maly.model.agent.types.AgentTypeBaseLocation;
import cz.jan.maly.model.agent.types.AgentTypeRegion;
import cz.jan.maly.service.implementation.BotFacade;

import java.util.Optional;

/**
 * Strategy to initialize player
 * Created by Jan on 05-Apr-17.
 */
public class AgentLocationInitializer implements LocationInitializer {

    @Override
    public Optional<AgentBaseLocation> createAgent(BaseLocation baseLocation, BotFacade botFacade) {
        return Optional.of(new AgentBaseLocation(BASE_LOCATION, botFacade, baseLocation));
    }

    public static final AgentTypeBaseLocation BASE_LOCATION = AgentTypeBaseLocation.builder()
            .name("BASE_LOCATION")
            .skipTurnsToMakeObservation(50)
            .initializationStrategy(type -> {

                //todo

            })
            .build();

    @Override
    public Optional<AgentRegion> createAgent(Region region, BotFacade botFacade) {
        return Optional.of(new AgentRegion(REGION, botFacade, region));
    }

    public static final AgentTypeRegion REGION = AgentTypeRegion.builder()
            .name("REGION")
            .initializationStrategy(type -> {

                //todo

            })
            .build();
}
