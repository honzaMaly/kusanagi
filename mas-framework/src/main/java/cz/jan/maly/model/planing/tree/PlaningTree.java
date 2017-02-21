package cz.jan.maly.model.planing.tree;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jan on 21-Feb-17.
 */
public class PlaningTree implements VisitorAcceptor {
    private final Set<TreeNode> nodesInTree = new HashSet<>();

    @Override
    public void accept(TreeVisitorInterface treeVisitor) {
        nodesInTree.forEach(treeNode -> treeNode.accept(treeVisitor));
    }

}
