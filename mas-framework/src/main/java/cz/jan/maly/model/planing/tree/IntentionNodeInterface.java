package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.SharedDesireForAgents;

import java.util.List;
import java.util.Set;

/**
 * Contract for each node representing intention
 * Created by Jan on 28-Feb-17.
 */
public interface IntentionNodeInterface {

    /**
     * Adds shared desires to given set if node contains shared desire. This method is introduce to collect desires which
     * will be removed from register as agent is no longer committed to subtree
     *
     * @param sharedDesiresInSubtree
     */
    void collectSharedDesiresForOtherAgentsInSubtree(Set<SharedDesireForAgents> sharedDesiresInSubtree);

    /**
     * Remove commitment to this intention and replace itself by desire
     *
     * @return
     */
    boolean removeCommitment(List<DesireKey> madeCommitmentToTypes, List<DesireKey> didNotMakeCommitmentToTypes,
                             List<DesireKey> typesAboutToMakeDecision);

    /**
     * Add own desire key to list + when intermediate node - ask childes
     *
     * @param list
     */
    void collectKeysOfCommittedDesiresInSubtree(List<DesireKey> list);

    /**
     * Add desire key to list - ask childes (if they are desires)
     *
     * @param list
     */
    void collectKeysOfDesiresInSubtree(List<DesireKey> list);

}
