package cz.jan.maly.service.implementation;

import bwapi.DefaultBWListener;
import bwapi.Mirror;
import bwapi.Player;
import bwta.BWTA;
import cz.jan.maly.model.Replay;
import cz.jan.maly.model.game_info.GameData;
import cz.jan.maly.service.FileReplayParserService;
import cz.jan.maly.service.ReplayParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
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

    private Mirror mirror = new Mirror();

    private GameData currentGame;
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

        @Override
        public void onStart() {
            log.info("New game started");
            currentGame = new GameData(mirror.getGame());

            //Use BWTA to analyze map
            //This may take a few minutes if the map is processed first time!
            log.info("Analyzing map...");
            BWTA.readMap();
            BWTA.analyze();
            log.info("Map data ready");

            //speed up game to maximal possible
            currentGame.getGame().setLocalSpeed(0);

            //todo check if game was already parsed, if so, leave game and move to next play
            //todo if not start processing of game

        }

        @Override
        public void onEnd(boolean b){
            log.info("Game has finished. Processing data...");

            //todo collect all data and save them

            setNextReplay();
        }

        @Override
        public void onFrame() {

            //todo collect game states

        }

        @Override
        public void run() {
            mirror.getModule().setEventListener(this);
            mirror.startGame();
        }
    }

}