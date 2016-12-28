package cz.jan.maly.model.agent.implementation;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.service.implementation.MASService;
import lombok.Getter;

import java.util.Set;

/**
 * Extension of Agent class to represent unit in game as agent (example: probe)
 * Created by Jan on 16-Dec-16.
 */
@Getter
public abstract class AgentWithGameRepresentation extends Agent {
    protected final AUnit unit;
    protected final boolean destroyOnMorph;

    protected AgentWithGameRepresentation(long timeBetweenCycles, Set<KeyToFact> factsToUseInInternalKnowledge, Set<KeyToFact> factsToUseInCache, MASService service, AUnit unit, boolean destroyOnMorph) {
        super(timeBetweenCycles, factsToUseInInternalKnowledge, factsToUseInCache, false, service);
        this.unit = unit;
        this.destroyOnMorph = destroyOnMorph;
        service.getAgentsManager().addAgent(this);
    }

    protected void removeAgent() {
        service.getAgentsManager().removeAgent(this);
    }

}
