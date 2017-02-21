package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.*;

/**
 * Leaf node with desire for other agent
 * Created by Jan on 21-Feb-17.
 */
public class LeafNodeWithDesireForOtherAgents extends LeafNode<DesireForOthers, IntentionWithDesireForOtherAgents> {
    LeafNodeWithDesireForOtherAgents(DesireForOthers desire) {
        super(desire);
    }

    @Override
    public void accept(TreeVisitorInterface treeVisitor) {
        treeVisitor.visit(this);
    }
}
