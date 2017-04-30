package cz.jan.maly.model.watcher.agent_watcher_extension;

import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.game.wrappers.AUnitTypeWrapper;
import cz.jan.maly.model.watcher.*;
import cz.jan.maly.model.watcher.agent_watcher_type_extension.BuildOrderManagerWatcherType;
import cz.jan.maly.service.WatcherMediatorService;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static cz.jan.maly.model.bot.AgentTypes.*;
import static cz.jan.maly.model.bot.DesireKeys.*;
import static cz.jan.maly.model.bot.FactKeys.IS_BEING_CONSTRUCT;
import static cz.jan.maly.model.bot.FactKeys.IS_MORPHING_TO;
import static cz.jan.maly.model.bot.FeatureContainerHeaders.*;

/**
 * Implementation of watcher for planing build construction
 * Created by Jan on 30-Apr-17.
 */
public class BuildOrderManagerWatcher extends AgentWatcher<BuildOrderManagerWatcherType> {

    public BuildOrderManagerWatcher() {
        super(BuildOrderManagerWatcherType.builder()
                .agentTypeID(AgentTypes.BUILDING_ORDER_MANAGER)
                .planWatchers(Arrays.asList(new AgentWatcherType.PlanWatcherInitializationStrategy[]{

                                //ENABLE_GROUND_MELEE
                                () -> new PlanWatcher(() -> new FeatureContainer(BUILDING_POOL), ENABLE_GROUND_MELEE) {

                                    @Override
                                    protected boolean isAgentCommitted(WatcherMediatorService mediatorService, Beliefs beliefs) {

                                        //pool being build
                                        return mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(DRONE.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_MORPHING_TO))
                                                .filter(Optional::isPresent)
                                                .map(Optional::get)
                                                .anyMatch(typeWrapper -> typeWrapper.equals(AUnitTypeWrapper.SPAWNING_POOL_TYPE))
                                                || mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(SPAWNING_POOL.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_BEING_CONSTRUCT))
                                                .filter(Optional::isPresent)
                                                .anyMatch(Optional::get);
                                    }

                                    @Override
                                    protected Stream<AgentWatcher<?>> streamOfAgentsToNotifyAboutCommitment() {
                                        return Stream.empty();
                                    }
                                },

                                //UPGRADE_TO_LAIR
                                () -> new PlanWatcher(() -> new FeatureContainer(UPGRADING_TO_LAIR), UPGRADE_TO_LAIR) {

                                    @Override
                                    protected boolean isAgentCommitted(WatcherMediatorService mediatorService, Beliefs beliefs) {

                                        //lair being build
                                        return mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(LAIR.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_BEING_CONSTRUCT))
                                                .filter(Optional::isPresent)
                                                .anyMatch(Optional::get);
                                    }

                                    @Override
                                    protected Stream<AgentWatcher<?>> streamOfAgentsToNotifyAboutCommitment() {
                                        return Stream.empty();
                                    }
                                },

                                //ENABLE_AIR
                                () -> new PlanWatcher(() -> new FeatureContainer(BUILDING_SPIRE), ENABLE_AIR) {

                                    @Override
                                    protected boolean isAgentCommitted(WatcherMediatorService mediatorService, Beliefs beliefs) {

                                        //spire being build
                                        return mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(DRONE.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_MORPHING_TO))
                                                .filter(Optional::isPresent)
                                                .map(Optional::get)
                                                .anyMatch(typeWrapper -> typeWrapper.equals(AUnitTypeWrapper.SPIRE_TYPE))
                                                || mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(SPIRE.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_BEING_CONSTRUCT))
                                                .filter(Optional::isPresent)
                                                .anyMatch(Optional::get);
                                    }

                                    @Override
                                    protected Stream<AgentWatcher<?>> streamOfAgentsToNotifyAboutCommitment() {
                                        return Stream.empty();
                                    }
                                },

                                //ENABLE_GROUND_RANGED
                                () -> new PlanWatcher(() -> new FeatureContainer(BUILDING_HYDRALISK_DEN), ENABLE_GROUND_RANGED) {

                                    @Override
                                    protected boolean isAgentCommitted(WatcherMediatorService mediatorService, Beliefs beliefs) {

                                        //spire being build
                                        return mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(DRONE.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_MORPHING_TO))
                                                .filter(Optional::isPresent)
                                                .map(Optional::get)
                                                .anyMatch(typeWrapper -> typeWrapper.equals(AUnitTypeWrapper.HYDRALISK_DEN_TYPE))
                                                || mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(HYDRALISK_DEN.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_BEING_CONSTRUCT))
                                                .filter(Optional::isPresent)
                                                .anyMatch(Optional::get);
                                    }

                                    @Override
                                    protected Stream<AgentWatcher<?>> streamOfAgentsToNotifyAboutCommitment() {
                                        return Stream.empty();
                                    }
                                },

                                //ENABLE_STATIC_ANTI_AIR
                                () -> new PlanWatcher(() -> new FeatureContainer(BUILDING_EVOLUTION_CHAMBER), ENABLE_STATIC_ANTI_AIR) {

                                    @Override
                                    protected boolean isAgentCommitted(WatcherMediatorService mediatorService, Beliefs beliefs) {

                                        //spire being build
                                        return mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(DRONE.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_MORPHING_TO))
                                                .filter(Optional::isPresent)
                                                .map(Optional::get)
                                                .anyMatch(typeWrapper -> typeWrapper.equals(AUnitTypeWrapper.EVOLUTION_CHAMBER_TYPE))
                                                || mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(EVOLUTION_CHAMBER.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_BEING_CONSTRUCT))
                                                .filter(Optional::isPresent)
                                                .anyMatch(Optional::get);
                                    }

                                    @Override
                                    protected Stream<AgentWatcher<?>> streamOfAgentsToNotifyAboutCommitment() {
                                        return Stream.empty();
                                    }
                                }
                        }
                        )
                )
                .build()
        );
    }
}
