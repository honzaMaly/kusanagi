package cz.jan.maly.model.metadata.commitment;

import cz.jan.maly.model.metadata.DesireParameters;

/**
 * Contract to be implemented by each node in working memory to return copy of subtree induced by it
 * Created by Jan on 24-Feb-17.
 */
public abstract class CommitmentTreeNodeInWorkingMemory extends CommitmentTreeNode<IntentionNodeInWorkingMemoryCommitment> {
    CommitmentTreeNodeInWorkingMemory(DesireParameters desireParameters, IntentionNodeInWorkingMemoryCommitment parent, boolean isAgentCommittedToIt) {
        super(desireParameters, parent, isAgentCommittedToIt);
    }

    CommitmentTreeNodeInWorkingMemory(DesireParameters desireParameters, boolean isAgentCommittedToIt) {
        super(desireParameters, isAgentCommittedToIt);
    }

    /**
     * Returns copy of subtree
     *
     * @param parent
     * @return
     */
    abstract CommitmentTreeNodeReadOnly getCopy(IntentionNodeCommitment parent);

    /**
     * Returns copy of subtree
     *
     * @return
     */
    abstract CommitmentTreeNodeReadOnly getCopy();

}
