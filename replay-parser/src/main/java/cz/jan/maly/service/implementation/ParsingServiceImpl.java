package cz.jan.maly.service.implementation;

import com.google.common.io.Files;
import cz.jan.maly.service.Observer;
import cz.jan.maly.service.ParsingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of service to parse replays
 * Created by Jan on 30-Oct-16.
 */
@Slf4j
@Service
public class ParsingServiceImpl implements ParsingService, Observer {

    @Value(value = "${paths-to-replays.replays-path}")
    private String replaysPath;

    @Value("${paths-to-replays.bots}")
    private String botsFolder;

    @Value("${paths-to-replays.players}")
    private String playersFolder;

    @Value("${chaosluncher-configuration.path}")
    private String chaosluncherPath;

    private List<File> playersReplays, botsReplays;

    @Override
    public void parseReplays() {
        playersReplays = getAllFilesInFolder(replaysPath + "\\" + playersFolder);

        //todo get all replay files to work with

        //todo method to setup replay

        //todo start game

    }

    /**
     * Method to start Chaosluncher with predefined configuration. Sadly process can be only started, not closed. See comment in method body to setup it appropriately
     * @throws IOException
     */
    private void startChaosluncher() throws IOException {
        /**
         * To fully automate process of lunching Starcraft:
         * Lunch C haosluncher with full privileges, guide to setup such a shortucut is on http://lifehacker.com/how-to-eliminate-uac-prompts-for-specific-applications-493128966.
         * Also do not forget to set: Setting > Run Starcraft on Startup to initialize game based on configuration file.
         */
        Runtime rt = Runtime.getRuntime();
        rt.exec("cmd /c start \"\" \""+ chaosluncherPath +"\"");
    }

    private static List<File> getAllFilesInFolder(String directoryName) {
        File directory = new File(directoryName);

        //get all the files from a directory
        File[] fList = directory.listFiles();
        List<File> files = new ArrayList<>();
        for (File file : fList) {
            if (file.isFile()) {
                String fileExtension = Files.getFileExtension(file.getPath());
                if (fileExtension.equals("rep")) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    @Override
    public void update(String parsedFileName) {
        //todo replace replay file
    }

}
