package cz.jan.maly.model.irl;

import burlap.mdp.core.action.UniversalActionType;
import cz.jan.maly.model.decision.NextActionEnumerations;
import lombok.Getter;

/**
 * Extension of UniversalActionType to contain action associated with decision
 * Created by Jan on 24-Apr-17.
 */
@Getter
public class DecisionAction extends UniversalActionType {
    private final NextActionEnumerations action;

    public DecisionAction(NextActionEnumerations action) {
        super(action.name());
        this.action = action;
    }
}
