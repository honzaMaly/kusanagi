package cz.jan.maly.model.servicies.desires;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.SharedDesire;
import cz.jan.maly.model.planing.SharedDesireInRegister;
import cz.jan.maly.model.servicies.Register;

import java.util.Map;

/**
 * DesireRegister contains desires received from agents
 * Created by Jan on 17-Feb-17.
 */
abstract class DesireRegister extends Register<Map<SharedDesire, SharedDesireInRegister>> {

    DesireRegister(Map<Agent, Map<SharedDesire, SharedDesireInRegister>> desiresForOthersByOriginator) {
        super(desiresForOthersByOriginator);
    }
}
