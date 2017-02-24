package cz.jan.maly.model.metadata.commitment;

import cz.jan.maly.model.metadata.DesireParameters;

/**
 * Template for read only nodes
 * Created by Jan on 24-Feb-17.
 */
public class CommitmentTreeNodeReadOnly extends CommitmentTreeNode<IntentionNodeCommitment> {
    CommitmentTreeNodeReadOnly(DesireParameters desireParameters, IntentionNodeCommitment parent, boolean isAgentCommittedToIt) {
        super(desireParameters, parent, isAgentCommittedToIt);
    }

    CommitmentTreeNodeReadOnly(DesireParameters desireParameters, boolean isAgentCommittedToIt) {
        super(desireParameters, isAgentCommittedToIt);
    }
}
