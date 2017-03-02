package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.DesireKeyIdentificationInterface;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.planing.DecisionAboutCommitment;

/**
 * Template for node. It defines common data structure (methods) for various nodes which extend it.
 * Created by Jan on 28-Feb-17.
 */
public abstract class Node<K extends Parent> implements DesireKeyIdentificationInterface, VisitorAcceptor, DecisionAboutCommitment {
    final DesireParameters desireParameters;
    final int level;
    final K parent;

    //to only extend classes defined in this scope
    private Node(DesireParameters desireParameters, int level, K parent) {
        this.desireParameters = desireParameters;
        this.level = level;
        this.parent = parent;
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
    static abstract class TopLevel extends Node<Tree> {

        TopLevel(Tree parent, DesireParameters desireParameters) {
            super(desireParameters, 0, parent);
        }
    }

    /**
     * Template for nodes not in top level
     */
    static abstract class NotTopLevel<K extends Node & IntentionNodeWithChildes & Parent> extends Node<K> {
        NotTopLevel(K parent, DesireParameters desireParameters) {
            super(desireParameters, parent.level + 1, parent);
        }
    }

}
