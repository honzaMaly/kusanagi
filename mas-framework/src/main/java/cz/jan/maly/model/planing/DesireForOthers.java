package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;

/**
 * Desire class for agent's desires to be achieved by others
 * Created by Jan on 15-Feb-17.
 */
public class DesireForOthers extends InternalDesire<IntentionWithDesireForOtherAgents> {
    private final DesireKey sharedDesireKey;
    private final int limitOnNumberOfAgentsToCommit;

    public DesireForOthers(DesireKey desireKey, WorkingMemory memory, CommitmentDeciderInitializer commitmentDecider,
                           CommitmentDeciderInitializer removeCommitment, DesireKey sharedDesireKey, int limitOnNumberOfAgentsToCommit) {
        super(desireKey, memory, commitmentDecider, removeCommitment, false);
        this.sharedDesireKey = sharedDesireKey;
        this.limitOnNumberOfAgentsToCommit = limitOnNumberOfAgentsToCommit;
    }

    public DesireForOthers(DesireKey desireKey, WorkingMemory memory, CommitmentDeciderInitializer commitmentDecider,
                           CommitmentDeciderInitializer removeCommitment, DesireKey sharedDesireKey,
                           int limitOnNumberOfAgentsToCommit, DesireParameters parentsDesireParameters) {
        super(desireKey, memory, commitmentDecider, removeCommitment, false, parentsDesireParameters);
        this.sharedDesireKey = sharedDesireKey;
        this.limitOnNumberOfAgentsToCommit = limitOnNumberOfAgentsToCommit;
    }

    @Override
    public IntentionWithDesireForOtherAgents formIntention(Agent agent) {
        return new IntentionWithDesireForOtherAgents(this, agent, removeCommitment, limitOnNumberOfAgentsToCommit, sharedDesireKey);
    }
}
