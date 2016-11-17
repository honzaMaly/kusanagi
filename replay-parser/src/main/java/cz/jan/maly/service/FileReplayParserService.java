package cz.jan.maly.service;

import cz.jan.maly.model.Replay;

import java.nio.file.NoSuchFileException;
import java.util.Optional;

/**
 * Interface to describe service for replays file parser
 * Created by Jan on 30-Oct-16.
 */
public interface FileReplayParserService {

    void loadReplaysToParse();

    Optional<Replay> setNextReplayToPlay() throws NoSuchFileException;

}
