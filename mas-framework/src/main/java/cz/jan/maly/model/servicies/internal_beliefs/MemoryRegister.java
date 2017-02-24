package cz.jan.maly.model.servicies.internal_beliefs;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.ReadOnlyMemory;
import cz.jan.maly.model.servicies.Register;

import java.util.Map;

/**
 * MemoryRegister contains knowledge (internal beliefs) received from agents
 * Created by Jan on 24-Feb-17.
 */
abstract class MemoryRegister extends Register<ReadOnlyMemory> {
    MemoryRegister(Map<Agent, ReadOnlyMemory> dataByOriginator) {
        super(dataByOriginator);
    }
}
