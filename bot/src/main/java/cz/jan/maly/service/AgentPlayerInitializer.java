package cz.jan.maly.service;

import bwapi.Race;
import cz.jan.maly.model.agent.AgentPlayer;
import cz.jan.maly.model.agent.types.AgentTypePlayer;
import cz.jan.maly.model.game.wrappers.APlayer;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.service.implementation.BotFacade;

import java.util.Arrays;
import java.util.HashSet;

import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.FactsKeys.BASE_FOR_POOL;

/**
 * Strategy to initialize agent representing "player"
 * Created by Jan on 05-Apr-17.
 */
public class AgentPlayerInitializer implements PlayerInitializer {

    public static final AgentTypePlayer PLAYER = AgentTypePlayer.builder()
            .name("PLAYER")
            .initializationStrategy(type -> {
            })
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(new DesireKey[]{FIND_PLACE_TO_BUILD})))
            .desiresForOthers(new HashSet<>(Arrays.asList(new DesireKey[]{PLAN_BUILDING_POOL, MORPH_TO_ZERGLING, MORPH_TO_OVERLORD})))
            .usingTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{BASE_FOR_POOL})))
            .build();

    @Override
    public AgentPlayer createAgentForPlayer(APlayer player, BotFacade botFacade, Race enemyInitialRace) {
        return new AgentPlayer(PLAYER, botFacade, player, enemyInitialRace);
    }
}
