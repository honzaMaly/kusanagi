package cz.jan.maly.model.planing;

import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.Optional;
import java.util.Set;

/**
 * Class extending Desire describes template for internal desires agents may want to commit to. Concrete implementation
 * of this are used in planning tree.
 * Created by Jan on 22-Feb-17.
 */
public abstract class InternalDesire<T extends Intention<? extends InternalDesire<?>>> extends Desire implements DecisionAboutCommitment, FactContainerInterface {
    final Commitment commitment;
    private final WorkingMemory memory;
    final RemoveCommitment removeCommitment;
    private final Set<DesireKey> typesOfDesiresToConsiderWhenCommitting;
    final Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment;

    @Getter
    final boolean isAbstract;

    InternalDesire(DesireKey desireKey, WorkingMemory memory, Commitment commitment, RemoveCommitment removeCommitment,
                   Set<DesireKey> typesOfDesiresToConsiderWhenCommitting, Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment,
                   boolean isAbstract) {
        super(desireKey, memory);
        this.commitment = commitment;
        this.memory = memory;
        this.removeCommitment = removeCommitment;
        this.isAbstract = isAbstract;
        this.typesOfDesiresToConsiderWhenCommitting = typesOfDesiresToConsiderWhenCommitting;
        this.typesOfDesiresToConsiderWhenRemovingCommitment = typesOfDesiresToConsiderWhenRemovingCommitment;
    }

    InternalDesire(DesireParameters desireParameters, WorkingMemory memory, Commitment commitment, RemoveCommitment removeCommitment,
                   Set<DesireKey> typesOfDesiresToConsiderWhenCommitting, Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment,
                   boolean isAbstract, int originatorId) {
        super(desireParameters, originatorId);
        this.memory = memory;
        this.commitment = commitment;
        this.removeCommitment = removeCommitment;
        this.isAbstract = isAbstract;
        this.typesOfDesiresToConsiderWhenCommitting = typesOfDesiresToConsiderWhenCommitting;
        this.typesOfDesiresToConsiderWhenRemovingCommitment = typesOfDesiresToConsiderWhenRemovingCommitment;
    }

    @Override
    public Set<DesireKey> getParametersToLoad() {
        return typesOfDesiresToConsiderWhenCommitting;
    }

    public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
        return memory.returnFactValueForGivenKey(factKey);
    }

    public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
        return memory.returnFactSetValueForGivenKey(factKey);
    }

    /**
     * Decides commitment - should agent commit?
     *
     * @param dataForDecision
     * @return
     */
    public boolean shouldCommit(DataForDecision dataForDecision) {
        return commitment.shouldCommit(this, dataForDecision);
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
