package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.planing.OwnDesire;

/**
 * Factory to get correct TreeNode instance based on arguments
 * Created by Jan on 21-Feb-17.
 */
public class TreeNodeFactory {

    public static TreeNode getTreeNode(OwnDesire.WithIntentionWithPlan desire) {
        return new LeafNodeWithPlan.WithOwnDesire(desire);
    }

    public static TreeNode getTreeNode(OwnDesire.WithAbstractIntention desire) {
        return new WithOwnDesireIntermediateNode(desire);
    }

    public static TreeNode getTreeNode(DesireFromAnotherAgent.WithAbstractIntention desire) {
        return new WithAnotherAgentDesireIntermediateNode(desire);
    }

    public static TreeNode getTreeNode(DesireFromAnotherAgent.WithIntentionWithPlan desire) {
        return new LeafNodeWithPlan.WithAnotherAgentDesire(desire);
    }

    public static TreeNode getTreeNode(DesireForOthers desireForOthers) {
        return new LeafNodeWithDesireForOtherAgents(desireForOthers);
    }


}
