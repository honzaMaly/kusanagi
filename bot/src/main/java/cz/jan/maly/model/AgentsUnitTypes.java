package cz.jan.maly.model;

import bwapi.Order;
import bwapi.Position;
import bwapi.TilePosition;
import bwta.BWTA;
import cz.jan.maly.model.agent.types.AgentTypeUnit;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.bot.FactKeys;
import cz.jan.maly.model.game.wrappers.*;
import cz.jan.maly.model.knowledge.ReadOnlyMemory;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithAbstractPlan;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.service.implementation.BotFacade;
import cz.jan.maly.utils.Util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.agent.types.AgentTypeUnit.*;
import static cz.jan.maly.model.bot.FactConverters.*;
import static cz.jan.maly.model.bot.FactKeys.*;
import static cz.jan.maly.model.bot.FactKeys.IS_BASE;
import static cz.jan.maly.model.bot.FactKeys.IS_BEING_CONSTRUCT;
import static cz.jan.maly.model.bot.FactKeys.IS_ISLAND;
import static cz.jan.maly.model.bot.FactKeys.IS_START_LOCATION;
import static cz.jan.maly.model.bot.FactKeys.LAST_TIME_SCOUTED;
import static cz.jan.maly.model.game.wrappers.AUnitTypeWrapper.*;

/**
 * Created by Jan on 15-Mar-17.
 */
public class AgentsUnitTypes {
    private static final Random RANDOM = new Random();

