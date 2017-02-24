package cz.jan.maly.model.metadata.commitment;

import cz.jan.maly.model.metadata.DesireParameters;

/**
 * Contract to be implemented by each node in working memory to return copy of subtree induced by it
 * Created by Jan on 24-Feb-17.
 */
public abstract class TreeNodeInWorkingMemory extends TreeNode<IntentionNodeInWorkingMemory> {
    TreeNodeInWorkingMemory(DesireParameters desireParameters, IntentionNodeInWorkingMemory parent, boolean isAgentCommittedToIt) {
        super(desireParameters, parent, isAgentCommittedToIt);
    }

    TreeNodeInWorkingMemory(DesireParameters desireParameters, boolean isAgentCommittedToIt) {
        super(desireParameters, isAgentCommittedToIt);
    }

    /**
     * Returns copy of subtree
     *
     * @param parent
     * @return
     */
    abstract TreeNodeReadOnly getCopy(IntentionNode parent);

    /**
     * Returns copy of subtree
     *
     * @return
     */
    abstract TreeNodeReadOnly getCopy();

}
