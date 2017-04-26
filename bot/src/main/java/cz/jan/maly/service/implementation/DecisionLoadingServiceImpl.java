package cz.jan.maly.service.implementation;

import cz.jan.maly.model.decision.DecisionPoint;
import cz.jan.maly.model.metadata.AgentTypeID;
import cz.jan.maly.model.metadata.DesireKeyID;
import cz.jan.maly.service.DecisionLoadingService;
import cz.jan.maly.utils.MyLogger;
import cz.jan.maly.utils.SerializationUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cz.jan.maly.utils.Configuration.getParsedAgentTypesContainedInStorage;
import static cz.jan.maly.utils.Configuration.getParsedDesireTypesForAgentTypeContainedInStorage;

/**
 * Implementation for DecisionLoadingService
 * Created by Jan on 26-Apr-17.
 */
public class DecisionLoadingServiceImpl implements DecisionLoadingService {
    private static DecisionLoadingServiceImpl instance = null;
    private final Map<AgentTypeID, Map<DesireKeyID, DecisionPoint>> cache = new HashMap<>();

    //get path to resources folder root
    private final String folderPath = this.getClass().getClassLoader().getResource("dummy.txt").getPath().replace("/dummy.txt", "");

    /**
     * Initialize cache (loads models from resources)
     */
    private DecisionLoadingServiceImpl() {
        getParsedAgentTypesContainedInStorage(folderPath).stream()
                .collect(Collectors.toMap(Function.identity(), t -> getParsedDesireTypesForAgentTypeContainedInStorage(t, folderPath)))
                .forEach((agentTypeID, desireKeyIDS) -> desireKeyIDS.forEach(desireKeyID -> loadDecisionPoint(agentTypeID, desireKeyID)));
    }

    /**
     * Only one instance
     *
     * @return
     */
    public static DecisionLoadingService getInstance() {
        if (instance == null) {
            instance = new DecisionLoadingServiceImpl();
        }
        return instance;
    }

    /**
     * Try to load decision points for given keys and put it in to cache
     *
     * @param agentTypeID
     * @param desireKeyID
     */
    private void loadDecisionPoint(AgentTypeID agentTypeID, DesireKeyID desireKeyID) {
        try {
            DecisionPoint decisionPoint = new DecisionPoint(SerializationUtil.deserialize(folderPath + "/" + agentTypeID.getName() + "/" + desireKeyID.getName() + ".db"));
            cache.computeIfAbsent(agentTypeID, id -> new HashMap<>()).put(desireKeyID, decisionPoint);
        } catch (Exception e) {
            MyLogger.getLogger().warning(e.getLocalizedMessage());
        }
    }

    @Override
    public DecisionPoint getDecisionPoint(AgentTypeID agentTypeID, DesireKeyID desireKeyID) {
        if (!cache.containsKey(agentTypeID)) {
            MyLogger.getLogger().warning("No models of decision for " + agentTypeID.getName() + " are present.");
            throw new RuntimeException("No models of decision for " + agentTypeID.getName() + " are present.");
        }
        if (!cache.get(agentTypeID).containsKey(desireKeyID)) {
            MyLogger.getLogger().warning("No models of " + desireKeyID.getName() + " for " + agentTypeID.getName() + " are present.");
            throw new RuntimeException("No models of " + desireKeyID.getName() + " for " + agentTypeID.getName() + " are present.");
        }
        return cache.get(agentTypeID).get(desireKeyID);
    }
}
