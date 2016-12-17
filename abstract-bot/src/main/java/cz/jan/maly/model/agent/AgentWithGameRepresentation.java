package cz.jan.maly.model.agent;

import cz.jan.maly.model.game.wrappers.AUnit;
import lombok.Getter;

/**
 * Extension of Agent class to represent unit in game as agent
 * Created by Jan on 16-Dec-16.
 */
@Getter
public abstract class AgentWithGameRepresentation extends Agent {
    protected final AUnit unit;

    protected AgentWithGameRepresentation(long timeBetweenCycles, AUnit unit) {
        super(timeBetweenCycles);
        this.unit = unit;
        initializeKnowledgeOnCreation();
        act();
    }


    protected abstract void initializeKnowledgeOnCreation();
}
