package cz.jan.maly.model.servicies.beliefs;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.ReadOnlyMemory;
import cz.jan.maly.model.metadata.AgentType;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Concrete implementation of MemoryRegister. Instance of class is intended as read only as it is shared among agents.
 * Created by Jan on 24-Feb-17.
 */
public class ReadOnlyMemoryRegister extends MemoryRegister {
    ReadOnlyMemoryRegister(Map<Agent, ReadOnlyMemory> dataByOriginator) {
        super(dataByOriginator);
    }

    /**
     * Transform map to remove agent from result
     *
     * @return
     */
    public Map<AgentType, Map<Integer, ReadOnlyMemory>> formKnowledge() {
        return dataByOriginator.values().stream()
                .collect(Collectors.groupingBy(ReadOnlyMemory::getAgentType, Collectors.toMap(ReadOnlyMemory::getAgentId, Function.identity())));
    }
}
