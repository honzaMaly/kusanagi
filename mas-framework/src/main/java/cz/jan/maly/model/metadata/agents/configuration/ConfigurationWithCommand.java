package cz.jan.maly.model.metadata.agents.configuration;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.Commitment;
import cz.jan.maly.model.planing.IntentionCommand;
import cz.jan.maly.model.planing.RemoveCommitment;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.CommandForIntention;
import cz.jan.maly.model.planing.command.CommandFormulationStrategy;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Template for configuration container with strategy to create command
 * Created by Jan on 03-Apr-17.
 */
@Getter
public class ConfigurationWithCommand<K extends CommandFormulationStrategy<? extends CommandForIntention<?>, ? extends IntentionCommand<?, ?>>> extends CommonConfiguration {
    private K commandCreationStrategy;

    ConfigurationWithCommand(Commitment decisionInDesire, RemoveCommitment decisionInIntention, Set<DesireKey> typesOfDesiresToConsiderWhenCommitting, Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment, K commandCreationStrategy) {
        super(decisionInDesire, decisionInIntention, typesOfDesiresToConsiderWhenCommitting, typesOfDesiresToConsiderWhenRemovingCommitment);
        this.commandCreationStrategy = commandCreationStrategy;
    }

    //For acting command desired by another agent
    public static class WithActingCommandDesiredByOtherAgent extends ConfigurationWithCommand<CommandFormulationStrategy<ActCommand.DesiredByAnotherAgent, IntentionCommand.FromAnotherAgent>> {

        @Builder
        private WithActingCommandDesiredByOtherAgent(Commitment decisionInDesire, RemoveCommitment decisionInIntention, Set<DesireKey> typesOfDesiresToConsiderWhenCommitting, Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment, CommandFormulationStrategy<ActCommand.DesiredByAnotherAgent, IntentionCommand.FromAnotherAgent> commandCreationStrategy) {
            super(decisionInDesire, decisionInIntention, typesOfDesiresToConsiderWhenCommitting, typesOfDesiresToConsiderWhenRemovingCommitment, commandCreationStrategy);
        }

        //builder with default fields
        public static class WithActingCommandDesiredByOtherAgentBuilder extends CommonConfiguration.CommonConfigurationBuilder {
            private Set<DesireKey> typesOfDesiresToConsiderWhenCommitting = new HashSet<>();
            private Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment = new HashSet<>();
        }
    }

    //For acting command desired by itself
    public static class WithActingCommandDesiredBySelf extends ConfigurationWithCommand<CommandFormulationStrategy<ActCommand.Own, IntentionCommand.OwnActing>> {

        @Builder
        private WithActingCommandDesiredBySelf(Commitment decisionInDesire, RemoveCommitment decisionInIntention, Set<DesireKey> typesOfDesiresToConsiderWhenCommitting, Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment, CommandFormulationStrategy<ActCommand.Own, IntentionCommand.OwnActing> commandCreationStrategy) {
            super(decisionInDesire, decisionInIntention, typesOfDesiresToConsiderWhenCommitting, typesOfDesiresToConsiderWhenRemovingCommitment, commandCreationStrategy);
        }

        //builder with default fields
        public static class WithActingCommandDesiredBySelfBuilder extends CommonConfiguration.CommonConfigurationBuilder {
            private Set<DesireKey> typesOfDesiresToConsiderWhenCommitting = new HashSet<>();
            private Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment = new HashSet<>();
        }
    }

    //For reasoning command desired by itself
    public static class WithReasoningCommandDesiredBySelf extends ConfigurationWithCommand<CommandFormulationStrategy<ReasoningCommand, IntentionCommand.OwnReasoning>> {

        @Builder
        private WithReasoningCommandDesiredBySelf(Commitment decisionInDesire, RemoveCommitment decisionInIntention, Set<DesireKey> typesOfDesiresToConsiderWhenCommitting, Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment, CommandFormulationStrategy<ReasoningCommand, IntentionCommand.OwnReasoning> commandCreationStrategy) {
            super(decisionInDesire, decisionInIntention, typesOfDesiresToConsiderWhenCommitting, typesOfDesiresToConsiderWhenRemovingCommitment, commandCreationStrategy);
        }

        //builder with default fields
        public static class WithReasoningCommandDesiredBySelfBuilder extends CommonConfiguration.CommonConfigurationBuilder {
            private Set<DesireKey> typesOfDesiresToConsiderWhenCommitting = new HashSet<>();
            private Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment = new HashSet<>();
        }
    }

}