    //BUILDINGS
    public static final AgentTypeUnit HATCHERY = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.HATCHERY)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);

                //upgrade to lair
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent upgradeToLair = ConfigurationWithCommand.
                        WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().morph(LAIR_TYPE);
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) ->
                                        dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_MINERALS) >= LAIR_TYPE.getMineralPrice()
                                                && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_GAS) >= LAIR_TYPE.getGasPrice()
                                                //is on start position or there is no base on start position
                                                && (memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getNearestBaseLocation().get().isStartLocation()
                                                || memory.getReadOnlyMemoriesForAgentType(AgentTypes.HATCHERY)
                                                .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getNearestBaseLocation().get())
                                                .noneMatch(ABaseLocationWrapper::isStartLocation))
                                )
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(COUNT_OF_MINERALS, COUNT_OF_GAS)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> false)
                                .build())
                        .build();
                type.addConfiguration(UPGRADE_TO_LAIR, upgradeToLair);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();

    public static final AgentTypeUnit SPAWNING_POOL = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.SPAWNING_POOL)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit EXTRACTOR = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.EXTRACTOR)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit LAIR = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.LAIR)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit SPIRE = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.SPIRE)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit EVOLUTION_CHAMBER = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.EVOLUTION_CHAMBER)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit HYDRALISK_DEN = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.HYDRALISK_DEN)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit SUNKEN_COLONY = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.SUNKEN_COLONY)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit CREEP_COLONY = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.CREEP_COLONY)
            .initializationStrategy(type -> {

                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);

                //upgrade to sunken
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent upgradeToSunken = ConfigurationWithCommand.
                        WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().morph(SUNKEN_COLONY_TYPE);
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {
                                            if (dataForDecision.madeDecisionToAny()) {
                                                return false;
                                            }
                                            AUnitOfPlayer me = memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get();
                                            if (!me.getNearestBaseLocation().isPresent()) {
                                                return false;
                                            }
                                            return dataForDecision.returnFactValueForGivenKey(BASE_TO_MOVE).get().equals(me.getNearestBaseLocation().orElse(null))
                                                    && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_MINERALS) >= SUNKEN_COLONY_TYPE.getMineralPrice()
                                                    && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_GAS) >= SUNKEN_COLONY_TYPE.getGasPrice();
                                        }
                                )
                                .desiresToConsider(new HashSet<>(Arrays.asList(MORPH_TO_SPORE_COLONY, MORPH_TO_SUNKEN_COLONY)))
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(COUNT_OF_MINERALS, COUNT_OF_GAS)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> false)
                                .build())
                        .build();
                type.addConfiguration(MORPH_TO_SUNKEN_COLONY, upgradeToSunken);

                //upgrade to spore colony
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent upgradeToSporeColony = ConfigurationWithCommand.
                        WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().morph(SPORE_COLONY_TYPE);
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {
                                            if (dataForDecision.madeDecisionToAny()) {
                                                return false;
                                            }
                                            AUnitOfPlayer me = memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get();
                                            if (!me.getNearestBaseLocation().isPresent()) {
                                                return false;
                                            }
                                            return dataForDecision.returnFactValueForGivenKey(BASE_TO_MOVE).get().equals(me.getNearestBaseLocation().orElse(null))
                                                    && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_MINERALS) >= SPORE_COLONY_TYPE.getMineralPrice()
                                                    && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_GAS) >= SPORE_COLONY_TYPE.getGasPrice();
                                        }
                                )
                                .desiresToConsider(new HashSet<>(Arrays.asList(MORPH_TO_SPORE_COLONY, MORPH_TO_SUNKEN_COLONY)))
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(COUNT_OF_MINERALS, COUNT_OF_GAS)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> false)
                                .build())
                        .build();
                type.addConfiguration(MORPH_TO_SPORE_COLONY, upgradeToSporeColony);

            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    public static final AgentTypeUnit SPORE_COLONY = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.SPORE_COLONY)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_BEING_CONSTRUCT)))
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(UPDATE_BELIEFS_ABOUT_CONSTRUCTION)))
            .build();
    //BUILDINGS

    //UNITS
    public static final AgentTypeUnit ZERGLING = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.ZERGLING)
            .initializationStrategy(type -> {
                initAttackPlan(type, HOLD_GROUND, false);
                initAttackPlan(type, DEFEND, false);
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);
            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(SURROUNDING_UNITS_AND_LOCATION)))
            .build();
    public static final AgentTypeUnit MUTALISK = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.MUTALISK)
            .initializationStrategy(type -> {
                initAttackPlan(type, HOLD_AIR, true);
                initAttackPlan(type, DEFEND, true);
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);
            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(SURROUNDING_UNITS_AND_LOCATION)))
            .build();
    public static final AgentTypeUnit HYDRALISK = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.HYDRALISK)
            .initializationStrategy(type -> {
                initAttackPlan(type, HOLD_GROUND, false);
                initAttackPlan(type, DEFEND, false);
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);
            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(SURROUNDING_UNITS_AND_LOCATION)))
            .build();

    //worker
    public static final AgentTypeUnit DRONE = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.DRONE)
            .usingTypesForFacts(new HashSet<>(Arrays.asList(MINING_MINERAL, MINERAL_TO_MINE, IS_MORPHING_TO,
                    IS_GATHERING_MINERALS, IS_GATHERING_GAS, BASE_TO_SCOUT_BY_WORKER, PLACE_TO_GO, PLACE_FOR_POOL,
                    PLACE_FOR_EXPANSION, BASE_TO_MOVE, PLACE_FOR_EXTRACTOR, MINING_IN_EXTRACTOR, BUILDING_LAST_CHECK, PLACE_FOR_SPIRE,
                    PLACE_FOR_HYDRALISK_DEN, PLACE_FOR_CREEP_COLONY, PLACE_FOR_EVOLUTION_CHAMBER, IDLE_SINCE, PLACE_TO_REACH,
                    HAS_ENOUGH_RESOURCES)))
            .initializationStrategy(type -> {

                //mine gas
                ConfigurationWithAbstractPlan mineGas = ConfigurationWithAbstractPlan.builder()
                        .reactionOnChangeStrategy((memory, desireParameters) -> memory.updateFact(MINING_IN_EXTRACTOR, desireParameters.returnFactSetValueForGivenKey(HAS_EXTRACTOR).get().findAny().get()))
                        .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.eraseFactValueForGivenKey(MINING_IN_EXTRACTOR))
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> (!dataForDecision.madeDecisionToAny()
                                        //committed agents
                                        && dataForDecision.getNumberOfCommittedAgents() < 3
                                        //is in base location
                                        && dataForDecision.returnFactSetValueForGivenKey(HAS_EXTRACTOR).get()
                                        .findAny().get().getNearestBaseLocation().get()
                                        .equals(memory.returnFactValueForGivenKey(IS_UNIT).get().getNearestBaseLocation().orElse(null))
                                ))
                                .useFactsInMemory(true)
                                .desiresToConsider(new HashSet<>(Arrays.asList(WORKER_SCOUT, GO_TO_BASE, MINE_MINERALS_IN_BASE,
                                        MINE_GAS_IN_BASE, BUILD)))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.madeDecisionToAny() || dataForDecision.getNumberOfCommittedAgents() > 3
                                )
                                .desiresToConsider(new HashSet<>(Arrays.asList(WORKER_SCOUT, GO_TO_BASE,
                                        MINE_MINERALS_IN_BASE, BUILD)))
                                .useFactsInMemory(true)
                                .build())
                        .desiresWithIntentionToAct(new HashSet<>(Collections.singleton(MINE_GAS)))
                        .build();
                type.addConfiguration(MINE_GAS_IN_BASE, mineGas, false);

                //send worker to mine gas
                ConfigurationWithCommand.WithActingCommandDesiredBySelf sendForGas = ConfigurationWithCommand.
                        WithActingCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().gather(intention.returnFactValueForGivenKey(MINING_IN_EXTRACTOR).get());
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(IS_MINING_GAS) == 0
                                        && dataForDecision.getFeatureValueBeliefs(IS_CARRYING_GAS) == 0
                                        && dataForDecision.getFeatureValueBeliefs(IS_WAITING_ON_GAS) == 0)
                                .beliefTypes(new HashSet<>(Arrays.asList(IS_MINING_GAS, IS_CARRYING_GAS, IS_WAITING_ON_GAS)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .build();
                type.addConfiguration(MINE_GAS, MINE_GAS_IN_BASE, sendForGas);

                //go scouting
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent goScouting = ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent.builder()
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> false)
                                .useFactsInMemory(true)
                                .build())
                        .reactionOnChangeStrategy((memory, desireParameters) -> {
                            Optional<ABaseLocationWrapper> baseToScout = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                    .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(LAST_TIME_SCOUTED).isPresent())
                                    .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(IS_ISLAND).get())
                                    .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_START_LOCATION).get())
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION))
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .findAny();
                            baseToScout.ifPresent(aBaseLocationWrapper -> memory.updateFact(BASE_TO_SCOUT_BY_WORKER, aBaseLocationWrapper));
                        })
                        .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.eraseFactValueForGivenKey(BASE_TO_SCOUT_BY_WORKER))
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {

                                //todo hack...
                                AUnitWithCommands me = intention.returnFactValueForGivenKey(IS_UNIT).get();
                                if (intention.returnFactValueForGivenKey(BASE_TO_SCOUT_BY_WORKER).get().distanceTo(me.getPosition()) < 10) {
                                    Optional<ABaseLocationWrapper> baseToScout = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                            .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(LAST_TIME_SCOUTED).isPresent())
                                            .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(IS_ISLAND).get())
                                            .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_START_LOCATION).get())
                                            .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION))
                                            .filter(Optional::isPresent)
                                            .map(Optional::get)
                                            .findAny();
                                    baseToScout.ifPresent(aBaseLocationWrapper -> memory.updateFact(BASE_TO_SCOUT_BY_WORKER, aBaseLocationWrapper));
                                }
                                return me.move(intention.returnFactValueForGivenKey(BASE_TO_SCOUT_BY_WORKER).get());
                            }
                        })
                        .build();
                type.addConfiguration(WORKER_SCOUT, goScouting);

                //reason about activities related to worker
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf workerConcerns = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                AUnitOfPlayer me = memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get();
                                memory.updateFact(IS_GATHERING_MINERALS, me.isCarryingMinerals() || me.isGatheringMinerals()
                                        || (me.getOrder().isPresent() && (me.getOrder().get().equals(Order.MiningMinerals)
                                        || me.getOrder().get().equals(Order.MoveToMinerals) || me.getOrder().get().equals(Order.WaitForMinerals)
                                        || me.getOrder().get().equals(Order.ReturnMinerals))));
                                memory.updateFact(IS_GATHERING_GAS, me.isCarryingGas() || me.isGatheringGas()
                                        || (me.getOrder().isPresent() && (me.getOrder().get().equals(Order.HarvestGas)
                                        || me.getOrder().get().equals(Order.MoveToGas) || me.getOrder().get().equals(Order.WaitForGas)
                                        || me.getOrder().get().equals(Order.ReturnGas))));

                                //is idle
                                if (me.isIdle()) {
                                    if (!memory.returnFactValueForGivenKey(IDLE_SINCE).isPresent()) {
                                        memory.updateFact(IDLE_SINCE, memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).orElse(0));
                                    }
                                } else {
                                    memory.eraseFactValueForGivenKey(IDLE_SINCE);
                                }

                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, workingMemory) -> true)
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, workingMemory) -> true)
                                .build())
                        .build();
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_WORKER_ACTIVITIES, workerConcerns);

                //reason about morphing
                type.addConfiguration(MORPHING_TO, beliefsAboutMorphing);

                //abstract plan to mine minerals in base
                ConfigurationWithAbstractPlan mineInBase = ConfigurationWithAbstractPlan.builder()
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {
                                    if (!dataForDecision.madeDecisionToAny()
                                            //is in same base
                                            && (dataForDecision.returnFactValueForGivenKey(IS_BASE_LOCATION).get().equals(memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getNearestBaseLocation().orElse(null))
                                            && dataForDecision.getNumberOfCommittedAgents() <= 2.5 * dataForDecision.getFeatureValueDesireBeliefSets(COUNT_OF_MINERALS_ON_BASE))) {
                                        return true;
                                    }
                                    return false;
                                })
                                .useFactsInMemory(true)
                                .parameterValueSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                                .desiresToConsider(new HashSet<>(Arrays.asList(WORKER_SCOUT, MINE_GAS_IN_BASE, BUILD, GO_TO_BASE)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {
                                    if (dataForDecision.madeDecisionToAny() ||
                                            dataForDecision.getNumberOfCommittedAgents() >= 2.5 * dataForDecision.getFeatureValueDesireBeliefSets(COUNT_OF_MINERALS_ON_BASE)
                                            || memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                            .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get().equals(memory.returnFactValueForGivenKey(IS_UNIT).get().getNearestBaseLocation().orElse(null)))
                                            .anyMatch(readOnlyMemory -> readOnlyMemory.returnFactSetValueForGivenKey(WORKER_MINING_GAS).orElse(Stream.empty()).count() == 0)) {
                                        return true;
                                    }
                                    return false;
                                })
                                .parameterValueSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                                .desiresToConsider(new HashSet<>(Arrays.asList(WORKER_SCOUT, GO_TO_BASE, MINE_GAS_IN_BASE,
                                        BUILD)))
                                .useFactsInMemory(true)
                                .build()
                        )
                        .desiresWithIntentionToAct(new HashSet<>(Collections.singletonList(MINE_MINERALS)))
                        .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(SELECT_MINERAL, UNSELECT_MINERAL)))
                        .build();
                type.addConfiguration(MINE_MINERALS_IN_BASE, mineInBase, false);

                //select closest mineral to mine from set of available
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf selectMineral = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                Set<AUnit> mineralsBeingMined = memory.getReadOnlyMemoriesForAgentType(DRONE)
                                        .filter(readOnlyMemory -> readOnlyMemory.getAgentId() != memory.getAgentId())
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MINING_MINERAL))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .collect(Collectors.toSet());
                                Set<AUnit> mineralsToMine = intention.returnFactSetValueOfParentIntentionForGivenKey(MINERAL).get()
                                        .filter(unit -> !mineralsBeingMined.contains(unit))
                                        .collect(Collectors.toSet());
                                if (!mineralsToMine.isEmpty()) {

                                    //select free nearest mineral to closest hatchery
                                    APosition myPosition = intention.returnFactValueForGivenKey(IS_UNIT).get().getPosition();
                                    Optional<AUnit> mineralToPick = mineralsToMine.stream()
                                            .min(Comparator.comparingDouble(o -> myPosition.distanceTo(o.getPosition())));
                                    mineralToPick.ifPresent(aUnit -> {
                                        memory.updateFact(MINERAL_TO_MINE, aUnit);

                                        //release currently mined mineral if it differs with mineral about to be mined
                                        if (intention.returnFactValueForGivenKey(MINING_MINERAL).isPresent()
                                                && !intention.returnFactValueForGivenKey(MINING_MINERAL).get().equals(aUnit)) {
                                            memory.updateFact(MINING_MINERAL, MINING_MINERAL.getInitValue());
                                        }
                                    });
                                }
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> (dataForDecision.getFeatureValueBeliefs(IS_MINING_MINERAL) == 0
                                        || dataForDecision.getFeatureValueBeliefs(IS_WAITING_ON_MINERAL) == 1)
                                        && dataForDecision.getFeatureValueBeliefs(IS_CARRYING_MINERAL) == 0
                                )
                                .beliefTypes(new HashSet<>(Arrays.asList(IS_MINING_MINERAL, IS_CARRYING_MINERAL, IS_WAITING_ON_MINERAL)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build()
                        )
                        .build();
                type.addConfiguration(SELECT_MINERAL, MINE_MINERALS_IN_BASE, selectMineral);

                //remove occupancy of mineral when carrying it
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf unselectMineral = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                memory.eraseFactValueForGivenKey(MINING_MINERAL);
                                memory.eraseFactValueForGivenKey(MINERAL_TO_MINE);
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(IS_MINING_MINERAL) == 1
                                        && dataForDecision.getFeatureValueBeliefs(IS_CARRYING_MINERAL) == 1)
                                .beliefTypes(new HashSet<>(Arrays.asList(IS_MINING_MINERAL, IS_CARRYING_MINERAL)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build()
                        )
                        .build();
                type.addConfiguration(UNSELECT_MINERAL, MINE_MINERALS_IN_BASE, unselectMineral);

                //go to mine it
                ConfigurationWithCommand.WithActingCommandDesiredBySelf mine = ConfigurationWithCommand.
                        WithActingCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                boolean hasStartedMining = intention.returnFactValueForGivenKey(IS_UNIT).get().gather(intention.returnFactValueForGivenKeyInDesireParameters(MINERAL_TO_MINE).get());
                                if (hasStartedMining) {
                                    memory.updateFact(MINING_MINERAL, intention.returnFactValueForGivenKey(MINERAL_TO_MINE).get());
                                }
                                return hasStartedMining;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueBeliefs(HAS_SELECTED_MINERAL_TO_MINE) != 0
                                        && !memory.returnFactValueForGivenKey(MINERAL_TO_MINE).get().equals(memory.returnFactValueForGivenKey(MINING_MINERAL).orElse(null))
                                        && dataForDecision.getFeatureValueBeliefs(IS_CARRYING_MINERAL) == 0)
                                .beliefTypes(new HashSet<>(Arrays.asList(IS_MINING_MINERAL, IS_CARRYING_MINERAL, HAS_SELECTED_MINERAL_TO_MINE)))
                                .useFactsInMemory(true)
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .build();
                type.addConfiguration(MINE_MINERALS, MINE_MINERALS_IN_BASE, mine);

                //morphing
                type.addConfiguration(MORPHING_TO, beliefsAboutMorphing);

                //surrounding units and location belief update
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);

                //go to nearest own base with minerals when you have nothing to do
                ConfigurationWithCommand.WithActingCommandDesiredBySelf goToNearestBase = ConfigurationWithCommand.
                        WithActingCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                if (intention.returnFactValueForGivenKey(PLACE_TO_GO).isPresent()) {
                                    AUnitWithCommands me = intention.returnFactValueForGivenKey(IS_UNIT).get();
                                    if (me.isCarryingGas() || me.isCarryingMinerals()) {
                                        me.returnCargo();
                                    } else {
                                        intention.returnFactValueForGivenKey(IS_UNIT).get().move(intention.returnFactValueForGivenKey(PLACE_TO_GO).get());
                                    }
                                }
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) ->
                                        {
                                            if (dataForDecision.madeDecisionToAny()) {
                                                return false;
                                            }

                                            AUnitOfPlayer me = memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get();

                                            //is in enemy base
                                            if (me.getNearestBaseLocation().isPresent()) {
                                                boolean isInEnemyBase = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                                        .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(FactKeys.IS_ENEMY_BASE).get())
                                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION))
                                                        .filter(Optional::isPresent)
                                                        .map(Optional::get)
                                                        .anyMatch(aBaseLocationWrapper -> aBaseLocationWrapper.equals(me.getNearestBaseLocation().get()));
                                                //is in small distance
                                                if (isInEnemyBase) {
                                                    return me.getPosition().distanceTo(me.getNearestBaseLocation().get()) < 10;
                                                }
                                            } else {
                                                return true;
                                            }

                                            //is idle for too long
                                            return memory.returnFactValueForGivenKey(IDLE_SINCE).isPresent()
                                                    && (memory.returnFactValueForGivenKey(IDLE_SINCE).get() + 15) < memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).get();
                                        }
                                )
                                .desiresToConsider(new HashSet<>(Arrays.asList(WORKER_SCOUT, EXPAND,
                                        MORPH_TO_POOL, MORPH_TO_SPIRE, MORPH_TO_HYDRALISK_DEN, MORPH_TO_EXTRACTOR,
                                        MORPH_TO_CREEP_COLONY, MORPH_TO_EVOLUTION_CHAMBER, MINE_MINERALS_IN_BASE,
                                        MINE_GAS_IN_BASE, GO_TO_BASE, BUILD)))
                                .build()
                        )
                        .reactionOnChangeStrategy((memory, desireParameters) -> {
                            AUnitOfPlayer me = memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get();
                            Optional<ABaseLocationWrapper> basesToGo = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                    .filter(readOnlyMemory -> readOnlyMemory.returnFactSetValueForGivenKey(HAS_BASE).orElse(Stream.empty()).anyMatch(aUnitOfPlayer -> !aUnitOfPlayer.isMorphing()
                                            && !aUnitOfPlayer.isBeingConstructed()))
                                    .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE).get())
                                    .filter(readOnlyMemory -> readOnlyMemory.collectKeysOfDesiresInTreeCounts().keySet().stream()
                                            .anyMatch(desireKey -> desireKey.equals(MINE_MINERALS_IN_BASE) || desireKey.equals(MINE_GAS_IN_BASE)))
                                    .filter(readOnlyMemory -> {
                                        long countOfMinerals = readOnlyMemory.returnFactSetValueForGivenKey(MINERAL).orElse(Stream.empty()).count();
                                        long extractors = readOnlyMemory.returnFactSetValueForGivenKey(HAS_EXTRACTOR).orElse(Stream.empty()).count();
                                        long workers = readOnlyMemory.returnFactSetValueForGivenKey(WORKER_ON_BASE).orElse(Stream.empty()).count();
                                        return ((countOfMinerals * 2.5) + (extractors * 3)) > workers;
                                    })
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION))
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .filter(aBaseLocationWrapper -> !aBaseLocationWrapper.equals(me.getNearestBaseLocation().orElse(null)))
                                    .min(Comparator.comparingDouble(value -> value.distanceTo(me.getPosition())));
                            if (basesToGo.isPresent()) {
                                memory.updateFact(PLACE_TO_GO, basesToGo.get().getPosition());
                            } else {
                                memory.updateFact(PLACE_TO_GO, me.getPosition());
                            }
                        })
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.madeDecisionToAny()
                                        || memory.returnFactValueForGivenKey(PLACE_TO_GO).get().distanceTo(memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getPosition()) < 1)
                                .desiresToConsider(new HashSet<>(Arrays.asList(WORKER_SCOUT, EXPAND,
                                        MORPH_TO_POOL, MORPH_TO_SPIRE, MORPH_TO_HYDRALISK_DEN, MORPH_TO_EXTRACTOR,
                                        MORPH_TO_CREEP_COLONY, MORPH_TO_EVOLUTION_CHAMBER, MINE_MINERALS_IN_BASE,
                                        MINE_GAS_IN_BASE, BUILD)))
                                .build())
                        .build();
                type.addConfiguration(GO_TO_BASE, goToNearestBase);

                //build pool
                initAbstractBuildingPlan(type, AUnitTypeWrapper.SPAWNING_POOL_TYPE, PLACE_FOR_POOL,
                        MORPH_TO_POOL, FIND_PLACE_FOR_POOL, new HashSet<>(Arrays.asList(WORKER_SCOUT, EXPAND,
                                MORPH_TO_EXTRACTOR, MORPH_TO_SPIRE, MORPH_TO_HYDRALISK_DEN, MORPH_TO_CREEP_COLONY, MORPH_TO_EVOLUTION_CHAMBER)));

                //build spire
                initAbstractBuildingPlan(type, AUnitTypeWrapper.SPIRE_TYPE, PLACE_FOR_SPIRE, MORPH_TO_SPIRE,
                        FIND_PLACE_FOR_SPIRE, new HashSet<>(Arrays.asList(WORKER_SCOUT, EXPAND,
                                MORPH_TO_EXTRACTOR, MORPH_TO_POOL, MORPH_TO_HYDRALISK_DEN, MORPH_TO_CREEP_COLONY, MORPH_TO_EVOLUTION_CHAMBER)));

                //build hydralisk den
                initAbstractBuildingPlan(type, AUnitTypeWrapper.HYDRALISK_DEN_TYPE, PLACE_FOR_HYDRALISK_DEN,
                        MORPH_TO_HYDRALISK_DEN, FIND_PLACE_FOR_HYDRALISK_DEN, new HashSet<>(Arrays.asList(WORKER_SCOUT, EXPAND,
                                MORPH_TO_EXTRACTOR, MORPH_TO_POOL, MORPH_TO_SPIRE, MORPH_TO_CREEP_COLONY, MORPH_TO_EVOLUTION_CHAMBER)));

                //build expansion
                initAbstractBuildingPlan(type, AUnitTypeWrapper.HATCHERY_TYPE, PLACE_FOR_EXPANSION, EXPAND,
                        FIND_PLACE_FOR_HATCHERY, new HashSet<>(Arrays.asList(WORKER_SCOUT, MORPH_TO_POOL,
                                MORPH_TO_EXTRACTOR, MORPH_TO_SPIRE, MORPH_TO_HYDRALISK_DEN, MORPH_TO_CREEP_COLONY, MORPH_TO_EVOLUTION_CHAMBER)));

                //build extractor
                initAbstractBuildingPlan(type, AUnitTypeWrapper.EXTRACTOR_TYPE, PLACE_FOR_EXTRACTOR, MORPH_TO_EXTRACTOR,
                        FIND_PLACE_FOR_EXTRACTOR, new HashSet<>(Arrays.asList(WORKER_SCOUT, EXPAND,
                                MORPH_TO_POOL, MORPH_TO_SPIRE, MORPH_TO_HYDRALISK_DEN, MORPH_TO_CREEP_COLONY, MORPH_TO_EVOLUTION_CHAMBER)));

                //build creep colony
                initAbstractBuildingPlan(type, AUnitTypeWrapper.CREEP_COLONY_TYPE, PLACE_FOR_CREEP_COLONY,
                        MORPH_TO_CREEP_COLONY, FIND_PLACE_FOR_CREEP_COLONY, new HashSet<>(Arrays.asList(WORKER_SCOUT, EXPAND,
                                MORPH_TO_POOL, MORPH_TO_SPIRE, MORPH_TO_HYDRALISK_DEN, MORPH_TO_EXTRACTOR, MORPH_TO_EVOLUTION_CHAMBER)));

