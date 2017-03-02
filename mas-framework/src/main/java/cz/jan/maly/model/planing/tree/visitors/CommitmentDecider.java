package cz.jan.maly.model.planing.tree.visitors;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.tree.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Commitment visitor traverse subtrees to decide commitments to desires - for given desire in root of subtree decide
 * based on gate (and supplied arguments to it) if agent should commit to this desire (if so, intention is made) and
 * visitor move further in subtree else backtrack
 * Created by Jan on 22-Feb-17.
 */
public class CommitmentDecider implements TreeVisitorInterface {
    private final Agent agent;
    private final Tree tree;

    public CommitmentDecider(Agent agent, Tree tree) {
        this.agent = agent;
        this.tree = tree;
    }

    @Override
    public void visitTree() {
        branch(tree);
    }

    //todo add desire mediator to constructor and use it in commitment method for nodes with desire from another agent, desire for others. same for intention to remove them

    /**
     * Decides commitment of parent's childes and sends this visitor to subtree
     * @param parent
     * @param <V>
     * @param <K>
     */
    private <V extends Node & DesireNodeInterface, K extends Node & IntentionNodeInterface> void branch(Parent<V, K> parent) {
        List<V> desiresNodes = parent.getNodesWithDesire();
        List<DesireKey> didNotMakeCommitmentToTypes = new ArrayList<>();

        //decide commitment of desires
        Iterator<V> it = desiresNodes.iterator();
        while (it.hasNext()) {
            V node = it.next();
            Optional<K> committedDesire = node.makeCommitment(
                    new DataForDecision(node.getParametersToLoad(),
                            agent,
                            parent.getNodesWithIntention().stream()
                                    .map(Node::getDesireKey)
                                    .collect(Collectors.toList()),
                            didNotMakeCommitmentToTypes,
                            desiresNodes.stream()
                                    .map(Node::getDesireKey)
                                    .collect(Collectors.toList()),
                            parent.getDesireKeyAssociatedWithParent()
                    ));

            //visit
            if (!committedDesire.isPresent()) {
                didNotMakeCommitmentToTypes.add(node.getDesireKey());
            }
            it.remove();
        }

        //visit subtrees induced by intentions
        parent.getNodesWithIntention().forEach(k -> k.accept(this));
    }

    @Override
    public void visit(DesireNodeAtTopLevel node) {
        throw new IllegalStateException("Desire node should not be visited when deciding commitment");
    }

    @Override
    public void visit(DesireNodeNotTopLevel node) {
        throw new IllegalStateException("Desire node should not be visited when deciding commitment");
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithDesireForOthers node) {
        //do nothing, already committed
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithAbstractPlan node) {
        branch(node);
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithPlan node) {
        //do nothing, already committed
    }

    @Override
    public void visit(IntentionNodeNotTopLevel.ForOthers node) {
        //do nothing, already committed
    }

    @Override
    public void visit(IntentionNodeNotTopLevel.WithAbstractPlan node) {
        branch(node);
    }

    @Override
    public void visit(IntentionNodeNotTopLevel.WithPlan node) {
        //do nothing, already committed
    }
}
