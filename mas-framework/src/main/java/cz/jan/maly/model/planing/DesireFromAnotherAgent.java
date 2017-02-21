package cz.jan.maly.model.planing;

import cz.jan.maly.model.metadata.DesireKey;

/**
 * Template for agent's desires transformation. Desire originated from another agent is transformed
 * Created by Jan on 16-Feb-17.
 */
public abstract class DesireFromAnotherAgent extends Desire {

    //TODO object to track commitment from mediator. Container with data to create desire instead of DesireForOthers

    DesireFromAnotherAgent(DesireKey desireKey, DesireForOthers desireOriginatedFrom, boolean isAbstract) {
        super(desireOriginatedFrom.factParameterMap, desireOriginatedFrom.factSetParameterMap, isAbstract, desireKey);
    }
}
