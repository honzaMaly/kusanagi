package cz.jan.maly.model.agent;

import cz.jan.maly.model.Knowledge;

/**
 * Created by Jan on 09-Dec-16.
 */
public abstract class AgentsKnowledge implements Knowledge {
    private final Agent agent;

    protected AgentsKnowledge(Agent agent) {
        this.agent = agent;
    }

    public boolean isKnowledgeActive(){
        return agent.isAgentAlive();
    }

    @Override
    public Knowledge getCopyOfKnowledge() {
        return null;
    }

    //todo make features from attributes strategy? or have attributes and extract features from it using strategies?

}
