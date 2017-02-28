package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireKey;

/**
 * Template for agent's desires for others to achieve
 * Created by Jan on 15-Feb-17.
 */
public abstract class DesireForOthers extends InternalDesire<IntentionWithDesireForOtherAgents> {
    protected DesireForOthers(DesireKey desireKey, Agent agent) {
        super(desireKey, agent, false);
    }
}
