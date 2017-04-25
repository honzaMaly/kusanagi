package cz.jan.maly.model.tracking;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Trajectory - to capture player's decision on commitment given state
 * Created by Jan on 21-Apr-17.
 */
public class Trajectory implements Serializable {
    @Getter
    private final int numberOfFeatures;
    @Getter
    private List<State> states = new ArrayList<>();

    public Trajectory(int numberOfFeatures) {
        this.numberOfFeatures = numberOfFeatures;
    }

    /**
     * Add new state on trajectory
     *
     * @param state
     */
    public void addNewState(State state) {
        states.add(state);
    }
}
