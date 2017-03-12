package cz.jan.maly.model.servicies.beliefs;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.ReadOnlyMemory;
import cz.jan.maly.model.servicies.WorkingRegister;

import java.util.HashMap;
import java.util.Map;

import static cz.jan.maly.utils.FrameworkUtils.howManyCyclesStayAgentsMemoryInRegisterWithoutUpdate;

/**
 * Concrete implementation of MemoryRegister. This class is intended as working register -
 * register keeps up to date information about agents' internal memories and is intended
 * for mediator use only.
 * Created by Jan on 24-Feb-17.
 */
public class WorkingMemoryRegister extends MemoryRegister implements WorkingRegister<ReadOnlyMemoryRegister> {
    private final Map<Agent, Integer> decayMap = new HashMap<>();

    public WorkingMemoryRegister() {
        super(new HashMap<>());
    }

    @Override
    public ReadOnlyMemoryRegister makeSnapshot() {
        forget();
        Map<Agent, ReadOnlyMemory> copy = new HashMap<>();
        dataByOriginator.forEach(copy::put);
        return new ReadOnlyMemoryRegister(copy);
    }

    /**
     * Method erases no longer relevant information
     */
    private void forget() {
        decayMap.forEach((v, integer) -> decayMap.put(v, integer + 1));
        decayMap.keySet().removeIf(v -> decayMap.get(v) >= howManyCyclesStayAgentsMemoryInRegisterWithoutUpdate);
    }

    /**
     * Add memory of agent in register
     *
     * @param readOnlyMemory
     * @return
     */
    public boolean addAgentsMemory(ReadOnlyMemory readOnlyMemory, Agent owner) {
        dataByOriginator.put(owner, readOnlyMemory);
        decayMap.put(owner, 1);
        return true;
    }
}
