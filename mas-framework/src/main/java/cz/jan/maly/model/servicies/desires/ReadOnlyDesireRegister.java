package cz.jan.maly.model.servicies.desires;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.SharedDesire;
import cz.jan.maly.model.planing.SharedDesireInRegister;

import java.util.Map;

/**
 * Concrete implementation of DesireRegister. Instance of class is intended as read only as it is shared among agents.
 * Class contains method to get desires on system level
 * Created by Jan on 17-Feb-17.
 */
public class ReadOnlyDesireRegister extends DesireRegister {

    ReadOnlyDesireRegister(Map<Agent, Map<SharedDesire, SharedDesireInRegister>> dataByOriginator) {
        super(dataByOriginator);
    }

    //todo filters to get desires

}
