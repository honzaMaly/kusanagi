package cz.jan.maly.service;

import bwapi.Race;
import bwapi.TilePosition;
import bwta.BaseLocation;
import cz.jan.maly.model.agent.AgentPlayer;
import cz.jan.maly.model.agent.types.AgentTypePlayer;
import cz.jan.maly.model.game.wrappers.APlayer;
import cz.jan.maly.model.game.wrappers.ATilePosition;
import cz.jan.maly.model.game.wrappers.AUnit;
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

import static cz.jan.maly.model.AgentsUnitTypes.*;
import static cz.jan.maly.model.BasicFactsKeys.*;
import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.FactsKeys.BASE_FOR_POOL;
import static cz.jan.maly.model.FactsKeys.IS_BASE;
import static cz.jan.maly.service.AgentLocationInitializer.BASE_LOCATION;
import static cz.jan.maly.service.AgentUnitFactory.*;

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
                                            .map(AUnit::getNearestBaseLocation)
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
                        //go to find place little bit sooner
                        .decisionInDesire((desire, dataForDecision) -> desire.getReadOnlyMemoriesForAgentType(SPAWNING_POOL).isEmpty()
                                && desire.returnFactValueForGivenKey(BASE_FOR_POOL).isPresent()
                                && desire.returnFactValueForGivenKey(IS_PLAYER).get().getMinerals() >= SPAWNING_POOL_TYPE.getMineralPrice() - 32)
                        .decisionInIntention((intention, dataForDecision) -> !intention.getReadOnlyMemoriesForAgentType(SPAWNING_POOL).isEmpty())
                        .build();
                type.addConfiguration(PLAN_BUILDING_POOL, buildPool);

                //tell system to build zerglings
                ConfigurationWithSharedDesire buildZergling = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MORPH_TO_ZERGLING)
                        .decisionInDesire((desire, dataForDecision) -> !desire.getReadOnlyMemoriesForAgentType(SPAWNING_POOL).isEmpty()
                                && desire.getReadOnlyMemoriesForAgentType(SPAWNING_POOL).stream().anyMatch(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().isBeingConstructed())
                                && desire.returnFactValueForGivenKey(IS_PLAYER).get().getMinerals() >= ZERGLING_TYPE.getMineralPrice())
                        .decisionInIntention((intention, dataForDecision) -> false)
                        .build();
                type.addConfiguration(MORPH_TO_ZERGLING, buildZergling);

                //tell system to build one overlord
                ConfigurationWithSharedDesire buildOverlord = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MORPH_TO_OVERLORD)
                        .decisionInDesire((desire, dataForDecision) -> {
                            APlayer player = desire.returnFactValueForGivenKey(IS_PLAYER).get();
                            return player.getSupplyUsed() >= (player.getSupplyTotal() - 2)
                                    && player.getMinerals() >= OVERLORD_TYPE.getMineralPrice()
                                    && desire.getReadOnlyMemoriesForAgentType(EGG).stream()
                                    .noneMatch(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getTrainingQueue().contains(OVERLORD_TYPE));
                        })
                        .decisionInIntention((intention, dataForDecision) -> intention.getReadOnlyMemoriesForAgentType(EGG).stream()
                                .anyMatch(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getTrainingQueue().contains(OVERLORD_TYPE)))
                        .build();
                type.addConfiguration(MORPH_TO_OVERLORD, buildOverlord);

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
