package cz.jan.maly.main;

import cz.jan.maly.service.implementation.DecisionLearnerServiceImpl;

/**
 * Created by Jan on 05-May-17.
 */
public class Learner {
    private static final DecisionLearnerServiceImpl learnerService = new DecisionLearnerServiceImpl();

    public static void main(String[] args) throws Exception {

        //to speed things up when executing parallel stream
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "100");

        learnerService.learnDecisionMakers();
    }

}
