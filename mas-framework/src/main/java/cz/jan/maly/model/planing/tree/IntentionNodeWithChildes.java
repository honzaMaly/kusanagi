package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.metadata.DesireParameters;

import java.util.Set;

/**
 * Contract for intention nodes with childes
 * Created by Jan on 28-Feb-17.
 */
interface IntentionNodeWithChildes<A extends IntentionNodeNotTopLevel.WithCommand<?, ?, ?>, B extends IntentionNodeNotTopLevel.WithCommand<?, ?, ?>,
        C extends IntentionNodeNotTopLevel.WithDesireForOthers<?>, D extends IntentionNodeNotTopLevel.WithAbstractPlan<?, ?, ?>,
        E extends DesireNodeNotTopLevel.WithCommand<?, ?, ?>, F extends DesireNodeNotTopLevel.WithCommand<?, ?, ?>,
        G extends DesireNodeNotTopLevel.ForOthers<?>, H extends DesireNodeNotTopLevel.WithAbstractPlan<?, ?>> {

    /**
     * Call to update childes representing desires
     */
    void updateDesires();

    /**
     * To get reference
     *
     * @return
     */
    DesireUpdater<A, B, C, D, E, F, G, H, ?> getDesireUpdater();

    default void replaceDesireByIntentionReasoning(E desireNode, A intentionNode) {
        getDesireUpdater().replaceDesireByIntentionReasoning(desireNode, intentionNode);
    }

    default void replaceIntentionByDesireReasoning(A intentionNode, E desireNode) {
        getDesireUpdater().replaceIntentionByDesireReasoning(intentionNode, desireNode);
    }

    default void replaceDesireByIntentionActing(F desireNode, B intentionNode) {
        getDesireUpdater().replaceDesireByIntentionActing(desireNode, intentionNode);
    }

    default void replaceIntentionByDesireActing(B intentionNode, F desireNode) {
        getDesireUpdater().replaceIntentionByDesireActing(intentionNode, desireNode);
    }

    default void replaceDesireByIntentionWithDesireForOthers(G desireNode, C intentionNode) {
        getDesireUpdater().replaceDesireByIntentionWithDesireForOthers(desireNode, intentionNode);
    }

    default void replaceIntentionByDesireWithDesireForOthers(C intentionNode, G desireNode) {
        getDesireUpdater().replaceIntentionByDesireWithDesireForOthers(intentionNode, desireNode);
    }

    default void replaceDesireByIntentionWithAbstractPlan(H desireNode, D intentionNode) {
        getDesireUpdater().replaceDesireByIntentionWithAbstractPlan(desireNode, intentionNode);
    }

    default void replaceIntentionByDesireWithAbstractPlan(D intentionNode, H desireNode) {
        getDesireUpdater().replaceIntentionByDesireWithAbstractPlan(intentionNode, desireNode);
    }

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
