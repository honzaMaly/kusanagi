package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Template for intention which returns instance of Command
 * Created by Jan on 16-Feb-17.
 */
public abstract class IntentionWithPlan<V extends InternalDesire> extends Intention<V> {

    @Getter
    @Setter
    private boolean executed = false;

    IntentionWithPlan(V originalDesire, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent) {
        super(originalDesire, parametersTypesForFact, parametersTypesForFactSets, agent);
    }

    /**
     * Method returns command which will be executed by framework
     *
     * @return
     */
    public abstract Command getCommand();

}
