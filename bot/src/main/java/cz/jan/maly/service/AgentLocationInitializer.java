package cz.jan.maly.service;

import bwta.BaseLocation;
import bwta.Region;
import cz.jan.maly.model.agent.AgentBaseLocation;
import cz.jan.maly.model.agent.AgentRegion;
import cz.jan.maly.model.agent.types.AgentTypeBaseLocation;
import cz.jan.maly.model.agent.types.AgentTypeRegion;
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
import java.util.Set;
import java.util.stream.Collectors;

import static cz.jan.maly.model.AgentsUnitTypes.HATCHERY;
import static cz.jan.maly.model.BasicFactsKeys.IS_BASE_LOCATION;
import static cz.jan.maly.model.BasicFactsKeys.MINERAL;
import static cz.jan.maly.model.BasicFactsKeys.REPRESENTS_UNIT;
import static cz.jan.maly.model.DesiresKeys.AM_I_BASE;
import static cz.jan.maly.model.DesiresKeys.AM_I_STILL_BASE;
import static cz.jan.maly.model.DesiresKeys.MINE_MINERALS_IN_BASE;
import static cz.jan.maly.model.FactsKeys.HAS_HATCHERY;
import static cz.jan.maly.model.FactsKeys.IS_BASE;

/**
 * Strategy to initialize player
 * Created by Jan on 05-Apr-17.
 */
public class AgentLocationInitializer implements LocationInitializer {

    @Override
    public Optional<AgentBaseLocation> createAgent(BaseLocation baseLocation, BotFacade botFacade) {
        return Optional.of(new AgentBaseLocation(BASE_LOCATION, botFacade, baseLocation));
    }

    public static final AgentTypeBaseLocation BASE_LOCATION = AgentTypeBaseLocation.builder()
            .name("BASE_LOCATION")
            .initializationStrategy(type -> {

                //Am I base - if it is not base, reason about being base - has any hatcheries?
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf amIBase = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                BaseLocation base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();
                                Set<AUnit> hatcheries = memory.getReadOnlyMemoriesForAgentType(HATCHERY).stream()
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().isPresent())
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().get().getX() == base.getX()
                                                && aUnitOfPlayer.getNearestBaseLocation().get().getY() == base.getY())
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
                                BaseLocation base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();
                                Set<AUnit> hatcheries = memory.getReadOnlyMemoriesForAgentType(HATCHERY).stream()
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().isPresent())
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().get().getX() == base.getX()
                                                && aUnitOfPlayer.getNearestBaseLocation().get().getY() == base.getY())
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

                //Make request to start mining. Remove request when there are no more minerals to mine
                ConfigurationWithSharedDesire mineMinerals = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MINE_MINERALS_IN_BASE)
                        .decisionInDesire((desire, dataForDecision) -> !desire.returnFactSetValueForGivenKey(MINERAL).get().isEmpty()
                                && desire.returnFactValueForGivenKey(IS_BASE).get())
                        .decisionInIntention((intention, dataForDecision) -> intention.returnFactSetValueForGivenKey(MINERAL).get().isEmpty()
                                || !intention.returnFactValueForGivenKey(IS_BASE).get())
                        .build();
                type.addConfiguration(MINE_MINERALS_IN_BASE, mineMinerals);

            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{IS_BASE})))
            .usingTypesForFactSets(new HashSet<>(Arrays.asList(new FactKey<?>[]{HAS_HATCHERY})))
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(new DesireKey[]{AM_I_BASE, AM_I_STILL_BASE})))
            .desiresForOthers(new HashSet<>(Arrays.asList(new DesireKey[]{MINE_MINERALS_IN_BASE})))
            .build();

    @Override
    public Optional<AgentRegion> createAgent(Region region, BotFacade botFacade) {
        return Optional.of(new AgentRegion(REGION, botFacade, region));
    }

    public static final AgentTypeRegion REGION = AgentTypeRegion.builder()
            .name("REGION")
            .initializationStrategy(type -> {

                //todo

            })
            .build();
}
