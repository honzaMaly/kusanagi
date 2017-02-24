package cz.jan.maly.model.metadata.commitment;

import cz.jan.maly.model.metadata.DesireParameters;

/**
 * Describes desire node used in working memory - by agent
 * Created by Jan on 24-Feb-17.
 */
public class DesireNodeInWorkingMemory extends TreeNodeInWorkingMemory {

    DesireNodeInWorkingMemory(DesireParameters desireParameters, IntentionNodeInWorkingMemory parent) {
        super(desireParameters, parent, false);
    }

    DesireNodeInWorkingMemory(DesireParameters desireParameters) {
        super(desireParameters, false);
    }

    @Override
    TreeNodeReadOnly getCopy(IntentionNode parent) {
        return new DesireNode(desireParameters, parent);
    }

    @Override
    TreeNodeReadOnly getCopy() {
        return new DesireNode(desireParameters);
    }

    /**
     * Make commitment to this desire
     *
     * @return
     */
    public void makeCommitment(CommitmentTreeInWorkingMemory commitmentTree) {
        if (parent.isPresent()) {
            IntentionNodeInWorkingMemory intention = new IntentionNodeInWorkingMemory(desireParameters, parent.get());
            parent.get().removeChild(this);
            parent.get().addChild(intention);
        } else {
            IntentionNodeInWorkingMemory intention = new IntentionNodeInWorkingMemory(desireParameters);
            commitmentTree.removeChild(this);
            parent.get().addChild(intention);
        }
    }
}
