package cz.jan.maly.model.servicies;

import cz.jan.maly.model.agents.Agent;

import java.util.Map;

/**
 * Template for register
 * Created by Jan on 17-Feb-17.
 */
public abstract class Register<V> {
    protected final Map<Agent, V> desiresForOthersByOriginator;

    protected Register(Map<Agent, V> desiresForOthersByOriginator) {
        this.desiresForOthersByOriginator = desiresForOthersByOriginator;
    }
}
