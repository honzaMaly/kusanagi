package cz.jan.maly.model.planing;

import cz.jan.maly.model.CommandForIntentionFormulationStrategy;
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
    IntentionCommand(V originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters) {
        super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters);
    }

    /**
     * Method returns command which will be executed by framework
     *
     * @return
     */
    public abstract T getCommand();

    /**
     * From another agent's desire
     */
    public static class FromAnotherAgent extends IntentionCommand<DesireFromAnotherAgent.WithIntentionWithPlan, ActCommandForIntention.DesiredByAnotherAgent> {
        @Getter
        private final SharedDesireForAgents sharedDesireForAgents;
        private final ActCommandForIntention.DesiredByAnotherAgent command;

        FromAnotherAgent(DesireFromAnotherAgent.WithIntentionWithPlan originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, CommandForIntentionFormulationStrategy.AnotherAgentsDesireActing commandCreationStrategy) {
            super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters);
            this.sharedDesireForAgents = originalDesire.getDesireForAgents();
            this.command = commandCreationStrategy.formCommand(this);
        }

        @Override
        public ActCommandForIntention.DesiredByAnotherAgent getCommand() {
            return command;
        }
    }


    /**
     * Own command for reasoning
     */
    public static class OwnReasoning extends IntentionCommand<OwnDesire.Reasoning, ReasoningCommandForIntention> {
        private final ReasoningCommandForIntention command;

        OwnReasoning(OwnDesire.Reasoning originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, CommandForIntentionFormulationStrategy.OwnReasoning commandCreationStrategy) {
            super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters);
            this.command = commandCreationStrategy.formCommand(this);
        }

        @Override
        public ReasoningCommandForIntention getCommand() {
            return command;
        }
    }

    /**
     * Own command for acting
     */
    public static class OwnActing extends IntentionCommand<OwnDesire.Acting, ActCommandForIntention.Own> {
        private final ActCommandForIntention.Own command;

        OwnActing(OwnDesire.Acting originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionParameters decisionParameters, CommandForIntentionFormulationStrategy.OwnActing commandCreationStrategy) {
            super(originalDesire, intentionParameters, memory, removeCommitment, decisionParameters);
            this.command = commandCreationStrategy.formCommand(this);
        }


        @Override
        public ActCommandForIntention.Own getCommand() {
            return command;
        }
    }


}
