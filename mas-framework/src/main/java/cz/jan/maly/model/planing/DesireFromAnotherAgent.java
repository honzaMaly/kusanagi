package cz.jan.maly.model.planing;

/**
 * Template for agent's desires transformation. Desire originated from another agent is transformed
 * Created by Jan on 16-Feb-17.
 */
public abstract class DesireFromAnotherAgent extends InternalDesire {
    DesireFromAnotherAgent(SharedDesireForAgents desireOriginatedFrom, boolean isAbstract) {
        super(desireOriginatedFrom.desireParameters, isAbstract);
    }
}
