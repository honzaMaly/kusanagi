package cz.jan.maly.model.decision;

import cz.jan.maly.model.features.FeatureNormalizer;
import cz.jan.maly.utils.Configuration;
import cz.jan.maly.utils.MyLogger;
import jsat.linear.DenseVector;
import jsat.linear.Vec;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DecisionPoint decide next action based on current state. It is initialized from DecisionPointDataStructure
 * Created by Jan on 23-Apr-17.
 */
public class DecisionPoint {
    private final List<StateWithTransition> states;
    private final List<FeatureNormalizer> normalizers;

    public DecisionPoint(DecisionPointDataStructure dataStructure) {
        this.states = dataStructure.states.stream()
                .map(StateWithTransition::new)
                .collect(Collectors.toList());
        this.normalizers = dataStructure.normalizers;
    }

    /**
     * For given state (represented by feature vector) return optimal action based on policy
     *
     * @param featureVector
     * @return
     */
    public boolean nextAction(double[] featureVector) {
        Vec anotherInstance = new DenseVector((Configuration.normalizeFeatureVector(featureVector, normalizers)));
        Optional<StateWithTransition> closestState = states.stream()
                .min(Comparator.comparingDouble(o -> o.distance(anotherInstance)));
        if (!closestState.isPresent()) {
            MyLogger.getLogger().warning("No state is present.");
            return false;
        }
        return closestState.get().nextAction.commit();
    }


    /**
     * StateWithTransition to compute distance between instances and return next action (commitment) based on policy
     */
    private static class StateWithTransition {
        private final Vec center;
        final NextActionEnumerations nextAction;

        private StateWithTransition(DecisionPointDataStructure.StateWithTransition stateWithTransition) {
            this.center = stateWithTransition.getFeatureVector();
            this.nextAction = stateWithTransition.nextAction;
        }

        /**
         * Returns distance between center and passed instance
         *
         * @param anotherPoint
         * @return
         */
        double distance(Vec anotherPoint) {
            return Configuration.DISTANCE_FUNCTION.dist(center, anotherPoint);
        }

    }

}
