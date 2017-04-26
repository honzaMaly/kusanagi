package cz.jan.maly.service.implementation;

import burlap.behavior.functionapproximation.dense.DenseStateFeatures;
import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.learnfromdemo.mlirl.MLIRL;
import burlap.behavior.singleagent.learnfromdemo.mlirl.MLIRLRequest;
import burlap.behavior.singleagent.learnfromdemo.mlirl.commonrfs.LinearStateDifferentiableRF;
import burlap.behavior.singleagent.learnfromdemo.mlirl.differentiableplanners.DifferentiableSparseSampling;
import burlap.behavior.valuefunction.QProvider;
import burlap.debugtools.RandomFactory;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import cz.jan.maly.model.irl.DecisionState;
import cz.jan.maly.service.PolicyLearningService;

import java.util.List;

/**
 * Implementation of PolicyLearningService. To learn policy IRL is used. Policy is learnt
 * using provided "experts'" episodes
 * Created by Jan on 26-Apr-17.
 */
public class PolicyLearningServiceImpl implements PolicyLearningService {
    private static final double beta = 10;
    private static final boolean doNotPrintDebug = false;
    private static final int steps = 10;

    @Override
    public Policy learnPolicy(SADomain domain, List<Episode> episodes, int numberOfStates) {

        //create reward function features to use
        LocationFeatures features = new LocationFeatures(numberOfStates);

        //create a reward function that is linear with respect to those features and has small random
        //parameter values to start
        LinearStateDifferentiableRF rf = new LinearStateDifferentiableRF(features, numberOfStates);
        for (int i = 0; i < rf.numParameters(); i++) {
            rf.setParameter(i, RandomFactory.getMapped(0).nextDouble() * 0.2 - 0.1);
        }

        //use either DifferentiableVI or DifferentiableSparseSampling for planning. The latter enables receding horizon IRL,
        //but you will probably want to use a fairly large horizon for this kind of reward function.
        //DifferentiableVI dplanner = new DifferentiableVI(this.domain, rf, 0.99, beta, new SimpleHashableStateFactory(), 0.01, 100);
        HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
        DifferentiableSparseSampling dplanner = new DifferentiableSparseSampling(domain, rf, 0.99, hashingFactory, 10, -1, beta);

        dplanner.toggleDebugPrinting(doNotPrintDebug);

        //define the IRL problem
        MLIRLRequest request = new MLIRLRequest(domain, dplanner, episodes, rf);
        request.setBoltzmannBeta(beta);

        //run MLIRL on it
        MLIRL irl = new MLIRL(request, 0.1, 0.1, steps);
        irl.performIRL();

        return new GreedyQPolicy((QProvider) request.getPlanner());
    }

    /**
     * A state feature vector generator that create a binary feature vector where each element
     * indicates whether the agent is in a cell of a different type. All zeros indicates
     * that the agent is in an empty cell.
     */
    private static class LocationFeatures implements DenseStateFeatures {

        private int numLocations;

        LocationFeatures(int numLocations) {
            this.numLocations = numLocations;
        }

        @Override
        public double[] features(State s) {

            double[] fv = new double[this.numLocations];

            int location = ((DecisionState) s).getState();
            if (location != -1) {
                fv[location] = 1.0;
            }

            return fv;
        }

        @Override
        public DenseStateFeatures copy() {
            return new LocationFeatures(numLocations);
        }
    }
}
