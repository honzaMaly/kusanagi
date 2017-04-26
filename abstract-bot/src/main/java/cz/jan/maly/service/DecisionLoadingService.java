package cz.jan.maly.service;

import cz.jan.maly.model.decision.DecisionPoint;
import cz.jan.maly.model.metadata.AgentTypeID;
import cz.jan.maly.model.metadata.DesireKeyID;

/**
 * Contract for service loading decision points
 * Created by Jan on 26-Apr-17.
 */
public interface DecisionLoadingService {

    /**
     * For given keys get decision point instance
     *
     * @param agentTypeID
     * @param desireKeyID
     * @return
     */
    DecisionPoint getDecisionPoint(AgentTypeID agentTypeID, DesireKeyID desireKeyID);

}
