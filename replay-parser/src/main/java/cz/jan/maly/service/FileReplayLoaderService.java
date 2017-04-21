package cz.jan.maly.service;

import java.io.File;

/**
 * Interface to describe service for replays file parser
 * Created by Jan on 30-Oct-16.
 */
public interface FileReplayLoaderService {

    void loadReplaysToParse();

    File returnNextReplayToPlay() throws Exception;

}
