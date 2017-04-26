package cz.jan.maly.model.irl;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import cz.jan.maly.model.decision.NextActionEnumerations;

/**
 * DecisionDomainGenerator generates domain describing decision
 * Created by Jan on 24-Apr-17.
 */
public class DecisionDomainGenerator implements DomainGenerator {
    static final String VAR_STATE = "state";
    public static final int defaultReward = 0;
    private final DecisionModel model;

    public DecisionDomainGenerator(DecisionModel model) {
        this.model = model;
    }

    @Override
    public SADomain generateDomain() {
        SADomain domain = new SADomain();
        domain.addActionTypes(
                new UniversalActionType(NextActionEnumerations.YES.name()),
                new UniversalActionType(NextActionEnumerations.NO.name()));

        //unknown reward
        RewardFunction rf = (state, action, state1) -> defaultReward;

        //no terminal state
        TerminalFunction tf = state -> false;

        domain.setModel(new FactoredModel(model, rf, tf));

        return domain;
    }
}
