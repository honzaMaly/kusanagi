package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.Set;

/**
 * Template class for intention with desire for other agents - what this intention want them to achieve
 * Created by Jan on 21-Feb-17.
 */
public abstract class IntentionWithDesireForOtherAgents extends Intention<DesireForOthers> {

    @Getter
    private final SharedDesireForAgents sharedDesire;

    protected IntentionWithDesireForOtherAgents(DesireForOthers originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, int limitOnNumberOfAgentsToCommit, Agent agent, DesireKey sharedDesireKey) {
        super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
        this.sharedDesire = new SharedDesireForAgents(sharedDesireKey, agent, limitOnNumberOfAgentsToCommit);
    }

    protected IntentionWithDesireForOtherAgents(DesireForOthers originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent, DesireKey sharedDesireKey) {
        super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
        this.sharedDesire = new SharedDesireForAgents(sharedDesireKey, agent, Integer.MAX_VALUE);
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
