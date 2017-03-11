package cz.jan.maly.model;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;

import java.util.Map;
import java.util.Set;

/**
 * Contract for planning tree (and read only version of it shared with other agents) with methods to get
 * some metadata about planning
 * Created by Jan on 28-Feb-17.
 */
public interface PlanningTreeInterface {

    /**
     * Get set of desires type shared by other agents
     *
     * @return
     */
    Set<DesireParameters> committedSharedDesiresParametersByOtherAgents();

    /**
     * Get set of desires type shared by this agent
     *
     * @return
     */
    Set<DesireParameters> sharedDesiresParameters();

    /**
     * Return count of shared desires by other agents
     *
     * @return
     */
    int countOfCommittedSharedDesiresByOtherAgents();

    /**
     * Return count of shared desires
     *
     * @return
     */
    int countOfSharedDesires();

    /**
     * Method to get counts of types of intentions in tree
     */
    Map<DesireKey, Long> collectKeysOfCommittedDesiresInTreeCounts();

    /**
     * Method to get counts of types of desires in tree
     */
    Map<DesireKey, Long> collectKeysOfDesiresInTreeCounts();

    /**
     * Get parameters of desires agent is committed to on top level
     *
     * @return
     */
    Set<DesireParameters> getParametersOfCommittedDesiresOnTopLevel();

    /**
     * Get parameters of desires agent can commit to on top level
     *
     * @return
     */
    Set<DesireParameters> getParametersOfDesiresOnTopLevel();

}
