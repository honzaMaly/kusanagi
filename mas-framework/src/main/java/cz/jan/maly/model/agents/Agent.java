package cz.jan.maly.model.agents;

import cz.jan.maly.model.knowledge.Beliefs;
import cz.jan.maly.model.metadata.AgentTypeKey;
import cz.jan.maly.model.metadata.CommandManagerKey;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.Command;
import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.planing.OwnDesire;
import cz.jan.maly.model.planing.tree.Tree;
import cz.jan.maly.service.CommandManager;
import cz.jan.maly.service.implementation.AgentsRegister;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Jan on 09-Feb-17.
 */
public abstract class Agent {
    private final Map<CommandManagerKey, CommandManager> commandManagersByKey = new HashMap<>();

    @Getter
    private final int id;

    @Getter
    private Beliefs beliefs;

    @Getter
    private final AgentTypeKey agentType;

    private final Tree tree;

    //TODO initialize beliefs, when starting agent, check environment

    //TODO factory to make from committed desire intention

    //TODO factory for specification when to commit to desire / remove commitment to intention

    public Agent(AgentsRegister agentsRegister, AgentTypeKey agentType, Tree tree, Set<CommandManager> commandManagers) {
        this.id = agentsRegister.getFreeId();
        this.agentType = agentType;
        this.tree = tree;
        commandManagers.forEach(commandManager -> commandManagersByKey.put(commandManager.getCommandManagerKey(), commandManager));
    }

    public void executeCommand(Command<?> command) {
        //todo
    }

    private Optional<CommandManager> getCommandManager(CommandManagerKey key) {
        CommandManager manager = commandManagersByKey.get(key);
        if (manager != null) {
            return Optional.of(manager);
        }
        return Optional.empty();
    }

    public abstract OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey desireKey);

    public abstract OwnDesire.WithIntentionWithPlan formOwnDesireWithIntentionWithPlan(DesireKey desireKey);

    public abstract DesireForOthers formDesireForOthers(DesireKey desireKey);

    public abstract DesireForOthers formDesireForOthers(DesireKey desireKey, DesireKey parentDesireKey);

    public abstract OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey desireKey, DesireKey parentDesireKey);

    public abstract OwnDesire.WithIntentionWithPlan formOwnDesireWithIntentionWithPlan(DesireKey desireKey, DesireKey parentDesireKey);

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
}
