package cz.jan.maly.service;

/**
 * Contract for decision learning service
 * Created by Jan on 24-Apr-17.
 */
public interface DecisionLearnerService {

    /**
     * Method to learn decision makers and store them to storage
     */
    void learnDecisionMakers();

}
