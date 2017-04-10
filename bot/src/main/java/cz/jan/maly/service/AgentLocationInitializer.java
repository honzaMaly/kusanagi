package cz.jan.maly.service;

import bwta.BaseLocation;
import bwta.Region;
import cz.jan.maly.model.agent.AgentBaseLocation;
import cz.jan.maly.model.agent.AgentRegion;
import cz.jan.maly.model.agent.types.AgentTypeBaseLocation;
import cz.jan.maly.model.agent.types.AgentTypeRegion;
import cz.jan.maly.model.game.wrappers.ABaseLocationWrapper;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitOfPlayer;
import cz.jan.maly.model.game.wrappers.UnitWrapperFactory;
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
import java.util.Set;
import java.util.stream.Collectors;

import static cz.jan.maly.model.AgentsUnitTypes.*;
import static cz.jan.maly.model.BasicFactsKeys.*;
import static cz.jan.maly.model.DesiresKeys.*;
import static cz.jan.maly.model.FactsKeys.*;
import static cz.jan.maly.service.AgentUnitFactory.DRONE_TYPE;

/**
 * Strategy to initialize player
 * Created by Jan on 05-Apr-17.
 */
public class AgentLocationInitializer implements LocationInitializer {

    public static final AgentTypeBaseLocation BASE_LOCATION = AgentTypeBaseLocation.builder()
            .name("BASE_LOCATION")
            .initializationStrategy(type -> {

                //reason - am I enemy base?
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf amIEnemyBase = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                memory.updateFact(IS_ENEMY_BASE, true);
                                return true;
                            }
                        })
                        //there are some enemy buildings or I am only one unvisited initial base and no enemy was found
                        .decisionInDesire((desire, dataForDecision) -> desire.returnFactSetValueForGivenKey(ENEMY_UNIT).get().stream()
                                .anyMatch(unit -> unit.getType().isBuilding())
                                || (desire.getReadOnlyMemories().stream()
                                .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(IS_BASE))
                                .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(IS_BASE).get())
                                .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get().isStartLocation())
                                .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(LAST_TIME_SCOUTED))
                                .filter(integer -> !integer.isPresent())
                                .count() == 1
                                && !desire.returnFactValueForGivenKey(IS_BASE).get()
                                && !desire.returnFactValueForGivenKey(LAST_TIME_SCOUTED).isPresent()
                                && desire.returnFactValueForGivenKey(IS_BASE_LOCATION).get().isStartLocation()
                                && desire.getReadOnlyMemories().stream()
                                .filter(readOnlyMemory -> readOnlyMemory.getAgentId() != desire.getAgentId())
                                .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(IS_ENEMY_BASE))
                                .noneMatch(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_ENEMY_BASE).get()))
                        )
                        .decisionInIntention((intention, dataForDecision) -> true)
                        //todo when remove commitment set belief back to false
                        .build();
                type.addConfiguration(AM_I_ENEMY_BASE, amIEnemyBase);

                //TODO remove when have backtrack
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf amINotEnemyBase = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                memory.updateFact(IS_ENEMY_BASE, false);
                                return true;
                            }
                        })
                        //there are not any enemy buildings and does not hold that I am only one unvisited initial base and no enemy was found
                        .decisionInDesire((desire, dataForDecision) -> {
                                    if (desire.getReadOnlyMemories().stream()
                                            .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(IS_BASE))
                                            .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get().isStartLocation())
                                            .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(LAST_TIME_SCOUTED))
                                            .filter(integer -> !integer.isPresent())
                                            .count() == 1
                                            && !desire.returnFactValueForGivenKey(IS_BASE).get()
                                            && desire.returnFactValueForGivenKey(IS_BASE_LOCATION).get().isStartLocation()
                                            && !desire.returnFactValueForGivenKey(LAST_TIME_SCOUTED).isPresent()
                                            && desire.getReadOnlyMemories().stream()
                                            .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(IS_ENEMY_BASE))
                                            .filter(readOnlyMemory -> readOnlyMemory.getAgentId() != desire.getAgentId())
                                            .noneMatch(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_ENEMY_BASE).get())) {
                                        return false;
                                    }
                                    return desire.returnFactSetValueForGivenKey(ENEMY_UNIT).get().stream()
                                            .noneMatch(unit -> unit.getType().isBuilding());
                                }
                        )
                        //there are enemy buildings
                        .decisionInIntention((intention, dataForDecision) -> true)
                        //todo when remove commitment set belief back to false
                        .build();
                type.addConfiguration(AM_I_NOT_ENEMY_BASE, amINotEnemyBase);

                //reason enemies in base
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf enemiesInBase = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();
                                Set<AUnit.Enemy> enemies = UnitWrapperFactory.getStreamOfAllEnemyUnits()
                                        .filter(enemy -> enemy.getNearestBaseLocation().isPresent())
                                        .filter(enemy -> enemy.getNearestBaseLocation().get().isOnSameCoordinates(base))
                                        .collect(Collectors.toSet());
                                memory.updateFactSetByFacts(ENEMY_UNIT, enemies);
                                return true;
                            }
                        })
                        //there are some enemy buildings
                        .decisionInDesire((desire, dataForDecision) -> true)
                        //there are no enemy buildings
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();
                type.addConfiguration(ENEMIES_IN_BASE, enemiesInBase);

                //tell system to visit me
                ConfigurationWithSharedDesire visitMe = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(VISIT)
                        //am not our base
                        .decisionInDesire((desire, dataForDecision) -> {

                            //do not visit our base location
                            if (desire.returnFactValueForGivenKey(IS_BASE).get()) {
                                return false;
                            }

                            //is everything visited, if so, desire visit
                            if (desire.getReadOnlyMemories().stream()
                                    .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(IS_BASE))
                                    .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(IS_BASE).get())
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(LAST_TIME_SCOUTED))
                                    .allMatch(Optional::isPresent)) {
                                return true;
                            }

                            long countOfUnvisitedStartingPositions = desire.getReadOnlyMemories().stream()
                                    .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(IS_BASE))
                                    .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(IS_BASE).get())
                                    .filter(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get().isStartLocation())
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(LAST_TIME_SCOUTED))
                                    .filter(integer -> !integer.isPresent())
                                    .count();

                            //visit bases first
                            if (countOfUnvisitedStartingPositions > 0) {
                                return desire.returnFactValueForGivenKey(IS_BASE_LOCATION).get().isStartLocation();
                            }

                            long countOfUnvisitedOtherPositions = desire.getReadOnlyMemories().stream()
                                    .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(IS_BASE))
                                    .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(IS_BASE).get())
                                    .filter(readOnlyMemory -> !readOnlyMemory.returnFactValueForGivenKey(IS_BASE_LOCATION).get().isStartLocation())
                                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(LAST_TIME_SCOUTED))
                                    .filter(integer -> !integer.isPresent())
                                    .count();

                            //there are still other positions to scout
                            if (countOfUnvisitedOtherPositions > 0) {
                                return !desire.returnFactValueForGivenKeyInParameters(IS_BASE_LOCATION).get().isStartLocation();
                            }
                            return true;
                        })
                        //am our base now. or I was scouted
                        .decisionInIntention((intention, dataForDecision) -> {

                                    //do not visit our base location
                                    if (intention.returnFactValueForGivenKey(IS_BASE).get()) {
                                        return true;
                                    }

                                    //not visit anything
                                    if (!intention.returnFactValueForGivenKeyInDesireParameters(LAST_TIME_SCOUTED).isPresent()
                                            && !intention.returnFactValueForGivenKey(LAST_TIME_SCOUTED).isPresent()) {
                                        return false;
                                    }

                                    ///we made first visit
                                    if (!intention.returnFactValueForGivenKeyInDesireParameters(LAST_TIME_SCOUTED).isPresent()
                                            && intention.returnFactValueForGivenKey(LAST_TIME_SCOUTED).isPresent()) {
                                        return true;
                                    }

                                    //new visit
                                    return (intention.returnFactValueForGivenKeyInDesireParameters(LAST_TIME_SCOUTED).get() < intention.returnFactValueForGivenKey(LAST_TIME_SCOUTED).get());
                                }
                        )
                        .counts(1)
                        .build();
                type.addConfiguration(VISIT, visitMe);

                //tell system to visit me
                ConfigurationWithSharedDesire attack = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(ATTACK)
                        //am not our base
                        .decisionInDesire((desire, dataForDecision) -> desire.returnFactValueForGivenKey(IS_ENEMY_BASE).get())
                        //am our base now. or I was scouted
                        .decisionInIntention((intention, dataForDecision) -> !intention.returnFactValueForGivenKey(IS_ENEMY_BASE).get())
                        .build();
                type.addConfiguration(ATTACK, attack);

                //reason about last visit
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf reasonAboutVisit = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();
                                Optional<Integer> frameWhenLastVisited = memory.getReadOnlyMemories().stream()
                                        .filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(REPRESENTS_UNIT))
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .filter(unit -> unit.getNearestBaseLocation().isPresent())
                                        .filter(unit -> unit.getNearestBaseLocation().get().isOnSameCoordinates(base))
                                        .filter(unit -> unit.getNearestBaseLocation().get().distanceTo(base) < 20)
                                        .map(AUnit::getFrameCount)
                                        .min(Integer::compareTo);
                                frameWhenLastVisited.ifPresent(integer -> memory.updateFact(LAST_TIME_SCOUTED, integer));
                                return true;
                            }
                        })
                        //there are some enemy buildings
                        .decisionInDesire((desire, dataForDecision) -> true)
                        //there are no enemy buildings
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();
                type.addConfiguration(VISIT, reasonAboutVisit);

                //todo - attack
                //tell system to attack this base if it is for enemy base or it is last base location not scouted

                //todo - defend
                //tell system to defend if it is base and there are enemy ground units (do not defend in case of one enemy worker as he is probably only scouting)

                //Am I base - if it is not base, reason about being base - has any hatcheries?
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf amIBase = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();
                                Set<AUnit> hatcheries = memory.getReadOnlyMemoriesForAgentType(HATCHERY).stream()
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().isPresent())
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().get().isOnSameCoordinates(base))
                                        .collect(Collectors.toSet());
                                if (!hatcheries.isEmpty()) {
                                    memory.updateFactSetByFacts(HAS_HATCHERY, hatcheries);
                                    memory.updateFact(IS_BASE, true);
                                }
                                return true;
                            }
                        })
                        .decisionInDesire((desire, dataForDecision) -> !desire.returnFactValueForGivenKey(IS_BASE).get())
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();
                type.addConfiguration(AM_I_BASE, amIBase);

                //I am base. Look for hatcheries. Check if at least one hatchery is still alive. if not agent is no longer base
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf amIStillBase = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();
                                Set<AUnit> hatcheries = memory.getReadOnlyMemoriesForAgentType(HATCHERY).stream()
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().isPresent())
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().get().isOnSameCoordinates(base))
                                        .collect(Collectors.toSet());
                                if (!hatcheries.isEmpty()) {
                                    if (!memory.returnFactSetValueForGivenKey(HAS_HATCHERY).get().equals(hatcheries)) {
                                        memory.updateFactSetByFacts(HAS_HATCHERY, hatcheries);
                                    }
                                } else {
                                    memory.updateFact(IS_BASE, false);
                                }
                                return true;
                            }
                        })
                        .decisionInDesire((desire, dataForDecision) -> desire.returnFactValueForGivenKey(IS_BASE).get())
                        .decisionInIntention((intention, dataForDecision) -> true)
                        .build();
                type.addConfiguration(AM_I_STILL_BASE, amIStillBase);

                //Make request to start mining. Remove request when there are no more minerals to mine or there is no hatchery to bring mineral in
                ConfigurationWithSharedDesire mineMinerals = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MINE_MINERALS_IN_BASE)
                        .decisionInDesire((desire, dataForDecision) -> !desire.returnFactSetValueForGivenKey(MINERAL).get().isEmpty()
                                && desire.returnFactValueForGivenKey(IS_BASE).get())
                        .decisionInIntention((intention, dataForDecision) -> intention.returnFactSetValueForGivenKey(MINERAL).get().isEmpty()
                                || !intention.returnFactValueForGivenKey(IS_BASE).get())
                        .build();
                type.addConfiguration(MINE_MINERALS_IN_BASE, mineMinerals);

                //Make request to build drone where there are not enough drones
                //consider egg on location - is morphing to drone, take in account local drones, global pool - do not make more than 5 drones in base before pool
                ConfigurationWithSharedDesire buildDrone = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MORPH_TO_DRONE)
                        .decisionInDesire((desire, dataForDecision) -> {
                                    if (!desire.returnFactValueForGivenKey(IS_BASE).get()) {
                                        return false;
                                    }
                                    boolean isThereAnySpawningPoolBuild = !desire.getReadOnlyMemoriesForAgentType(SPAWNING_POOL).isEmpty();
                                    ABaseLocationWrapper myBaseLocation = desire.returnFactValueForGivenKey(IS_BASE_LOCATION).get();
                                    long countOfEggsInBaseMorphingToDrone = desire.getReadOnlyMemoriesForAgentType(EGG).stream()
                                            .filter(readOnlyMemory -> {
                                                AUnitOfPlayer unitOfPlayer = readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get();
                                                return unitOfPlayer.getNearestBaseLocation().get().isOnSameCoordinates(myBaseLocation) &&
                                                        unitOfPlayer.getTrainingQueue().contains(DRONE_TYPE);
                                            })
                                            .count();

                                    //pool is not build and globally there is less then 5 drones
                                    if (!isThereAnySpawningPoolBuild && desire.getReadOnlyMemoriesForAgentType(DRONE).size() + countOfEggsInBaseMorphingToDrone < 5) {
                                        return true;
                                    }

                                    long countOfDronesInBase = desire.getReadOnlyMemoriesForAgentType(DRONE).stream()
                                            .filter(readOnlyMemory -> {
                                                AUnitOfPlayer unitOfPlayer = readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get();
                                                return unitOfPlayer.getNearestBaseLocation().isPresent()
                                                        && unitOfPlayer.getNearestBaseLocation().get().isOnSameCoordinates(myBaseLocation);
                                            })
                                            .count();

                                    //pool is build and on base is less then 6 drones
                                    if (isThereAnySpawningPoolBuild && countOfDronesInBase + countOfEggsInBaseMorphingToDrone < 6) {
                                        return true;
                                    }
                                    return false;
                                }
                        )
                        .decisionInIntention((intention, dataForDecision) -> {
                            if (!intention.returnFactValueForGivenKey(IS_BASE).get()) {
                                return true;
                            }
                            boolean isThereAnySpawningPoolBuild = !intention.getReadOnlyMemoriesForAgentType(SPAWNING_POOL).isEmpty();
                            ABaseLocationWrapper myBaseLocation = intention.returnFactValueForGivenKey(IS_BASE_LOCATION).get();
                            long countOfEggsInBaseMorphingToDrone = intention.getReadOnlyMemoriesForAgentType(EGG).stream()
                                    .filter(readOnlyMemory -> {
                                        AUnitOfPlayer unitOfPlayer = readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get();
                                        return unitOfPlayer.getNearestBaseLocation().get().isOnSameCoordinates(myBaseLocation)
                                                && unitOfPlayer.getTrainingQueue().contains(DRONE_TYPE);
                                    })
                                    .count();

                            //pool is not build and globally there is at least 5 drones (build or to be build)
                            if (!isThereAnySpawningPoolBuild && intention.getReadOnlyMemoriesForAgentType(DRONE).size() + countOfEggsInBaseMorphingToDrone >= 5) {
                                return true;
                            }

                            long countOfDronesInBase = intention.getReadOnlyMemoriesForAgentType(DRONE).stream()
                                    .filter(readOnlyMemory -> {
                                        AUnitOfPlayer unitOfPlayer = readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT).get();
                                        return unitOfPlayer.getNearestBaseLocation().get().isOnSameCoordinates(myBaseLocation);
                                    })
                                    .count();

                            //pool is build and on base is at least 6 drones (build or to be build)
                            return isThereAnySpawningPoolBuild && countOfDronesInBase + countOfEggsInBaseMorphingToDrone >= 6;
                        })
                        .counts(1)
                        .build();
                type.addConfiguration(MORPH_TO_DRONE, buildDrone);

            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{IS_BASE, IS_ENEMY_BASE, LAST_TIME_SCOUTED})))
            .usingTypesForFactSets(new HashSet<>(Arrays.asList(new FactKey<?>[]{HAS_HATCHERY, ENEMY_UNIT})))
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(new DesireKey[]{AM_I_BASE, AM_I_STILL_BASE, AM_I_ENEMY_BASE, ENEMIES_IN_BASE, VISIT, AM_I_NOT_ENEMY_BASE})))
            .desiresForOthers(new HashSet<>(Arrays.asList(new DesireKey[]{MINE_MINERALS_IN_BASE, MORPH_TO_DRONE, VISIT, ATTACK})))
            .build();

    public static final AgentTypeRegion REGION = AgentTypeRegion.builder()
            .name("REGION")
            .initializationStrategy(type -> {
            })
            .build();

    @Override
    public Optional<AgentBaseLocation> createAgent(BaseLocation baseLocation, BotFacade botFacade) {
        return Optional.of(new AgentBaseLocation(BASE_LOCATION, botFacade, baseLocation));
    }

    @Override
    public Optional<AgentRegion> createAgent(Region region, BotFacade botFacade) {
        return Optional.of(new AgentRegion(REGION, botFacade, region));
    }
}
