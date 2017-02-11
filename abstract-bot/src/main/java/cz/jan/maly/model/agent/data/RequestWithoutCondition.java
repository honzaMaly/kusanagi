package cz.jan.maly.model.agent.data;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.data.knowledge_representation.Fact;
import cz.jan.maly.model.data.KeyToFact;

import java.util.Map;

/**
 * Class RequestWithoutCondition returns remove request only if user explicitly say so
 * Created by Jan on 12-Jan-17.
 */
public class RequestWithoutCondition extends RequestMadeByAgent {

    public RequestWithoutCondition(Map<KeyToFact, Fact> facts, boolean canCommitOneAgentOnly, Agent requestFrom, int id) {
        super(facts, canCommitOneAgentOnly, requestFrom, id);
    }

    @Override
    public boolean canBeRequestRemoved() {
        return canBeRemoved;
    }
}
