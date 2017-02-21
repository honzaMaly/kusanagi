package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireKey;

/**
 * Template for agent's own desires
 * Created by Jan on 15-Feb-17.
 */
public abstract class OwnDesire extends Desire {
    public OwnDesire(DesireKey desireKey, Agent agent, boolean isAbstract) {
        super(desireKey, agent, isAbstract);
    }
}
