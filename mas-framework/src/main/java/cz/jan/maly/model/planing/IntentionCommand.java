package cz.jan.maly.model.planing;

import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.CommandForIntention;
import cz.jan.maly.model.planing.command.CommandFormulationStrategy;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import lombok.Getter;

/**
 * Template for intention which returns instance of CommandForIntention
 * Created by Jan on 16-Feb-17.
 */
public abstract class IntentionCommand<V extends InternalDesire<? extends IntentionCommand<?, ?>>, T extends CommandForIntention<? extends IntentionCommand<V, T>>> extends Intention<V> {
    IntentionCommand(V originalDesire, RemoveCommitment removeCommitment) {
        super(originalDesire, removeCommitment);
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
    public static class FromAnotherAgent extends IntentionCommand<DesireFromAnotherAgent.WithIntentionWithPlan, ActCommand.DesiredByAnotherAgent> {
        @Getter
        private final SharedDesireForAgents sharedDesireForAgents;
        private final ActCommand.DesiredByAnotherAgent command;

        FromAnotherAgent(DesireFromAnotherAgent.WithIntentionWithPlan originalDesire, RemoveCommitment removeCommitment,
                         CommandFormulationStrategy<ActCommand.DesiredByAnotherAgent, FromAnotherAgent> commandCreationStrategy) {
            super(originalDesire, removeCommitment);
            this.sharedDesireForAgents = originalDesire.getDesireForAgents();
            this.command = commandCreationStrategy.formCommand(this);
        }

        @Override
        public ActCommand.DesiredByAnotherAgent getCommand() {
            return command;
        }
    }


    /**
     * Own command for reasoning
     */
    public static class OwnReasoning extends IntentionCommand<OwnDesire.Reasoning, ReasoningCommand> {
        private final ReasoningCommand command;

        OwnReasoning(OwnDesire.Reasoning originalDesire, RemoveCommitment removeCommitment,
                     CommandFormulationStrategy<ReasoningCommand, OwnReasoning> commandCreationStrategy) {
            super(originalDesire, removeCommitment);
            this.command = commandCreationStrategy.formCommand(this);
        }

        @Override
        public ReasoningCommand getCommand() {
            return command;
        }
    }

    /**
     * Own command for acting
     */
    public static class OwnActing extends IntentionCommand<OwnDesire.Acting, ActCommand.Own> {
        private final ActCommand.Own command;

        OwnActing(OwnDesire.Acting originalDesire, RemoveCommitment removeCommitment,
                  CommandFormulationStrategy<ActCommand.Own, OwnActing> commandCreationStrategy) {
            super(originalDesire, removeCommitment);
            this.command = commandCreationStrategy.formCommand(this);
        }


        @Override
        public ActCommand.Own getCommand() {
            return command;
        }
    }


}
