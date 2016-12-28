package cz.jan.maly.model.agent.data;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.data.Fact;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.model.data.Request;
import cz.jan.maly.model.sflo.FormulaInterface;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class extend request and is used by agent to decide when to remove request as at least one condition to remove it is met
 * Created by Jan on 21-Dec-16.
 */
public class RequestWithCondition extends Request {
    private final List<FormulaInterface> conditionsToRemoveRequest;

    public RequestWithCondition(Map<KeyToFact, Fact> facts, Set<Agent> agents, boolean canCommitOneAgentOnly, Agent requestFrom, int id, List<FormulaInterface> conditionsToRemoveRequest) {
        super(facts, agents, canCommitOneAgentOnly, requestFrom, id);
        this.conditionsToRemoveRequest = conditionsToRemoveRequest;
    }

    public RequestWithCondition(Map<KeyToFact, Fact> facts, boolean canCommitOneAgentOnly, Agent requestFrom, int id, List<FormulaInterface> conditionsToRemoveRequest) {
        super(facts, canCommitOneAgentOnly, requestFrom, id);
        this.conditionsToRemoveRequest = conditionsToRemoveRequest;
    }

    /**
     * OR - if any of the formula evaluates as true, returns true
     * @return
     */
    public boolean canBeRequestRemoved(){
        return conditionsToRemoveRequest.stream()
                .anyMatch(FormulaInterface::evaluate);
    }
}
