package cz.jan.maly.model.planing.tree.visitors;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.planing.tree.*;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Commitment visitor traverse subtrees to decide commitments to intentions removal - for given intention in root of subtree decide
 * based on gate (and supplied arguments to it) if agent should remove commitment to this intention (if so,
 * intention is removed and replaced by desire). If commitment to intention is not removed visitor move further in subtree
 * else do backtrack to go to other branches
 * Created by Jan on 22-Feb-17.
 */
public class CommitmentRemovalDecider implements TreeVisitorInterface {

    @Override
    public void visitTree(Tree tree, Agent agent) {
        branch(tree, agent);
    }

    /**
     * Decides commitment removal of node and sends this visitor to subtree
     *
     * @param parent
     * @param <V>
     * @param <K>
     */
    private <K extends Node<?> & IntentionNodeInterface & VisitorAcceptor, V extends Node<?> & DesireNodeInterface<K>> void branch(Parent<V, K> parent, Agent agent) {
        List<K> intentionNodes = parent.getNodesWithIntention();

        //decide removal of commitment to intentions
        Iterator<K> it = intentionNodes.iterator();
        while (it.hasNext()) {
            K node = it.next();
            node.removeCommitment(
                    new DataForDecision(node.getParametersToLoad(),
                            agent,
                            parent.getNodesWithIntention().stream()
                                    .map(Node::getDesireKey)
                                    .collect(Collectors.toList()),
                            parent.getNodesWithDesire().stream()
                                    .map(Node::getDesireKey)
                                    .collect(Collectors.toList()),
                            intentionNodes.stream()
                                    .map(Node::getDesireKey)
                                    .collect(Collectors.toList()),
                            parent.getDesireKeyAssociatedWithParent()
                    ),
                    agent
            );
            it.remove();
        }

        //visit subtrees induced by remaining intentions
        parent.getNodesWithIntention().forEach(k -> k.accept(this));
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithAbstractPlan<?, ?> node, Agent agent) {
        branch(node, agent);
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithPlan<?, ?> node, Agent agent) {
        //do nothing, already decided
    }

    @Override
    public void visit(IntentionNodeNotTopLevel.WithAbstractPlan<?, ?, ?> node, Agent agent) {
        branch(node, agent);
    }

    @Override
    public void visit(IntentionNodeNotTopLevel.WithPlan<?> node, Agent agent) {
        //do nothing, already decided
    }

    @Override
    public void visit(IntentionNodeAtTopLevel.WithDesireForOthers node, Agent agent) {
        //do nothing, already decided
    }

    @Override
    public void visit(IntentionNodeNotTopLevel.WithDesireForOthers<?> node, Agent agent) {
        //do nothing, already decided
    }
}
