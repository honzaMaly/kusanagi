package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.AbstractIntention;
import cz.jan.maly.model.planing.OwnDesire;

/**
 * Concrete implementation of intermediate node with own desire
 * Created by Jan on 21-Feb-17.
 */
public class WithOwnDesireIntermediateNode extends IntermediateNode<OwnDesire, AbstractIntention<OwnDesire>> {
    WithOwnDesireIntermediateNode(OwnDesire desire) {
        super(desire);
    }

    @Override
    public void accept(TreeVisitorInterface treeVisitor) {
        treeVisitor.visit(this);
    }
}
