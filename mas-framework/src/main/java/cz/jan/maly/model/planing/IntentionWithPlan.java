package cz.jan.maly.model.planing;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DecisionContainerParameters;
import cz.jan.maly.model.metadata.IntentionParameters;

/**
 * Template for intention which returns instance of Command
 * Created by Jan on 16-Feb-17.
 */
public class IntentionWithPlan<V extends InternalDesire> extends Intention<V> {
    private final Command command;

    IntentionWithPlan(V originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionContainerParameters decisionParameters, Command command) {
        super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters);
        this.command = command;
    }

    /**
     * Method returns command which will be executed by framework
     *
     * @return
     */
    public Command getCommand() {
        return command;
    }

}
