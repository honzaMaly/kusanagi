package cz.jan.maly.model.agent.data;

import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.metadata.Fact;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.sflo.FormulaInterface;

import java.util.List;
import java.util.Map;

/**
 * Class adds to RequestMadeByAgent possibility to decide if request should be removed based on formulas - at least one condition to remove it is met
 * Created by Jan on 21-Dec-16.
 */
public class RequestWithCondition extends RequestMadeByAgent {
    private final List<FormulaInterface> conditionsToRemoveRequest;

    public RequestWithCondition(Map<KeyToFact, Fact> facts, boolean canCommitOneAgentOnly, Agent requestFrom, int id, List<FormulaInterface> conditionsToRemoveRequest) {
        super(facts, canCommitOneAgentOnly, requestFrom, id);
        this.conditionsToRemoveRequest = conditionsToRemoveRequest;
    }

    /**
     * OR - if any of the formula evaluates as true, returns true, or request removal is decided by user explicitly
     *
     * @return
     */
    @Override
    public boolean canBeRequestRemoved() {
        return canBeRemoved || conditionsToRemoveRequest.stream()
                .anyMatch(FormulaInterface::evaluate);
    }
}
