package cz.jan.maly.model;

import cz.jan.maly.model.metadata.ActCommandParameters;
import cz.jan.maly.model.planing.CommandForIntention;
import cz.jan.maly.model.planing.IntentionCommand;
import cz.jan.maly.model.planing.command.ActCommandForIntention;
import cz.jan.maly.model.planing.command.ReasoningCommandForIntention;

/**
 * Contract for acting command creation strategy
 * Created by Jan on 01-Apr-17.
 */
public abstract class CommandForIntentionFormulationStrategy<V extends CommandForIntention<?, ?>, T extends IntentionCommand<?, ?>> {

    /**
     * Form command from intention
     *
     * @param intention
     * @return
     */
    public abstract V formCommand(T intention);

    /**
     * Template for strategy which consider parameters
     *
     * @param <V>
     * @param <T>
     */
    static abstract class CommandForIntentionFormulationStrategyWithParameters<V extends CommandForIntention<?, ?>, T extends IntentionCommand<?, ?>> extends CommandForIntentionFormulationStrategy<V, T> {
        final ActCommandParameters commandParameters;

        private CommandForIntentionFormulationStrategyWithParameters(ActCommandParameters commandParameters) {
            this.commandParameters = commandParameters;
        }

        @Override
        public V formCommand(T intention) {
            return formCommand(intention, commandParameters);
        }

        protected abstract V formCommand(T intention, ActCommandParameters commandParameters);
    }

    /**
     * Formulate own acting
     */
    public abstract static class OwnActing extends CommandForIntentionFormulationStrategyWithParameters<ActCommandForIntention.Own, IntentionCommand.OwnActing> {
        public OwnActing(ActCommandParameters commandParameters) {
            super(commandParameters);
        }
    }

    /**
     * Formulate own acting
     */
    public abstract static class OwnReasoning extends CommandForIntentionFormulationStrategy<ReasoningCommandForIntention, IntentionCommand.OwnReasoning> {
    }

    /**
     * Formulate acting desired by another agent
     */
    public abstract static class AnotherAgentsDesireActing extends CommandForIntentionFormulationStrategyWithParameters<ActCommandForIntention.DesiredByAnotherAgent, IntentionCommand.FromAnotherAgent> {
        public AnotherAgentsDesireActing(ActCommandParameters commandParameters) {
            super(commandParameters);
        }
    }
}
