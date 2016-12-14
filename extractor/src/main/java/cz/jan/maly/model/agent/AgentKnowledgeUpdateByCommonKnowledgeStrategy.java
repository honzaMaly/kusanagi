package cz.jan.maly.model.agent;

import cz.jan.maly.model.game.CommonKnowledge;

/**
 * Interface describing strategy to update agent's knowledge by common knowledge
 *
 * Method to implement describes way how to update agentsKnowledgeToUpdate using workingCommonKnowledge
 *
 * Created by Jan on 14-Dec-16.
 */
public interface AgentKnowledgeUpdateByCommonKnowledgeStrategy {
    void updateKnowledge(CommonKnowledge workingCommonKnowledge, AgentsKnowledge agentsKnowledgeToUpdate);
}
