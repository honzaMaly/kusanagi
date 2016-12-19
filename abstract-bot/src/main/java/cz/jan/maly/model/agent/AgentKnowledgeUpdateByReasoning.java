package cz.jan.maly.model.agent;

/**
 * Interface describing strategy to update agent's knowledge by facts in his own knowledge and facts by other agents
 *
 * Method to implement describes way how to update agentsKnowledgeToUpdate using own facts and other agents facts in knowledge
 * Created by Jan on 18-Dec-16.
 */
public interface AgentKnowledgeUpdateByReasoning {
    void updateKnowledge(AgentsKnowledge agentsKnowledgeToUpdate);
}
