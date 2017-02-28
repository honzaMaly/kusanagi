package cz.jan.maly.model.planing.tree.visitors;

import cz.jan.maly.model.planing.tree.TreeVisitorInterface;

/**
 * Commitment visitor traverse subtrees to decide commitments to intentions removal - for given intention in root of subtree decide
 * based on gate (and supplied arguments to it) if agent should remove commitment to this intention (if so,
 * intention is removed and replaced by desire). If commitment to intention is not removed visitor move further in subtree
 * else do backtrack to go to other branches
 * Created by Jan on 22-Feb-17.
 */
public class RemoveCommitmentExecutor implements TreeVisitorInterface {

    @Override
    public void visit(LeafNodeWithPlan.WithOwnDesire leafNodeWithOwnDesire) {

        //only try to decide commitment if node is intention
        if (leafNodeWithOwnDesire.isCommitted()) {
            if (leafNodeWithOwnDesire.removedCommitment()) {

                //todo reflect this in meta tree
            }
        }
    }

    @Override
    public void visit(LeafNodeWithPlan.WithAnotherAgentDesire leafNodeWithAnotherAgentDesire) {

        //only try to decide commitment if node is intention
        if (leafNodeWithAnotherAgentDesire.isCommitted()) {
            if (leafNodeWithAnotherAgentDesire.removedCommitment()) {

                //todo reflect this in meta tree
                //TODO remove itself from shared register
            }
        }
    }

    @Override
    public void visit(LeafNodeWithDesireForOtherAgents leafNodeWithDesireForOtherAgents) {

        //only try to decide commitment if node is intention
        if (leafNodeWithDesireForOtherAgents.isCommitted()) {
            if (leafNodeWithDesireForOtherAgents.removedCommitment()) {

                //todo reflect this in meta tree
                //TODO remove itself from shared register
            }
        }
    }

    @Override
    public void visit(WithOwnDesireIntermediateNode withOwnDesireIntermediateNode) {

        //only try to decide commitment if node is intention
        if (withOwnDesireIntermediateNode.isCommitted()) {
            if (withOwnDesireIntermediateNode.removedCommitment()) {

                //todo reflect this in meta tree

                withOwnDesireIntermediateNode.cut();
            } else {

                //go to childes to decide their commitment
                withOwnDesireIntermediateNode.branch(this);
            }
        }
    }

    @Override
    public void visit(WithAnotherAgentDesireIntermediateNode withAnotherAgentDesireIntermediateNode) {

        //only try to decide commitment if node is intention
        if (withAnotherAgentDesireIntermediateNode.isCommitted()) {
            if (withAnotherAgentDesireIntermediateNode.removedCommitment()) {

                //todo reflect this in meta tree
                //TODO remove itself from shared register

                withAnotherAgentDesireIntermediateNode.cut();
            } else {

                //go to childes to decide their commitment
                withAnotherAgentDesireIntermediateNode.branch(this);
            }
        }
    }
}
