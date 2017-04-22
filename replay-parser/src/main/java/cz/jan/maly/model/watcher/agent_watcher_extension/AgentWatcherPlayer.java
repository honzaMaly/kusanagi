package cz.jan.maly.model.watcher.agent_watcher_extension;

import bwapi.Player;
import bwapi.UnitType;
import cz.jan.maly.model.AgentMakingObservations;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.watcher.AgentWatcher;
import cz.jan.maly.model.watcher.AgentWatcherType;
import cz.jan.maly.model.watcher.FeatureContainer;
import cz.jan.maly.model.watcher.PlanWatcher;
import cz.jan.maly.model.watcher.agent_watcher_type_extension.AgentWatcherPlayerType;

import java.util.Arrays;
import java.util.HashSet;

import static cz.jan.maly.model.bot.DesireKeys.BUILD_POOL;
import static cz.jan.maly.model.bot.FactKeys.AVAILABLE_MINERALS;
import static cz.jan.maly.model.bot.FeatureContainerHeaders.BUILDING_POOL;

/**
 * Implementation of watcher for player
 * Created by Jan on 18-Apr-17.
 */
public class AgentWatcherPlayer extends AgentWatcher<AgentWatcherPlayerType> implements AgentMakingObservations {
    private final Player player;

    public AgentWatcherPlayer(Player player) {
        super(AgentWatcherPlayerType.builder()
                .factKeys(new HashSet<>(Arrays.asList(new FactKey<?>[]{AVAILABLE_MINERALS})))
                .playerEnvironmentObservation((aPlayer, beliefs) -> beliefs.updateFact(AVAILABLE_MINERALS, (double) aPlayer.minerals()))
                .agentTypeID(AgentTypes.PLAYER)
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
