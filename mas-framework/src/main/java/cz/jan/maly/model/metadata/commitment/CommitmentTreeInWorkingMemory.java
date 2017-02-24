package cz.jan.maly.model.metadata.commitment;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Internal representation of agent commitment process
 * Created by Jan on 24-Feb-17.
 */
public class CommitmentTreeInWorkingMemory {
    private final Set<TreeNodeInWorkingMemory> nodeList = new HashSet<>();

    void addChild(TreeNodeInWorkingMemory treeNodeInWorkingMemory) {
        nodeList.add(treeNodeInWorkingMemory);
    }

    void removeChild(TreeNodeInWorkingMemory treeNodeInWorkingMemory) {
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
    public Set<IntentionNodeInWorkingMemory> getTopLevelIntentions() {
        return nodeList.stream()
                .filter(treeNodeInWorkingMemory -> treeNodeInWorkingMemory instanceof IntentionNodeInWorkingMemory)
                .map(treeNodeInWorkingMemory -> (IntentionNodeInWorkingMemory) treeNodeInWorkingMemory)
                .collect(Collectors.toSet());
    }

    /**
     * Returns top level desires to which this intention is parent
     *
     * @return
     */
    public Set<DesireNodeInWorkingMemory> getTopLevelDesires() {
        return nodeList.stream()
                .filter(treeNodeInWorkingMemory -> treeNodeInWorkingMemory instanceof DesireNodeInWorkingMemory)
                .map(treeNodeInWorkingMemory -> (DesireNodeInWorkingMemory) treeNodeInWorkingMemory)
                .collect(Collectors.toSet());
    }

}