//                //build evolution chamber
//                initAbstractBuildingPlan(type, AUnitTypeWrapper.EVOLUTION_CHAMBER_TYPE, PLACE_FOR_EVOLUTION_CHAMBER,
//                        MORPH_TO_EVOLUTION_CHAMBER, FIND_PLACE_FOR_EVOLUTION_CHAMBER, new HashSet<>(Arrays.asList(WORKER_SCOUT, EXPAND,
//                                MORPH_TO_POOL, MORPH_TO_SPIRE, MORPH_TO_HYDRALISK_DEN, MORPH_TO_EXTRACTOR, MORPH_TO_CREEP_COLONY)));

            })
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(SURROUNDING_UNITS_AND_LOCATION,
                    UPDATE_BELIEFS_ABOUT_WORKER_ACTIVITIES, MORPHING_TO)))
            .desiresWithIntentionToAct(new HashSet<>(Arrays.asList(GO_TO_BASE)))
            .build();

    /**
     * Init building plan
     *
     * @param type
     * @param typeOfBuilding
     * @param placeForBuilding
     * @param reactOn
     * @param findPlace
     */
    private static void initAbstractBuildingPlan(AgentType type, AUnitTypeWrapper typeOfBuilding,
                                                 FactKey<ATilePosition> placeForBuilding, DesireKey reactOn,
                                                 DesireKey findPlace, Set<DesireKey> desiresToConsider) {

        //abstract plan for building
        ConfigurationWithAbstractPlan buildPlan = ConfigurationWithAbstractPlan
                .builder()
                .reactionOnChangeStrategy((memory, desireParameters) -> {
                    memory.updateFact(BASE_TO_MOVE, desireParameters.returnFactValueForGivenKey(BASE_TO_MOVE).get());
                    memory.updateFact(BUILDING_LAST_CHECK, memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).get());
                })
                .reactionOnChangeStrategyInIntention((memory, desireParameters) -> {
                    memory.eraseFactValueForGivenKey(placeForBuilding);
                    memory.eraseFactValueForGivenKey(BASE_TO_MOVE);
                    memory.eraseFactValueForGivenKey(BUILDING_LAST_CHECK);
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) ->
                                !dataForDecision.madeDecisionToAny()
                                        && dataForDecision.returnFactValueForGivenKey(BASE_TO_MOVE).isPresent()
                                        //is idle or no one is idle
                                        && (memory.returnFactValueForGivenKey(IS_UNIT).get().isIdle() || dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_IDLE_DRONES) == 0)
                                        //is on location or no one is on location
                                        && (dataForDecision.returnFactValueForGivenKey(BASE_TO_MOVE).get().equals(memory.returnFactValueForGivenKey(IS_UNIT).get().getNearestBaseLocation().orElse(null))
                                        || memory.getReadOnlyMemoriesForAgentType(DRONE)
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getNearestBaseLocation())
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .noneMatch(aBaseLocationWrapper -> aBaseLocationWrapper.equals(dataForDecision.returnFactValueForGivenKey(BASE_TO_MOVE).get())))
                                        //resources
                                        && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_MINERALS) >= (typeOfBuilding.getMineralPrice() - 0.2 * typeOfBuilding.getMineralPrice())
                                        && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_GAS) >= (typeOfBuilding.getGasPrice() - 0.2 * typeOfBuilding.getGasPrice())
                        )
                        .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(COUNT_OF_MINERALS, COUNT_OF_GAS, COUNT_OF_IDLE_DRONES)))
                        .desiresToConsider(Stream.concat(desiresToConsider.stream(), Stream.of(reactOn)).collect(Collectors.toSet()))
                        .build())
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) ->
                                        dataForDecision.madeDecisionToAny()
