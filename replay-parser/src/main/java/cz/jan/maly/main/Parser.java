package cz.jan.maly.main;

import cz.jan.maly.service.ReplayParserService;
import cz.jan.maly.service.implementation.ReplayParserServiceImpl;

/**
 * Created by Jan on 30-Oct-16.
 */
public class Parser {
    private static final ReplayParserService replayParserService = new ReplayParserServiceImpl();

    public static void main(String[] args) throws Exception {

        //to speed things up when executing parallel stream
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "100");

//        DecisionLearnerServiceImpl learnerService = new DecisionLearnerServiceImpl();
//        learnerService.learnDecisionMakers();

        replayParserService.parseReplays();
    }

}
