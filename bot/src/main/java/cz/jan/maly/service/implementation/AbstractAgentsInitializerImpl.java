package cz.jan.maly.service.implementation;

import cz.jan.maly.model.Decider;
import cz.jan.maly.model.agent.AbstractAgent;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.bot.DesireKeys;
import cz.jan.maly.model.features.FeatureContainerHeader;
import cz.jan.maly.model.game.wrappers.ABaseLocationWrapper;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireKeyID;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithSharedDesire;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.service.AbstractAgentsInitializer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.bot.FactConverters.*;
import static cz.jan.maly.model.bot.FactKeys.*;
import static cz.jan.maly.model.bot.FactKeys.IS_BASE;
import static cz.jan.maly.model.bot.FactKeys.IS_ENEMY_BASE;
import static cz.jan.maly.model.bot.FactKeys.IS_ISLAND;
import static cz.jan.maly.model.bot.FactKeys.IS_MINERAL_ONLY;
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
            .usingTypesForFacts(new HashSet<>(Collections.singleton(BASE_TO_MOVE)))
            .initializationStrategy(type -> {

                //train drone
                ConfigurationWithSharedDesire trainDrone = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MORPH_TO_DRONE)
                        .counts(1)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> Decider.getDecision(AgentTypes.ECO_MANAGER, DesireKeys.BUILD_WORKER, dataForDecision, TRAINING_WORKER))
                                .globalBeliefTypesByAgentType(TRAINING_WORKER.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(TRAINING_WORKER.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .globalBeliefTypes(TRAINING_WORKER.getConvertersForFactsForGlobalBeliefs())
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !Decider.getDecision(AgentTypes.ECO_MANAGER, DesireKeys.BUILD_WORKER, dataForDecision, TRAINING_WORKER))
                                .globalBeliefTypesByAgentType(TRAINING_WORKER.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(TRAINING_WORKER.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .globalBeliefTypes(TRAINING_WORKER.getConvertersForFactsForGlobalBeliefs())
                                .build())
                        .build();
                type.addConfiguration(BUILD_WORKER, trainDrone);

                //morph to overlord
                ConfigurationWithSharedDesire trainOverlord = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MORPH_TO_OVERLORD)
                        .counts(1)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) ->
                                        dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_MORPHING_OVERLORDS) == 0
                                                && (dataForDecision.getFeatureValueGlobalBeliefs(CURRENT_POPULATION) >= dataForDecision.getFeatureValueGlobalBeliefs(MAX_POPULATION)
                                                || Decider.getDecision(AgentTypes.ECO_MANAGER, DesireKeys.INCREASE_CAPACITY, dataForDecision, INCREASING_CAPACITY)))
                                .globalBeliefTypesByAgentType(Stream.concat(INCREASING_CAPACITY.getConvertersForFactsForGlobalBeliefsByAgentType().stream(),
                                        Stream.of(CURRENT_POPULATION, MAX_POPULATION)).collect(Collectors.toSet()))
                                .globalBeliefSetTypesByAgentType(INCREASING_CAPACITY.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .globalBeliefTypes(Stream.concat(INCREASING_CAPACITY.getConvertersForFactsForGlobalBeliefs().stream(), Stream.of(COUNT_OF_MORPHING_OVERLORDS)).collect(Collectors.toSet()))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(CURRENT_POPULATION)
                                        < dataForDecision.getFeatureValueGlobalBeliefs(MAX_POPULATION))
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(CURRENT_POPULATION, MAX_POPULATION)))
                                .build())
                        .build();
                type.addConfiguration(INCREASE_CAPACITY, trainOverlord);

                //expand to another base
                ConfigurationWithSharedDesire expand = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(EXPAND)
                        .counts(1)
                        .reactionOnChangeStrategy((memory, desireParameters) -> {

                            //find nearest free base closest to any of our bases
                            Set<ABaseLocationWrapper> ourBases = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                    .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE).get())
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get())
                                    .collect(Collectors.toSet());

                            Optional<ABaseLocationWrapper> basesToExpand = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                    .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(IS_BASE).get()
                                            && !readOnlyMemory.returnFactValueForGivenKey(IS_ENEMY_BASE).get()
                                            && !readOnlyMemory.returnFactValueForGivenKey(IS_ISLAND).get())
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get())
                                    .min(Comparator.comparingDouble(value -> ourBases.stream()
                                            .mapToDouble(other -> other.distanceTo(value))
                                            .min().orElse(0)));

                            if (basesToExpand.isPresent()) {
                                memory.updateFact(BASE_TO_MOVE, basesToExpand.get());
                            } else {
                                memory.eraseFactValueForGivenKey(BASE_TO_MOVE);
                            }

                        })
                        .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.eraseFactValueForGivenKey(BASE_TO_MOVE))
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_HATCHERIES_IN_CONSTRUCTION) == 0
                                        && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_HATCHERIES_BEING_CONSTRUCT) == 0
                                        && (Decider.getDecision(AgentTypes.ECO_MANAGER, DesireKeys.EXPAND, dataForDecision, EXPANDING)
                                        || (dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS) > 0
                                        && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_HETCH) == 1 && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_MINERALS) >= 350)))
                                .globalBeliefTypes(Stream.concat(EXPANDING.getConvertersForFactsForGlobalBeliefs().stream(), Stream.of(COUNT_OF_HATCHERIES_IN_CONSTRUCTION)).collect(Collectors.toSet()))
                                .globalBeliefTypesByAgentType(Stream.concat(EXPANDING.getConvertersForFactsForGlobalBeliefsByAgentType().stream(), Stream.of(COUNT_OF_HATCHERIES_BEING_CONSTRUCT, COUNT_OF_POOLS, COUNT_OF_MINERALS, COUNT_OF_HETCH)).collect(Collectors.toSet()))
                                .globalBeliefSetTypesByAgentType(EXPANDING.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_HATCHERIES_IN_CONSTRUCTION) > 0
                                        || dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_HATCHERIES_BEING_CONSTRUCT) > 0
                                        || !memory.returnFactValueForGivenKey(BASE_TO_MOVE).isPresent())
                                .globalBeliefTypes(new HashSet<>(Collections.singletonList(COUNT_OF_HATCHERIES_IN_CONSTRUCTION)))
                                .globalBeliefTypesByAgentType(new HashSet<>(Collections.singletonList(COUNT_OF_HATCHERIES_BEING_CONSTRUCT)))
                                .useFactsInMemory(true)
                                .build())
                        .build();
                type.addConfiguration(EXPAND, expand);

                ConfigurationWithSharedDesire buildExtractor = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MORPH_TO_EXTRACTOR)
                        .counts(1)
                        .reactionOnChangeStrategy((memory, desireParameters) -> {

                            //extractors bases
                            Set<ABaseLocationWrapper> extractorsBases = memory.getReadOnlyMemoriesForAgentType(AgentTypes.EXTRACTOR)
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get())
                                    .map(aUnitWithCommands -> aUnitWithCommands.getNearestBaseLocation().get())
                                    .collect(Collectors.toSet());

                            //gas bases
                            Optional<ABaseLocationWrapper> ourBase = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                    .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE).get())
                                    .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(IS_MINERAL_ONLY).get())
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get())
                                    .filter(aBaseLocationWrapper -> !extractorsBases.contains(aBaseLocationWrapper))
                                    .findAny();

                            if (ourBase.isPresent()) {
                                memory.updateFact(BASE_TO_MOVE, ourBase.get());
                            } else {
                                memory.eraseFactValueForGivenKey(BASE_TO_MOVE);
                            }

                        })
                        .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.eraseFactValueForGivenKey(BASE_TO_MOVE))
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> Decider.getDecision(AgentTypes.ECO_MANAGER, DesireKeys.BUILD_EXTRACTOR, dataForDecision, BUILDING_EXTRACTOR))
                                .globalBeliefTypes(BUILDING_EXTRACTOR.getConvertersForFactsForGlobalBeliefs())
                                .globalBeliefTypesByAgentType(BUILDING_EXTRACTOR.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(BUILDING_EXTRACTOR.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(BASE_TO_MOVE).isPresent()
                                        || !Decider.getDecision(AgentTypes.ECO_MANAGER, DesireKeys.BUILD_EXTRACTOR, dataForDecision, BUILDING_EXTRACTOR))
                                .globalBeliefTypes(BUILDING_EXTRACTOR.getConvertersForFactsForGlobalBeliefs())
                                .globalBeliefTypesByAgentType(BUILDING_EXTRACTOR.getConvertersForFactsForGlobalBeliefsByAgentType())
                                .globalBeliefSetTypesByAgentType(BUILDING_EXTRACTOR.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .useFactsInMemory(true)
                                .build())
                        .build();
                type.addConfiguration(BUILD_EXTRACTOR, buildExtractor);

            })
            .desiresForOthers(new HashSet<>(Arrays.asList(BUILD_WORKER, INCREASE_CAPACITY, EXPAND, BUILD_EXTRACTOR)))
            .build();

    //todo
    private static final AgentType UNIT_ORDER_MANAGER = AgentType.builder()
            .agentTypeID(AgentTypes.UNIT_ORDER_MANAGER)
            .initializationStrategy(type -> {

                //build zerglings
                type.addConfiguration(BOOST_GROUND_MELEE, shareIntentionToTrainUnit(BOOST_GROUND_MELEE, DesireKeys.BOOST_GROUND_MELEE, BOOSTING_GROUND_MELEE));

            })
            .desiresForOthers(new HashSet<>(Collections.singleton(BOOST_GROUND_MELEE)))
            .build();

    private static ConfigurationWithSharedDesire shareIntentionToTrainUnit(DesireKey boostingTypeDesire, DesireKeyID boostingTypeDesireID,
                                                                           FeatureContainerHeader featureContainerHeader) {
        return ConfigurationWithSharedDesire.builder()
                .sharedDesireKey(boostingTypeDesire)
                .counts(1)
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> Decider.getDecision(AgentTypes.UNIT_ORDER_MANAGER, boostingTypeDesireID, dataForDecision, featureContainerHeader))
                        .globalBeliefTypesByAgentType(featureContainerHeader.getConvertersForFactsForGlobalBeliefsByAgentType())
                        .globalBeliefSetTypesByAgentType(featureContainerHeader.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                        .build())
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> !Decider.getDecision(AgentTypes.UNIT_ORDER_MANAGER, boostingTypeDesireID, dataForDecision, featureContainerHeader))
                        .globalBeliefTypesByAgentType(featureContainerHeader.getConvertersForFactsForGlobalBeliefsByAgentType())
                        .globalBeliefSetTypesByAgentType(featureContainerHeader.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                        .build())
                .build();
    }

    private static final AgentType BUILDING_ORDER_MANAGER = AgentType.builder()
            .agentTypeID(AgentTypes.BUILDING_ORDER_MANAGER)
            .initializationStrategy(type -> {

                ConfigurationWithSharedDesire buildPool = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MORPH_TO_POOL)
                        .counts(1)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) ->
                                        dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS) == 0
                                                && ((dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS_IN_CONSTRUCTION) == 0
                                                && Decider.getDecision(AgentTypes.BUILDING_ORDER_MANAGER, DesireKeys.ENABLE_GROUND_MELEE, dataForDecision, BUILDING_POOL))
                                                || dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_MINERALS) >= 250)
                                )
                                .globalBeliefTypesByAgentType(Stream.concat(BUILDING_POOL.getConvertersForFactsForGlobalBeliefsByAgentType().stream(),
                                        Stream.of(COUNT_OF_POOLS_IN_CONSTRUCTION, COUNT_OF_POOLS, COUNT_OF_MINERALS)).collect(Collectors.toSet()))
                                .globalBeliefSetTypesByAgentType(BUILDING_POOL.getConvertersForFactSetsForGlobalBeliefsByAgentType())
                                .globalBeliefTypes(BUILDING_POOL.getConvertersForFactsForGlobalBeliefs())
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS) > 0
                                        || dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS_IN_CONSTRUCTION) > 0)
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(COUNT_OF_POOLS_IN_CONSTRUCTION, COUNT_OF_POOLS)))
                                .build())
                        .build();
                type.addConfiguration(ENABLE_GROUND_MELEE, buildPool);

                //todo do other building + building prerequisites

            })
            .desiresForOthers(new HashSet<>(Collections.singleton(ENABLE_GROUND_MELEE)))
            .build();

}
