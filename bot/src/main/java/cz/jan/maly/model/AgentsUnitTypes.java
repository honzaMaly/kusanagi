package cz.jan.maly.model;

import bwapi.Order;
import cz.jan.maly.model.agent.types.AgentTypeUnit;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.game.wrappers.APosition;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitOfPlayer;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithAbstractPlan;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;

import java.util.*;
import java.util.stream.Collectors;

import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.agent.types.AgentTypeUnit.*;
import static cz.jan.maly.model.bot.FactConverters.*;
import static cz.jan.maly.model.bot.FactKeys.IS_BEING_CONSTRUCT;
import static cz.jan.maly.model.bot.FactKeys.*;

/**
 * Created by Jan on 15-Mar-17.
 */
public class AgentsUnitTypes {

    //BUILDINGS
    public static final AgentTypeUnit HATCHERY = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.HATCHERY)
            .initializationStrategy(type -> {
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_CONSTRUCTION, beliefsAboutConstruction);
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
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);
            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(SURROUNDING_UNITS_AND_LOCATION)))
            .build();
    public static final AgentTypeUnit MUTALISK = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.MUTALISK)
            .initializationStrategy(type -> {
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);
            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(SURROUNDING_UNITS_AND_LOCATION)))
            .build();
    public static final AgentTypeUnit HYDRALISK = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.HYDRALISK)
            .initializationStrategy(type -> {
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);
            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(SURROUNDING_UNITS_AND_LOCATION)))
            .build();

    //worker
    public static final AgentTypeUnit DRONE = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.DRONE)
            .usingTypesForFacts(new HashSet<>(Arrays.asList(MINING_MINERAL, MINERAL_TO_MINE, IS_MORPHING_TO,
                    IS_GATHERING_MINERALS, IS_GATHERING_GAS)))
            .initializationStrategy(type -> {

                //reason about activities related to worker
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf workerCorncerns = ConfigurationWithCommand.
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
                type.addConfiguration(UPDATE_BELIEFS_ABOUT_WORKER_ACTIVITIES, workerCorncerns);

                //reason about morphing
                type.addConfiguration(MORPHING_TO, beliefsAboutMorphing);

                //abstract plan to mine minerals in base
                ConfigurationWithAbstractPlan mineInBase = ConfigurationWithAbstractPlan.builder()
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {
                                    if (dataForDecision.getNumberOfCommittedAgents() < dataForDecision.getFeatureValueDesireBeliefSets(COUNT_OF_MINERALS_ON_BASE)) {
                                        return true;
                                    }
                                    return false;
                                })
                                .parameterValueSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> {

                                    if (dataForDecision.getNumberOfCommittedAgents() > dataForDecision.getFeatureValueDesireBeliefSets(COUNT_OF_MINERALS_ON_BASE)) {
                                        return true;
                                    }
                                    return false;
                                })
                                .parameterValueSetTypes(new HashSet<>(Collections.singletonList(COUNT_OF_MINERALS_ON_BASE)))
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

                //morph
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent morphToPool = ConfigurationWithCommand.
                        WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().build(intention.getDesireKey().returnFactValueForGivenKey(MORPH_TO).get().returnType(),
                                        intention.returnFactValueForGivenKeyInDesireParameters(PLACE_FOR_POOL).get());
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build())
                        .build();
                type.addConfiguration(MORPH_TO_POOL, morphToPool);

                //morph to pool
                type.addConfiguration(MORPHING_TO, beliefsAboutMorphing);

                //surrounding units and location belief update
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);
            })
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(SURROUNDING_UNITS_AND_LOCATION,
                    UPDATE_BELIEFS_ABOUT_WORKER_ACTIVITIES, MORPHING_TO)))
            .build();

    //population and scout
    public static final AgentTypeUnit OVERLORD = AgentTypeUnit.builder()
            .agentTypeID(AgentTypes.OVERLORD)
            .initializationStrategy(type -> {
                type.addConfiguration(SURROUNDING_UNITS_AND_LOCATION, beliefsAboutSurroundingUnitsAndLocation);
            })
            .desiresWithIntentionToReason(new HashSet<>(Collections.singletonList(SURROUNDING_UNITS_AND_LOCATION)))
            .build();

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


                //TODO give conditions - morph in base where it makes sense
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
                                .decisionStrategy((dataForDecision, memory) -> true)
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy((dataForDecision, memory) -> true)
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
