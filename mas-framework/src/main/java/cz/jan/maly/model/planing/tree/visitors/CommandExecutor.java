package cz.jan.maly.model.planing.tree.visitors;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.tree.*;

/**
 * CommandExecutor visitor traverse tree to get to leafs. When agent is committed to desire in leaf and it contains
 * command to be executed and sends command to agent to handle it
 * Created by Jan on 22-Feb-17.
 */
public class CommandExecutor implements TreeVisitorInterface {

    @Override
    public void visitTree(Tree tree, Agent agent) {
        branch(tree);
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
    public void visit(IntentionNodeAtTopLevel.WithAbstractPlan<?, ?> node, Agent agent) {
        branch(node);
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithPlan<?, ?> node, Agent agent) {
        agent.executeCommand(node.getCommand());
    }

    @Override
    public void visit(IntentionNodeNotTopLevel.WithAbstractPlan<?, ?, ?> node, Agent agent) {
        branch(node);
    }

    @Override
    public void visit(IntentionNodeNotTopLevel.WithPlan<?> node, Agent agent) {
        agent.executeCommand(node.getCommand());
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithDesireForOthers node, Agent agent) {
        //no action to execute
    }

    @Override
    public void visit(IntentionNodeNotTopLevel.WithDesireForOthers<?> node, Agent agent) {
        //no action to execute
    }
}
