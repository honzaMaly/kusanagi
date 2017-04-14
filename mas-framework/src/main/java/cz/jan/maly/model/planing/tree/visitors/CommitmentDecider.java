package cz.jan.maly.model.planing.tree.visitors;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.model.planing.tree.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CommitmentDecider visitor traverse subtrees to decide commitments to desires - for given desire in root of subtree decide
 * based on gate (and supplied arguments to it) if agent should commit to this desire (if so, intention is made) and
 * visitor move further in subtree else backtrack
 * Created by Jan on 22-Feb-17.
 */
public class CommitmentDecider implements TreeVisitorInterface {
    private final Tree tree;

    public CommitmentDecider(Tree tree) {
        this.tree = tree;
    }

    @Override
    public void visitTree() {
        branch(tree);
    }

    /**
     * Decides commitment of parent's childes and sends this visitor to subtree
     *
     * @param parent
     * @param <V>
     * @param <K>
     */
    private <K extends Node<?> & IntentionNodeInterface & VisitorAcceptor, V extends Node<?> & DesireNodeInterface<K>> void branch(Parent<V, K> parent) {
        List<V> desiresNodes = parent.getNodesWithDesire();
        List<DesireKey> didNotMakeCommitmentToTypes = new ArrayList<>();

        //decide commitment of desires
        Iterator<V> it = desiresNodes.iterator();
        while (it.hasNext()) {
            V node = it.next();
            Optional<K> committedDesire = node.makeCommitment(
                    parent.getNodesWithIntention().stream()
                            .map(Node::getDesireKey)
                            .collect(Collectors.toList()),
                    didNotMakeCommitmentToTypes,
                    desiresNodes.stream()
                            .map(Node::getDesireKey)
                            .collect(Collectors.toList()));
            if (!committedDesire.isPresent()) {
                didNotMakeCommitmentToTypes.add(node.getDesireKey());
            }
            it.remove();
        }

        //visit subtrees induced by intentions
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
        //do nothing, already committed
    }

    @Override
    public void visitNodeWithReasoningCommand(IntentionNodeNotTopLevel.WithCommand<?, ?, ReasoningCommand> node) {
        //do nothing, already committed
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithDesireForOthers node) {
        //do nothing, already committed
    }

    @Override
    public void visit(IntentionNodeNotTopLevel.WithDesireForOthers<?> node) {
        //do nothing, already committed
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithCommand.OwnReasoning node) {
        //do nothing, already committed
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithCommand.OwnActing node) {
        //do nothing, already committed
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithCommand.FromAnotherAgent node) {
        //do nothing, already committed
    }
}
