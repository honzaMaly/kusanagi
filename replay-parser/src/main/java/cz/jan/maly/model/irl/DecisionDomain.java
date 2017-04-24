package cz.jan.maly.model.irl;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.Domain;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import cz.jan.maly.model.decision.NextActionEnumerations;

/**
 * DecisionDomain generates domain describing decision
 * Created by Jan on 24-Apr-17.
 */
public class DecisionDomain implements DomainGenerator {
    static final String VAR_STATE = "state";

    @Override
    public Domain generateDomain() {
        SADomain domain = new SADomain();
        domain.addActionTypes(
                new DecisionAction(NextActionEnumerations.YES),
                new DecisionAction(NextActionEnumerations.NO));

        DecisionModel model = new DecisionModel();

        //unknown reward
        RewardFunction rf = (state, action, state1) -> -1;

        //no terminal state
        TerminalFunction tf = state -> false;

        domain.setModel(new FactoredModel(model, rf, tf));

        return domain;
    }
}
