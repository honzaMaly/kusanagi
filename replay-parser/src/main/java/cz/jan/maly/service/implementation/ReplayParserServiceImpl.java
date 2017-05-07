package cz.jan.maly.service.implementation;

import bwapi.*;
import bwta.BWTA;
import cz.jan.maly.model.AgentMakingObservations;
import cz.jan.maly.model.game.wrappers.ABaseLocationWrapper;
import cz.jan.maly.model.game.wrappers.AbstractPositionWrapper;
import cz.jan.maly.model.game.wrappers.UnitWrapperFactory;
import cz.jan.maly.model.game.wrappers.WrapperTypeFactory;
import cz.jan.maly.model.tracking.Replay;
import cz.jan.maly.model.watcher.agent_watcher_extension.*;
import cz.jan.maly.service.*;
import cz.jan.maly.utils.MyLogger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Concrete implementation of service to parse replays
 * Created by Jan on 17-Nov-16.
 */
public class ReplayParserServiceImpl extends DefaultBWListener implements ReplayParserService {
    private static final String chaosluncherPath = "c:\\Users\\Jan\\Desktop\\Chaosluncher Run With Full Privileges.lnk";
    private static final StorageService STORAGE_SERVICE = StorageServiceImp.getInstance();
    private FileReplayLoaderService fileReplayLoaderService = new FileReplayLoaderServiceImpl();
    private WatcherMediatorService watcherMediatorService = WatcherMediatorServiceImpl.getInstance();
    private Optional<Replay> replay;

    /**
     * Method to start Chaosluncher with predefined configuration. Sadly process can be only started, not closed. See comment in method body to setup it appropriately
     *
     * @throws IOException
     */
    private void startChaosluncher() throws IOException {
        /**
         * To fully automate process of lunching Starcraft:
         * Lunch C haosluncher with full privileges, guide to setup such a shortucut is on http://lifehacker.com/how-to-eliminate-uac-prompts-for-specific-applications-493128966.
         * Also do not forget to set: Setting > Run Starcraft on Startup to initialize game based on configuration file.
         */
        MyLogger.getLogger().info("Starting Chaosluncher...");
        Runtime rt = Runtime.getRuntime();
        rt.exec("cmd /c start \"\" \"" + chaosluncherPath + "\"");
    }

    /**
     * Return next replay to parse
     *
     * @return
     */
    private Replay getNextReplay() throws Exception {
        File nextReplay = fileReplayLoaderService.returnNextReplayToPlay();
        return new Replay(nextReplay);
    }

    /**
     * Set next replay or terminate listener
     *
     * @return
     */
    private void setNextReplay() {
        try {
            replay = Optional.of(getNextReplay());
        } catch (Exception e) {
            MyLogger.getLogger().warning(e.getLocalizedMessage());

            //terminate process
            System.exit(1);
        }
    }

    @Override
    public void parseReplays() {

        //start game listener
        Thread gameListener = new Thread(new GameListener(), "GameListener");
        gameListener.start();

        //load all not parsed replays
        fileReplayLoaderService.loadReplaysToParse();
        setNextReplay();

//        //try to lunch chaosluncher
//        try {
//            startChaosluncher();
//        } catch (IOException e) {
//
//            //terminate process
//            MyLogger.getLogger().warning("Could not start Chaosluncher. " + e.getLocalizedMessage());
//            System.exit(1);
//        }
    }

    private class GameListener extends DefaultBWListener implements Runnable {
        private final List<AgentMakingObservations> agentsWithObservations = new ArrayList<>();
        private Mirror mirror = new Mirror();
        private Game currentGame;
        private Player parsingPlayer;
        private AgentUnitHandler agentUnitHandler = null;
        //keep track of units watchers
        private final Map<Integer, UnitWatcher> watchersOfUnits = new HashMap<>();
        private boolean track = false, shouldExit = false;

