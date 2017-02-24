package cz.jan.maly.model.metadata.commitment;

import cz.jan.maly.model.metadata.DesireParameters;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Describes intention node used in working memory - by agent
 * Created by Jan on 24-Feb-17.
 */
public class IntentionNodeInWorkingMemoryCommitment extends CommitmentTreeNodeInWorkingMemory {
    private final Set<CommitmentTreeNodeInWorkingMemory> nodeList = new HashSet<>();

    IntentionNodeInWorkingMemoryCommitment(DesireParameters desireParameters, IntentionNodeInWorkingMemoryCommitment parent) {
        super(desireParameters, parent, true);
    }

    IntentionNodeInWorkingMemoryCommitment(DesireParameters desireParameters) {
        super(desireParameters, true);
    }

    public void addChild(CommitmentTreeNodeInWorkingMemory treeNodeInWorkingMemory) {
        nodeList.add(treeNodeInWorkingMemory);
    }

    void removeChild(CommitmentTreeNodeInWorkingMemory treeNodeInWorkingMemory) {
        nodeList.remove(treeNodeInWorkingMemory);
    }

    /**
     * Returns child intentions to which this intention is parent
     *
     * @return
     */
    public Set<IntentionNodeInWorkingMemoryCommitment> getChildIntentions() {
        return nodeList.stream()
                .filter(treeNodeInWorkingMemory -> treeNodeInWorkingMemory instanceof IntentionNodeInWorkingMemoryCommitment)
                .map(treeNodeInWorkingMemory -> (IntentionNodeInWorkingMemoryCommitment) treeNodeInWorkingMemory)
                .collect(Collectors.toSet());
    }

    /**
     * Returns child desires to which this intention is parent
     *
     * @return
     */
    public Set<DesireNodeInWorkingMemoryCommitment> getChildDesires() {
        return nodeList.stream()
                .filter(treeNodeInWorkingMemory -> treeNodeInWorkingMemory instanceof DesireNodeInWorkingMemoryCommitment)
                .map(treeNodeInWorkingMemory -> (DesireNodeInWorkingMemoryCommitment) treeNodeInWorkingMemory)
                .collect(Collectors.toSet());
    }


    /**
     * Remove commitment to this intention
     */
    public void removeCommitment(CommitmentTreeInWorkingMemory commitmentTree) {
        if (parent.isPresent()) {
            DesireNodeInWorkingMemoryCommitment desire = new DesireNodeInWorkingMemoryCommitment(desireParameters, parent.get());
            parent.get().removeChild(this);
            parent.get().addChild(desire);
        } else {
            DesireNodeInWorkingMemoryCommitment desire = new DesireNodeInWorkingMemoryCommitment(desireParameters);
            commitmentTree.removeChild(this);
            commitmentTree.addChild(desire);
        }
    }

    @Override
    CommitmentTreeNodeReadOnly getCopy(IntentionNodeCommitment parent) {
        return new IntentionNodeCommitment(desireParameters, parent, nodeList);
    }

    @Override
    CommitmentTreeNodeReadOnly getCopy() {
        return new IntentionNodeCommitment(desireParameters, nodeList);
    }
}
