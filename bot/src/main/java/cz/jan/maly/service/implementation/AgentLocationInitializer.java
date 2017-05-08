package cz.jan.maly.service.implementation;

import bwta.BaseLocation;
import cz.jan.maly.model.agent.AgentBaseLocation;
import cz.jan.maly.model.agent.types.AgentTypeBaseLocation;
import cz.jan.maly.model.bot.FactConverters;
import cz.jan.maly.model.game.wrappers.ABaseLocationWrapper;
import cz.jan.maly.model.game.wrappers.AUnitOfPlayer;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithSharedDesire;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.service.LocationInitializer;

import java.util.*;
import java.util.stream.Collectors;

import static cz.jan.maly.model.AgentsUnitTypes.HATCHERY;
import static cz.jan.maly.model.DesiresKeys.AM_I_BASE;
import static cz.jan.maly.model.DesiresKeys.MINE_MINERALS_IN_BASE;
import static cz.jan.maly.model.bot.FactConverters.COUNT_OF_MINERALS_ON_BASE;
import static cz.jan.maly.model.bot.FactConverters.HAS_HATCHERY_COUNT;
import static cz.jan.maly.model.bot.FactKeys.*;

/**
 * Strategy to initialize player
 * Created by Jan on 05-Apr-17.
 */
public class AgentLocationInitializer implements LocationInitializer {

    public static final AgentTypeBaseLocation BASE_LOCATION = AgentTypeBaseLocation.builder()
            .initializationStrategy(type -> {

                //todo remove is base from beliefs

                //Am I base - if it is not base, reason about being base - has any hatcheries?
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf amIBase = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();
                                Set<AUnitOfPlayer> hatcheries = memory.getReadOnlyMemoriesForAgentType(HATCHERY)
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().isPresent())
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().get().isOnSameCoordinates(base))
                                        .collect(Collectors.toSet());
                                memory.updateFactSetByFacts(HAS_BASE, hatcheries);
                                if (!hatcheries.isEmpty()) {
                                    memory.updateFact(IS_BASE, true);
                                }
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 0)
                                .beliefTypes(new HashSet<>(Collections.singletonList(FactConverters.IS_BASE)))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> dataForDecision.getFeatureValueBeliefSets(HAS_HATCHERY_COUNT) == 0)
                                .beliefSetTypes(new HashSet<>(Collections.singletonList(HAS_HATCHERY_COUNT)))
                                .build())
                        .build();
                type.addConfiguration(AM_I_BASE, amIBase);

                //Make request to start mining. Remove request when there are no more minerals to mine or there is no hatchery to bring mineral in
                ConfigurationWithSharedDesire mineMinerals = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MINE_MINERALS_IN_BASE)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 1
                                        && dataForDecision.getFeatureValueDesireBeliefSets(COUNT_OF_MINERALS_ON_BASE) > 0)
                                .beliefTypes(new HashSet<>(Collections.singletonList(FactConverters.IS_BASE)))
                                .parameterValueSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> dataForDecision.getFeatureValueBeliefs(FactConverters.IS_BASE) == 0
                                        || dataForDecision.getFeatureValueBeliefSets(COUNT_OF_MINERALS_ON_BASE) == 0
                                        || dataForDecision.getFeatureValueBeliefSets(COUNT_OF_MINERALS_ON_BASE) != dataForDecision.getFeatureValueDesireBeliefSets(COUNT_OF_MINERALS_ON_BASE)
                                )
                                .beliefTypes(new HashSet<>(Collections.singletonList(FactConverters.IS_BASE)))
                                .beliefSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                                .parameterValueSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                                .build()
                        )
                        .build();
                type.addConfiguration(MINE_MINERALS_IN_BASE, mineMinerals);

            })
            .usingTypesForFacts(new HashSet<>(Collections.singletonList(IS_BASE)))
            .usingTypesForFactSets(new HashSet<>(Collections.singletonList(HAS_BASE)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(AM_I_BASE)))
            .desiresForOthers(new HashSet<>(Collections.singletonList(MINE_MINERALS_IN_BASE)))
            .build();

    @Override
    public Optional<AgentBaseLocation> createAgent(BaseLocation baseLocation, BotFacade botFacade) {
        return Optional.of(new AgentBaseLocation(BASE_LOCATION, botFacade, baseLocation));
    }
}
