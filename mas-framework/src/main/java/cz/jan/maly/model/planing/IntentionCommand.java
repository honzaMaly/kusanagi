package cz.jan.maly.model.planing;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.IntentionParameters;
import cz.jan.maly.model.planing.command.ActCommandForIntention;
import cz.jan.maly.model.planing.command.ReasoningCommandForIntention;
import lombok.Getter;

/**
 * Template for intention which returns instance of CommandForIntention
 * Created by Jan on 16-Feb-17.
 */
public abstract class IntentionCommand<V extends InternalDesire<? extends IntentionCommand<?, ?>>, T extends CommandForIntention<? extends IntentionCommand<V, T>, ?>> extends Intention<V> {
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
    public static class FromAnotherAgent extends IntentionCommand<DesireFromAnotherAgent.WithIntentionWithPlan, ActCommandForIntention.DesiredByAnotherAgent> {
        @Getter
        private final SharedDesireForAgents sharedDesireForAgents;

        FromAnotherAgent(DesireFromAnotherAgent.WithIntentionWithPlan originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, ActCommandForIntention.DesiredByAnotherAgent command) {
            super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters, command);
            this.sharedDesireForAgents = originalDesire.getDesireForAgents();
        }
    }


    /**
     * Own command for reasoning
     */
    public static class OwnReasoning extends IntentionCommand<OwnDesire.Reasoning, ReasoningCommandForIntention> {
        OwnReasoning(OwnDesire.Reasoning originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, ReasoningCommandForIntention command) {
            super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters, command);
        }
    }

    /**
     * Own command for acting
     */
    public static class OwnActing extends IntentionCommand<OwnDesire.Acting, ActCommandForIntention.Own> {
        OwnActing(OwnDesire.Acting originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, ActCommandForIntention.Own command) {
            super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters, command);
        }
    }


}
