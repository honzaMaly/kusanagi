package cz.jan.maly.main.example;

import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.StateUtilities;
import burlap.mdp.core.state.UnknownKeyException;
import burlap.mdp.core.state.annotations.DeepCopyState;

import java.util.Arrays;
import java.util.List;

import static cz.jan.maly.main.ExampleGridWorld.VAR_X;
import static cz.jan.maly.main.ExampleGridWorld.VAR_Y;

/**
 * Created by Jan on 23-Apr-17.
 */
@DeepCopyState
public class EXGridState implements MutableState {

    public int x;
    public int y;

    private final static List<Object> keys = Arrays.<Object>asList(VAR_X, VAR_Y);

    public EXGridState() {
    }

    public EXGridState(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getIndex() {
        return (11 * y) + x;
    }

    @Override
    public MutableState set(Object variableKey, Object value) {
        if (variableKey.equals(VAR_X)) {
            this.x = StateUtilities.stringOrNumber(value).intValue();
        } else if (variableKey.equals(VAR_Y)) {
            this.y = StateUtilities.stringOrNumber(value).intValue();
        } else {
            throw new UnknownKeyException(variableKey);
        }
        return this;
    }

    public List<Object> variableKeys() {
        return keys;
    }

    @Override
    public Object get(Object variableKey) {
        if (variableKey.equals(VAR_X)) {
            return x;
        } else if (variableKey.equals(VAR_Y)) {
            return y;
        }
        throw new UnknownKeyException(variableKey);
    }

    @Override
    public EXGridState copy() {
        return new EXGridState(x, y);
    }

    @Override
    public String toString() {
        return StateUtilities.stateToString(this);
    }
}
