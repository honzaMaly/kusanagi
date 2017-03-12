package cz.jan.maly.model.planing;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DecisionParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.metadata.IntentionParameters;
import lombok.Getter;

/**
 * Class extending Desire describes template for internal desires agents may want to commit to. Concrete implementation
 * of this are used in planning tree.
 * Created by Jan on 22-Feb-17.
 */
public abstract class InternalDesire<T extends Intention<? extends InternalDesire<?>>> extends Desire implements DecisionAboutCommitment {
    final Commitment commitment;
    private final DecisionParameters decisionDesire;
    final RemoveCommitment removeCommitment;
    final DecisionParameters decisionIntention;
    final IntentionParameters intentionParameters;

    @Getter
    final boolean isAbstract;

    InternalDesire(DesireKey desireKey, Memory memory, Commitment commitment, DecisionParameters decisionDesire, RemoveCommitment removeCommitment, DecisionParameters decisionIntention, IntentionParameters intentionParameters, boolean isAbstract) {
        super(desireKey, memory);
        this.commitment = commitment;
        this.decisionDesire = decisionDesire;
        this.removeCommitment = removeCommitment;
        this.decisionIntention = decisionIntention;
        this.intentionParameters = intentionParameters;
        this.isAbstract = isAbstract;
    }

    InternalDesire(DesireParameters desireParameters, Commitment commitment, DecisionParameters decisionDesire, RemoveCommitment removeCommitment, DecisionParameters decisionIntention, IntentionParameters intentionParameters, boolean isAbstract) {
        super(desireParameters);
        this.commitment = commitment;
        this.decisionDesire = decisionDesire;
        this.removeCommitment = removeCommitment;
        this.decisionIntention = decisionIntention;
        this.intentionParameters = intentionParameters;
        this.isAbstract = isAbstract;
    }

    @Override
    public DecisionParameters getParametersToLoad() {
        return decisionDesire;
    }

    /**
     * Decides commitment - should agent commit?
     *
     * @param dataForDecision
     * @return
     */
    public boolean shouldCommit(DataForDecision dataForDecision) {
        return commitment.shouldCommit(dataForDecision);
    }

    /**
     * Return intention induced by this desire for given agent
     *
     * @param agent
     * @return
     */
    public abstract T formIntention(Agent agent);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InternalDesire)) return false;
        if (!super.equals(o)) return false;

        InternalDesire that = (InternalDesire) o;

        return isAbstract == that.isAbstract;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (isAbstract ? 1 : 0);
        return result;
    }
}
