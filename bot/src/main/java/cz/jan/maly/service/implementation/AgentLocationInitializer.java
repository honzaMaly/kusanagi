package cz.jan.maly.service.implementation;

import bwta.BaseLocation;
import bwta.Region;
import cz.jan.maly.model.agent.AgentBaseLocation;
import cz.jan.maly.model.agent.AgentRegion;
import cz.jan.maly.model.agent.types.AgentTypeBaseLocation;
import cz.jan.maly.model.agent.types.AgentTypeRegion;
import cz.jan.maly.model.game.wrappers.ABaseLocationWrapper;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithSharedDesire;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValue;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValueSet;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.service.LocationInitializer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.jan.maly.model.AgentsUnitTypes.HATCHERY;
import static cz.jan.maly.model.DesiresKeys.AM_I_BASE;
import static cz.jan.maly.model.DesiresKeys.MINE_MINERALS_IN_BASE;
import static cz.jan.maly.model.FactsKeys.HAS_HATCHERY;
import static cz.jan.maly.model.FactsKeys.IS_BASE;
import static cz.jan.maly.model.bot.BasicFactsKeys.*;

/**
 * Strategy to initialize player
 * Created by Jan on 05-Apr-17.
 */
public class AgentLocationInitializer implements LocationInitializer {

    public static final AgentTypeBaseLocation BASE_LOCATION = AgentTypeBaseLocation.builder()
            .name("BASE_LOCATION")
            .initializationStrategy(type -> {

                //todo remove is base from beliefs

                //Am I base - if it is not base, reason about being base - has any hatcheries?
                ConfigurationWithCommand.WithReasoningCommandDesiredBySelf amIBase = ConfigurationWithCommand.
                        WithReasoningCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ReasoningCommand(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                ABaseLocationWrapper base = memory.returnFactValueForGivenKey(IS_BASE_LOCATION).get();
                                Set<AUnit> hatcheries = memory.getReadOnlyMemoriesForAgentType(HATCHERY)
                                        .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(REPRESENTS_UNIT))
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().isPresent())
                                        .filter(aUnitOfPlayer -> aUnitOfPlayer.getNearestBaseLocation().get().isOnSameCoordinates(base))
                                        .collect(Collectors.toSet());
                                memory.updateFactSetByFacts(HAS_HATCHERY, hatcheries);
                                if (!hatcheries.isEmpty()) {
                                    memory.updateFact(IS_BASE, true);
                                }
                                return true;
                            }
                        })
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> dataForDecision.getFeatureValueBeliefs(IS_BASE) == 0)
                                .beliefTypes(new HashSet<>(Arrays.asList(new FactWithOptionalValue<?>[]{
                                        new FactWithOptionalValue<>(IS_BASE, aBoolean -> {
                                            if (aBoolean.isPresent()) {
                                                if (aBoolean.get()) {
                                                    return 1;
                                                }
                                            }
                                            return 0;
                                        })
                                })))
                                .build())
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> dataForDecision.getFeatureValueBeliefSets(HAS_HATCHERY) == 0)
                                .beliefSetTypes(new HashSet<>(Arrays.asList(new FactWithOptionalValueSet<?>[]{
                                        new FactWithOptionalValueSet<>(HAS_HATCHERY, aUnitStream -> aUnitStream.map(aUnitStream1 -> (double) aUnitStream1.count()).orElse(0.0))
                                })))
                                .build())
                        .build();
                type.addConfiguration(AM_I_BASE, amIBase);

                //Make request to start mining. Remove request when there are no more minerals to mine or there is no hatchery to bring mineral in
                ConfigurationWithSharedDesire mineMinerals = ConfigurationWithSharedDesire.builder()
                        .sharedDesireKey(MINE_MINERALS_IN_BASE)
                        .decisionInDesire(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> dataForDecision.getFeatureValueBeliefs(IS_BASE) == 1
                                        && dataForDecision.getFeatureValueDesireBeliefSets(MINERAL) > 0)
                                .beliefTypes(new HashSet<>(Arrays.asList(new FactWithOptionalValue<?>[]{
                                        new FactWithOptionalValue<>(IS_BASE, aBoolean -> {
                                            if (aBoolean.isPresent()) {
                                                if (aBoolean.get()) {
                                                    return 1;
                                                }
                                            }
                                            return 0;
                                        })
                                })))
                                .parameterValueSetTypes(new HashSet<>(Arrays.asList(new FactWithOptionalValueSet<?>[]{
                                        new FactWithOptionalValueSet<>(MINERAL, aUnitStream -> aUnitStream.map(aUnitStream1 -> (double) aUnitStream1.count()).orElse(0.0))
                                })))
                                .build()
                        )
                        .decisionInIntention(CommitmentDeciderInitializer.builder()
                                .decisionStrategy(dataForDecision -> dataForDecision.getFeatureValueBeliefs(IS_BASE) == 0
                                        || dataForDecision.getFeatureValueBeliefSets(MINERAL) == 0
                                        || dataForDecision.getFeatureValueBeliefSets(MINERAL) != dataForDecision.getFeatureValueDesireBeliefSets(MINERAL)
                                )
                                .beliefTypes(new HashSet<>(Arrays.asList(new FactWithOptionalValue<?>[]{
                                        new FactWithOptionalValue<>(IS_BASE, aBoolean -> {
                                            if (aBoolean.isPresent()) {
                                                if (aBoolean.get()) {
                                                    return 1;
                                                }
                                            }
                                            return 0;
                                        })
                                })))
                                .beliefSetTypes(new HashSet<>(Arrays.asList(new FactWithOptionalValueSet<?>[]{
                                        new FactWithOptionalValueSet<>(MINERAL, aUnitStream -> aUnitStream.map(aUnitStream1 -> (double) aUnitStream1.count()).orElse(0.0))
                                })))
                                .parameterValueSetTypes(new HashSet<>(Arrays.asList(new FactWithOptionalValueSet<?>[]{
                                        new FactWithOptionalValueSet<>(MINERAL, aUnitStream -> aUnitStream.map(aUnitStream1 -> (double) aUnitStream1.count()).orElse(0.0))
                                })))
                                .build()
                        )
                        .build();
                type.addConfiguration(MINE_MINERALS_IN_BASE, mineMinerals);

            })
            .usingTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{IS_BASE})))
            .usingTypesForFactSets(new HashSet<>(Arrays.asList(new FactKey<?>[]{HAS_HATCHERY})))
            .desiresWithIntentionToReason(new HashSet<>(Arrays.asList(new DesireKey[]{AM_I_BASE})))
            .desiresForOthers(new HashSet<>(Arrays.asList(new DesireKey[]{MINE_MINERALS_IN_BASE})))
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
