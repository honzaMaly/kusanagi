package cz.jan.maly.model.metadata.commitment;

import cz.jan.maly.model.DesireKeyIdentificationInterface;
import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.Optional;
import java.util.Set;

/**
 * Template for node which is used in agent's reasoning status key - this tree is used internally or shared to check
 * commitment process by agents
 * Created by Jan on 24-Feb-17.
 */
abstract class TreeNode<V extends TreeNode> implements FactContainerInterface, DesireKeyIdentificationInterface {
    final DesireParameters desireParameters;

    @Getter
    final Optional<V> parent;

    @Getter
    private final boolean isAgentCommittedToIt;

    TreeNode(DesireParameters desireParameters, V parent, boolean isAgentCommittedToIt) {
        this.desireParameters = desireParameters;
        this.parent = Optional.ofNullable(parent);
        this.isAgentCommittedToIt = isAgentCommittedToIt;
    }

    TreeNode(DesireParameters desireParameters, boolean isAgentCommittedToIt) {
        this.desireParameters = desireParameters;
        this.parent = Optional.empty();
        this.isAgentCommittedToIt = isAgentCommittedToIt;
    }

    @Override
    public DesireKey getDesireKey() {
        return desireParameters.getDesireKey();
    }

    @Override
    public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
        return desireParameters.returnFactValueForGivenKey(factKey);
    }

    @Override
    public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
        return desireParameters.returnFactSetValueForGivenKey(factKey);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeNode)) return false;

        TreeNode treeNode = (TreeNode) o;

        return desireParameters.equals(treeNode.desireParameters);
    }

    @Override
    public int hashCode() {
        return desireParameters.hashCode();
    }
}