        @Override
        public void onStart() {
            UnitWrapperFactory.clearCache();
            WrapperTypeFactory.clearCache();
            AbstractPositionWrapper.clearCache();

            track = false;
            MyLogger.getLogger().info("New game from replay " + replay.get().getFile());
            currentGame = mirror.getGame();

            //speed up game to maximal possible, disable gui
            currentGame.setLocalSpeed(0);
//            currentGame.setGUI(false);

            //kill no matter what to prevent crash (and rerun by outside script :))
            if (shouldExit) {
                System.exit(0);
            }

            if (!replay.get().isParsedMoreTimes()) {
                track = true;
                watchersOfUnits.clear();

                //init types
                if (agentUnitHandler == null) {
                    agentUnitHandler = new AgentUnitFactory();
                }

                //mark replay as loaded to skip it next time
                STORAGE_SERVICE.markReplayAsParsed(replay.get());

                try {

                    //Use BWTA to analyze map
                    //This may take a few minutes if the map is processed first time!
                    MyLogger.getLogger().info("Analyzing map...");
                    BWTA.readMap();
                    BWTA.analyze();
                    MyLogger.getLogger().info("Map data ready");

                    //set player to parse
                    Set<Integer> playersToParse = currentGame.getPlayers().stream()
                            .filter(p -> p.getRace().equals(Race.Zerg))
                            .filter(p -> p.allUnitCount() == 9)
                            .peek(player -> MyLogger.getLogger().info(player.getRace() + " id: " + player.getID() + " units: " + player.allUnitCount()))
                            .map(Player::getID)
                            .collect(Collectors.toSet());
                    parsingPlayer = currentGame.getPlayers().stream()
                            .filter(p -> playersToParse.contains(p.getID()))
                            .findFirst()
                            .get();

                    WatcherPlayer watcherPlayer = new WatcherPlayer(parsingPlayer);
                    agentsWithObservations.add(watcherPlayer);
                    watcherMediatorService.addWatcher(watcherPlayer);

                    //init base agents
                    BWTA.getBaseLocations().forEach(baseLocation -> {
                        BaseWatcher baseWatcher = new BaseWatcher(ABaseLocationWrapper.wrap(baseLocation), currentGame, new BaseWatcher.UpdateChecksStrategy());
                        agentsWithObservations.add(baseWatcher);
                        watcherMediatorService.addWatcher(baseWatcher);
                    });

                    //abstract managers
                    watcherMediatorService.addWatcher(new EcoManagerWatcher());
                    watcherMediatorService.addWatcher(new BuildOrderManagerWatcher());
                    watcherMediatorService.addWatcher(new UnitOrderManagerWatcher());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onUnitCreate(Unit unit) {
            if (track && parsingPlayer != null) {
                try {
                    if (parsingPlayer.getID() == unit.getPlayer().getID()) {
                        Optional<UnitWatcher> unitWatcher = agentUnitHandler.createAgentForUnit(unit, currentGame);
                        unitWatcher.ifPresent(watcher -> {
                            agentsWithObservations.add(watcher);
                            watcherMediatorService.addWatcher(watcher);
                            watchersOfUnits.put(unit.getID(), watcher);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        @Override
        public void onEnd(boolean b) {
            try {
                MyLogger.getLogger().info("Game has finished. Processing data...");

                //save trajectories and reset register
                watcherMediatorService.clearAllAgentsAndSaveTheirTrajectories();

                //if all players in queue were parsed, move to next replay
                STORAGE_SERVICE.markReplayAsParsed(replay.get());
//                setNextReplay();

                //tell app to exit
                shouldExit = true;

                MyLogger.getLogger().info("Data processed.");
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onFrame() {
            if (track && parsingPlayer != null) {
                try {

                    //make observations
                    agentsWithObservations.forEach(AgentMakingObservations::makeObservation);

                    //watch agents, update their additional beliefs and track theirs commitment
                    watcherMediatorService.tellAgentsToObserveSystemAndHandlePlans();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onUnitDestroy(Unit unit) {
            if (track && parsingPlayer != null) {
                try {
                    if (parsingPlayer.getID() == unit.getPlayer().getID()) {
                        Optional<UnitWatcher> watcher = Optional.ofNullable(watchersOfUnits.remove(unit.getID()));
                        watcher.ifPresent(unitWatcher -> watcherMediatorService.removeWatcher(unitWatcher));
                    }
                    UnitWrapperFactory.unitDied(unit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onUnitMorph(Unit unit) {
            if (track && parsingPlayer != null) {
                try {
                    if (parsingPlayer.getID() == unit.getPlayer().getID()) {
                        Optional<UnitWatcher> watcher = Optional.ofNullable(watchersOfUnits.remove(unit.getID()));
                        watcher.ifPresent(unitWatcher -> watcherMediatorService.removeWatcher(unitWatcher));
                        onUnitCreate(unit);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            mirror.getModule().setEventListener(this);
            mirror.startGame();
        }
    }

}
