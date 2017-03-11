package cz.jan.maly.model.planing;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.IntentionParameters;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;

/**
 * Template for intention which returns instance of Command
 * Created by Jan on 16-Feb-17.
 */
public abstract class IntentionWithPlan<V extends InternalDesire<?>, T extends Command<? extends IntentionWithPlan<V, T, K>, K>, K extends Memory<?>> extends Intention<V> {
    private final T command;

    IntentionWithPlan(V originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, T command) {
        super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters);
        this.command = command;
    }

    /**
     * Method returns command which will be executed by framework
     *
     * @param memory
     * @return
     */
    public boolean act(K memory) {
        return command.act(memory);
    }

    /**
     * From another agent's desire
     */
    public static class FromAnotherAgent extends IntentionWithPlan<DesireFromAnotherAgent.WithIntentionWithPlan, ActCommand.DesiredByAnotherAgent, Memory<?>> {
        public FromAnotherAgent(DesireFromAnotherAgent.WithIntentionWithPlan originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, ActCommand.DesiredByAnotherAgent command) {
            super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters, command);
        }
    }

    /**
     * From own desire
     */
    public abstract static class Own<T extends Command<? extends IntentionWithPlan<OwnDesire.WithIntentionWithPlan, T, K>, K>, K extends Memory<?>> extends IntentionWithPlan<OwnDesire.WithIntentionWithPlan, T, K> {
        private Own(OwnDesire.WithIntentionWithPlan originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, T command) {
            super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters, command);
        }

        /**
         * Own command for reasoning
         */
        public static class Reasoning extends Own<ReasoningCommand, WorkingMemory> {
            public Reasoning(OwnDesire.WithIntentionWithPlan originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, ReasoningCommand command) {
                super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters, command);
            }
        }

        /**
         * Own command for acting
         */
        public static class Acting extends Own<ActCommand.Own, Memory<?>> {
            public Acting(OwnDesire.WithIntentionWithPlan originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, ActCommand.Own command) {
                super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters, command);
            }
        }

    }

}
