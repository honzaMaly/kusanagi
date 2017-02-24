package cz.jan.maly.model.metadata.commitment;

import cz.jan.maly.model.metadata.DesireParameters;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Intention node which is read only
 * Created by Jan on 24-Feb-17.
 */
public class IntentionNode extends TreeNodeReadOnly {
    @Getter
    final Set<IntentionNode> intentionsOnLowerLevel = new HashSet<>();

    @Getter
    final Set<DesireNode> desiresOnLowerLevel = new HashSet<>();

    IntentionNode(DesireParameters desireParameters, Set<TreeNodeInWorkingMemory> childes) {
        super(desireParameters, true);
        addChildes(childes);
    }

    IntentionNode(DesireParameters desireParameters, IntentionNode parent, Set<TreeNodeInWorkingMemory> childes) {
        super(desireParameters, parent, true);
        addChildes(childes);
    }

    private void addChildes(Set<TreeNodeInWorkingMemory> childes) {
        childes.forEach(treeNodeInWorkingMemory -> {
            TreeNodeReadOnly treeNode = treeNodeInWorkingMemory.getCopy(this);
            if (treeNode instanceof IntentionNode) {
                intentionsOnLowerLevel.add((IntentionNode) treeNode);
            } else {
                desiresOnLowerLevel.add((DesireNode) treeNode);
            }
        });
    }
}
