package cz.jan.maly.model.watcher.agent_watcher_extension;

import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.game.wrappers.AUnitTypeWrapper;
import cz.jan.maly.model.watcher.*;
import cz.jan.maly.model.watcher.agent_watcher_type_extension.EcoManagerWatcherType;
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
 * Implementation of watcher for eco. side of the game
 * Created by Jan on 29-Apr-17.
 */
public class EcoManagerWatcher extends AgentWatcher<EcoManagerWatcherType> {

    public EcoManagerWatcher() {
        super(EcoManagerWatcherType.builder()
                .agentTypeID(AgentTypes.ECO_MANAGER)
                .planWatchers(Arrays.asList(new AgentWatcherType.PlanWatcherInitializationStrategy[]{

                                //BUILD_EXTRACTOR
                                () -> new PlanWatcher(() -> new FeatureContainer(BUILDING_EXTRACTOR), BUILD_EXTRACTOR) {

                                    @Override
                                    protected boolean isAgentCommitted(WatcherMediatorService mediatorService, Beliefs beliefs) {

                                        //extractors being build
                                        return mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(DRONE.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_MORPHING_TO))
                                                .filter(Optional::isPresent)
                                                .map(Optional::get)
                                                .anyMatch(typeWrapper -> typeWrapper.equals(AUnitTypeWrapper.EXTRACTOR_TYPE))
                                                || mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(EXTRACTOR.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_BEING_CONSTRUCT))
                                                .filter(Optional::isPresent)
                                                .anyMatch(Optional::get);
                                    }

                                    @Override
                                    protected Stream<AgentWatcher<?>> streamOfAgentsToNotifyAboutCommitment() {
                                        return Stream.empty();
                                    }
                                },

                                //TRAINING_WORKER
                                () -> new PlanWatcher(() -> new FeatureContainer(TRAINING_WORKER), BUILD_WORKER) {
                                    private long workersBeingConstructs = 0;

                                    @Override
                                    protected boolean isAgentCommitted(WatcherMediatorService mediatorService, Beliefs beliefs) {

                                        //morphing to worker
                                        long workersBeingConstructsCurrentNumber = mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(LARVA.getName())
                                                        || agentWatcher.getAgentWatcherType().getName().equals(EGG.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_MORPHING_TO))
                                                .filter(Optional::isPresent)
                                                .map(Optional::get)
                                                .filter(AUnitTypeWrapper::isWorker)
                                                .count();
                                        if (workersBeingConstructsCurrentNumber != workersBeingConstructs) {
                                            boolean isGreater = workersBeingConstructsCurrentNumber > workersBeingConstructs;
                                            workersBeingConstructs = workersBeingConstructsCurrentNumber;
                                            return isGreater;
                                        }
                                        return false;
                                    }

                                    @Override
                                    protected Stream<AgentWatcher<?>> streamOfAgentsToNotifyAboutCommitment() {
                                        return Stream.empty();
                                    }
                                },

                                //EXPANDING
                                () -> new PlanWatcher(() -> new FeatureContainer(EXPANDING), EXPAND) {

                                    @Override
                                    protected boolean isAgentCommitted(WatcherMediatorService mediatorService, Beliefs beliefs) {

                                        //hatchery is being build
                                        return mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(DRONE.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_MORPHING_TO))
                                                .filter(Optional::isPresent)
                                                .map(Optional::get)
                                                .anyMatch(typeWrapper -> typeWrapper.equals(AUnitTypeWrapper.HATCHERY_TYPE))
                                                || mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(HATCHERY.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_BEING_CONSTRUCT))
                                                .filter(Optional::isPresent)
                                                .anyMatch(Optional::get);
                                    }

                                    @Override
                                    protected Stream<AgentWatcher<?>> streamOfAgentsToNotifyAboutCommitment() {
                                        return Stream.empty();
                                    }
                                },

                                //INCREASE_CAPACITY
                                () -> new PlanWatcher(() -> new FeatureContainer(INCREASING_CAPACITY), INCREASE_CAPACITY) {
                                    private long overlordsBeingConstructs = 0;

                                    @Override
                                    protected boolean isAgentCommitted(WatcherMediatorService mediatorService, Beliefs beliefs) {

                                        //overlord being morphed
                                        long overlordsBeingConstructsCurrentNumber = mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(LARVA.getName()) ||
                                                        agentWatcher.getAgentWatcherType().getName().equals(EGG.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_MORPHING_TO))
                                                .filter(Optional::isPresent)
                                                .map(Optional::get)
                                                .filter(typeWrapper -> typeWrapper.equals(AUnitTypeWrapper.OVERLORD_TYPE))
                                                .count();
                                        if (overlordsBeingConstructs != overlordsBeingConstructsCurrentNumber) {
                                            boolean isGreater = overlordsBeingConstructsCurrentNumber > overlordsBeingConstructs;
                                            overlordsBeingConstructs = overlordsBeingConstructsCurrentNumber;
                                            return isGreater;
                                        }
                                        return false;
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
