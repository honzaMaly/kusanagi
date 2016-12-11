package cz.jan.maly.model.agent;

import cz.jan.maly.model.Knowledge;

/**
 * Created by Jan on 07-Dec-16.
 */
public abstract class GameObservation implements Knowledge {
    @Override
    public Knowledge getCopyOfKnowledge() {
        return null;
    }
}
