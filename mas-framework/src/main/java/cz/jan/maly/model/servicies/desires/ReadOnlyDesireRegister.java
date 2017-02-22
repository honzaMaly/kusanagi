package cz.jan.maly.model.servicies.desires;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.SharedDesire;
import cz.jan.maly.model.planing.SharedDesireForAgents;
import cz.jan.maly.model.planing.SharedDesireInRegister;

import java.util.Map;

/**
 * Concrete implementation of DesireRegister. Instance of class is intended as read only as it is shared among agents.
 * Class contains method to get desires on system level / update desires it is committed to
 * Created by Jan on 17-Feb-17.
 */
public class ReadOnlyDesireRegister extends DesireRegister {

    ReadOnlyDesireRegister(Map<Agent, Map<SharedDesire, SharedDesireInRegister>> desiresForOthersByOriginator) {
        super(desiresForOthersByOriginator);
    }

    /**
     * Method update set of committed agents registered in provided sharedDesireForAgents with current one
     *
     * @param sharedDesireForAgents
     */
    public void updateCommitmentToDesires(SharedDesireForAgents sharedDesireForAgents) {
        if (desiresForOthersByOriginator.containsKey(sharedDesireForAgents.getOriginatedFromAgent())) {
            SharedDesireInRegister desire = desiresForOthersByOriginator.get(sharedDesireForAgents.getOriginatedFromAgent()).getOrDefault(sharedDesireForAgents, null);
            if (desire != null) {
                sharedDesireForAgents.updateCommittedAgentsSet(desire.getCommittedAgents());
            }
        }
    }

}
