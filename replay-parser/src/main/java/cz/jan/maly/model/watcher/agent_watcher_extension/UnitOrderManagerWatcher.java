package cz.jan.maly.model.watcher.agent_watcher_extension;

import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.game.wrappers.AUnitTypeWrapper;
import cz.jan.maly.model.watcher.*;
import cz.jan.maly.model.watcher.agent_watcher_type_extension.UnitOrderManagerWatcherType;
import cz.jan.maly.service.WatcherMediatorService;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static cz.jan.maly.model.bot.AgentTypes.EGG;
import static cz.jan.maly.model.bot.AgentTypes.LARVA;
import static cz.jan.maly.model.bot.DesireKeys.*;
import static cz.jan.maly.model.bot.FactKeys.IS_MORPHING_TO;
import static cz.jan.maly.model.bot.FeatureContainerHeaders.*;

/**
 * Implementation of watcher for planing unit ordering
 * Created by Jan on 30-Apr-17.
 */
public class UnitOrderManagerWatcher extends AgentWatcher<UnitOrderManagerWatcherType> {

    public UnitOrderManagerWatcher() {
        super(UnitOrderManagerWatcherType.builder()
                .agentTypeID(AgentTypes.UNIT_ORDER_MANAGER)
                .planWatchers(Arrays.asList(new AgentWatcherType.PlanWatcherInitializationStrategy[]{

                                //BOOST_AIR
                                () -> new PlanWatcher(() -> new FeatureContainer(BOOSTING_AIR), BOOST_AIR) {
                                    private long flayersBeingConstructs = 0;

                                    @Override
                                    protected boolean isAgentCommitted(WatcherMediatorService mediatorService, Beliefs beliefs) {

                                        //morphing to flayer (not overlord)
                                        long flayersBeingConstructsCurrentNumber = mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(LARVA.getName())
                                                        || agentWatcher.getAgentWatcherType().getName().equals(EGG.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_MORPHING_TO))
                                                .filter(Optional::isPresent)
                                                .map(Optional::get)
                                                .filter(typeWrapper -> typeWrapper.isFlyer() && !typeWrapper.equals(AUnitTypeWrapper.OVERLORD_TYPE))
                                                .count();
                                        if (flayersBeingConstructsCurrentNumber != flayersBeingConstructs) {
                                            boolean isGreater = flayersBeingConstructsCurrentNumber > flayersBeingConstructs;
                                            flayersBeingConstructs = flayersBeingConstructsCurrentNumber;
                                            return isGreater;
                                        }
                                        return false;
                                    }

                                    @Override
                                    protected Stream<AgentWatcher<?>> streamOfAgentsToNotifyAboutCommitment() {
                                        return Stream.empty();
                                    }
                                },

                                //BOOST_GROUND_MELEE
                                () -> new PlanWatcher(() -> new FeatureContainer(BOOSTING_GROUND_MELEE), BOOST_GROUND_MELEE) {
                                    private long lingsBeingConstructs = 0;

                                    @Override
                                    protected boolean isAgentCommitted(WatcherMediatorService mediatorService, Beliefs beliefs) {

                                        //morphing to ling
                                        long lingsBeingConstructsCurrentNumber = mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(LARVA.getName())
                                                        || agentWatcher.getAgentWatcherType().getName().equals(EGG.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_MORPHING_TO))
                                                .filter(Optional::isPresent)
                                                .map(Optional::get)
                                                .filter(typeWrapper -> typeWrapper.equals(AUnitTypeWrapper.ZERGLING_TYPE))
                                                .count();
                                        if (lingsBeingConstructsCurrentNumber != lingsBeingConstructs) {
                                            boolean isGreater = lingsBeingConstructsCurrentNumber > lingsBeingConstructs;
                                            lingsBeingConstructs = lingsBeingConstructsCurrentNumber;
                                            return isGreater;
                                        }
                                        return false;
                                    }

                                    @Override
                                    protected Stream<AgentWatcher<?>> streamOfAgentsToNotifyAboutCommitment() {
                                        return Stream.empty();
                                    }
                                },

                                //BOOST_GROUND_RANGED
                                () -> new PlanWatcher(() -> new FeatureContainer(BOOSTING_GROUND_RANGED), BOOST_GROUND_RANGED) {
                                    private long rangedBeingConstructs = 0;

                                    @Override
                                    protected boolean isAgentCommitted(WatcherMediatorService mediatorService, Beliefs beliefs) {

                                        //morphing to any other ground attack unit except ling
                                        long rangedBeingConstructsCurrentNumber = mediatorService.getStreamOfWatchers()
                                                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getName().equals(LARVA.getName())
                                                        || agentWatcher.getAgentWatcherType().getName().equals(EGG.getName()))
                                                .map(agentWatcher -> agentWatcher.getBeliefs().returnFactValueForGivenKey(IS_MORPHING_TO))
                                                .filter(Optional::isPresent)
                                                .map(Optional::get)
                                                .filter(typeWrapper -> !typeWrapper.isFlyer()
                                                        && !typeWrapper.equals(AUnitTypeWrapper.ZERGLING_TYPE)
                                                        && !typeWrapper.isWorker())
                                                .count();
                                        if (rangedBeingConstructsCurrentNumber != rangedBeingConstructs) {
                                            boolean isGreater = rangedBeingConstructsCurrentNumber > rangedBeingConstructs;
                                            rangedBeingConstructs = rangedBeingConstructsCurrentNumber;
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
