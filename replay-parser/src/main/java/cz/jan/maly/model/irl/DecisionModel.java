package cz.jan.maly.model.irl;

import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;
import cz.jan.maly.model.decision.NextActionEnumerations;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of model - how transitions are made
 * Created by Jan on 24-Apr-17.
 */
public class DecisionModel implements FullStateModel {
    private final Map<DecisionState, Map<NextActionEnumerations, Map<DecisionState, Double>>> transitionsProbabilitiesBasedOnActions = new HashMap<>();

    /**
     * Add transition based on action and its probability
     *
     * @param from
     * @param action
     * @param transitingToState
     * @param probability
     */
    public void addTransition(DecisionState from, NextActionEnumerations action, DecisionState transitingToState, double probability) {
        transitionsProbabilitiesBasedOnActions.computeIfAbsent(from, decisionState -> new HashMap<>())
                .computeIfAbsent(action, actionEnumerations -> new HashMap<>())
                .put(transitingToState, probability);
    }

    /**
     * Return all possible state transitions from this state based on action taken
     *
     * @param a
     * @return
     */
    public List<StateTransitionProb> stateTransitions(DecisionState from, DecisionAction a) {
        if (!transitionsProbabilitiesBasedOnActions.containsKey(from) || !transitionsProbabilitiesBasedOnActions.get(from).containsKey(a.getAction())) {
            //no possible transition. return this state
            return Collections.singletonList(new StateTransitionProb(from.copy(), 1.0));
        }
        return transitionsProbabilitiesBasedOnActions.get(from).get(a.getAction()).entrySet().stream()
                .map(entry -> new StateTransitionProb(entry.getKey().copy(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<StateTransitionProb> stateTransitions(State state, Action action) {
        return stateTransitions((DecisionState) state, (DecisionAction) action);
    }

    @Override
    public DecisionState sample(State state, Action action) {
        List<StateTransitionProb> reachableStates = stateTransitions(state, action);
        Collections.shuffle(reachableStates);

        //sample random roll
        double randomThreshold = Math.random(), sumOfProbability = 0;
        for (StateTransitionProb reachableState : reachableStates) {
            sumOfProbability = sumOfProbability + reachableState.p;
            if (randomThreshold <= sumOfProbability) {
                return ((DecisionState) reachableState.s).copy();
            }
        }
        throw new IndexOutOfBoundsException("No state found!");
    }
}
