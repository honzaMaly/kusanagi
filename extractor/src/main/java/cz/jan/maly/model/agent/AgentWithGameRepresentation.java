package cz.jan.maly.model.agent;

import bwapi.Unit;
import lombok.Getter;

/**
 * Extension of Agent class to represent unit in game as agent
 * Created by Jan on 16-Dec-16.
 */
@Getter
public abstract class AgentWithGameRepresentation extends Agent {
    protected final Unit unit;

    protected AgentWithGameRepresentation(AgentsKnowledge agentsKnowledge, long timeBetweenCycles, Unit unit) {
        super(agentsKnowledge, timeBetweenCycles);
        this.unit = unit;
    }
}
