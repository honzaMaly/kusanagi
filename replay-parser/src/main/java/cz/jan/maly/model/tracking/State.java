package cz.jan.maly.model.tracking;

import lombok.Getter;

import java.io.Serializable;

/**
 * Class describing state as vector with transition (taken action - committed - true/false) and next state
 * Created by Jan on 21-Apr-17.
 */
public class State implements Serializable {
    @Getter
    private final double[] featureVector;
    @Getter
    private final boolean committedWhenTransiting;

    public State(double[] featureVector, boolean committedWhenTransiting) {
        this.featureVector = featureVector;
        this.committedWhenTransiting = committedWhenTransiting;
    }
}
