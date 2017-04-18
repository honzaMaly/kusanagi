package cz.jan.maly.model.watcher.agent_watcher_extension;

import bwapi.Player;
import bwapi.UnitType;
import cz.jan.maly.model.AgentMakingObservations;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.watcher.*;
import cz.jan.maly.model.watcher.agent_watcher_type_extension.AgentWatcherPlayerType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static cz.jan.maly.model.bot.BasicFactsKeys.AVAILABLE_MINERALS;
import static cz.jan.maly.model.watcher.DesireID.BUILD_POOL;

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
                .name("PLAYER")
                .planWatchers(Arrays.asList(new AgentWatcherType.PlanWatcherInitializationStrategy[]{

                                //check for new pool building
                                () -> new PlanWatcher(() -> FeatureContainer.builder()
                                        .convertersForFacts(new HashSet<>(Arrays.asList(new FactConverter<?>[]{
                                                        new FactConverter<Double>(AVAILABLE_MINERALS, value -> Optional.ofNullable(value).orElse(0.0), 0)
                                                }))
                                        )
                                        .build(), BUILD_POOL) {
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
