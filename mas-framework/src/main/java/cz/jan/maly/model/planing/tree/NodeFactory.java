package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.planing.OwnDesire;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Factory to get correct Node instance based on arguments
 * Created by Jan on 28-Feb-17.
 */
class NodeFactory {

    static Node getTreeNode(DesireForOthers desire, IntentionNodeAtTopLevel.TopLevel node) {
        throw new NotImplementedException();
    }

    static Node getTreeNode(OwnDesire.WithAbstractIntention desire, IntentionNodeAtTopLevel.TopLevel node) {
        throw new NotImplementedException();
    }

    static Node getTreeNode(OwnDesire.WithIntentionWithPlan desire, IntentionNodeAtTopLevel.TopLevel node) {
        throw new NotImplementedException();
    }

}
