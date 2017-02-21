package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.planing.OwnDesire;

/**
 * Factory to get correct TreeNode instance based on arguments
 * Created by Jan on 21-Feb-17.
 */
public class TreeNodeFactory {

    public static TreeNode getTreeNode(OwnDesire ownDesire) {
        if (!ownDesire.isAbstract()) {
            return new LeafNodeWithPlan.WithOwnDesire(ownDesire);
        }
        return new WithOwnDesireIntermediateNode(ownDesire);
    }

    public static TreeNode getTreeNode(DesireFromAnotherAgent desireFromAnotherAgent) {
        if (!desireFromAnotherAgent.isAbstract()) {
            return new LeafNodeWithPlan.WithAnotherAgentDesire(desireFromAnotherAgent);
        }
        return new WithAnotherAgentDesireIntermediateNode(desireFromAnotherAgent);
    }

    public static TreeNode getTreeNode(DesireForOthers desireForOthers) {
        return new LeafNodeWithDesireForOtherAgents(desireForOthers);
    }


}
