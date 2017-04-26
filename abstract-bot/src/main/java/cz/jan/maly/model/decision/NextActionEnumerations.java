package cz.jan.maly.model.decision;

import java.io.Serializable;

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
    };

    /**
     * Return action corresponding to label
     *
     * @param commitment
     * @return
     */
    public static NextActionEnumerations returnNextAction(boolean commitment) {
        if (commitment) {
            return YES;
        }
        return NO;
    }

    /**
     * Return action corresponding to label
     *
     * @param commitment
     * @return
     */
    public static NextActionEnumerations returnNextAction(String commitment) {
        if (commitment.equals("YES")) {
            return YES;
        }
        return NO;
    }

}
