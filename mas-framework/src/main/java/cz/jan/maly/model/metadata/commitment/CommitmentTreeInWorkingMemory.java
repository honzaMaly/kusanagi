package cz.jan.maly.model.metadata.commitment;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Internal representation of agent commitment process
 * Created by Jan on 24-Feb-17.
 */
public class CommitmentTreeInWorkingMemory implements CommitmentTreeInterface {
    private final Set<CommitmentTreeNodeInWorkingMemory> nodeList = new HashSet<>();

    void addChild(CommitmentTreeNodeInWorkingMemory treeNodeInWorkingMemory) {
        nodeList.add(treeNodeInWorkingMemory);
    }

    void removeChild(CommitmentTreeNodeInWorkingMemory treeNodeInWorkingMemory) {
        nodeList.remove(treeNodeInWorkingMemory);
    }

    /**
     * Returns read only copy
     * @return
     */
    public CommitmentTreeReadOnly getReadOnlyCopy(){
        return new CommitmentTreeReadOnly(nodeList);
    }

    /**
     * Returns top level intentions to which this intention is parent
     *
     * @return
     */
    public Set<IntentionNodeInWorkingMemoryCommitment> getTopLevelIntentions() {
        return nodeList.stream()
                .filter(treeNodeInWorkingMemory -> treeNodeInWorkingMemory instanceof IntentionNodeInWorkingMemoryCommitment)
                .map(treeNodeInWorkingMemory -> (IntentionNodeInWorkingMemoryCommitment) treeNodeInWorkingMemory)
                .collect(Collectors.toSet());
    }

    /**
     * Returns top level desires to which this intention is parent
     *
     * @return
     */
    public Set<DesireNodeInWorkingMemoryCommitment> getTopLevelDesires() {
        return nodeList.stream()
                .filter(treeNodeInWorkingMemory -> treeNodeInWorkingMemory instanceof DesireNodeInWorkingMemoryCommitment)
                .map(treeNodeInWorkingMemory -> (DesireNodeInWorkingMemoryCommitment) treeNodeInWorkingMemory)
                .collect(Collectors.toSet());
    }

}
