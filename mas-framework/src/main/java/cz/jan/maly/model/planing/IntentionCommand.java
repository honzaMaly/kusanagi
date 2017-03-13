package cz.jan.maly.model.planing;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.IntentionParameters;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import lombok.Getter;

/**
 * Template for intention which returns instance of Command
 * Created by Jan on 16-Feb-17.
 */
public abstract class IntentionCommand<V extends InternalDesire<? extends IntentionCommand<?, ?>>, T extends Command<? extends IntentionCommand<V, T>, ?>> extends Intention<V> {
    private final T command;

    IntentionCommand(V originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, T command) {
        super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters);
        this.command = command;
    }

    /**
     * Method returns command which will be executed by framework
     *
     * @return
     */
    public T getCommand() {
        return command;
    }

    /**
     * From another agent's desire
     */
    public static class FromAnotherAgent extends IntentionCommand<DesireFromAnotherAgent.WithIntentionWithPlan, ActCommand.DesiredByAnotherAgent> {
        @Getter
        private final SharedDesireForAgents sharedDesireForAgents;

        FromAnotherAgent(DesireFromAnotherAgent.WithIntentionWithPlan originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, ActCommand.DesiredByAnotherAgent command) {
            super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters, command);
            this.sharedDesireForAgents = originalDesire.getDesireForAgents();
        }
    }


    /**
     * Own command for reasoning
     */
    public static class OwnReasoning extends IntentionCommand<OwnDesire.Reasoning, ReasoningCommand> {
        OwnReasoning(OwnDesire.Reasoning originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, ReasoningCommand command) {
            super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters, command);
        }
    }

    /**
     * Own command for acting
     */
    public static class OwnActing extends IntentionCommand<OwnDesire.Acting, ActCommand.Own> {
        OwnActing(OwnDesire.Acting originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, ActCommand.Own command) {
            super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters, command);
        }
    }


}
