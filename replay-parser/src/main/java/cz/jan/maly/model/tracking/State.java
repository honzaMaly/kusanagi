package cz.jan.maly.model.tracking;

import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

import java.io.Serializable;
import java.util.Optional;

/**
 * Class describing state as vector with transition (taken action - committed - true/false) and next state
 * Created by Jan on 21-Apr-17.
 */
public class State implements Serializable {
    @Getter
    private final double[] featureVector;
    private State nextState = null;
    @Getter
    private final boolean committedWhenTransiting;

    public State(double[] featureVector, boolean committedWhenTransiting) {
        this.featureVector = featureVector;
        this.committedWhenTransiting = committedWhenTransiting;
    }

    public Optional<State> getNextState() {
        return Optional.ofNullable(nextState);
    }

    public void setNextState(State nextState) {
        if (this.nextState == null) {
            this.nextState = nextState;
        } else {
            MyLogger.getLogger().warning("Trying to replace state which is not allowed.");
            throw new RuntimeException("Trying to replace state which is not allowed.");
        }
    }
}
