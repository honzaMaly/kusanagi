package cz.jan.maly.model.decision;

import java.io.Serializable;
import java.util.Random;

/**
 * Enumeration of all possible commitments based on policy
 * Created by Jan on 23-Apr-17.
 */
public enum NextActionEnumerations implements Serializable, NextActionStrategy {
    YES {
        @Override
        public boolean commit() {
            return true;
        }
    },
    NO {
        @Override
        public boolean commit() {
            return false;
        }
    },
    RANDOM {
        @Override
        public boolean commit() {
            return RANDOMNESS_GENERATOR.nextBoolean();
        }
    };

    private static final Random RANDOMNESS_GENERATOR = new Random();

}
