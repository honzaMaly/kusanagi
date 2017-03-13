package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.IntentionParameters;
import lombok.Getter;

/**
 * Intention with desire for other agents - what this intention want them to achieve
 * Created by Jan on 21-Feb-17.
 */
public class IntentionWithDesireForOtherAgents extends Intention<DesireForOthers> {

    @Getter
    private final SharedDesireForAgents sharedDesire;

    IntentionWithDesireForOtherAgents(DesireForOthers originalDesire, IntentionParameters intentionParameters, Agent agent, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, int limitOnNumberOfAgentsToCommit, DesireKey sharedDesireKey) {
        super(originalDesire, intentionParameters, agent.getBeliefs(), removeCommitment, decisionParameters);
        this.sharedDesire = new SharedDesireForAgents(sharedDesireKey, agent, limitOnNumberOfAgentsToCommit);
    }

    /**
     * Returns clone of desire as instance of desire to share
     *
     * @return
     */
    public SharedDesireInRegister makeDesireToShare() {
        return new SharedDesireInRegister(sharedDesire.desireParameters, sharedDesire.originatedFromAgent, sharedDesire.limitOnNumberOfAgentsToCommit);
    }

}
