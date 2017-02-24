package cz.jan.maly.model.metadata.commitment;

import cz.jan.maly.model.metadata.DesireParameters;

/**
 * Describes desire node used in working memory - by agent
 * Created by Jan on 24-Feb-17.
 */
public class DesireNodeInWorkingMemoryCommitment extends CommitmentTreeNodeInWorkingMemory {

    DesireNodeInWorkingMemoryCommitment(DesireParameters desireParameters, IntentionNodeInWorkingMemoryCommitment parent) {
        super(desireParameters, parent, false);
    }

    DesireNodeInWorkingMemoryCommitment(DesireParameters desireParameters) {
        super(desireParameters, false);
    }

    @Override
    CommitmentTreeNodeReadOnly getCopy(IntentionNodeCommitment parent) {
        return new DesireNodeCommitment(desireParameters, parent);
    }

    @Override
    CommitmentTreeNodeReadOnly getCopy() {
        return new DesireNodeCommitment(desireParameters);
    }

    /**
     * Make commitment to this desire
     *
     * @return
     */
    public void makeCommitment(CommitmentTreeInWorkingMemory commitmentTree) {
        if (parent.isPresent()) {
            IntentionNodeInWorkingMemoryCommitment intention = new IntentionNodeInWorkingMemoryCommitment(desireParameters, parent.get());
            parent.get().removeChild(this);
            parent.get().addChild(intention);
        } else {
            IntentionNodeInWorkingMemoryCommitment intention = new IntentionNodeInWorkingMemoryCommitment(desireParameters);
            commitmentTree.removeChild(this);
            parent.get().addChild(intention);
        }
    }
}
