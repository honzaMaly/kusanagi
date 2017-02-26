package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.AbstractIntention;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;

/**
 * Concrete implementation of intermediate node with desire by another agent
 * Created by Jan on 21-Feb-17.
 */
public class WithAnotherAgentDesireIntermediateNode extends IntermediateNode<DesireFromAnotherAgent.WithAbstractIntention, AbstractIntention<DesireFromAnotherAgent.WithAbstractIntention>> {
    WithAnotherAgentDesireIntermediateNode(DesireFromAnotherAgent.WithAbstractIntention desire) {
        super(desire);
    }

    @Override
    AbstractIntention<DesireFromAnotherAgent.WithAbstractIntention> getIntention() {
        return desire.formIntention();
    }

    @Override
    public void accept(TreeVisitorInterface treeVisitor) {
        treeVisitor.visit(this);
    }
}
