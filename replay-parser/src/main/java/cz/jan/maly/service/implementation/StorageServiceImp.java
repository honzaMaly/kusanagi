package cz.jan.maly.service.implementation;

import cz.jan.maly.model.tracking.Replay;
import cz.jan.maly.service.StorageService;
import cz.jan.maly.utils.MyLogger;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.io.File;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * StorageService implementation... as singleton
 * Created by Jan on 21-Apr-17.
 */
public class StorageServiceImp implements StorageService {
    private static StorageServiceImp instance = null;

    //Serializers
    public final Serializer<Replay> replaySerializer = new Replay.ReplaySerializer();

    //databases
    private static final String dbFileReplays = "storage/replays.db";

    private StorageServiceImp() {
        //singleton
    }

    /**
     * Return database
     */
    private DB initDatabase(String databaseFile) {
        return DBMaker.fileDB(databaseFile).make();
    }

    public static StorageServiceImp getInstance() {
        if (instance == null) {
            instance = new StorageServiceImp();
        }
        return instance;
    }

    private Set<Replay> getReplays(DB db) {
        return db.hashSet("replays")
                .serializer(replaySerializer)
                .createOrOpen();
    }

    @Override
    public Set<File> filterNotPlayedReplays(Set<File> files) {
        DB db = initDatabase(dbFileReplays);
        Set<File> replays = getReplays(db).stream()
                .map(Replay::getReplayFile)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        db.close();
        return files.stream()
                .filter(file -> !replays.contains(file))
                .collect(Collectors.toSet());
    }

    @Override
    public void markReplayAsParsed(Replay replay) {
        DB db = initDatabase(dbFileReplays);
        Set<Replay> replays = getReplays(db);
        if (replays.contains(replay)) {
            MyLogger.getLogger().info("Replay is already contained.");
        }
        replays.add(replay);
        db.commit();
        db.close();
    }
}
