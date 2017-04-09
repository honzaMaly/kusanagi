package cz.jan.maly.service;

import bwapi.Race;
import bwapi.TilePosition;
import bwta.BaseLocation;
import cz.jan.maly.model.agent.AgentPlayer;
import cz.jan.maly.model.agent.types.AgentTypePlayer;
import cz.jan.maly.model.game.wrappers.APlayer;
import cz.jan.maly.model.game.wrappers.ATilePosition;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithSharedDesire;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.service.implementation.BotFacade;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static cz.jan.maly.model.AgentsUnitTypes.HATCHERY;
import static cz.jan.maly.model.AgentsUnitTypes.SPAWNING_POOL;
import static cz.jan.maly.model.BasicFactsKeys.*;
import static cz.jan.maly.model.DesiresKeys.FIND_PLACE_TO_BUILD;
import static cz.jan.maly.model.DesiresKeys.PLAN_BUILDING_POOL;
import static cz.jan.maly.model.FactsKeys.BASE_FOR_POOL;
import static cz.jan.maly.model.FactsKeys.IS_BASE;
import static cz.jan.maly.service.AgentLocationInitializer.BASE_LOCATION;
import static cz.jan.maly.service.AgentUnitFactory.SPAWNING_POOL_TYPE;

/**
 * Strategy to initialize agent representing "player"
 * Created by Jan on 05-Apr-17.
 */
public class AgentPlayerInitializer implements PlayerInitializer {

    public static final AgentTypePlayer PLAYER = AgentTypePlayer.builder()
            .name("PLAYER")
            .initializationStrategy(type -> {

                //find best place to build pool
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf baseForPool = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                Optional<BaseLocation> base = memory.getReadOnlyMemoriesForAgentType(BASE_LOCATION)
                                        .stream()
                                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE).get())
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .findAny();
                                base.ifPresent(baseLocation -> {
                                    Optional<TilePosition> tilePosition = memory.getReadOnlyMemoriesForAgentType(HATCHERY).stream()
                                            .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT))
                                            .filter(Optional::isPresent)
                                            .map(Optional::get)
                                            .map(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation())
                                            .filter(Optional::isPresent)
                                            .map(Optional::get)
                                            .filter(location -> location.getY() == baseLocation.getY() &&
                                                    location.getX() == baseLocation.getX())
                                            .map(BaseLocation::getTilePosition)
                                            .findFirst();
                                    tilePosition.ifPresent(tp -> memory.updateFact(BASE_FOR_POOL, new ATilePosition(tp)));
                                });
                                return true;
                            }
                        })
                        .decisionInDesire((desire, dataForDecision) -> !desire.returnFactValueForGivenKey(BASE_FOR_POOL).isPresent()
                                || desire.getReadOnlyMemoriesForAgentType(SPAWNING_POOL).isEmpty())
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();
                type.addConfiguration(FIND_PLACE_TO_BUILD, baseForPool);

                //tell system to build pool
                ConfigurationWithSharedDesire buildPool = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(PLAN_BUILDING_POOL)
                        .counts(1)
                        .decisionInDesire((desire, dataForDecision) -> desire.getReadOnlyMemoriesForAgentType(SPAWNING_POOL).isEmpty()
                                && desire.returnFactValueForGivenKey(BASE_FOR_POOL).isPresent()
                                && desire.returnFactValueForGivenKey(IS_PLAYER).get().getMinerals() >= SPAWNING_POOL_TYPE.getMineralPrice())
                        .decisionInIntention((intention, dataForDecision) -> !intention.getReadOnlyMemoriesForAgentType(SPAWNING_POOL).isEmpty())
                        .build();
                type.addConfiguration(PLAN_BUILDING_POOL, buildPool);
            })
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(new DesireKey[]{FIND_PLACE_TO_BUILD})))
            .desiresForOthers(new HashSet<>(Arrays.asList(new DesireKey[]{PLAN_BUILDING_POOL})))
            .usingTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{BASE_FOR_POOL})))
            .build();

    @Override
    public AgentPlayer createAgentForPlayer(APlayer player, BotFacade botFacade, Race enemyInitialRace) {
        return new AgentPlayer(PLAYER, botFacade, player, enemyInitialRace);
    }
}
