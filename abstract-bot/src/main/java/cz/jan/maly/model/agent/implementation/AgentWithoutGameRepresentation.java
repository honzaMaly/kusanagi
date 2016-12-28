package cz.jan.maly.model.agent.implementation;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.service.implementation.MASService;

import java.util.Set;

/**
 * Extension of Agent class to represent unit in game as agent (example: probe)
 * Created by Jan on 28-Dec-16.
 */
public abstract class AgentWithoutGameRepresentation extends Agent {

    protected AgentWithoutGameRepresentation(long timeBetweenCycles, Set<KeyToFact> factsToUseInInternalKnowledge, Set<KeyToFact> factsToUseInCache, MASService service) {
        super(timeBetweenCycles, factsToUseInInternalKnowledge, factsToUseInCache, true, service);
        service.getAgentsManager().addAgent(this);
    }

    protected void removeAgent() {
        service.getAgentsManager().removeAgent(this);
    }
}
