package cz.jan.maly.service.implementation;

import cz.jan.maly.model.agent.Agent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class to manage game agents. It keeps reference to existing agents.
 * Created by Jan on 14-Dec-16.
 */
public class AgentsManager {
    private final Set<Agent> activeAgents = new HashSet<>();
    private int idCounter = 0;

    /**
     * Remove Agent from register
     *
     * @param agent
     */
    public void removeAgent(Agent agent) {
        synchronized (activeAgents) {
            activeAgents.remove(agent);
        }
    }

    /**
     * Register Agent
     *
     * @param agent
     */
    public void addAgent(Agent agent) {
        synchronized (activeAgents) {
            activeAgents.add(agent);
        }
    }

    /**
     * Get free id for agent to be used
     *
     * @return
     */
    public synchronized int getFreeId() {
        idCounter++;
        return idCounter;
    }

    /**
     * Method to get relevant active agents
     *
     * @param filteringStrategy
     * @return
     */
    public <T extends Agent> Set<T> getRelevantAgents(FilteringStrategy<T> filteringStrategy, Class<T> typeParameterClass) {
        synchronized (activeAgents) {
            return filteringStrategy.filter(activeAgents.stream()
                    .filter(typeParameterClass::isInstance)
                    .map(typeParameterClass::cast));
        }
    }

    /**
     * Method to get relevant active agent
     *
     * @param filteringStrategy
     * @return
     */
    public <T extends Agent> Optional<T> getRelevantAgentWithGameRepresentation(FilterSingleAgentStrategy<T> filteringStrategy, Class<T> typeParameterClass) {
        synchronized (activeAgents) {
            return filteringStrategy.filter(activeAgents.stream()
                    .filter(typeParameterClass::isInstance)
                    .map(typeParameterClass::cast));
        }
    }

    /**
     * Terminate all agents
     */
    public synchronized void terminateAllAgents() {
        activeAgents.forEach(Agent::terminateAgent);
    }

    /**
     * Strategy to use to filter agents from list. For example one wants to get only agents representing workers
     */
    public interface FilteringStrategy<T extends Agent> {
        Set<T> filter(Stream<T> agentsToFilterFrom);
    }

    /**
     * Strategy to use to filter agent from list. For example to find agent representing concrete unit
     */
    public interface FilterSingleAgentStrategy<T extends Agent> {
        Optional<T> filter(Stream<T> agentsToFilterFrom);
    }

}
