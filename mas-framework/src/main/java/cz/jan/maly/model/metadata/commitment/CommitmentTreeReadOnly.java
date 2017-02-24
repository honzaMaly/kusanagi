package cz.jan.maly.model.metadata.commitment;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entry point for commitment tree. This tree is intended to be read only. This should get idea (if interested) to
 * other agents what is going on in another agent to make better informed decisions
 * Created by Jan on 24-Feb-17.
 */
public class CommitmentTreeReadOnly implements CommitmentTreeInterface {
    @Getter
    final Set<IntentionNodeCommitment> intentionsOnTopLevel = new HashSet<>();

    @Getter
    final Set<DesireNodeCommitment> desiresOnTopLevel = new HashSet<>();

    CommitmentTreeReadOnly(Set<CommitmentTreeNodeInWorkingMemory> topLevelNodes) {
        topLevelNodes.forEach(treeNodeInWorkingMemory -> {
            CommitmentTreeNodeReadOnly treeNode = treeNodeInWorkingMemory.getCopy();
            if (treeNode instanceof IntentionNodeCommitment) {
                intentionsOnTopLevel.add((IntentionNodeCommitment) treeNode);
            } else {
                desiresOnTopLevel.add((DesireNodeCommitment) treeNode);
            }
        });
    }
}
