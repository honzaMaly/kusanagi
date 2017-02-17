package cz.jan.maly.model.servicies.desires;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.DesireForOthers;

import java.util.Map;
import java.util.Set;

/**
 * Concrete implementation of DesireRegister. Instance of class is intended as read only as it is shared among agents.
 * Class contains method to get desires on system level / update desires it is committed to
 * Created by Jan on 17-Feb-17.
 */
public class ReadOnlyDesireRegister extends DesireRegister {

    ReadOnlyDesireRegister(Map<Agent, Set<DesireForOthers>> desiresForOthersByOriginator) {
        super(desiresForOthersByOriginator);
    }

    //todo methods - get / update desires agent is committed to

}
