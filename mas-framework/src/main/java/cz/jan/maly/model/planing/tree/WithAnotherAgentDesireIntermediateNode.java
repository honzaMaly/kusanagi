package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.AbstractIntention;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;

/**
 * Concrete implementation of intermediate node with desire by another agent
 * Created by Jan on 21-Feb-17.
 */
public class WithAnotherAgentDesireIntermediateNode extends IntermediateNode<DesireFromAnotherAgent, AbstractIntention<DesireFromAnotherAgent>> {
    WithAnotherAgentDesireIntermediateNode(DesireFromAnotherAgent desire) {
        super(desire);
    }

    @Override
    public void accept(TreeVisitorInterface treeVisitor) {
        treeVisitor.visit(this);
    }
}
