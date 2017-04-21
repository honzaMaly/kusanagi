package cz.jan.maly.service;

import cz.jan.maly.model.tracking.Replay;

import java.io.File;
import java.util.Set;

/**
 * StorageService contract - to store/load entities
 * Created by Jan on 21-Apr-17.
 */
public interface StorageService {

    /**
     * Load replays associated with given files if exists
     *
     * @param files
     * @return
     */
    Set<File> filterNotPlayedReplays(Set<File> files);

    /**
     * Save or update given replay
     *
     * @param replay
     */
    void markReplayAsParsed(Replay replay);

    //todo save replay with both players

}
