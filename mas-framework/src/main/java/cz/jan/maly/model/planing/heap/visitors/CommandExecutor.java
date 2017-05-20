package cz.jan.maly.model.planing.heap.visitors;

import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.model.planing.heap.*;
import cz.jan.maly.utils.MyLogger;

/**
 * CommandExecutor visitor traverse heapOfTrees to get to leafs. When agent is committed to desire in leaf and it contains
 * command to be executed it sends command to agent to handle it
 * Created by Jan on 22-Feb-17.
 */
public class CommandExecutor implements TreeVisitorInterface, ResponseReceiverInterface<Boolean> {
    private final Object lockMonitor = new Object();
    private final HeapOfTrees heapOfTrees;
    private final Agent<?> agent;

    public CommandExecutor(HeapOfTrees heapOfTrees, Agent<?> agent) {
        this.heapOfTrees = heapOfTrees;
        this.agent = agent;
    }

    @Override
    public void visitTree() {
        branch(heapOfTrees);
    }

    /**
     * Visit subtrees induced by intentions
     *
     * @param parent
     * @param <V>
     * @param <K>
     */
    private <K extends Node<?> & IntentionNodeInterface & VisitorAcceptor, V extends Node<?> & DesireNodeInterface<K>> void branch(Parent<V, K> parent) {
        parent.getNodesWithIntention().forEach(k -> k.accept(this));
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithAbstractPlan<?, ?> node) {
        branch(node);
    }

    @Override
    public void visit(IntentionNodeNotTopLevel.WithAbstractPlan<?, ?, ?> node) {
        branch(node);
    }

    @Override
    public void visitNodeWithActingCommand(IntentionNodeNotTopLevel.WithCommand<?, ?, ActCommand.Own> node) {
        sendActingCommandForExecution(node.getCommand());
    }

    private void sendActingCommandForExecution(ActCommand<?> command) {
        synchronized (lockMonitor) {
            if (agent.sendCommandToExecute(command, this)) {
                try {
                    lockMonitor.wait();
                } catch (InterruptedException e) {
                    MyLogger.getLogger().warning(this.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
                }
            }
        }
    }

    @Override
    public void visitNodeWithReasoningCommand(IntentionNodeNotTopLevel.WithCommand<?, ?, ReasoningCommand> node) {
        agent.executeCommand(node.getCommand());
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithDesireForOthers node) {
        //no action to execute
    }

    @Override
    public void visit(IntentionNodeNotTopLevel.WithDesireForOthers<?> node) {
        //no action to execute
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithCommand.OwnReasoning node) {
        agent.executeCommand(node.getCommand());
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithCommand.OwnActing node) {
        sendActingCommandForExecution(node.getCommand());
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithCommand.FromAnotherAgent node) {
        sendActingCommandForExecution(node.getCommand());
    }

    @Override
    public void receiveResponse(Boolean response) {

        //notify waiting method
        synchronized (lockMonitor) {
            if (!response) {
                MyLogger.getLogger().info(this.getClass().getSimpleName() + " could not execute acting command");
            }
            lockMonitor.notify();
        }
    }
}
