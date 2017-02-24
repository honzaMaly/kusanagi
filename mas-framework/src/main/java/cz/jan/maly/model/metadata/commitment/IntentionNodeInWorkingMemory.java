package cz.jan.maly.model.metadata.commitment;

import cz.jan.maly.model.metadata.DesireParameters;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Describes intention node used in working memory - by agent
 * Created by Jan on 24-Feb-17.
 */
public class IntentionNodeInWorkingMemory extends TreeNodeInWorkingMemory {
    private final Set<TreeNodeInWorkingMemory> nodeList = new HashSet<>();

    IntentionNodeInWorkingMemory(DesireParameters desireParameters, IntentionNodeInWorkingMemory parent) {
        super(desireParameters, parent, true);
    }

    IntentionNodeInWorkingMemory(DesireParameters desireParameters) {
        super(desireParameters, true);
    }

    public void addChild(TreeNodeInWorkingMemory treeNodeInWorkingMemory) {
        nodeList.add(treeNodeInWorkingMemory);
    }

    void removeChild(TreeNodeInWorkingMemory treeNodeInWorkingMemory) {
        nodeList.remove(treeNodeInWorkingMemory);
    }

    /**
     * Returns child intentions to which this intention is parent
     *
     * @return
     */
    public Set<IntentionNodeInWorkingMemory> getChildIntentions() {
        return nodeList.stream()
                .filter(treeNodeInWorkingMemory -> treeNodeInWorkingMemory instanceof IntentionNodeInWorkingMemory)
                .map(treeNodeInWorkingMemory -> (IntentionNodeInWorkingMemory) treeNodeInWorkingMemory)
                .collect(Collectors.toSet());
    }

    /**
     * Returns child desires to which this intention is parent
     *
     * @return
     */
    public Set<DesireNodeInWorkingMemory> getChildDesires() {
        return nodeList.stream()
                .filter(treeNodeInWorkingMemory -> treeNodeInWorkingMemory instanceof DesireNodeInWorkingMemory)
                .map(treeNodeInWorkingMemory -> (DesireNodeInWorkingMemory) treeNodeInWorkingMemory)
                .collect(Collectors.toSet());
    }


    /**
     * Remove commitment to this intention
     */
    public void removeCommitment(CommitmentTreeInWorkingMemory commitmentTree) {
        if (parent.isPresent()) {
            DesireNodeInWorkingMemory desire = new DesireNodeInWorkingMemory(desireParameters, parent.get());
            parent.get().removeChild(this);
            parent.get().addChild(desire);
        } else {
            DesireNodeInWorkingMemory desire = new DesireNodeInWorkingMemory(desireParameters);
            commitmentTree.removeChild(this);
            commitmentTree.addChild(desire);
        }
    }

    @Override
    TreeNodeReadOnly getCopy(IntentionNode parent) {
        return new IntentionNode(desireParameters, parent, nodeList);
    }

    @Override
    TreeNodeReadOnly getCopy() {
        return new IntentionNode(desireParameters, nodeList);
    }
}
