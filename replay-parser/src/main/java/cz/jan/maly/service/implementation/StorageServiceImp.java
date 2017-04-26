package cz.jan.maly.service.implementation;

import cz.jan.maly.model.decision.DecisionPointDataStructure;
import cz.jan.maly.model.metadata.AgentTypeID;
import cz.jan.maly.model.metadata.DesireKeyID;
import cz.jan.maly.model.tracking.Replay;
import cz.jan.maly.model.tracking.Trajectory;
import cz.jan.maly.service.StorageService;
import cz.jan.maly.utils.MyLogger;
import cz.jan.maly.utils.SerializationUtil;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cz.jan.maly.utils.Configuration.getParsedAgentTypesContainedInStorage;
import static cz.jan.maly.utils.Configuration.getParsedDesireTypesForAgentTypeContainedInStorage;

/**
 * StorageService implementation... as singleton
 * Created by Jan on 21-Apr-17.
 */
public class StorageServiceImp implements StorageService {
    private static StorageServiceImp instance = null;

    //Serializers
    public final Serializer<Replay> replaySerializer = new Replay.ReplaySerializer();

    //databases
    private static final String storageFolder = "storage";
    private static final String parsingFolder = storageFolder + "/parsing";
    private static final String outputFolder = storageFolder + "/output";
    private static final String dbFileReplays = storageFolder + "/replays.db";

    private StorageServiceImp() {
        //singleton
    }

    /**
     * Return database
     */
    private DB initDatabase(String databaseFile) {
        return DBMaker.fileDB(databaseFile).make();
    }

    static StorageService getInstance() {
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

    @Override
    public void saveTrajectory(AgentTypeID agentTypeID, DesireKeyID desireKeyID, List<Trajectory> trajectories) {
        createDirectoryIfItDoesNotExist(agentTypeID.getName(), parsingFolder);
        String path = parsingFolder + "/" + agentTypeID.getName() + "/" + desireKeyID.getName() + ".db";
        ArrayList<Trajectory> savedTrajectories;
        try {
            savedTrajectories = SerializationUtil.deserialize(path);
        } catch (Exception e) {
            savedTrajectories = new ArrayList<>();
            MyLogger.getLogger().info("Could not obtain serialized list, creating new one. Due to " + e.getLocalizedMessage());
        }
        savedTrajectories.addAll(trajectories);
        try {
            SerializationUtil.serialize(savedTrajectories, path);
        } catch (Exception e) {
            MyLogger.getLogger().warning("Could not save list. Due to " + e.getLocalizedMessage());
        }
    }

    @Override
    public Map<AgentTypeID, Set<DesireKeyID>> getParsedAgentTypesWithDesiresTypesContainedInStorage() {
        return getParsedAgentTypesContainedInStorage(parsingFolder).stream()
                .collect(Collectors.toMap(Function.identity(), t -> getParsedDesireTypesForAgentTypeContainedInStorage(t, parsingFolder)));
    }

    @Override
    public List<Trajectory> getTrajectories(AgentTypeID agentTypeID, DesireKeyID desireKeyID) throws Exception {
        return SerializationUtil.deserialize(parsingFolder + "/" + agentTypeID.getName() + "/" + desireKeyID.getName() + ".db");
    }

    @Override
    public void storeLearntDecision(DecisionPointDataStructure structure, AgentTypeID agentTypeID, DesireKeyID desireKeyID) throws Exception {
        createDirectoryIfItDoesNotExist(agentTypeID.getName(), outputFolder);
        String path = outputFolder + "/" + agentTypeID.getName() + "/" + desireKeyID.getName() + ".db";
        SerializationUtil.serialize(structure, path);
    }

    /**
     * Create story directory for agent
     *
     * @param name
     */
    private void createDirectoryIfItDoesNotExist(String name, String directory) {
        File file = new File(directory + "/" + name);
        if (!file.exists()) {
            if (file.mkdir()) {
                MyLogger.getLogger().info("Creating storage directory for " + name);
            } else {
                MyLogger.getLogger().warning("Could not create storage directory for " + name);
            }
        }
    }
}
