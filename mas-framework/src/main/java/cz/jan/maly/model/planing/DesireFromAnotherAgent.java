package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.CommandFormulationStrategy;
import lombok.Getter;

import java.util.Set;

/**
 * Template for agent's desires transformation. Desire originated from another agent is transformed
 * Created by Jan on 16-Feb-17.
 */
public abstract class DesireFromAnotherAgent<T extends Intention<? extends DesireFromAnotherAgent<?>>> extends InternalDesire<T> {

    @Getter
    private final SharedDesireForAgents desireForAgents;
    final ReactionOnChangeStrategy reactionOnChangeStrategyInIntention;

    DesireFromAnotherAgent(SharedDesireForAgents desireOriginatedFrom, WorkingMemory memory, CommitmentDeciderInitializer commitmentDecider,
                           CommitmentDeciderInitializer removeCommitment, boolean isAbstract, ReactionOnChangeStrategy reactionOnChangeStrategy,
                           ReactionOnChangeStrategy reactionOnChangeStrategyInIntention) {
        super(desireOriginatedFrom.desireParameters, memory, commitmentDecider, removeCommitment, isAbstract,
                desireOriginatedFrom.originatorId, reactionOnChangeStrategy);
        this.desireForAgents = desireOriginatedFrom;
        this.reactionOnChangeStrategyInIntention = reactionOnChangeStrategyInIntention;
    }

    public int countOfCommittedAgents() {
        return desireForAgents.countOfCommittedAgents();
    }

    /**
     * Desire to initialize abstract intention
     */
    public static class WithAbstractIntention extends DesireFromAnotherAgent<AbstractIntention<WithAbstractIntention>> {
        private final Set<DesireKey> desiresForOthers;
        private final Set<DesireKey> desiresWithAbstractIntention;
        private final Set<DesireKey> desiresWithIntentionToAct;
        private final Set<DesireKey> desiresWithIntentionToReason;

        public WithAbstractIntention(SharedDesireForAgents desireOriginatedFrom, WorkingMemory memory,
                                     CommitmentDeciderInitializer commitmentDecider,
                                     CommitmentDeciderInitializer removeCommitment, Set<DesireKey> desiresForOthers,
                                     Set<DesireKey> desiresWithAbstractIntention, Set<DesireKey> desiresWithIntentionToAct,
                                     Set<DesireKey> desiresWithIntentionToReason,
                                     ReactionOnChangeStrategy reactionOnChangeStrategy,
                                     ReactionOnChangeStrategy reactionOnChangeStrategyInIntention) {
            super(desireOriginatedFrom, memory, commitmentDecider, removeCommitment, true, reactionOnChangeStrategy,
                    reactionOnChangeStrategyInIntention);
            this.desiresForOthers = desiresForOthers;
            this.desiresWithAbstractIntention = desiresWithAbstractIntention;
            this.desiresWithIntentionToAct = desiresWithIntentionToAct;
            this.desiresWithIntentionToReason = desiresWithIntentionToReason;
        }

        @Override
        public AbstractIntention<DesireFromAnotherAgent.WithAbstractIntention> formIntention(Agent agent) {
            return new AbstractIntention<>(this, removeCommitment, desiresForOthers, desiresWithAbstractIntention,
                    desiresWithIntentionToAct, desiresWithIntentionToReason, reactionOnChangeStrategyInIntention);
        }
    }

    /**
     * Desire to initialize intention with plan
     */
    public static class WithIntentionWithPlan extends DesireFromAnotherAgent<IntentionCommand.FromAnotherAgent> {
        private final CommandFormulationStrategy<ActCommand.DesiredByAnotherAgent, IntentionCommand.FromAnotherAgent> commandCreationStrategy;

        public WithIntentionWithPlan(SharedDesireForAgents desireOriginatedFrom, WorkingMemory memory,
                                     CommitmentDeciderInitializer commitmentDecider, CommitmentDeciderInitializer removeCommitment,
                                     CommandFormulationStrategy<ActCommand.DesiredByAnotherAgent, IntentionCommand.FromAnotherAgent> commandCreationStrategy,
                                     ReactionOnChangeStrategy reactionOnChangeStrategy,
                                     ReactionOnChangeStrategy reactionOnChangeStrategyInIntention) {
            super(desireOriginatedFrom, memory, commitmentDecider, removeCommitment, false, reactionOnChangeStrategy,
                    reactionOnChangeStrategyInIntention);
            this.commandCreationStrategy = commandCreationStrategy;
        }

        @Override
        public IntentionCommand.FromAnotherAgent formIntention(Agent agent) {
            return new IntentionCommand.FromAnotherAgent(this, removeCommitment, commandCreationStrategy,
                    reactionOnChangeStrategyInIntention);
        }
    }

}
