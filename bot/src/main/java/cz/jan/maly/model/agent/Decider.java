package cz.jan.maly.model.agent;

import cz.jan.maly.model.features.FeatureContainerHeader;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.metadata.AgentTypeID;
import cz.jan.maly.model.metadata.DesireKeyID;
import cz.jan.maly.service.DecisionLoadingService;
import cz.jan.maly.service.implementation.DecisionLoadingServiceImpl;

/**
 * Class with static method to get decision for passed configuration
 * Created by Jan on 08-May-17.
 */
public class Decider {
    private static final DecisionLoadingService DECISION_LOADING_SERVICE = DecisionLoadingServiceImpl.getInstance();

    /**
     * Get decision
     * @param agentTypeID
     * @param desireKeyID
     * @param dataForDecision
     * @param featureContainerHeader
     * @return
     */
    public static boolean getDecision(AgentTypeID agentTypeID, DesireKeyID desireKeyID, DataForDecision dataForDecision, FeatureContainerHeader featureContainerHeader){
        return DECISION_LOADING_SERVICE.getDecisionPoint(agentTypeID, desireKeyID).nextAction(featureContainerHeader.formVector(dataForDecision));
    }

}
