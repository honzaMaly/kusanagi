package cz.jan.maly.model.agents;

import cz.jan.maly.model.knowledge.Beliefs;
import cz.jan.maly.model.metadata.AgentTypeKey;
import lombok.Getter;

/**
 * Created by Jan on 09-Feb-17.
 */
public abstract class Agent {

    @Getter
    private final AgentTypeKey agentTypeKey;

    @Getter
    private final Beliefs beliefs;

    public Agent(AgentTypeKey agentTypeKey, Beliefs beliefs) {
        this.agentTypeKey = agentTypeKey;
        this.beliefs = beliefs;
    }

    //TODO factory to make from committed desire intention

    //TODO factory for specification when to commit to desire / remove commitment to intention

}
