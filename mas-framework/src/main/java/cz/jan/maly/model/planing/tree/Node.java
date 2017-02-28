package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.DesireKeyIdentificationInterface;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;

/**
 * Template for node. It defines common data structure (methods) for various nodes which extend it.
 * Created by Jan on 28-Feb-17.
 */
abstract class Node implements DesireKeyIdentificationInterface, VisitorAcceptor {
    private final DesireParameters desireParameters;
    final int level;

    //to only extend classes defined in this scope
    private Node(DesireParameters desireParameters, int level) {
        this.desireParameters = desireParameters;
        this.level = level;
    }

    @Override
    public DesireKey getDesireKey() {
        return desireParameters.getDesireKey();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node node = (Node) o;

        if (level != node.level) return false;
        return desireParameters.equals(node.desireParameters);
    }

    @Override
    public int hashCode() {
        int result = desireParameters.hashCode();
        result = 31 * result + level;
        return result;
    }

    /**
     * Template for nodes in top level
     */
    static abstract class TopLevel extends Node {
        final Tree tree;

        TopLevel(Tree tree, DesireParameters desireParameters) {
            super(desireParameters, 0);
            this.tree = tree;
        }
    }

    /**
     * Template for nodes not in top level
     */
    static abstract class NotTopLevel<K extends Node & IntentionNodeWithChildes> extends Node {
        final K parent;

        NotTopLevel(K parent, DesireParameters desireParameters) {
            super(desireParameters, parent.level + 1);
            this.parent = parent;
        }
    }

}
