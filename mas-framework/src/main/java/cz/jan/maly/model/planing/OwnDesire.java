package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Map;
import java.util.Set;

/**
 * Template for agent's own desires
 * Created by Jan on 15-Feb-17.
 */
public class OwnDesire extends Desire<OwnDesire> {

    public OwnDesire(DesireKey desireKey, Agent agent) {
        super(desireKey, agent);
    }

    private OwnDesire(Map<FactKey, Object> factParameterMap, Map<FactKey, Set> factSetParameterMap, DesireKey desireKey) {
        super(factParameterMap, factSetParameterMap, desireKey);
    }

    @Override
    public OwnDesire copyDesire() {
        return new OwnDesire(factParameterMap, factSetParameterMap, desireKey);
    }

}
