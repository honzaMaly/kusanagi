package cz.jan.maly.model;

import bwta.BWTA;
import bwta.BaseLocation;
import cz.jan.maly.model.agent.types.AgentTypeUnit;
import cz.jan.maly.model.game.wrappers.APlayer;
import cz.jan.maly.model.game.wrappers.APosition;
import cz.jan.maly.model.game.wrappers.ATilePosition;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithAbstractPlan;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.service.implementation.BotFacade;

import java.util.*;
import java.util.stream.Collectors;

import static cz.jan.maly.model.BasicFactsKeys.*;
import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.FactsKeys.*;
import static cz.jan.maly.service.AgentUnitFactory.SPAWNING_POOL_TYPE;

/**
 * Created by Jan on 15-Mar-17.
 */
public class AgentsUnitTypes {

    public static final AgentTypeUnit HATCHERY = AgentTypeUnit.builder()
            .name("HATCHERY")
            .initializationStrategy(type -> {
            })
            .build();

    public static final AgentTypeUnit OVERLORD = AgentTypeUnit.builder()
            .name("OVERLORD")
            .initializationStrategy(type -> {
            })
            .build();

    public static final AgentTypeUnit EGG = AgentTypeUnit.builder()
            .name("EGG")
            .initializationStrategy(type -> {
            })
            .build();

    public static final AgentTypeUnit SPAWNING_POOL = AgentTypeUnit.builder()
            .name("SPAWNING_POOL")
            .initializationStrategy(type -> {
            })
            .build();

    //morph
    public static final AgentTypeUnit LARVA = AgentTypeUnit.builder()
            .name("LARVA")
            .initializationStrategy(type -> {

                //command to morph to drone
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent morphToDrone = ConfigurationWithCommand
                        .WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().morph(intention.getDesireKey().returnFactValueForGivenKey(UNIT_TYPE).get());
                            }
                        })
                        //is there enough resources, also does not morph to something else
                        .decisionInDesire((desire, dataForDecision) -> {
                            Optional<APlayer> aPlayer = desire.getReadOnlyMemories().stream()
                                    .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(IS_PLAYER))
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_PLAYER))
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .findAny();

                            if (!aPlayer.isPresent() || aPlayer.get().getMinerals() < desire.getDesireKey().returnFactValueForGivenKey(UNIT_TYPE).get().getMineralPrice()) {
                                return false;
                            }
                            BaseLocation myBaseLocation = desire.returnFactValueForGivenKey(IS_UNIT).get().getNearestBaseLocation().get();
                            BaseLocation otherLocation = desire.returnFactValueForGivenKeyInParameters(IS_BASE_LOCATION).get();
                            return !(myBaseLocation.getX() != otherLocation.getX()
                                    || myBaseLocation.getY() != otherLocation.getY());
                        })
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();

                //morph to drone
                type.addConfiguration(MORPH_TO_DRONE, morphToDrone);

                //general command to morph to type
                ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent morphToType = ConfigurationWithCommand
                        .WithActingCommandDesiredByOtherAgent.builder()
                        .commandCreationStrategy(intention -> new ActCommand.DesiredByAnotherAgent(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().morph(intention.getDesireKey().returnFactValueForGivenKey(UNIT_TYPE).get());
                            }
                        })
                        //is there enough resources, also does not morph to something else
                        .decisionInDesire((desire, dataForDecision) -> {
                            Optional<APlayer> aPlayer = desire.getReadOnlyMemories().stream()
                                    .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(IS_PLAYER))
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_PLAYER))
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .findAny();
                            return !(!aPlayer.isPresent() || aPlayer.get().getMinerals() < desire.getDesireKey().returnFactValueForGivenKey(UNIT_TYPE).get().getMineralPrice());
                        })
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();

                //morph to zergling
                type.addConfiguration(MORPH_TO_ZERGLING, morphToType);

                //morph to overlord
                type.addConfiguration(MORPH_TO_OVERLORD, morphToType);

            })
            .build();

    public static final AgentTypeUnit DRONE = AgentTypeUnit.builder()
            .name("DRONE")
