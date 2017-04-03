package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;

import java.util.Set;

/**
 * Desire class for agent's desires to be achieved by others
 * Created by Jan on 15-Feb-17.
 */
public class DesireForOthers extends InternalDesire<IntentionWithDesireForOtherAgents> {
    private final DesireKey sharedDesireKey;
    private final int limitOnNumberOfAgentsToCommit;

    public DesireForOthers(DesireKey desireKey, WorkingMemory memory, Commitment commitment, RemoveCommitment removeCommitment,
                           Set<DesireKey> typesOfDesiresToConsiderWhenCommitting, Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment,
                           DesireKey sharedDesireKey, int limitOnNumberOfAgentsToCommit) {
        super(desireKey, memory, commitment, removeCommitment, typesOfDesiresToConsiderWhenCommitting,
                typesOfDesiresToConsiderWhenRemovingCommitment, false);
        this.sharedDesireKey = sharedDesireKey;
        this.limitOnNumberOfAgentsToCommit = limitOnNumberOfAgentsToCommit;
    }

    @Override
    public IntentionWithDesireForOtherAgents formIntention(Agent agent) {
        return new IntentionWithDesireForOtherAgents(this, agent, removeCommitment, limitOnNumberOfAgentsToCommit, sharedDesireKey);
    }
}
