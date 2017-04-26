package cz.jan.maly.service;

import cz.jan.maly.model.decision.DecisionPointDataStructure;
import cz.jan.maly.model.metadata.AgentTypeID;
import cz.jan.maly.model.metadata.DesireKeyID;
import cz.jan.maly.model.tracking.Replay;
import cz.jan.maly.model.tracking.Trajectory;

import java.io.File;
import java.util.List;
import java.util.Map;
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

    /**
     * Save trajectories of given agent type for desire id
     *
     * @param agentTypeID
     * @param desireKeyID
     * @param trajectories
     */
    void saveTrajectory(AgentTypeID agentTypeID, DesireKeyID desireKeyID, List<Trajectory> trajectories);

    /**
     * Get all parsed agent types with their desires types contained in storage
     *
     * @return
     */
    Map<AgentTypeID, Set<DesireKeyID>> getParsedAgentTypesWithDesiresTypesContainedInStorage();

    /**
     * Get stored trajectories for given parameters
     * @param agentTypeID
     * @param desireKeyID
     * @return
     */
    List<Trajectory> getTrajectories(AgentTypeID agentTypeID, DesireKeyID desireKeyID) throws Exception;

    /**
     * Store learnt DecisionPointDataStructure
     * @param structure
     * @param agentTypeID
     * @param desireKeyID
     */
    void storeLearntDecision(DecisionPointDataStructure structure, AgentTypeID agentTypeID, DesireKeyID desireKeyID) throws Exception;

}
