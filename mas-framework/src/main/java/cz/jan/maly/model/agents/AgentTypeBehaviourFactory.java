package cz.jan.maly.model.agents;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.planing.OwnDesire;

/**
 * Interface for each agent to form desires
 * Created by Jan on 11-Mar-17.
 */
interface AgentTypeBehaviourFactory {

    /**
     * Forms OwnDesire WithAbstractIntention
     *
     * @param desireKey
     * @return
     */
    OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey desireKey);

    /**
     * Forms OwnDesire WithAbstractIntention with parent node
     *
     * @param desireKey
     * @param parentDesireKey
     * @return
     */
    OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey desireKey, DesireKey parentDesireKey);

    /**
     * Forms OwnDesire WithIntentionWithPlan
     *
     * @param desireKey
     * @return
     */
    OwnDesire.WithIntentionWithPlan formOwnDesireWithIntentionWithPlan(DesireKey desireKey);

    /**
     * Forms OwnDesire WithIntentionWithPlan with parent node
     *
     * @param desireKey
     * @param parentDesireKey
     * @return
     */
    OwnDesire.WithIntentionWithPlan formOwnDesireWithIntentionWithPlan(DesireKey desireKey, DesireKey parentDesireKey);

    /**
     * Forms DesireForOthers
     *
     * @param desireKey
     * @return
     */
    DesireForOthers formDesireForOthers(DesireKey desireKey);

    /**
     * Forms DesireForOthers with parent node
     *
     * @param desireKey
     * @param parentDesireKey
     * @return
     */
    DesireForOthers formDesireForOthers(DesireKey desireKey, DesireKey parentDesireKey);

    /**
     * Forms DesireFromAnotherAgent WithAbstractIntention
     *
     * @param desireKey
     * @return
     */
    DesireFromAnotherAgent.WithAbstractIntention formDesireFromOtherAgentWithAbstractIntention(DesireKey desireKey);

    /**
     * Forms DesireFromAnotherAgent WithAbstractIntention
     *
     * @param desireKey
     * @return
     */
    DesireFromAnotherAgent.WithIntentionWithPlan formDesireFromOtherAgentWithIntentionWithPlan(DesireKey desireKey);

}