//                                        || (dataForDecision.getFeatureValueBeliefs(IS_CONSTRUCTING_BUILDING) != 0
                                                && !memory.returnFactValueForGivenKey(placeForBuilding).isPresent()
                                                //wait for minerals
                                                && (dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_MINERALS) > typeOfBuilding.getMineralPrice()
                                                && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_GAS) > typeOfBuilding.getGasPrice()
                                                && dataForDecision.getFeatureValueBeliefs(MADE_BUILDING_LAST_CHECK) + 300 < dataForDecision.getFeatureValueBeliefs(LAST_OBSERVATION))
                        )
                        .beliefTypes(new HashSet<>(Arrays.asList(MADE_BUILDING_LAST_CHECK, LAST_OBSERVATION, IS_CONSTRUCTING_BUILDING)))
                        .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(COUNT_OF_MINERALS, COUNT_OF_GAS, COUNT_OF_IDLE_DRONES)))
                        .desiresToConsider(desiresToConsider)
                        .build())
                .desiresWithIntentionToAct(new HashSet<>(Arrays.asList(BUILD, GO_TO_BASE, findPlace)))
                .desiresWithIntentionToReason(new HashSet<>(Collections.singleton(REASON_ABOUT_RESOURCES)))
                .build();
        type.addConfiguration(reactOn, buildPlan, false);

        //has enough resources
        ConfigurationWithCommand.WithReasoningCommandDesiredBySelf hasEnoughResources = ConfigurationWithCommand.
                WithReasoningCommandDesiredBySelf.builder()
                .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                    @Override
                    public boolean act(WorkingMemory memory) {
                        return true;
                    }
                })
                .reactionOnChangeStrategy((memory, desireParameters) -> memory.updateFact(HAS_ENOUGH_RESOURCES, true))
                .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.eraseFactValueForGivenKey(HAS_ENOUGH_RESOURCES))
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) ->
                                dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_MINERALS) >= (typeOfBuilding.getMineralPrice() - 0.2 * typeOfBuilding.getMineralPrice())
                                        && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_GAS) >= (typeOfBuilding.getGasPrice() - 0.2 * typeOfBuilding.getGasPrice())
                        )
                        .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(COUNT_OF_MINERALS, COUNT_OF_GAS)))
                        .build()
                )
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_MINERALS) < (typeOfBuilding.getMineralPrice() - 0.1 * typeOfBuilding.getMineralPrice())
                                || dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_GAS) < (typeOfBuilding.getGasPrice() - 0.1 * typeOfBuilding.getGasPrice()))
                        .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(COUNT_OF_MINERALS, COUNT_OF_GAS)))
                        .build()
                )
                .build();
        type.addConfiguration(REASON_ABOUT_RESOURCES, reactOn, hasEnoughResources);

        //move to location
        ConfigurationWithCommand.WithActingCommandDesiredBySelf moveToBase = ConfigurationWithCommand.
                WithActingCommandDesiredBySelf.builder()
                .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                    @Override
                    public boolean act(WorkingMemory memory) {
                        memory.updateFact(BUILDING_LAST_CHECK, memory.returnFactValueForGivenKey(MADE_OBSERVATION_IN_FRAME).get());

                        AUnitWithCommands me = intention.returnFactValueForGivenKey(IS_UNIT).get();
                        APosition destination = intention.returnFactValueForGivenKey(BASE_TO_MOVE).get().getPosition();
                        List<TilePosition> path = BWTA.getShortestPath(me.getPosition().getATilePosition().getWrappedPosition(), destination.getATilePosition().getWrappedPosition());
                        if (!path.isEmpty()){
                            me.move(ATilePosition.wrap(path.get(path.size()/2)));
                        } else {
                            me.move(destination);
                        }
                        return true;
                    }
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) ->
                                !memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().isCarryingMinerals()
                                        && !memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().isCarryingGas()
                                        && memory.returnFactValueForGivenKey(HAS_ENOUGH_RESOURCES).get()
                                        && !memory.returnFactValueForGivenKey(BASE_TO_MOVE).get().equals(memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getNearestBaseLocation().orElse(null)))
                        .useFactsInMemory(true)
                        .build()
                )
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) ->
                                !memory.returnFactValueForGivenKey(HAS_ENOUGH_RESOURCES).get() ||
                                        memory.returnFactValueForGivenKey(BASE_TO_MOVE).get().equals(memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getNearestBaseLocation().orElse(null)))
                        .build())
                .build();
        type.addConfiguration(GO_TO_BASE, reactOn, moveToBase);

        //find place near the building
        ConfigurationWithCommand.WithActingCommandDesiredBySelf findPlaceReasoning = ConfigurationWithCommand.WithActingCommandDesiredBySelf
                .builder()
                .reactionOnChangeStrategy((memory, desireParameters) -> {
                    AUnitWithCommands me = memory.returnFactValueForGivenKey(IS_UNIT).get();
                    if (me.getNearestBaseLocation().isPresent()) {
                        BotFacade.ADDITIONAL_OBSERVATIONS_PROCESSOR.requestObservation((mem, environment) -> {
                            Optional<ATilePosition> place = Util.getBuildTile(typeOfBuilding, me.getNearestBaseLocation().get().getTilePosition(), me, environment);

                            //keep unoccupied place
                            if (place.isPresent()) {
                                memory.updateFact(placeForBuilding, place.get());
                            } else {
                                memory.eraseFactValueForGivenKey(placeForBuilding);
                            }
                            return true;
                        }, memory, DRONE);
                    }
                })
                .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                    @Override
                    public boolean act(WorkingMemory memory) {
                        AUnitWithCommands me = intention.returnFactValueForGivenKey(IS_UNIT).get();
                        if (me.getNearestBaseLocation().isPresent()) {

                            //move somewhere else in location only if place to build is not present
                            if (!memory.returnFactValueForGivenKey(placeForBuilding).isPresent()) {

                                Optional<APosition> aPlaceToGo = memory.returnFactValueForGivenKey(PLACE_TO_GO);
                                if (!aPlaceToGo.isPresent() || me.getPosition().distanceTo(aPlaceToGo.get()) < 2) {
                                    Optional<ReadOnlyMemory> memoryOptional = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                            .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get().equals(me.getNearestBaseLocation().orElse(null)))
                                            .findAny();
                                    if (memoryOptional.isPresent()) {
                                        List<AUnit> unitsOnLocation = memoryOptional.get().returnFactSetValueForGivenKey(OWN_BUILDING)
                                                .orElse(Stream.empty())
                                                .collect(Collectors.toList());
                                        if (unitsOnLocation.isEmpty()) {
                                            memory.updateFact(PLACE_TO_GO, intention.returnFactValueForGivenKey(BASE_TO_MOVE).get().getPosition());
                                        } else {
                                            memory.updateFact(PLACE_TO_GO, unitsOnLocation.get(RANDOM.nextInt(unitsOnLocation.size())).getPosition());
                                        }
                                    } else {
                                        memory.updateFact(PLACE_TO_GO, intention.returnFactValueForGivenKey(BASE_TO_MOVE).get().getPosition());
                                    }
                                }
                                me.move(memory.returnFactValueForGivenKey(PLACE_TO_GO).get());
                            }
                        }
                        return true;
                    }
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> !dataForDecision.madeDecisionToAny()
                                && memory.returnFactValueForGivenKey(BASE_TO_MOVE).get().equals(memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getNearestBaseLocation().orElse(null)))
                        .desiresToConsider(new HashSet<>(Collections.singleton(GO_TO_BASE)))
                        .build())
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> true)
                        .build())
                .build();
        type.addConfiguration(findPlace, reactOn, findPlaceReasoning);

        //morph to building
        ConfigurationWithCommand.WithActingCommandDesiredBySelf build = ConfigurationWithCommand.
                WithActingCommandDesiredBySelf.builder()
                .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                    @Override
                    public boolean act(WorkingMemory memory) {
                        if (intention.returnFactValueForGivenKey(placeForBuilding).isPresent()) {
                            intention.returnFactValueForGivenKey(IS_UNIT).get().build(typeOfBuilding, intention.returnFactValueForGivenKey(placeForBuilding).get());
                        }
                        return true;
                    }
                })
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> memory.returnFactValueForGivenKey(placeForBuilding).isPresent()
                                && memory.returnFactValueForGivenKey(HAS_ENOUGH_RESOURCES).get())
                        .build()
                )
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(placeForBuilding).isPresent()
                                || !memory.returnFactValueForGivenKey(HAS_ENOUGH_RESOURCES).get()
                        )
                        .build())
                .build();
        type.addConfiguration(BUILD, reactOn, build);
    }

    //population and scout
    public static final AgentTypeUnit OVERLORD = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.OVERLORD)
            .usingTypesForFacts(new HashSet<>(Arrays.asList(IS_MORPHING_TO, BASE_TO_MOVE)))
            .initializationStrategy(type -> {
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);

                //scouting - move to base scouted for last time. at start prefer unvisited base locations
                ConfigurationWithAbstractPlan goScouting = ConfigurationWithAbstractPlan.builder()
                        .reactionOnChangeStrategy((memory, desireParameters) -> memory.updateFact(PLACE_TO_REACH, desireParameters.returnFactValueForGivenKey(IS_BASE_LOCATION).get().getPosition()))
                        .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.eraseFactValueForGivenKey(PLACE_TO_REACH))
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueMadeCommitmentToType(VISIT) != 1.0
                                        && dataForDecision.getNumberOfCommittedAgents() <= 2
                                )
                                .desiresToConsider(new HashSet<>(Collections.singletonList(VISIT)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getNumberOfCommittedAgents() > 2)
                                .build())
                        .desiresWithIntentionToAct(new HashSet<>(Arrays.asList(MOVE_AWAY_FROM_DANGER, MOVE_TO_POSITION)))
                        .build();
                type.addConfiguration(VISIT, goScouting, false);

                //return to base
                ConfigurationWithCommand.WithActingCommandDesiredBySelf returnToBase = ConfigurationWithCommand.WithActingCommandDesiredBySelf.builder()
                        .reactionOnChangeStrategy((memory, desireParameters) -> {
                            AUnitOfPlayer unitOfPlayer = memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get();
                            Optional<ABaseLocationWrapper> baseToReturnTo = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                    .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE).get())
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get())
                                    .min(Comparator.comparingDouble(value -> value.distanceTo(unitOfPlayer.getPosition())));
                            baseToReturnTo.ifPresent(aBaseLocationWrapper -> memory.updateFact(BASE_TO_MOVE, aBaseLocationWrapper));
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !dataForDecision.madeDecisionToAny())
                                .desiresToConsider(new HashSet<>(Collections.singletonList(VISIT)))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.madeDecisionToAny())
                                .desiresToConsider(new HashSet<>(Collections.singletonList(VISIT)))
                                .build())
                        .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return !memory.returnFactValueForGivenKey(BASE_TO_MOVE).isPresent() || memory.returnFactValueForGivenKey(IS_UNIT).get().move(memory.returnFactValueForGivenKey(BASE_TO_MOVE).get());
                            }
                        })
                        .build();
                type.addConfiguration(GO_TO_BASE, returnToBase);

                //if is in danger - select the closest anti-air (or other if missing) unit and move away from it
                ConfigurationWithCommand.WithActingCommandDesiredBySelf flee = ConfigurationWithCommand.WithActingCommandDesiredBySelf.builder()
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> memory.returnFactValueForGivenKey(IS_UNIT).get().isUnderAttack())
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                AUnitWithCommands me = intention.returnFactValueForGivenKey(IS_UNIT).get();
                                Optional<AUnit.Enemy> enemyAntiAir = me.getEnemyUnitsInRadiusOfSight().stream()
                                        .filter(enemy -> enemy.getType().canAttackAirUnits())
                                        .min(Comparator.comparingDouble(value -> value.getPosition().distanceTo(me.getPosition())));
                                if (enemyAntiAir.isPresent()) {
                                    return me.move(positionToMove(me.getPosition(), enemyAntiAir.get().getPosition()));
                                } else {
                                    Optional<AUnit.Enemy> enemy = me.getEnemyUnitsInRadiusOfSight().stream()
                                            .min(Comparator.comparingDouble(value -> value.getPosition().distanceTo(me.getPosition())));
                                    if (enemy.isPresent()) {
                                        return me.move(positionToMove(me.getPosition(), enemy.get().getPosition()));
                                    }
                                }
                                return true;
                            }
                        })
                        .build();
                type.addConfiguration(MOVE_AWAY_FROM_DANGER, VISIT, flee);

                //if is not in danger - continue to position
                ConfigurationWithCommand.WithActingCommandDesiredBySelf moveOnPosition = ConfigurationWithCommand.WithActingCommandDesiredBySelf.builder()
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !memory.returnFactValueForGivenKey(IS_UNIT).get().isUnderAttack()
                                        && memory.returnFactValueForGivenKey(IS_UNIT).get().getHPPercent() > 0.5
                                )
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return memory.returnFactValueForGivenKey(IS_UNIT).get().move(memory.returnFactValueForGivenKey(PLACE_TO_REACH).get());
                            }
                        })
                        .build();
                type.addConfiguration(MOVE_TO_POSITION, VISIT, moveOnPosition);

                //reason about morphing
                type.addConfiguration(MORPHING_TO, beliefsAboutMorphing);
            })
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(SURROUNDING_UNITS_AND_LOCATION, MORPHING_TO)))
            .desiresWithIntentionToAct(new HashSet<>(Collections.singleton(GO_TO_BASE)))
            .build();

    private static APosition positionToMove(APosition myPosition, APosition dangerPosition) {
        int difX = (myPosition.getX() - dangerPosition.getY()) * 4, difY = (myPosition.getY() - dangerPosition.getY()) * 4;
        if (difX > 0) {
            if (difY > 0) {
                return APosition.wrap(new Position(myPosition.getX() - difX, myPosition.getY() - difY));
            } else {
                return APosition.wrap(new Position(myPosition.getX() - difX, myPosition.getY() + difY));
            }
        } else {
            if (difY > 0) {
                return APosition.wrap(new Position(myPosition.getX() + difX, myPosition.getY() - difY));
            } else {
                return APosition.wrap(new Position(myPosition.getX() + difX, myPosition.getY() + difY));
            }
        }
    }

    private static void initAttackPlan(AgentType type, DesireKey desireKey, boolean isScaredOfAntiAir) {

        //attack
        ConfigurationWithAbstractPlan attackPlan = ConfigurationWithAbstractPlan.builder()
                .reactionOnChangeStrategy((memory, desireParameters) -> {
                    memory.updateFact(PLACE_TO_REACH, desireParameters.returnFactValueForGivenKey(IS_BASE_LOCATION).get().getPosition());
                })
                .reactionOnChangeStrategyInIntention((memory, desireParameters) -> memory.eraseFactValueForGivenKey(PLACE_TO_REACH))
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> !dataForDecision.madeDecisionToAny())
                        .desiresToConsider(new HashSet<>(Arrays.asList(desireKey, DEFEND)))
                        .build()
                )
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> false)
                        .build()
                )
                .desiresWithIntentionToAct(new HashSet<>(Arrays.asList(MOVE_AWAY_FROM_DANGER, MOVE_TO_POSITION, ATTACK)))
                .build();
        type.addConfiguration(desireKey, attackPlan, false);

        //if is in danger - select the closest unit (based on type) and move away from it
        ConfigurationWithCommand.WithActingCommandDesiredBySelf flee = ConfigurationWithCommand.WithActingCommandDesiredBySelf.builder()
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> {
                            AUnitOfPlayer me = memory.returnFactValueForGivenKey(IS_UNIT).get();
                            return me.isUnderAttack() && me.getHPPercent() < 0.4;
                        })
                        .build()
                )
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> true)
                        .build())
                .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                    @Override
                    public boolean act(WorkingMemory memory) {
                        AUnitWithCommands me = intention.returnFactValueForGivenKey(IS_UNIT).get();
                        Optional<AUnit.Enemy> enemyToFleeFrom = me.getEnemyUnitsInRadiusOfSight().stream()
                                .filter(enemy -> isScaredOfAntiAir ? enemy.getType().canAttackAirUnits() : enemy.getType().canAttackGroundUnits())
                                .min(Comparator.comparingDouble(value -> value.getPosition().distanceTo(me.getPosition())));
                        if (enemyToFleeFrom.isPresent()) {
                            return me.move(positionToMove(me.getPosition(), enemyToFleeFrom.get().getPosition()));
                        } else {
                            Optional<AUnit.Enemy> enemy = me.getEnemyUnitsInRadiusOfSight().stream()
                                    .min(Comparator.comparingDouble(value -> value.getPosition().distanceTo(me.getPosition())));
                            if (enemy.isPresent()) {
                                return me.move(positionToMove(me.getPosition(), enemy.get().getPosition()));
                            }
                        }
                        return true;
                    }
                })
                .build();
        type.addConfiguration(MOVE_AWAY_FROM_DANGER, desireKey, flee);

        //if is not in danger - continue to position
        ConfigurationWithCommand.WithActingCommandDesiredBySelf moveOnPosition = ConfigurationWithCommand.WithActingCommandDesiredBySelf.builder()
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> !dataForDecision.madeDecisionToAny()
                                && memory.returnFactValueForGivenKey(IS_UNIT).isPresent()
                                && memory.returnFactValueForGivenKey(PLACE_TO_REACH).isPresent()
                                && !memory.returnFactValueForGivenKey(IS_UNIT).get().isUnderAttack()
                                && memory.returnFactValueForGivenKey(PLACE_TO_REACH).get().distanceTo(memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get().getPosition()) > 10
                        )
                        .desiresToConsider(new HashSet<>(Collections.singleton(ATTACK)))
                        .build()
                )
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> true)
                        .build())
                .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                    @Override
                    public boolean act(WorkingMemory memory) {
                        return memory.returnFactValueForGivenKey(IS_UNIT).get().attack(memory.returnFactValueForGivenKey(PLACE_TO_REACH).get());
                    }
                })
                .build();
        type.addConfiguration(MOVE_TO_POSITION, desireKey, moveOnPosition);

        //attack on position
        ConfigurationWithCommand.WithActingCommandDesiredBySelf attack = ConfigurationWithCommand.WithActingCommandDesiredBySelf.builder()
                .decisionInDesire(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) ->
                                memory.returnFactValueForGivenKey(IS_UNIT).isPresent()
                                        && !memory.returnFactValueForGivenKey(IS_UNIT).get().isUnderAttack()
                        )
                        .build()
                )
                .decisionInIntention(CommitmentDeciderInitializer.builder()
                        .decisionStrategy((dataForDecision, memory) -> true)
                        .build())
                .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                    @Override
                    public boolean act(WorkingMemory memory) {
                        //select closest enemy
                        AUnitWithCommands unitOfPlayer = memory.returnFactValueForGivenKey(IS_UNIT).get();

                        Optional<AUnit.Enemy> enemy = unitOfPlayer.getEnemyUnitsInRadiusOfSight().stream()
                                .filter(AUnit::isAttacking)
                                .min(Comparator.comparingDouble(value -> value.getPosition().distanceTo(unitOfPlayer.getPosition())));
                        if (enemy.isPresent()) {
                            unitOfPlayer.attack(enemy.get());
                        } else {

                            //attack units on position
                            Optional<ReadOnlyMemory> base = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                    .min(Comparator.comparingDouble(value -> value.returnFactValueForGivenKey(IS_BASE_LOCATION).get().distanceTo(unitOfPlayer.getPosition())));
                            if (base.isPresent()) {
                                enemy = base.get().returnFactSetValueForGivenKey(ENEMY_UNIT).orElse(Stream.empty())
                                        .filter(AUnit::isAttacking)
                                        .min(Comparator.comparingDouble(value -> value.getPosition().distanceTo(unitOfPlayer.getPosition())));
                                if (enemy.isPresent()) {
                                    unitOfPlayer.attack(enemy.get());
                                } else {
                                    enemy = base.get().returnFactSetValueForGivenKey(ENEMY_UNIT).orElse(Stream.empty())
                                            .min(Comparator.comparingDouble(value -> value.getPosition().distanceTo(unitOfPlayer.getPosition())));
                                    enemy.ifPresent(unitOfPlayer::attack);
                                }
                            }
                        }
                        return true;
                    }
                })
                .build();
        type.addConfiguration(ATTACK, desireKey, attack);

    }

    //"barracks"
    public static final AgentTypeUnit EGG = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.EGG)
            .usingTypesForFacts(new HashSet<>(Collections.singletonList(IS_MORPHING_TO)))
            .initializationStrategy(type -> {

                //reason about morphing
                type.addConfiguration(MORPHING_TO, beliefsAboutMorphing);

            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(MORPHING_TO)))
            .build();

    public static final AgentTypeUnit LARVA = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.LARVA)
            .usingTypesForFacts(new HashSet<>(Collections.singletonList(IS_MORPHING_TO)))
            .initializationStrategy(type -> {

                //morph to overlord
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent morphToOverlord = ConfigurationWithCommand.
                        WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().morph(intention.getDesireKey().returnFactValueForGivenKey(MORPH_TO).get().returnType());
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                        .decisionStrategy((dataForDecision, memory) -> !dataForDecision.madeDecisionToAny()
                                                        && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_MORPHING_OVERLORDS) == 0
                                                        && dataForDecision.getFeatureValueGlobalBeliefs(CURRENT_POPULATION)
                                                        >= dataForDecision.getFeatureValueGlobalBeliefs(MAX_POPULATION)
//                                        && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_SUPPLY_BY_OVERLORDS) + dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_HATCHERIES)
//                                        == (dataForDecision.getFeatureValueGlobalBeliefs(MAX_POPULATION))
                                        )
                                        .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(CURRENT_POPULATION, MAX_POPULATION, COUNT_OF_SUPPLY_BY_OVERLORDS, COUNT_OF_HATCHERIES)))
                                        .globalBeliefTypes(new HashSet<>(Collections.singleton(COUNT_OF_MORPHING_OVERLORDS)))
                                        .desiresToConsider(new HashSet<>(Arrays.asList(MORPH_TO_OVERLORD, BOOST_GROUND_MELEE,
                                                BOOST_GROUND_RANGED, BOOST_AIR, MORPH_TO_DRONE)))
                                        .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> false)
                                .build())
                        .build();
                type.addConfiguration(MORPH_TO_OVERLORD, morphToOverlord);

                //morph to zergling
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent morphToZergling = ConfigurationWithCommand.
                        WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().morph(intention.getDesireKey().returnFactValueForGivenKey(MORPH_TO).get().returnType());
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !dataForDecision.madeDecisionToAny()
                                        && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS) != 0
                                        && dataForDecision.getFeatureValueGlobalBeliefs(CURRENT_POPULATION)
                                        < dataForDecision.getFeatureValueGlobalBeliefs(MAX_POPULATION))
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(CURRENT_POPULATION, MAX_POPULATION, COUNT_OF_POOLS)))
                                .desiresToConsider(new HashSet<>(Arrays.asList(MORPH_TO_OVERLORD, BOOST_GROUND_MELEE,
                                        BOOST_GROUND_RANGED, BOOST_AIR, MORPH_TO_DRONE)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_POOLS) == 0
                                        || dataForDecision.getFeatureValueGlobalBeliefs(CURRENT_POPULATION)
                                        >= dataForDecision.getFeatureValueGlobalBeliefs(MAX_POPULATION))
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(CURRENT_POPULATION, MAX_POPULATION, COUNT_OF_POOLS)))
                                .build())
                        .build();
                type.addConfiguration(BOOST_GROUND_MELEE, morphToZergling);

                //morph to hydras
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent morphToHydra = ConfigurationWithCommand.
                        WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().morph(intention.getDesireKey().returnFactValueForGivenKey(MORPH_TO).get().returnType());
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !dataForDecision.madeDecisionToAny()
                                        && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_HYDRALISK_DENS) != 0 && dataForDecision.getFeatureValueGlobalBeliefs(CURRENT_POPULATION)
                                        < dataForDecision.getFeatureValueGlobalBeliefs(MAX_POPULATION))
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(CURRENT_POPULATION, MAX_POPULATION, COUNT_OF_HYDRALISK_DENS)))
                                .desiresToConsider(new HashSet<>(Arrays.asList(MORPH_TO_OVERLORD, BOOST_GROUND_MELEE,
                                        BOOST_GROUND_RANGED, BOOST_AIR, MORPH_TO_DRONE)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) ->
                                        dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_HYDRALISK_DENS) == 0
                                                || dataForDecision.getFeatureValueGlobalBeliefs(CURRENT_POPULATION)
                                                >= dataForDecision.getFeatureValueGlobalBeliefs(MAX_POPULATION))
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(CURRENT_POPULATION, MAX_POPULATION, COUNT_OF_HYDRALISK_DENS)))
                                .build())
                        .build();
                type.addConfiguration(BOOST_GROUND_RANGED, morphToHydra);

                //morph to mutalisk
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent morphToMutalisk = ConfigurationWithCommand.
                        WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().morph(intention.getDesireKey().returnFactValueForGivenKey(MORPH_TO).get().returnType());
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> !dataForDecision.madeDecisionToAny()
                                        && dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_SPIRES) != 0
                                        && dataForDecision.getFeatureValueGlobalBeliefs(CURRENT_POPULATION)
                                        < dataForDecision.getFeatureValueGlobalBeliefs(MAX_POPULATION))
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(CURRENT_POPULATION, MAX_POPULATION, COUNT_OF_SPIRES)))
                                .desiresToConsider(new HashSet<>(Arrays.asList(MORPH_TO_OVERLORD, BOOST_GROUND_MELEE,
                                        BOOST_GROUND_RANGED, BOOST_AIR, MORPH_TO_DRONE)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(COUNT_OF_SPIRES) == 0
                                        || dataForDecision.getFeatureValueGlobalBeliefs(CURRENT_POPULATION)
                                        >= dataForDecision.getFeatureValueGlobalBeliefs(MAX_POPULATION))
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(CURRENT_POPULATION, MAX_POPULATION, COUNT_OF_SPIRES)))
                                .build())
                        .build();
                type.addConfiguration(BOOST_AIR, morphToMutalisk);

                //morph
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent mine = ConfigurationWithCommand.
                        WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().morph(intention.getDesireKey().returnFactValueForGivenKey(MORPH_TO).get().returnType());
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {
                                            if (dataForDecision.madeDecisionToAny() || dataForDecision.getFeatureValueGlobalBeliefs(CURRENT_POPULATION)
                                                    >= dataForDecision.getFeatureValueGlobalBeliefs(MAX_POPULATION)) {
                                                return false;
                                            }
                                            AUnitOfPlayer me = memory.returnFactValueForGivenKey(REPRESENTS_UNIT).get();
                                            if (!me.getEnemyUnitsInRadiusOfSight().isEmpty()) {
                                                return false;
                                            }
                                            Optional<ABaseLocationWrapper> locationWrapper = me.getNearestBaseLocation();
                                            Optional<ReadOnlyMemory> baseLarvaIsLocatedBeliefs = memory.getReadOnlyMemoriesForAgentType(AgentTypes.BASE_LOCATION)
                                                    .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).isPresent())
                                                    .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get().equals(locationWrapper.orElse(null)))
                                                    .findAny();
                                            if (!baseLarvaIsLocatedBeliefs.isPresent()) {
                                                return false;
                                            }
                                            ReadOnlyMemory readOnlyMemory = baseLarvaIsLocatedBeliefs.get();
                                            long countOfMinerals = readOnlyMemory.returnFactSetValueForGivenKey(MINERAL).orElse(Stream.empty()).count();
                                            long extractors = readOnlyMemory.returnFactSetValueForGivenKey(HAS_EXTRACTOR).orElse(Stream.empty()).count();
                                            long workers = readOnlyMemory.returnFactSetValueForGivenKey(WORKER_ON_BASE).orElse(Stream.empty()).count();
                                            return ((countOfMinerals * 2.5) + (extractors * 3)) > workers;
                                        }
                                )
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(CURRENT_POPULATION, MAX_POPULATION)))
                                .desiresToConsider(new HashSet<>(Arrays.asList(MORPH_TO_OVERLORD, BOOST_GROUND_MELEE,
                                        BOOST_GROUND_RANGED, BOOST_AIR, MORPH_TO_DRONE)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> dataForDecision.getFeatureValueGlobalBeliefs(CURRENT_POPULATION)
                                        >= dataForDecision.getFeatureValueGlobalBeliefs(MAX_POPULATION))
                                .globalBeliefTypesByAgentType(new HashSet<>(Arrays.asList(CURRENT_POPULATION, MAX_POPULATION)))
                                .build())
                        .build();
                type.addConfiguration(MORPH_TO_DRONE, mine);

                //reason about morphing
                type.addConfiguration(MORPHING_TO, beliefsAboutMorphing);

            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(MORPHING_TO)))
            .build();
    //UNITS
}