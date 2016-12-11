package cz.jan.maly.model.agent;

import cz.jan.maly.model.Knowledge;

/**
 * Created by Jan on 09-Dec-16.
 */
public abstract class AgentsKnowledge implements Knowledge {

    public abstract void updateKnowledge(GameObservation gameObservation);

    @Override
    public Knowledge getCopyOfKnowledge() {
        return null;
    }
}
