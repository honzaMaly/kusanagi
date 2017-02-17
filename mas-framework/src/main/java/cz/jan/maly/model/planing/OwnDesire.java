package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireKey;

/**
 * Template for agent's own desires
 * Created by Jan on 15-Feb-17.
 */
public class OwnDesire extends Desire {

    public OwnDesire(DesireKey desireKey, Agent agent) {
        super(desireKey, agent);
    }

}
