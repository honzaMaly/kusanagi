package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.CommandFormulationStrategy;
import cz.jan.maly.model.planing.command.ReasoningCommand;

import java.util.Set;

/**
 * Template for agent's own desires
 * Created by Jan on 15-Feb-17.
 */
public abstract class OwnDesire<T extends Intention<? extends OwnDesire<?>>> extends InternalDesire<T> {
    OwnDesire(DesireKey desireKey, WorkingMemory memory, CommitmentDeciderInitializer commitmentDecider, CommitmentDeciderInitializer removeCommitment,
              boolean isAbstract) {
        super(desireKey, memory, commitmentDecider, removeCommitment, isAbstract);
    }

    OwnDesire(DesireKey desireKey, WorkingMemory memory, CommitmentDeciderInitializer commitmentDecider, CommitmentDeciderInitializer removeCommitment,
              boolean isAbstract, DesireParameters parentsDesireParameters) {
        super(desireKey, memory, commitmentDecider, removeCommitment, isAbstract, parentsDesireParameters);
    }

    /**
     * Desire to initialize abstract intention
     */
    public static class WithAbstractIntention extends OwnDesire<AbstractIntention<WithAbstractIntention>> {
        private final Set<DesireKey> desiresForOthers;
        private final Set<DesireKey> desiresWithAbstractIntention;
        private final Set<DesireKey> desiresWithIntentionToAct;
        private final Set<DesireKey> desiresWithIntentionToReason;

        public WithAbstractIntention(DesireKey desireKey, WorkingMemory memory, CommitmentDeciderInitializer commitmentDecider,
                                     CommitmentDeciderInitializer removeCommitment, Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention,
                                     Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason) {
            super(desireKey, memory, commitmentDecider, removeCommitment, true);
            this.desiresForOthers = desiresForOthers;
            this.desiresWithAbstractIntention = desiresWithAbstractIntention;
            this.desiresWithIntentionToAct = desiresWithIntentionToAct;
            this.desiresWithIntentionToReason = desiresWithIntentionToReason;
        }

        public WithAbstractIntention(DesireKey desireKey, WorkingMemory memory, CommitmentDeciderInitializer commitmentDecider,
                                     CommitmentDeciderInitializer removeCommitment, Set<DesireKey> desiresForOthers,
                                     Set<DesireKey> desiresWithAbstractIntention, Set<DesireKey> desiresWithIntentionToAct,
                                     Set<DesireKey> desiresWithIntentionToReason,
                                     DesireParameters parentsDesireParameters) {
            super(desireKey, memory, commitmentDecider, removeCommitment, true, parentsDesireParameters);
            this.desiresForOthers = desiresForOthers;
            this.desiresWithAbstractIntention = desiresWithAbstractIntention;
            this.desiresWithIntentionToAct = desiresWithIntentionToAct;
            this.desiresWithIntentionToReason = desiresWithIntentionToReason;
        }

        @Override
        public AbstractIntention<OwnDesire.WithAbstractIntention> formIntention(Agent agent) {
            return new AbstractIntention<>(this, removeCommitment, desiresForOthers,
                    desiresWithAbstractIntention, desiresWithIntentionToAct, desiresWithIntentionToReason);
        }
    }

    /**
     * Desire to initialize intention with reasoning command
     */
    public static class Reasoning extends OwnDesire<IntentionCommand.OwnReasoning> {
        private final CommandFormulationStrategy<ReasoningCommand, IntentionCommand.OwnReasoning> commandCreationStrategy;

        public Reasoning(DesireKey desireKey, WorkingMemory memory, CommitmentDeciderInitializer commitmentDecider,
                         CommitmentDeciderInitializer removeCommitment,
                         CommandFormulationStrategy<ReasoningCommand, IntentionCommand.OwnReasoning> commandCreationStrategy) {
            super(desireKey, memory, commitmentDecider, removeCommitment, false);
            this.commandCreationStrategy = commandCreationStrategy;
        }

        public Reasoning(DesireKey desireKey, WorkingMemory memory, CommitmentDeciderInitializer commitmentDecider,
                         CommitmentDeciderInitializer removeCommitment,
                         CommandFormulationStrategy<ReasoningCommand, IntentionCommand.OwnReasoning> commandCreationStrategy,
                         DesireParameters parentsDesireParameters) {
            super(desireKey, memory, commitmentDecider, removeCommitment, false, parentsDesireParameters);
            this.commandCreationStrategy = commandCreationStrategy;
        }

        @Override
        public IntentionCommand.OwnReasoning formIntention(Agent agent) {
            return new IntentionCommand.OwnReasoning(this, removeCommitment, commandCreationStrategy);
        }
    }

    /**
     * Desire to initialize intention with acting command
     */
    public static class Acting extends OwnDesire<IntentionCommand.OwnActing> {
        private final CommandFormulationStrategy<ActCommand.Own, IntentionCommand.OwnActing> commandCreationStrategy;

        public Acting(DesireKey desireKey, WorkingMemory memory, CommitmentDeciderInitializer commitmentDecider, CommitmentDeciderInitializer removeCommitment,
                      CommandFormulationStrategy<ActCommand.Own, IntentionCommand.OwnActing> commandCreationStrategy) {
            super(desireKey, memory, commitmentDecider, removeCommitment, false);
            this.commandCreationStrategy = commandCreationStrategy;
        }

        public Acting(DesireKey desireKey, WorkingMemory memory, CommitmentDeciderInitializer commitmentDecider, CommitmentDeciderInitializer removeCommitment,
                      CommandFormulationStrategy<ActCommand.Own, IntentionCommand.OwnActing> commandCreationStrategy,
                      DesireParameters parentsDesireParameters) {
            super(desireKey, memory, commitmentDecider, removeCommitment, false, parentsDesireParameters);
            this.commandCreationStrategy = commandCreationStrategy;
        }

        @Override
        public IntentionCommand.OwnActing formIntention(Agent agent) {
            return new IntentionCommand.OwnActing(this, removeCommitment, commandCreationStrategy);
        }
    }

}
