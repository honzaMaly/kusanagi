package cz.jan.maly.model.watcher.agent_watcher_extension;

import bwapi.Player;
import bwapi.Unit;
import bwapi.UnitType;
import cz.jan.maly.model.AgentMakingObservations;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.game.wrappers.WrapperTypeFactory;
import cz.jan.maly.model.watcher.AgentWatcher;
import cz.jan.maly.model.watcher.AgentWatcherType;
import cz.jan.maly.model.watcher.FeatureContainer;
import cz.jan.maly.model.watcher.PlanWatcher;
import cz.jan.maly.model.watcher.agent_watcher_type_extension.AgentWatcherPlayerType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

import static cz.jan.maly.model.bot.DesireKeys.BUILD_POOL;
import static cz.jan.maly.model.bot.FactConverters.AVAILABLE_MINERALS_COUNT;
import static cz.jan.maly.model.bot.FactKeys.*;
import static cz.jan.maly.model.bot.FeatureContainerHeaders.BUILDING_POOL;

/**
 * Implementation of watcher for player
 * Created by Jan on 18-Apr-17.
 */
public class AgentWatcherPlayer extends AgentWatcher<AgentWatcherPlayerType> implements AgentMakingObservations {
    private final Player player;

    public AgentWatcherPlayer(Player player) {
        super(AgentWatcherPlayerType.builder()
                .factKeys(new HashSet<>(Arrays.asList(AVAILABLE_MINERALS, HAS_RESOURCES_TO_BUILD_POOL, COUNT_OF_POOLS)))
                .playerEnvironmentObservation((aPlayer, beliefs) -> beliefs.updateFact(AVAILABLE_MINERALS, (double) aPlayer.minerals()))
                .agentTypeID(AgentTypes.PLAYER)
                .reasoning((bl, ms) -> {
                    double minerals = bl.getFeatureValueOfFact(AVAILABLE_MINERALS_COUNT);
                    bl.updateFact(HAS_RESOURCES_TO_BUILD_POOL, minerals >= WrapperTypeFactory.createFrom(UnitType.Zerg_Spawning_Pool).getMineralPrice());

                    //todo nasty hack remove. pull it from beliefs
                    bl.updateFact(COUNT_OF_POOLS, (double)player.getUnits().stream()
                            .filter(unit -> unit.getType().equals(UnitType.Zerg_Spawning_Pool))
                            .filter(Unit::isCompleted)
                            .count());
                })
                .planWatchers(Arrays.asList(new AgentWatcherType.PlanWatcherInitializationStrategy[]{

                                //check for new pool building
                                () -> new PlanWatcher(() -> new FeatureContainer(BUILDING_POOL), BUILD_POOL) {
                                    private long poolsInGame = 0;

                                    @Override
                                    protected boolean isAgentCommitted() {
                                        long poolsInGameCurrently = player.getUnits().stream()
                                                .filter(unit -> unit.getType().equals(UnitType.Zerg_Spawning_Pool))
                                                .count();
                                        if (poolsInGame != poolsInGameCurrently) {
                                            poolsInGame = poolsInGameCurrently;
                                            return true;
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
        this.player = player;
    }

    public void makeObservation() {
        agentWatcherType.getPlayerEnvironmentObservation().updateBeliefs(player, beliefs);
    }

}
