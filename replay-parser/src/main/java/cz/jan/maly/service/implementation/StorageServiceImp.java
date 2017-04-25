package cz.jan.maly.service.implementation;

import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.bot.DesireKeys;
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
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
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
    private static final String storageFolder = "storage";
    private static final String parsingFolder = "parsing";
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

    @Override
    public void saveTrajectory(AgentTypeID agentTypeID, DesireKeyID desireKeyID, List<Trajectory> trajectories) {
        createDirectoryIfItDoesNotExist(agentTypeID.getName(), parsingFolder);
        String path = storageFolder + "/" + parsingFolder + "/" + agentTypeID.getName() + "/" + desireKeyID.getName() + ".db";
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
        return getParsedAgentTypesContainedInStorage().stream()
                .collect(Collectors.toMap(Function.identity(), this::getParsedDesireTypesForAgentTypeContainedInStorage));
    }

    @Override
    public List<Trajectory> getTrajectories(AgentTypeID agentTypeID, DesireKeyID desireKeyID) throws Exception {
        return SerializationUtil.deserialize(storageFolder + "/" + parsingFolder + "/" + agentTypeID.getName() + "/" + desireKeyID.getName() + ".db");
    }

    /**
     * Map static fields of agentTypeId from AgentTypes to folders in storage
     *
     * @return
     */
    private Set<AgentTypeID> getParsedAgentTypesContainedInStorage() {
        File directory = new File(storageFolder + "/" + parsingFolder);
        Set<String> foldersInParsingDirectory = Arrays.stream(directory.listFiles())
                .filter(File::isDirectory)
                .map(File::getName)
                .collect(Collectors.toSet());
        return Arrays.stream(AgentTypes.class.getDeclaredFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .filter(field -> field.getType().equals(AgentTypeID.class))
                .map(field -> {
                            try {
                                return (AgentTypeID) field.get(null);
                            } catch (IllegalAccessException e) {
                                MyLogger.getLogger().warning(e.getLocalizedMessage());
                            }
                            return null;
                        }
                )
                .filter(Objects::nonNull)
                .filter(agentTypeID -> foldersInParsingDirectory.contains(agentTypeID.getName()))
                .collect(Collectors.toSet());
    }

    /**
     * Map static fields of desireKeyID from DesireKeys to folders in storage
     *
     * @return
     */
    private Set<DesireKeyID> getParsedDesireTypesForAgentTypeContainedInStorage(AgentTypeID agentTypeID) {
        File directory = new File(storageFolder + "/" + parsingFolder + "/" + agentTypeID.getName());
        Set<String> filesInParsingDirectory = Arrays.stream(directory.listFiles())
                .filter(File::isFile)
                .map(File::getName)
                .map(s -> s.replace(".db", ""))
                .collect(Collectors.toSet());
        return Arrays.stream(DesireKeys.class.getDeclaredFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .filter(field -> field.getType().equals(DesireKeyID.class))
                .map(field -> {
                            try {
                                return (DesireKeyID) field.get(null);
                            } catch (IllegalAccessException e) {
                                MyLogger.getLogger().warning(e.getLocalizedMessage());
                            }
                            return null;
                        }
                )
                .filter(Objects::nonNull)
                .filter(desireKeyID -> filesInParsingDirectory.contains(desireKeyID.getName()))
                .collect(Collectors.toSet());
    }

    /**
     * Create story directory for agent
     *
     * @param name
     */
    private void createDirectoryIfItDoesNotExist(String name, String directory) {
        File file = new File(storageFolder + "/" + directory + "/" + name);
        if (!file.exists()) {
            if (file.mkdir()) {
                MyLogger.getLogger().info("Creating storage directory for " + name);
            } else {
                MyLogger.getLogger().warning("Could not create storage directory for " + name);
            }
        }
    }
}
