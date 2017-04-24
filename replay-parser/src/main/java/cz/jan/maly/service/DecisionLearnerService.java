package cz.jan.maly.service;

import cz.jan.maly.model.decision.DecisionPointDataStructure;

/**
 * Contract for decision learning service
 * Created by Jan on 24-Apr-17.
 */
public interface DecisionLearnerService {

    DecisionPointDataStructure learnDecisionMaker();

}
