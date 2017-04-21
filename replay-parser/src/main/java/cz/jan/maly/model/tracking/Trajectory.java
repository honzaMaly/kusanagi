package cz.jan.maly.model.tracking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Trajectory - to capture player's decision on commitment given state
 * Created by Jan on 21-Apr-17.
 */
public class Trajectory implements Serializable {
    private final State firstState;
    private State currentState;

    public Trajectory(State firstState) {
        this.firstState = firstState;
        this.currentState = firstState;
    }

    /**
     * Add new state on trajectory
     *
     * @param state
     */
    public void addNewState(State state) {
        currentState.setNextState(state);
        currentState = state;
    }

    /**
     * Return trajectory as list (with initial state as state captured after first observation)
     *
     * @return
     */
    public List<State> getTrajectory() {
        if (firstState != null) {
            Optional<State> currentState = firstState.getNextState();
            List<State> trajectory = new ArrayList<>();
            while (currentState.isPresent()) {
                trajectory.add(currentState.get());
                currentState = currentState.get().getNextState();
            }
            return trajectory;
        }
        return new ArrayList<>();
    }

}
