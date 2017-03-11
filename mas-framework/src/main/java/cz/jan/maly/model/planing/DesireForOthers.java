package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DecisionContainerParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.IntentionParameters;

/**
 * Desire class for agent's desires to be achieved by others
 * Created by Jan on 15-Feb-17.
 */
public class DesireForOthers extends InternalDesire<IntentionWithDesireForOtherAgents> {
    private final DesireKey sharedDesireKey;
    private final int limitOnNumberOfAgentsToCommit;

    public DesireForOthers(DesireKey desireKey, Memory memory, Commitment commitment, DecisionContainerParameters decisionDesire, RemoveCommitment removeCommitment, DecisionContainerParameters decisionIntention, IntentionParameters intentionParameters, DesireKey sharedDesireKey, int limitOnNumberOfAgentsToCommit) {
        super(desireKey, memory, commitment, decisionDesire, removeCommitment, decisionIntention, intentionParameters, false);
        this.sharedDesireKey = sharedDesireKey;
        this.limitOnNumberOfAgentsToCommit = limitOnNumberOfAgentsToCommit;
    }

    @Override
    public IntentionWithDesireForOtherAgents formIntention(Agent agent) {
        return new IntentionWithDesireForOtherAgents(this, intentionParameters, agent, removeCommitment, decisionIntention, limitOnNumberOfAgentsToCommit, sharedDesireKey);
    }
}
