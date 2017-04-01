package cz.jan.maly.model.planing;

import cz.jan.maly.model.CommandForIntentionFormulationStrategy;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.IntentionParameters;

import java.util.Set;

/**
 * Template for agent's own desires
 * Created by Jan on 15-Feb-17.
 */
public abstract class OwnDesire<T extends Intention<? extends OwnDesire<T>>> extends InternalDesire<T> {
    OwnDesire(DesireKey desireKey, Memory memory, Commitment commitment, DecisionParameters decisionDesire, RemoveCommitment removeCommitment, DecisionParameters decisionIntention, IntentionParameters intentionParameters, boolean isAbstract) {
        super(desireKey, memory, commitment, decisionDesire, removeCommitment, decisionIntention, intentionParameters, isAbstract);
    }

    /**
     * Desire to initialize abstract intention
     */
    public static class WithAbstractIntention extends OwnDesire<AbstractIntention<WithAbstractIntention>> {
        private final Set<DesireKey> desiresForOthers;
        private final Set<DesireKey> desiresWithAbstractIntention;
        private final Set<DesireKey> desiresWithIntentionToAct;
        private final Set<DesireKey> desiresWithIntentionToReason;

        public WithAbstractIntention(DesireKey desireKey, Memory memory, Commitment commitment, DecisionParameters decisionDesire, RemoveCommitment removeCommitment, DecisionParameters decisionIntention, IntentionParameters intentionParameters, Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention, Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason) {
            super(desireKey, memory, commitment, decisionDesire, removeCommitment, decisionIntention, intentionParameters, true);
            this.desiresForOthers = desiresForOthers;
            this.desiresWithAbstractIntention = desiresWithAbstractIntention;
            this.desiresWithIntentionToAct = desiresWithIntentionToAct;
            this.desiresWithIntentionToReason = desiresWithIntentionToReason;
        }

        @Override
        public AbstractIntention<OwnDesire.WithAbstractIntention> formIntention(Agent agent) {
            return new AbstractIntention<>(this, intentionParameters, agent.getBeliefs(), removeCommitment, decisionIntention, desiresForOthers, desiresWithAbstractIntention, desiresWithIntentionToAct, desiresWithIntentionToReason);
        }
    }

    /**
     * Desire to initialize intention with reasoning command
     */
    public static class Reasoning extends OwnDesire<IntentionCommand.OwnReasoning> {
        private final CommandForIntentionFormulationStrategy.OwnReasoning commandCreationStrategy;

        public Reasoning(DesireKey desireKey, Memory memory, Commitment commitment, DecisionParameters decisionDesire, RemoveCommitment removeCommitment, DecisionParameters decisionIntention, IntentionParameters intentionParameters, CommandForIntentionFormulationStrategy.OwnReasoning commandCreationStrategy) {
            super(desireKey, memory, commitment, decisionDesire, removeCommitment, decisionIntention, intentionParameters, false);
            this.commandCreationStrategy = commandCreationStrategy;
        }

        @Override
        public IntentionCommand.OwnReasoning formIntention(Agent agent) {
            return new IntentionCommand.OwnReasoning(this, intentionParameters, agent.getBeliefs(), removeCommitment, decisionIntention, commandCreationStrategy);
        }
    }

    /**
     * Desire to initialize intention with acting command
     */
    public static class Acting extends OwnDesire<IntentionCommand.OwnActing> {
        private final CommandForIntentionFormulationStrategy.OwnActing commandCreationStrategy;

        public Acting(DesireKey desireKey, Memory memory, Commitment commitment, DecisionParameters decisionDesire, RemoveCommitment removeCommitment, DecisionParameters decisionIntention, IntentionParameters intentionParameters, CommandForIntentionFormulationStrategy.OwnActing commandCreationStrategy) {
            super(desireKey, memory, commitment, decisionDesire, removeCommitment, decisionIntention, intentionParameters, false);
            this.commandCreationStrategy = commandCreationStrategy;
        }

        @Override
        public IntentionCommand.OwnActing formIntention(Agent agent) {
            return new IntentionCommand.OwnActing(this, intentionParameters, agent.getBeliefs(), removeCommitment, decisionIntention, commandCreationStrategy);
        }
    }

}
