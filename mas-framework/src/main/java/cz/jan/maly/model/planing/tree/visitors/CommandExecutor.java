package cz.jan.maly.model.planing.tree.visitors;

import cz.jan.maly.model.metadata.AgentTypeKey;
import cz.jan.maly.model.metadata.CommandManagerKey;
import cz.jan.maly.model.planing.Command;
import cz.jan.maly.model.planing.tree.TreeVisitorInterface;
import cz.jan.maly.service.CommandManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * CommandExecutor visitor traverse tree to get to leafs. When agent is committed to desire in leaf and it contains
 * command to be executed, executor select appropriate manager to execute command
 * Created by Jan on 22-Feb-17.
 */
public class CommandExecutor implements TreeVisitorInterface {
    private final Map<CommandManagerKey, CommandManager<?>> commandManagersByKey = new HashMap<>();
    private final AgentTypeKey agentType;

    public CommandExecutor(Set<CommandManager<?>> commandManagers, AgentTypeKey agentType) {
        this.agentType = agentType;
        commandManagers.forEach(commandManager -> commandManagersByKey.put(commandManager.getCommandManagerKey(), commandManager));
    }

    private boolean executeCommand(Command command) {
        CommandManager commandManager = commandManagersByKey.get(command.getCommandManagerKey());
        if (commandManager == null) {
            throw new IllegalArgumentException("No command manager to handle command with key " + command.getCommandManagerKey().getName() + " was provided to agents with type " + agentType.getName());
        }
        return commandManager.executeCommand(command);
    }

    @Override
    public void visit(LeafNodeWithPlan.WithOwnDesire leafNodeWithOwnDesire) {

        //only get command if leaf is intention
        if (leafNodeWithOwnDesire.isCommitted()) {
            leafNodeWithOwnDesire.updateIntentionWithCommandExecutionStatus(executeCommand(leafNodeWithOwnDesire.getCommand()));
        }
    }

    @Override
    public void visit(LeafNodeWithPlan.WithAnotherAgentDesire leafNodeWithAnotherAgentDesire) {

        //only get command if leaf is intention
        if (leafNodeWithAnotherAgentDesire.isCommitted()) {
            leafNodeWithAnotherAgentDesire.updateIntentionWithCommandExecutionStatus(executeCommand(leafNodeWithAnotherAgentDesire.getCommand()));
        }
    }

    @Override
    public void visit(LeafNodeWithDesireForOtherAgents leafNodeWithDesireForOtherAgents) {
        //no command to execute, skip
    }

    @Override
    public void visit(WithOwnDesireIntermediateNode withOwnDesireIntermediateNode) {

        //no command to execute, branch
        withOwnDesireIntermediateNode.branch(this);
    }

    @Override
    public void visit(WithAnotherAgentDesireIntermediateNode withAnotherAgentDesireIntermediateNode) {

        //no command to execute, branch
        withAnotherAgentDesireIntermediateNode.branch(this);
    }
}
