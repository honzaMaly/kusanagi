package cz.jan.maly.model.agent.implementation;

import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.model.game.wrappers.APosition;
import cz.jan.maly.service.implementation.MASService;
import lombok.Getter;

import java.util.Set;

/**
 * Extension of Agent class to represent abstract agent tight with position (example: base with minerals, some interesting defense point)
 * Created by Jan on 28-Dec-16.
 */
@Getter
public abstract class AgentTightWithPosition extends AgentWithoutGameRepresentation {
    protected final APosition position;

    protected AgentTightWithPosition(long timeBetweenCycles, Set<KeyToFact> factsToUseInInternalKnowledge, Set<KeyToFact> factsToUseInCache, MASService service, APosition position) {
        super(timeBetweenCycles, factsToUseInInternalKnowledge, factsToUseInCache, service);
        this.position = position;
    }
}
