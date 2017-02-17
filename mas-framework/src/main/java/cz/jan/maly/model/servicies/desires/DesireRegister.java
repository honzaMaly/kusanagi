package cz.jan.maly.model.servicies.desires;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.servicies.Register;

import java.util.Map;
import java.util.Set;

/**
 * DesireRegister contains desires received from agents
 * Created by Jan on 17-Feb-17.
 */
abstract class DesireRegister extends Register<Set<DesireForOthers>> {

    DesireRegister(Map<Agent, Set<DesireForOthers>> desiresForOthersByOriginator) {
        super(desiresForOthersByOriginator);
    }
}
