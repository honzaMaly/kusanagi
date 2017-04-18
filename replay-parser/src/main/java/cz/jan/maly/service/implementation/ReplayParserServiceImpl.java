package cz.jan.maly.service.implementation;

import bwapi.*;
import bwta.BWTA;
import cz.jan.maly.model.AgentMakingObservations;
import cz.jan.maly.model.Replay;
import cz.jan.maly.model.watcher.agent_watcher_extension.AgentWatcherPlayer;
import cz.jan.maly.service.FileReplayParserService;
import cz.jan.maly.service.ReplayParserService;
import cz.jan.maly.service.WatcherMediatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Concrete implementation of service to parse replays
 * Created by Jan on 17-Nov-16.
 */
@Slf4j
@Service
public class ReplayParserServiceImpl extends DefaultBWListener implements ReplayParserService {

    @Value("${chaosluncher-configuration.path}")
    private String chaosluncherPath;

    @Autowired
    private FileReplayParserService fileReplayParserService;

    @Autowired
    private WatcherMediatorService watcherMediatorService;

    private Mirror mirror = new Mirror();

    private Game currentGame;
    private Replay currentReplay;

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
        log.info("Starting Chaosluncher...");
        Runtime rt = Runtime.getRuntime();
        rt.exec("cmd /c start \"\" \"" + chaosluncherPath + "\"");
    }

    private void setNextReplay() {
        try {
            Optional<Replay> nextReplay = fileReplayParserService.setNextReplayToPlay();
            while (!nextReplay.isPresent()) {
                nextReplay = fileReplayParserService.setNextReplayToPlay();
            }
            currentReplay = nextReplay.get();
        } catch (NoSuchFileException e) {
            log.info(e.getLocalizedMessage());
        }
    }

    @Override
    public void parseReplays() {
        Thread gameListener = new Thread(new GameListener(), "GameListener");
        gameListener.start();
        fileReplayParserService.loadReplaysToParse();
        try {
            setNextReplay();
            startChaosluncher();
        } catch (IOException e) {
            log.error("Could not start Chaosluncher.");
        }
    }

    private class GameListener extends DefaultBWListener implements Runnable {
        private final List<AgentMakingObservations> agentsWithObservations = new ArrayList<>();

        @Override
        public void onStart() {
            log.info("New game started");
            currentGame = mirror.getGame();

            //Use BWTA to analyze map
            //This may take a few minutes if the map is processed first time!
            log.info("Analyzing map...");
            BWTA.readMap();
            BWTA.analyze();
            log.info("Map data ready");

            //todo check if game was already parsed, if so, leave game and move to next play
            //todo if not start processing of game

            Optional<Player> player = currentGame.getPlayers().stream()
                    .filter(p -> p.getRace().equals(Race.Zerg))
                    .findAny();

            AgentWatcherPlayer agentWatcherPlayer = new AgentWatcherPlayer(player.get());
            agentsWithObservations.add(agentWatcherPlayer);
            watcherMediatorService.addWatcher(agentWatcherPlayer);

            //speed up game to maximal possible
//            currentGame.setLocalSpeed(0);
        }

        @Override
        public void onEnd(boolean b) {
            log.info("Game has finished. Processing data...");

            //todo collect all data and save them

            setNextReplay();
            watcherMediatorService.clearAllAgents();
        }

        @Override
        public void onFrame() {

            //make observations
            agentsWithObservations.forEach(AgentMakingObservations::makeObservation);

            //watch agents, update their additional beliefs and track theirs commitment
            watcherMediatorService.watchAgents();

            //todo

        }

        @Override
        public void run() {
            mirror.getModule().setEventListener(this);
            mirror.startGame();
        }
    }

}
