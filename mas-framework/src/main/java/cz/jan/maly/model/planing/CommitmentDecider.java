package cz.jan.maly.model.planing;

import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;

import java.util.List;

/**
 * Decision point to decide agent's commitmentDecider to task
 * Created by Jan on 21-Feb-17.
 */
public class CommitmentDecider {
    private final CommitmentDeciderInitializer.DecisionStrategy decisionStrategy;
    private final DataForDecision dataForDecision;

    CommitmentDecider(CommitmentDeciderInitializer commitmentDeciderInitializer,
                      DesireParameters desireParameters) {
        this.dataForDecision = new DataForDecision(desireParameters.getDesireKey(), desireParameters, commitmentDeciderInitializer);
        this.decisionStrategy = commitmentDeciderInitializer.getDecisionStrategy();
    }

    /**
     * Returns if agent should commit to desire and make intention from it
     *
     * @return
     */
    public boolean shouldCommit(List<DesireKey> madeCommitmentToTypes, List<DesireKey> didNotMakeCommitmentToTypes,
                                List<DesireKey> typesAboutToMakeDecision, WorkingMemory memory) {
        dataForDecision.updateBeliefs(madeCommitmentToTypes, didNotMakeCommitmentToTypes, typesAboutToMakeDecision, memory);
        if (dataForDecision.isBeliefsChanged()) {
            dataForDecision.setBeliefsChanged(false);
            return decisionStrategy.shouldCommit(dataForDecision);
        } else {
            return false;
        }
    }

    /**
     * Returns if agent should commit to desire and make intention from it
     *
     * @return
     */
    public boolean shouldCommit(List<DesireKey> madeCommitmentToTypes, List<DesireKey> didNotMakeCommitmentToTypes,
                                List<DesireKey> typesAboutToMakeDecision, WorkingMemory memory, int numberOfCommittedAgents) {
        dataForDecision.updateBeliefs(madeCommitmentToTypes, didNotMakeCommitmentToTypes, typesAboutToMakeDecision, memory,
                numberOfCommittedAgents);
        if (dataForDecision.isBeliefsChanged()) {
            dataForDecision.setBeliefsChanged(false);
            return decisionStrategy.shouldCommit(dataForDecision);
        } else {
            return false;
        }
    }

}
