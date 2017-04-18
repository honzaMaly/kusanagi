package cz.jan.maly.service.implementation;

import com.google.common.io.Files;
import cz.jan.maly.model.Replay;
import cz.jan.maly.service.FileReplayParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Concrete implementation of service to parse replays files
 * Created by Jan on 30-Oct-16.
 */
@Slf4j
@Service
public class FileReplayParserServiceImpl implements FileReplayParserService {

    private final List<Replay> replaysToParse = new ArrayList<>();
    private final Pattern lineWithMatchPattern = Pattern.compile("map\\s=\\s[\\w\\\\\\*\\.\\?-]+");
    @Value(value = "${paths-to-replays.replays-path}")
    private String replaysPath;
    @Value("${paths-to-replays.bots}")
    private String botsFolder;
    @Value("${paths-to-replays.players}")
    private String playersFolder;
    @Value("${path-bwapi-ini.path}")
    private String bwapiIniPath;
    @Value("${path-starcraft.path}")
    private String starcraftPath;
    private File bwapiIni;
    private File starcraftFolder;

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
            } else {
                if (file.isDirectory()){
                    List<File> replays = getAllFilesInFolder(directoryName+"\\"+file.getName());
                    files.addAll(replays);
                }
            }
        }
        return files;
    }

    @PostConstruct
    public void intConfigurationFileAndGameDirectoryFields() throws Exception {
        bwapiIni = new File(bwapiIniPath);
        starcraftFolder = new File(starcraftPath);
    }

    @Override
    public void loadReplaysToParse() {
        addReplaysToMap(replaysPath + "\\" + playersFolder, false);
//        addReplaysToMap(replaysPath + "\\" + botsFolder, true);
        log.info("Replays to be parsed: " + replaysToParse.size());
    }

    private void addReplaysToMap(String pathToReplays, boolean isForBots) {
        List<File> allReplays = getAllFilesInFolder(pathToReplays);
        allReplays.forEach(file -> replaysToParse.add(new Replay(file, isForBots)));
    }

    @Override
    public Optional<Replay> setNextReplayToPlay() throws NoSuchFileException {
        if (replaysToParse.isEmpty()) {
            throw new NoSuchFileException("All replayes have been parsed");
        } else {
            Replay replayToParseNext = replaysToParse.remove(0);
            try {
                setupReplayInConfigurationFile(replayToParseNext.getFile());
                log.info("New replay was setup in configuration file. Replay file set to: " + replayToParseNext.getFile());
            } catch (IOException e) {
                log.error("Could not setup replay. " + e);
                return Optional.empty();
            }
            return Optional.of(replayToParseNext);
        }
    }

    private void setupReplayInConfigurationFile(File replayFile) throws IOException {
        Path pathToReplay = replayFile.toPath();
        Path pathOfSC = starcraftFolder.toPath();
        String pathToReplayRelativeToSCFolder = pathOfSC.relativize(pathToReplay).toString();

        List<String> fileLines = Files.readLines(bwapiIni, StandardCharsets.UTF_8);
        for (int i = 0; i < fileLines.size(); i++) {
            Matcher matcher = lineWithMatchPattern.matcher(fileLines.get(i));
            if (matcher.matches()) {
                fileLines.set(i, "map = " + pathToReplayRelativeToSCFolder);
                break;
            }
        }
        String fileContent = fileLines.stream().map(Object::toString).collect(Collectors.joining("\n"));
        Files.write(fileContent, bwapiIni, StandardCharsets.UTF_8);
    }

}
