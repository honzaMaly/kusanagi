package cz.jan.maly.model.planing.tree.visitors;

import cz.jan.maly.model.planing.tree.TreeVisitorInterface;

/**
 * Commitment visitor traverse subtrees to decide commitments to desires - for given desire in root of subtree decide
 * based on gate (and supplied arguments to it) if agent should commit to this desire (if so, intention is made) and
 * visitor move further in subtree else backtrack
 * Created by Jan on 22-Feb-17.
 */
public class CommitmentExecutor implements TreeVisitorInterface {

    @Override
    public void visit(LeafNodeWithPlan.WithOwnDesire leafNodeWithOwnDesire) {

        //only try to decide commitment if node is not intention yet
        if (!leafNodeWithOwnDesire.isCommitted()) {
            if (leafNodeWithOwnDesire.madeCommitment()) {

                //todo reflect this in meta tree
            }
        }
    }

    @Override
    public void visit(LeafNodeWithPlan.WithAnotherAgentDesire leafNodeWithAnotherAgentDesire) {

        //only try to decide commitment if node is not intention yet
        if (!leafNodeWithAnotherAgentDesire.isCommitted()) {
            if (leafNodeWithAnotherAgentDesire.madeCommitment()) {

                //todo reflect this in meta tree
                //TODO add itself to register. if operation failed, remove commitment
            }
        }
    }

    @Override
    public void visit(LeafNodeWithDesireForOtherAgents leafNodeWithDesireForOtherAgents) {

        //only try to decide commitment if node is not intention yet
        if (!leafNodeWithDesireForOtherAgents.isCommitted()) {
            if (leafNodeWithDesireForOtherAgents.madeCommitment()) {

                //todo reflect this in meta tree
                //todo add to register - share with mediator, put shared object to agent, to easily update it by data from mediator
            }
        }
    }

    @Override
    public void visit(WithOwnDesireIntermediateNode withOwnDesireIntermediateNode) {

        //only try to decide commitment if node is not intention yet
        if (!withOwnDesireIntermediateNode.isCommitted()) {
            if (withOwnDesireIntermediateNode.madeCommitment()) {

                //todo reflect this in meta tree

                //go to subtree as root is opened but first add those nodes
                withOwnDesireIntermediateNode.addChildes();
                withOwnDesireIntermediateNode.branch(this);
            }
        } else {

            //go to subtree as root is opened
            withOwnDesireIntermediateNode.branch(this);
        }
    }

    @Override
    public void visit(WithAnotherAgentDesireIntermediateNode withAnotherAgentDesireIntermediateNode) {

        //only try to decide commitment if node is not intention yet
        if (!withAnotherAgentDesireIntermediateNode.isCommitted()) {
            if (withAnotherAgentDesireIntermediateNode.madeCommitment()) {

                //todo reflect this in meta tree
                //TODO add itself to register. if operation failed, remove commitment

                //go to subtree as root is opened but first add those nodes
                withAnotherAgentDesireIntermediateNode.addChildes();
                withAnotherAgentDesireIntermediateNode.branch(this);
            }
        } else {

            //go to subtree as root is opened
            withAnotherAgentDesireIntermediateNode.branch(this);
        }
    }
}
