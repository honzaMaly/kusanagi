package cz.jan.maly.model.metadata.commitment;

import cz.jan.maly.model.metadata.DesireParameters;

/**
 * Template for read only nodes
 * Created by Jan on 24-Feb-17.
 */
public class TreeNodeReadOnly extends TreeNode<IntentionNode> {
    TreeNodeReadOnly(DesireParameters desireParameters, IntentionNode parent, boolean isAgentCommittedToIt) {
        super(desireParameters, parent, isAgentCommittedToIt);
    }

    TreeNodeReadOnly(DesireParameters desireParameters, boolean isAgentCommittedToIt) {
        super(desireParameters, isAgentCommittedToIt);
    }
}
