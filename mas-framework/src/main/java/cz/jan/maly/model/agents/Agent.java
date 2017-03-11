package cz.jan.maly.model.agents;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.CommandManagerKey;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.Command;
import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.planing.OwnDesire;
import cz.jan.maly.model.planing.tree.Tree;
import cz.jan.maly.service.CommandManager;
import cz.jan.maly.service.implementation.AgentsRegister;
import cz.jan.maly.service.implementation.DesireMediator;
import cz.jan.maly.service.implementation.KnowledgeMediator;
import cz.jan.maly.service.implementation.ReasoningManager;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Jan on 09-Feb-17.
 */
public class Agent implements AgentTypeBehaviourFactory {
    private final Map<CommandManagerKey, CommandManager> commandManagersByKey = new HashMap<>();

    //instance of reasoning manager, it can be shared by agents as it is stateless
    private static final ReasoningManager REASONING_MANAGER = new ReasoningManager();

    //register of agents - to assign ids to them
    private static final AgentsRegister AGENTS_REGISTER = new AgentsRegister();

    //shared desire mediator
    public static final DesireMediator DESIRE_MEDIATOR = new DesireMediator();

    //shared knowledge mediator
    private static final KnowledgeMediator KNOWLEDGE_MEDIATOR = new KnowledgeMediator();

    @Getter
    private final int id;

    //TODO init
    private WorkingMemory beliefs;

    @Getter
    private final AgentType agentType;

    private final Tree tree = new Tree(this);

    public Agent(AgentType agentType, Set<CommandManager> commandManagers) {
        this.id = AGENTS_REGISTER.getFreeId();
        this.agentType = agentType;
        commandManagersByKey.put(REASONING_MANAGER.getCommandManagerKey(), REASONING_MANAGER);
        commandManagers.forEach(commandManager -> commandManagersByKey.put(commandManager.getCommandManagerKey(), commandManager));

//        //init desires from types provided by user
//        getInitialOwnDesireWithAbstractIntentionTypes().forEach(desireKey -> tree.addDesire(formOwnDesireWithAbstractIntention(desireKey)));
//        getInitialOwnDesireWithIntentionWithPlanTypes().forEach(desireKey -> tree.addDesire(formOwnDesireWithIntentionWithPlan(desireKey)));
//        getDesireForOthersTypes().forEach(desireKey -> tree.addDesire(formDesireForOthers(desireKey)));
    }

    /**
     * Get memory of agent
     *
     * @return
     */
    public Memory getBeliefs() {
        return beliefs;
    }

    /**
     * For given command appropriate manager to execute it is found. Command is then executed.
     *
     * @param command
     */
    public void executeCommand(Command command) {
        Optional<CommandManager> manager = getCommandManager(command.getCommandManagerKey());
        if (!manager.isPresent()) {
            throw new NoSuchFieldError(agentType.getName() + " does not support " + command.getCommandManagerKey().getName() + " type.");
        }
        manager.get().executeCommand(command);
    }

    public WorkingMemory getWorkingMemory() {
        //todo, replace by methods to prevent access
        return null;
    }

    /**
     * Return command manager for given command if present
     *
     * @param key
     * @return
     */
    private Optional<CommandManager> getCommandManager(CommandManagerKey key) {
        CommandManager manager = commandManagersByKey.get(key);
        if (manager != null) {
            return Optional.of(manager);
        }
        return Optional.empty();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agent)) return false;

        Agent agent = (Agent) o;

        return id == agent.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey desireKey) {
        return;
    }

    @Override
    public OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey desireKey, DesireKey parentDesireKey) {
        return;
    }

    @Override
    public OwnDesire.WithIntentionWithPlan formOwnDesireWithIntentionWithPlan(DesireKey desireKey) {
        return;
    }

    @Override
    public OwnDesire.WithIntentionWithPlan formOwnDesireWithIntentionWithPlan(DesireKey desireKey, DesireKey parentDesireKey) {
        return;
    }

    @Override
    public DesireForOthers formDesireForOthers(DesireKey desireKey) {
        return;
    }

    @Override
    public DesireForOthers formDesireForOthers(DesireKey desireKey, DesireKey parentDesireKey) {
        return;
    }

    @Override
    public DesireFromAnotherAgent.WithAbstractIntention formDesireFromOtherAgentWithAbstractIntention(DesireKey desireKey) {
        return;
    }

    @Override
    public DesireFromAnotherAgent.WithIntentionWithPlan formDesireFromOtherAgentWithIntentionWithPlan(DesireKey desireKey) {
        return;
    }
}
