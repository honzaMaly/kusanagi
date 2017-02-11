package cz.jan.maly.model.agent.data;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.data.knowledge_representation.Fact;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.model.data.Request;
import lombok.Setter;

import java.util.Map;

/**
 * This class extend request and is used by agent to decide when to remove request and to keep relevant information to request (committed agents,...)
 * Created by Jan on 12-Jan-17.
 */
@Setter
public abstract class RequestMadeByAgent extends Request {
    protected boolean canBeRemoved = false;

    public RequestMadeByAgent(Map<KeyToFact, Fact> facts, boolean canCommitOneAgentOnly, Agent requestFrom, int id) {
        super(facts, canCommitOneAgentOnly, requestFrom, id);
    }

    /**
     * Update local view of commitments of other agents to this request
     * @param request
     */
    public void updateCommittedAgentsBasedOnRequestInCopyOfRegister(Request request){
        if (!request.equals(this)){
            throw new IllegalArgumentException("Request can be updated with same request only");
        }
        if (canCommitOneAgentOnly && request.numberOfCommittedAgents()>1){
            throw new IllegalArgumentException("There is more then one agent committed to request with single commitment");
        }
        committedAgents.clear();
        committedAgents.addAll(request.committedAgents());
    }

    /**
     * Can be request removed
     * @return
     */
    public abstract boolean canBeRequestRemoved();

}
