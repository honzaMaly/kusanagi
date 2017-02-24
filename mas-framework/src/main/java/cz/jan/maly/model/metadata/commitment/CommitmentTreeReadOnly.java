package cz.jan.maly.model.metadata.commitment;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entry point for commitment tree. This tree is intended to be read only. This should get idea (if interested) to
 * other agents what is going on in another agent to make better informed decisions
 * Created by Jan on 24-Feb-17.
 */
public class CommitmentTreeReadOnly {
    @Getter
    final Set<IntentionNode> intentionsOnTopLevel = new HashSet<>();

    @Getter
    final Set<DesireNode> desiresOnTopLevel = new HashSet<>();

    CommitmentTreeReadOnly(Set<TreeNodeInWorkingMemory> topLevelNodes) {
        topLevelNodes.forEach(treeNodeInWorkingMemory -> {
            TreeNodeReadOnly treeNode = treeNodeInWorkingMemory.getCopy();
            if (treeNode instanceof IntentionNode) {
                intentionsOnTopLevel.add((IntentionNode) treeNode);
            } else {
                desiresOnTopLevel.add((DesireNode) treeNode);
            }
        });
    }
}
