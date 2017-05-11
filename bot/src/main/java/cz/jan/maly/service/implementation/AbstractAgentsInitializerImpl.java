package cz.jan.maly.service.implementation;

import cz.jan.maly.model.Decider;
import cz.jan.maly.model.TypeWrapperStrategy;
import cz.jan.maly.model.agent.AbstractAgent;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.bot.DesireKeys;
import cz.jan.maly.model.bot.FactKeys;
import cz.jan.maly.model.features.FeatureContainerHeader;
import cz.jan.maly.model.game.wrappers.ABaseLocationWrapper;
import cz.jan.maly.model.game.wrappers.ATilePosition;
import cz.jan.maly.model.game.wrappers.AUnitOfPlayer;
import cz.jan.maly.model.game.wrappers.AUnitTypeWrapper;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireKeyID;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithAbstractPlan;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithSharedDesire;
import cz.jan.maly.model.metadata.containers.FactWithSetOfOptionalValuesForAgentType;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.service.AbstractAgentsInitializer;
import cz.jan.maly.utils.Util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.bot.FactConverters.COUNT_OF_POOLS;
import static cz.jan.maly.model.bot.FactKeys.IS_BASE_LOCATION;
import static cz.jan.maly.model.bot.FactKeys.PLACE_FOR_POOL;
import static cz.jan.maly.model.bot.FeatureContainerHeaders.*;

/**
 * Implementation of AbstractAgentsInitializer
 * Created by Jan on 09-May-17.
 */
public class AbstractAgentsInitializerImpl implements AbstractAgentsInitializer {

    @Override
    public List<AbstractAgent> initializeAbstractAgents(BotFacade botFacade) {
        List<AbstractAgent> abstractAgents = new ArrayList<>();
        abstractAgents.add(new AbstractAgent(ECO_MANAGER, botFacade));
        abstractAgents.add(new AbstractAgent(BUILDING_ORDER_MANAGER, botFacade));
        abstractAgents.add(new AbstractAgent(UNIT_ORDER_MANAGER, botFacade));
        return abstractAgents;
    }

