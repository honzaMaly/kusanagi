package cz.jan.maly.model.decision;

import cz.jan.maly.model.features.FeatureNormalizer;
import cz.jan.maly.utils.Configuration;
import weka.core.Instance;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Serializable data structure containing data of decision point
 * Created by Jan on 23-Apr-17.
 */
public class DecisionPointDataStructure implements Serializable {
    final Set<StateWithTransition> states;
    final List<FeatureNormalizer> normalizers;

    public DecisionPointDataStructure(Set<StateWithTransition> states, List<FeatureNormalizer> normalizers) {
        this.states = states;
        this.normalizers = normalizers;
    }

    /**
     * Class containing feature vector describing state and optimal action (according to policy) to make transition
     */
    public static class StateWithTransition implements Serializable {
        final double[] featureVector;
        final NextActionEnumerations nextAction;

        /**
         * Create state with transition
         *
         * @param clusterRepresentative
         * @param nextAction
         */
        public StateWithTransition(Instance clusterRepresentative, NextActionEnumerations nextAction) {
            this.nextAction = nextAction;
            this.featureVector = new double[clusterRepresentative.numValues()];
            for (int i = 0; i < featureVector.length; i++) {
                featureVector[i] = clusterRepresentative.value(i);
            }
        }

        /**
         * Converts featureVector to Instance
         *
         * @return
         */
        Instance convertVectorToInstance() {
            return Configuration.convertVectorToInstance(featureVector);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StateWithTransition that = (StateWithTransition) o;

            if (!Arrays.equals(featureVector, that.featureVector)) return false;
            return nextAction == that.nextAction;
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(featureVector);
            result = 31 * result + nextAction.hashCode();
            return result;
        }
    }

}
