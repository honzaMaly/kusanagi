package cz.jan.maly.model.agent;

import bwapi.Game;

/**
 * Interface describing strategy to update agent's knowledge by observation of the game
 *
 * Method to implement describes way how to update agentsKnowledgeToUpdate using observation
 * Created by Jan on 14-Dec-16.
 */
public interface AgentKnowledgeUpdateByGameObservationStrategy {
    void updateKnowledge(Game game, AgentsKnowledge agentsKnowledgeToUpdate);
}
