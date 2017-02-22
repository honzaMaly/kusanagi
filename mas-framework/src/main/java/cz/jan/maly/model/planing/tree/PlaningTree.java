package cz.jan.maly.model.planing.tree;

import java.util.HashSet;
import java.util.Set;

/**
 * Entry point for visitors of planning tree to travers it - it represents "root" of tree
 * Created by Jan on 21-Feb-17.
 */
public class PlaningTree implements VisitorAcceptor {
    private final Set<TreeNode> nodesInTree = new HashSet<>();

    @Override
    public void accept(TreeVisitorInterface treeVisitor) {
        nodesInTree.forEach(treeNode -> treeNode.accept(treeVisitor));
    }

    //todo methods - to work with trees by visitors / to remove or add desires

}
