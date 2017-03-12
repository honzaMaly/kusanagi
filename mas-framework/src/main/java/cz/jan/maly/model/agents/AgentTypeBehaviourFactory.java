package cz.jan.maly.model.agents;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.planing.OwnDesire;
import cz.jan.maly.model.planing.SharedDesireForAgents;

import java.util.Optional;

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
     * Forms OwnDesire with reasoning command
     *
     * @param desireKey
     * @return
     */
    OwnDesire.Reasoning formOwnDesireWithReasoningCommand(DesireKey desireKey);

    /**
     * Forms OwnDesire with reasoning command with parent node
     *
     * @param desireKey
     * @param parentDesireKey
     * @return
     */
    OwnDesire.Reasoning formOwnDesireWithReasoningCommand(DesireKey desireKey, DesireKey parentDesireKey);

    /**
     * Forms OwnDesire with reasoning command
     *
     * @param desireKey
     * @return
     */
    OwnDesire.Acting formOwnDesireWithActingCommand(DesireKey desireKey);

    /**
     * Forms OwnDesire with reasoning command with parent node
     *
     * @param desireKey
     * @param parentDesireKey
     * @return
     */
    OwnDesire.Acting formOwnDesireWithActingCommand(DesireKey desireKey, DesireKey parentDesireKey);

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
     * @param desireForAgents
     * @return
     */
    Optional<DesireFromAnotherAgent.WithAbstractIntention> formDesireFromOtherAgentWithAbstractIntention(SharedDesireForAgents desireForAgents);

    /**
     * Forms DesireFromAnotherAgent WithAbstractIntention
     *
     * @param desireForAgents
     * @return
     */
    Optional<DesireFromAnotherAgent.WithIntentionWithPlan> formDesireFromOtherAgentWithIntentionWithPlan(SharedDesireForAgents desireForAgents);

}
