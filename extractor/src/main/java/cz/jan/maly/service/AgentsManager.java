package cz.jan.maly.service;

import cz.jan.maly.model.ServiceInterface;
import cz.jan.maly.model.agent.Agent;

import java.util.HashSet;
import java.util.Set;

/**
 * Class to manage game agents. It keeps reference to existing agents.
 * Created by Jan on 14-Dec-16.
 */
public class AgentsManager implements ServiceInterface {
    private final Set<Agent> activeAgents = new HashSet<>();
    private static AgentsManager instance = null;
    private int idCounter = 0;

    protected AgentsManager() {
        // Exists only to defeat instantiation.
    }
    public static AgentsManager getInstance() {
        if(instance == null) {
            instance = new AgentsManager();
        }
        return instance;
    }

    public synchronized void removeAgent(Agent agent) {
        activeAgents.remove(agent);
    }

    public synchronized int addAgent(Agent agent) {
        activeAgents.add(agent);
        idCounter++;
        return idCounter;
    }

    /**
     * Method to get relevant active agents
     * @param filteringStrategy
     * @return
     */
    public Set<Agent> getRelevantAgents(FilteringStrategy filteringStrategy) {
        synchronized (activeAgents) {
            return filteringStrategy.filter(activeAgents);
        }
    }

    @Override
    public synchronized void reinitializedServiceForNewGame() {
        activeAgents.clear();
        idCounter = 0;
    }

    /**
     * Strategy to use to filter agents from list. For example one wants to get only agents representing workers
     */
    private interface FilteringStrategy {
        Set<Agent> filter(Set<Agent> agentsToFilterFrom);
    }

}
