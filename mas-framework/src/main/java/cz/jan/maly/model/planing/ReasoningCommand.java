package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Set;

import static cz.jan.maly.utils.FrameworkUtils.REASONING_MANAGER;

/**
 * Reasoning command to be implemented by user. On top of methods provided by command class, it provides user with complete
 * memory of agent's.
 * Created by jean on 06/03/2017.
 */
public abstract class ReasoningCommand extends Command {
    private final Agent agent;

    public ReasoningCommand(Intention intention, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent) {
        super(intention, parametersTypesForFact, parametersTypesForFactSets, agent, REASONING_MANAGER);
        this.agent = agent;
    }

    //TODO do not give direct access to memory use filters instead - for cache and tree
    protected WorkingMemory getWorkingMemoryForReasoning() {
        return agent.getWorkingMemory();
    }
}
