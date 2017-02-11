package cz.jan.maly.model.data;

import cz.jan.maly.model.agent.AgentActionCycleAbstract;

import java.util.Iterator;

/**
 * Simple wrapper to have define set of actions which can be executed together
 * Created by Jan on 28-Dec-16.
 */
public class ActionToEvaluateTogether <E extends AgentActionCycleAbstract> implements Iterator<E> {
    private final E[] actions;
    private int pointer = 0;

    public ActionToEvaluateTogether(E[] actions) {
        this.actions = actions;
    }

    @Override
    public boolean hasNext() {
        return pointer<actions.length;
    }

    @Override
    public E next() {
        return actions[pointer++];
    }
}
