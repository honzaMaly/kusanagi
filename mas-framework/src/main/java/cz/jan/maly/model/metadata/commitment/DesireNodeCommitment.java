package cz.jan.maly.model.metadata.commitment;

import cz.jan.maly.model.metadata.DesireParameters;

/**
 * Describes desire node
 * Created by Jan on 24-Feb-17.
 */
public class DesireNodeCommitment extends CommitmentTreeNodeReadOnly {
    DesireNodeCommitment(DesireParameters desireParameters) {
        super(desireParameters, false);
    }

    DesireNodeCommitment(DesireParameters desireParameters, IntentionNodeCommitment parent) {
        super(desireParameters, parent, false);
    }
}
