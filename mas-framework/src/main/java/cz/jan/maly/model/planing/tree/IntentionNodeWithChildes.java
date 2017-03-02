package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.metadata.DesireParameters;

import java.util.Set;

/**
 * Contract for intention nodes with childes
 * Created by Jan on 28-Feb-17.
 */
interface IntentionNodeWithChildes {

    /**
     * Replace desire by intention
     *
     * @param desireNode
     * @param intentionNode
     */
    void replaceDesireByIntention(DesireNodeNotTopLevel<?, ?> desireNode, IntentionNodeNotTopLevel<?, ?, ?> intentionNode);

    /**
     * Replace intention by desire
     *
     * @param desireNode
     * @param intentionNode
     */
    void replaceIntentionByDesire(IntentionNodeNotTopLevel<?, ?, ?> intentionNode, DesireNodeNotTopLevel<?, ?> desireNode);

    /**
     * Get parameters of desires agent is committed to
     *
     * @return
     */
    Set<DesireParameters> getParametersOfCommittedDesires();

    /**
     * Get parameters of desires agent can commit to
     *
     * @return
     */
    Set<DesireParameters> getParametersOfDesires();

}
