package cz.jan.maly.utils;

import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.bot.DesireKeys;
import cz.jan.maly.model.features.FeatureNormalizer;
import cz.jan.maly.model.metadata.AgentTypeID;
import cz.jan.maly.model.metadata.DesireKeyID;
import jsat.linear.distancemetrics.DistanceMetric;
import jsat.linear.distancemetrics.EuclideanDistance;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Contains configuration...
 * Created by Jan on 23-Apr-17.
 */
public class Configuration {
    public static final DistanceMetric DISTANCE_FUNCTION = new EuclideanDistance();

    /**
     * Standardize each part of feature vector according to normalizers
     *
     * @param featureVector
     * @param normalizers
     * @return
     */
    public static double[] normalizeFeatureVector(double[] featureVector, List<FeatureNormalizer> normalizers) {
        double[] normalizeFeatureVector = new double[featureVector.length];
        for (int i = 0; i < featureVector.length; i++) {
            normalizeFeatureVector[i] = normalizers.get(i).zScoreNormalization(featureVector[i]);
        }
        return normalizeFeatureVector;
    }

    /**
     * Map static fields of agentTypeId from AgentTypes to folders in storage
     *
     * @return
     */
    public static Set<AgentTypeID> getParsedAgentTypesContainedInStorage(String folder) {
        File directory = new File(folder);
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
    public static Set<DesireKeyID> getParsedDesireTypesForAgentTypeContainedInStorage(AgentTypeID agentTypeID, String folder) {
        File directory = new File(folder + "/" + agentTypeID.getName());
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

}
