package cz.jan.maly.service;

import com.rits.cloning.Cloner;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.service.implementation.AgentsRegister;
import cz.jan.maly.service.implementation.DesireMediator;
import cz.jan.maly.service.implementation.KnowledgeMediator;
import cz.jan.maly.service.implementation.ReasoningExecutor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Facade for framework. It keeps useful references as well as declaration of common data structures
 * Created by Jan on 14-Mar-17.
 */
public class MASFacade<E> implements TerminableService {

    //for cloning data
    public static final Cloner CLONER = new Cloner();

    //framework timing configuration...
    @Setter
    @Getter
    public static int lengthOfIntervalBeforeUpdatingRegisterWithDesires = 100;
    @Setter
    public static int howManyCyclesStayAgentsMemoryInRegisterWithoutUpdate = 100;
    @Setter
    @Getter
    public static int lengthOfIntervalBeforeUpdatingRegisterWithMemory = 100;

    //instance of reasoning manager, it can be shared by agents as it is stateless
    public static final ReasoningExecutor REASONING_EXECUTOR = new ReasoningExecutor();

    //register of agents - to assign ids to them
    @Getter
    private final AgentsRegister agentsRegister = new AgentsRegister();

    //shared desire mediator
    @Getter
    private final DesireMediator desireMediator = new DesireMediator();

    //shared knowledge mediator
    @Getter
    private final KnowledgeMediator knowledgeMediator = new KnowledgeMediator();

    private final Set<Agent<E>> agentsInSystem = new HashSet<>();

    /**
     * Register agent in system
     *
     * @param agent
     */
    public void addAgentToSystem(Agent<E> agent) {
        agentsInSystem.add(agent);
    }

    /**
     * Unregister agent from system
     *
     * @param agent
     */
    public void removeAgentFromSystem(Agent<E> agent) {
        if (agentsInSystem.remove(agent)) {
            agent.terminateAgent();
        } else {
            throw new IllegalArgumentException("Agent is not registered in system.");
        }
    }

    public void terminate() {
        agentsInSystem.forEach(this::removeAgentFromSystem);
        desireMediator.terminate();
        knowledgeMediator.terminate();
    }

}