    //todo
    private static final AgentType ECO_MANAGER = AgentType.builder()
            .agentTypeID(AgentTypes.ECO_MANAGER)
            .initializationStrategy(type -> {

                //todo - global desire
                ConfigurationWithSharedDesire buildWorker = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MORPH_TO_DRONE)
                        .counts(1)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> Decider.getDecision(AgentTypes.ECO_MANAGER, DesireKeys.BUILD_WORKER, dataForDecision, TRAINING_WORKER))
                                .globalBeliefTypesByAgentType(TRAINING_WORKER.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(TRAINING_WORKER.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !Decider.getDecision(AgentTypes.ECO_MANAGER, DesireKeys.BUILD_WORKER, dataForDecision, TRAINING_WORKER))
                                .globalBeliefTypesByAgentType(TRAINING_WORKER.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(TRAINING_WORKER.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .build())
                        .build();
                type.addConfiguration(BUILD_WORKER, buildWorker);

                //todo - global desire
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf expand = ConfigurationWithCommand.WithReasoningCommandDesiredBySelf
                        .builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                System.out.println("expand");
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> Decider.getDecision(AgentTypes.ECO_MANAGER, DesireKeys.EXPAND, dataForDecision, EXPANDING))
                                .globalBeliefTypesByAgentType(EXPANDING.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(EXPANDING.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !Decider.getDecision(AgentTypes.ECO_MANAGER, DesireKeys.EXPAND, dataForDecision, EXPANDING))
                                .globalBeliefTypesByAgentType(EXPANDING.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(EXPANDING.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .build())
                        .build();
                type.addConfiguration(EXPAND, expand);

            })
            .desiresForOthers(new HashSet<>(Collections.singleton(BUILD_WORKER)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singleton(EXPAND)))
            .build();

    //todo
    private static final AgentType UNIT_ORDER_MANAGER = AgentType.builder()
            .agentTypeID(AgentTypes.UNIT_ORDER_MANAGER)
            .initializationStrategy(type -> {

                //todo - global desire, make template method
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf expand = ConfigurationWithCommand.WithReasoningCommandDesiredBySelf
                        .builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                System.out.println("melee");
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> Decider.getDecision(AgentTypes.UNIT_ORDER_MANAGER, DesireKeys.BOOST_GROUND_MELEE, dataForDecision, BOOSTING_GROUND_MELEE))
                                .globalBeliefTypesByAgentType(BOOSTING_GROUND_MELEE.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(BOOSTING_GROUND_MELEE.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !Decider.getDecision(AgentTypes.UNIT_ORDER_MANAGER, DesireKeys.BOOST_GROUND_MELEE, dataForDecision, BOOSTING_GROUND_MELEE))
                                .globalBeliefTypesByAgentType(BOOSTING_GROUND_MELEE.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(BOOSTING_GROUND_MELEE.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .build())
                        .build();
                type.addConfiguration(BOOST_GROUND_MELEE, expand);

            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singleton(BOOST_GROUND_MELEE)))
            .build();

    //todo
    private static final AgentType BUILDING_ORDER_MANAGER = AgentType.builder()
            .agentTypeID(AgentTypes.BUILDING_ORDER_MANAGER)
            .usingTypesForFacts(new HashSet<>(Arrays.asList(PLACE_FOR_POOL)))
            .initializationStrategy(type -> {

                //pool
                initBuildPlan(type, ENABLE_GROUND_MELEE, FIND_PLACE_FOR_POOL, CHECK_PLACE_FOR_POOL, MORPH_TO_POOL,
                        PLACE_FOR_POOL, () -> AUnitTypeWrapper.SPAWNING_POOL_TYPE, DesireKeys.ENABLE_GROUND_MELEE, BUILDING_POOL,
                        COUNT_OF_POOLS);


            })
            .desiresWithAbstractIntention(new HashSet<>(Collections.singleton(ENABLE_GROUND_MELEE)))
            .build();

    /**
     * Template to create building tree
     *
     * @param type
     * @param buildDesire
     * @param findPlaceDesire
     * @param checkPlaceDesire
     * @param buildDesireToShare
     * @param factWithPosition
     * @param typeWrapperStrategy
     * @param desireKey
     * @param header
     * @param countConverter
     */
    private static void initBuildPlan(AgentType type, DesireKey buildDesire, DesireKey findPlaceDesire,
                                      DesireKey checkPlaceDesire, DesireKey buildDesireToShare,
                                      FactKey<ATilePosition> factWithPosition, TypeWrapperStrategy typeWrapperStrategy,
                                      DesireKeyID desireKey, FeatureContainerHeader header,
                                      FactWithSetOfOptionalValuesForAgentType<AUnitOfPlayer> countConverter) {

        //abstract plan for building pool
        ConfigurationWithAbstractPlan buildPool = ConfigurationWithAbstractPlan
                .builder()
                .reactionOnChangeStrategy(memory -> memory.eraseFactValueForGivenKey(factWithPosition))
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) ->
                                //check if this is only one commitment
                                memory.collectKeysOfCommittedDesiresInTreeCounts().getOrDefault(buildDesire, 0L) == 0
                                        && dataForDecision.getFeatureValueGlobalBeliefs(countConverter) == 0
                                        && Decider.getDecision(AgentTypes.BUILDING_ORDER_MANAGER, desireKey, dataForDecision, header))
                        .globalBeliefTypesByAgentType(Stream.concat(header.getConvertersForFactsForGlobalBeliefsByAgentType().stream(), Stream.of(countConverter)).collect(Collectors.toSet()))
                        .globalBeliefSetTypesByAgentType(header.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                        .useFactsInMemory(true)
                        .build())
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(countConverter) > 0
                                || !Decider.getDecision(AgentTypes.BUILDING_ORDER_MANAGER, desireKey, dataForDecision, header))
                        .globalBeliefTypesByAgentType(Stream.concat(header.getConvertersForFactsForGlobalBeliefsByAgentType().stream(), Stream.of(countConverter)).collect(Collectors.toSet()))
                        .globalBeliefSetTypesByAgentType(header.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                        .build())
                .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(findPlaceDesire, checkPlaceDesire)))
                .desiresForOthers(new HashSet<>(Collections.singleton(buildDesire)))
                .build();

        //register abstract plan - it can be initialized by self/other
        type.addConfiguration(buildDesire, buildPool, true);
        type.addConfiguration(buildDesire, buildPool, false);

        //find place
        ConfigurationWithCommand.WithReasoningCommandDesiredBySelf findPlace = ConfigurationWithCommand.WithReasoningCommandDesiredBySelf
                .builder()
                .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                    @Override
                    public boolean act(WorkingMemory memory) {
                        Set<ABaseLocationWrapper> locationWrapper = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(FactKeys.IS_BASE).orElse(false))
                                .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .collect(Collectors.toSet());
                        for (ABaseLocationWrapper aBaseLocationWrapper : locationWrapper) {
                            BotFacade.ADDITIONAL_OBSERVATIONS_PROCESSOR.requestObservation((mem, environment) -> {
                                Optional<ATilePosition> placeForBuilding = Util.getBuildTile(typeWrapperStrategy.returnType(), aBaseLocationWrapper.getTilePosition(), environment);
                                placeForBuilding.ifPresent(aTilePosition -> memory.updateFact(factWithPosition, aTilePosition));
                                return true;
                            }, memory, BUILDING_ORDER_MANAGER);
                            if (memory.returnFactValueForGivenKey(factWithPosition).isPresent()) {
                                break;
                            }
                        }
                        return true;
                    }
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(factWithPosition).isPresent())
                        .useFactsInMemory(true)
                        .build())
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> memory.returnFactValueForGivenKey(factWithPosition).isPresent())
                        .useFactsInMemory(true)
                        .build())
                .build();
        type.addConfiguration(findPlaceDesire, buildDesire, findPlace);

        //check place to build if it is taken
        ConfigurationWithCommand.WithReasoningCommandDesiredBySelf checkPlace = ConfigurationWithCommand.WithReasoningCommandDesiredBySelf
                .builder()
                .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                    @Override
                    public boolean act(WorkingMemory memory) {
                        if (!Util.canBuildingBeConstruct(memory.returnFactValueForGivenKey(factWithPosition).get())) {
                            memory.eraseFactValueForGivenKey(factWithPosition);
                        }
                        return true;
                    }
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> memory.returnFactValueForGivenKey(factWithPosition).isPresent())
                        .useFactsInMemory(true)
                        .build())
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> true)
                        .useFactsInMemory(true)
                        .build())
                .build();
        type.addConfiguration(checkPlaceDesire, buildDesire, checkPlace);

        //share desire
        ConfigurationWithSharedDesire buildCommand = ConfigurationWithSharedDesire.builder()
                .sharedDesireKey(buildDesireToShare)
                .counts(1)
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> memory.returnFactValueForGivenKey(factWithPosition).isPresent())
                        .useFactsInMemory(true)
                        .build())
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(factWithPosition).isPresent())
                        .useFactsInMemory(true)
                        .build())
                .build();
        type.addConfiguration(buildDesire, buildDesire, buildCommand);

    }

}