//            .desiresWithIntentionToAct(new HashSet<>(Arrays.asList(new DesireKey[]{MINE_MINERALS})))
//            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(new DesireKey[]{SELECT_MINERAL})))
            .usingTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{MINING_MINERAL, MINERAL_TO_MINE, PLACE_FOR_BUILDING})))
            .initializationStrategy((AgentType type) -> {

                //abstract plan to build pool in base
                ConfigurationWithAbstractPlan buildPool = ConfigurationWithAbstractPlan.builder()
                        .decisionInDesire((desire, dataForDecision) -> {
                            if (desire.returnFactValueForGivenKey(IS_UNIT).get().isCarryingMinerals()) {
                                return false;
                            }
                            Optional<APlayer> aPlayer = desire.getReadOnlyMemories().stream()
                                    .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(IS_PLAYER))
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_PLAYER))
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .findAny();

                            //commit little bit sooner to start looking for place and to get there
                            if (!aPlayer.isPresent() || aPlayer.get().getMinerals() <= desire.getDesireKey().returnFactValueForGivenKey(UNIT_TYPE).get().getMineralPrice() - 24) {
                                return false;
                            }
                            return true;
                        })
                        .decisionInIntention((intention, dataForDecision) -> false)
                        .desiresWithIntentionToAct(new HashSet<>(Arrays.asList(new DesireKey[]{BUILD_POOL})))
                        .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(new DesireKey[]{FIND_PLACE_TO_BUILD})))
                        .build();
                type.addConfiguration(PLAN_BUILDING_POOL, buildPool, false);

                //find position
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf findPlaceForBuilding = ConfigurationWithCommand
                        .WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                BotFacade.ADDITIONAL_OBSERVATIONS_PROCESSOR.requestObservation((m, e) -> {
                                    ATilePosition position = new ATilePosition(e.getBuildLocation(SPAWNING_POOL_TYPE.getType(), intention.returnFactValueOfParentIntentionForGivenKey(BASE_FOR_POOL).get().getTilePosition(), 20));
                                    memory.updateFact(PLACE_FOR_BUILDING, position);
                                    return true;
                                }, memory, DRONE);
                                return true;
                            }
                        })
                        .decisionInDesire((desire, dataForDecision) -> true)
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();
                type.addConfiguration(FIND_PLACE_TO_BUILD, PLAN_BUILDING_POOL, findPlaceForBuilding);

                //build pool
                ConfigurationWithCommand.WithActingCommandDesiredBySelf build = ConfigurationWithCommand
                        .WithActingCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                ATilePosition buildHere = intention.returnFactValueForGivenKey(PLACE_FOR_BUILDING).get();
                                memory.eraseFactValueForGivenKey(PLACE_FOR_BUILDING);
                                return intention.returnFactValueForGivenKey(IS_UNIT).get().build(SPAWNING_POOL_TYPE, buildHere);
                            }
                        })
                        .decisionInDesire((desire, dataForDecision) -> desire.returnFactValueForGivenKey(PLACE_FOR_BUILDING).isPresent()
                        )
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();
                type.addConfiguration(BUILD_POOL, PLAN_BUILDING_POOL, build);

                //abstract plan to mine minerals in base
                ConfigurationWithAbstractPlan mineInBase = ConfigurationWithAbstractPlan.builder()
                        //commit if there is actually less committed workers then there is active minerals. this is also reason to remove commitment
                        .decisionInDesire((desire, dataForDecision) -> {
                            Set<AUnit> mineralsBeingMined = desire.getReadOnlyMemories().stream()
                                    .filter(readOnlyMemory -> readOnlyMemory.getAgentId() != desire.getAgentId())
                                    .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(MINING_MINERAL))
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MINING_MINERAL))
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .collect(Collectors.toSet());
                            Set<AUnit> mineralsToMine = desire.returnFactSetValueForGivenKeyInParameters(MINERAL).get().stream()
                                    .filter(AUnit::isAlive)
                                    .filter(unit -> !mineralsBeingMined.contains(unit))
                                    .collect(Collectors.toSet());
                            return !mineralsToMine.isEmpty();
                        })
                        .decisionInIntention((intention, dataForDecision) -> {
                            if (intention.returnFactValueForGivenKey(IS_UNIT).get().isCarryingMinerals()) {
                                return false;
                            }
                            if (dataForDecision.getMadeCommitmentToTypes().contains(PLAN_BUILDING_POOL)) {
                                return true;
                            }
                            Set<AUnit> mineralsBeingMined = intention.getReadOnlyMemories().stream()
                                    .filter(readOnlyMemory -> readOnlyMemory.getAgentId() != intention.getAgentId())
                                    .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(MINING_MINERAL))
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MINING_MINERAL))
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .collect(Collectors.toSet());
                            Set<AUnit> mineralsToMine = intention.returnFactSetValueForGivenKeyInDesireParameters(MINERAL).get().stream()
                                    .filter(AUnit::isAlive)
                                    .filter(unit -> !mineralsBeingMined.contains(unit))
                                    .collect(Collectors.toSet());
                            return mineralsToMine.isEmpty();
                        })
                        .desiresWithIntentionToAct(new HashSet<>(Arrays.asList(new DesireKey[]{MINE_MINERALS})))
                        .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(new DesireKey[]{SELECT_MINERAL})))
                        .typesOfDesiresToConsiderWhenRemovingCommitment(new HashSet<>(Arrays.asList(new DesireKey[]{PLAN_BUILDING_POOL})))
                        .build();
                type.addConfiguration(MINE_MINERALS_IN_BASE, mineInBase, false);

                //select closest mineral to mine from set of available
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf selectMineral = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                Set<AUnit> mineralsBeingMined = memory.getReadOnlyMemoriesForAgentType(DRONE).stream()
                                        .filter(readOnlyMemory -> readOnlyMemory.getAgentId() != memory.getAgentId())
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(MINING_MINERAL))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .collect(Collectors.toSet());
                                Set<AUnit> mineralsToMine = intention.returnFactSetValueOfParentIntentionForGivenKey(MINERAL).get().stream()
                                        .filter(AUnit::isAlive)
                                        .filter(unit -> !mineralsBeingMined.contains(unit))
                                        .collect(Collectors.toSet());
                                if (!mineralsToMine.isEmpty()) {

                                    //select free nearest mineral to closest hatchery
                                    APosition myPosition = intention.returnFactValueForGivenKey(IS_UNIT).get().getPosition();
                                    Optional<AUnit> hatchery = intention.returnFactSetValueOfParentIntentionForGivenKey(HAS_HATCHERY).get().stream()
                                            .min(Comparator.comparingDouble(o -> BWTA.getGroundDistance(myPosition.getATilePosition().getTilePosition(), o.getPosition().getATilePosition().getTilePosition())));
                                    if (hatchery.isPresent()) {
                                        Optional<AUnit> mineralToPick = mineralsToMine.stream()
                                                .min(Comparator.comparingDouble(o -> BWTA.getGroundDistance(hatchery.get().getPosition().getATilePosition().getTilePosition(), o.getPosition().getATilePosition().getTilePosition())));
                                        mineralToPick.ifPresent(aUnit -> {
                                            memory.updateFact(MINERAL_TO_MINE, aUnit);

                                            //release currently mined mineral if it differs with mineral about to be mined
                                            if (intention.returnFactValueForGivenKey(MINING_MINERAL).isPresent()
                                                    && !intention.returnFactValueForGivenKey(MINING_MINERAL).get().equals(MINERAL_TO_MINE)) {
                                                memory.updateFact(MINING_MINERAL, MINING_MINERAL.getInitValue());
                                            }
                                        });
                                    }
                                }
                                return true;
                            }
                        })
                        //look for other minerals only if you carry one or zou have not decided one to mine
                        .decisionInDesire((desire, dataForDecision) -> !desire.returnFactValueForGivenKey(MINING_MINERAL).isPresent() || desire.returnFactValueForGivenKey(IS_UNIT).get().isCarryingMinerals())
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();
                type.addConfiguration(SELECT_MINERAL, MINE_MINERALS_IN_BASE, selectMineral);

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
                        //mineral is selected, agent does not carry mineral and mineral to mine differs from mineral which is being mined. do not start mining mineral if other agent has selected it
                        .decisionInDesire((desire, dataForDecision) -> !desire.returnFactValueForGivenKey(IS_UNIT).get().isCarryingMinerals()
                                && desire.returnFactValueForGivenKeyInParameters(MINERAL_TO_MINE).isPresent()
                                && !desire.returnFactValueForGivenKeyInParameters(MINERAL_TO_MINE).get().equals(desire.returnFactValueForGivenKey(MINING_MINERAL).orElse(null))
                        )
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();
                type.addConfiguration(MINE_MINERALS, MINE_MINERALS_IN_BASE, mine);

            })
            .build();

}
