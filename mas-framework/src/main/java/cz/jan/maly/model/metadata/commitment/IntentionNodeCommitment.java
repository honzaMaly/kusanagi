package cz.jan.maly.model.metadata.commitment;

import cz.jan.maly.model.metadata.DesireParameters;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Intention node which is read only
 * Created by Jan on 24-Feb-17.
 */
public class IntentionNodeCommitment extends CommitmentTreeNodeReadOnly {
    @Getter
    final Set<IntentionNodeCommitment> intentionsOnLowerLevel = new HashSet<>();

    @Getter
    final Set<DesireNodeCommitment> desiresOnLowerLevel = new HashSet<>();

    IntentionNodeCommitment(DesireParameters desireParameters, Set<CommitmentTreeNodeInWorkingMemory> childes) {
        super(desireParameters, true);
        addChildes(childes);
    }

    IntentionNodeCommitment(DesireParameters desireParameters, IntentionNodeCommitment parent, Set<CommitmentTreeNodeInWorkingMemory> childes) {
        super(desireParameters, parent, true);
        addChildes(childes);
    }

    private void addChildes(Set<CommitmentTreeNodeInWorkingMemory> childes) {
        childes.forEach(treeNodeInWorkingMemory -> {
            CommitmentTreeNodeReadOnly treeNode = treeNodeInWorkingMemory.getCopy(this);
            if (treeNode instanceof IntentionNodeCommitment) {
                intentionsOnLowerLevel.add((IntentionNodeCommitment) treeNode);
            } else {
                desiresOnLowerLevel.add((DesireNodeCommitment) treeNode);
            }
        });
    }
}
