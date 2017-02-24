package cz.jan.maly.model.metadata.commitment;

import cz.jan.maly.model.metadata.DesireParameters;

/**
 * Describes desire node
 * Created by Jan on 24-Feb-17.
 */
public class DesireNode extends TreeNodeReadOnly {
    DesireNode(DesireParameters desireParameters) {
        super(desireParameters, false);
    }

    DesireNode(DesireParameters desireParameters, IntentionNode parent) {
        super(desireParameters, parent, false);
    }
}
