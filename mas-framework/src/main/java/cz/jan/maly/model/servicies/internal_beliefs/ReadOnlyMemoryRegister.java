package cz.jan.maly.model.servicies.internal_beliefs;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.ReadOnlyMemory;

import java.util.Map;

/**
 * Concrete implementation of MemoryRegister. Instance of class is intended as read only as it is shared among agents.
 * Created by Jan on 24-Feb-17.
 */
public class ReadOnlyMemoryRegister extends MemoryRegister {
    ReadOnlyMemoryRegister(Map<Agent, ReadOnlyMemory> dataByOriginator) {
        super(dataByOriginator);
    }

    //todo filters to get knowledge / tree
}
