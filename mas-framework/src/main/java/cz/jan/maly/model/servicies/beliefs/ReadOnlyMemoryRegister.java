package cz.jan.maly.model.servicies.beliefs;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.ReadOnlyMemory;
import cz.jan.maly.model.metadata.AgentType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Concrete implementation of MemoryRegister. Instance of class is intended as read only as it is shared among agents.
 * Created by Jan on 24-Feb-17.
 */
public class ReadOnlyMemoryRegister extends MemoryRegister {
    private final Map<AgentType, Map<Integer, ReadOnlyMemory>> beliefsInSystem;
    private final Map<Integer, ReadOnlyMemory> beliefsInSystemByAgents;

    ReadOnlyMemoryRegister(Map<Agent, ReadOnlyMemory> dataByOriginator) {
        super(dataByOriginator);
        this.beliefsInSystem = dataByOriginator.values().stream()
                .collect(Collectors.groupingBy(ReadOnlyMemory::getAgentType, Collectors.toMap(ReadOnlyMemory::getAgentId, Function.identity())));
        this.beliefsInSystemByAgents = dataByOriginator.values().stream()
                .collect(Collectors.toMap(ReadOnlyMemory::getAgentId, Function.identity()));
    }

    /**
     * Get ReadOnlyMemory by agent id
     *
     * @param agentId
     * @return
     */
    public Optional<ReadOnlyMemory> getReadOnlyMemoryForAgent(int agentId) {
        return Optional.ofNullable(beliefsInSystemByAgents.get(agentId));
    }

    /**
     * Get set of memories by agent type
     *
     * @param agentType
     * @return
     */
    public Set<ReadOnlyMemory> getReadOnlyMemoriesForAgentType(AgentType agentType) {
        return new HashSet<>(beliefsInSystem.getOrDefault(agentType, new HashMap<>()).values());
    }

    /**
     * Get all beliefs in system
     *
     * @return
     */
    public Set<ReadOnlyMemory> getReadOnlyMemories() {
        return new HashSet<>(beliefsInSystemByAgents.values());
    }

}
