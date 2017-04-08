package cz.jan.maly.model.planing;

import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.knowledge.ReadOnlyMemory;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.utils.MyLogger;
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
    final RemoveCommitment removeCommitment;
    final Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment;
    @Getter
    final boolean isAbstract;
    private final WorkingMemory memory;
    private final Set<DesireKey> typesOfDesiresToConsiderWhenCommitting;
    private final Optional<DesireParameters> parentsDesireParameters;

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
        this.parentsDesireParameters = Optional.empty();
    }

    InternalDesire(DesireKey desireKey, WorkingMemory memory, Commitment commitment, RemoveCommitment removeCommitment,
                   Set<DesireKey> typesOfDesiresToConsiderWhenCommitting, Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment,
                   boolean isAbstract, DesireParameters parentsDesireParameters) {
        super(desireKey, memory);
        this.commitment = commitment;
        this.memory = memory;
        this.removeCommitment = removeCommitment;
        this.isAbstract = isAbstract;
        this.typesOfDesiresToConsiderWhenCommitting = typesOfDesiresToConsiderWhenCommitting;
        this.typesOfDesiresToConsiderWhenRemovingCommitment = typesOfDesiresToConsiderWhenRemovingCommitment;
        this.parentsDesireParameters = Optional.of(parentsDesireParameters);
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
        this.parentsDesireParameters = Optional.empty();
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

    public Optional<ReadOnlyMemory> getReadOnlyMemoryForAgent(int agentId) {
        return memory.getReadOnlyMemoryForAgent(agentId);
    }

    public Set<ReadOnlyMemory> getReadOnlyMemoriesForAgentType(AgentType agentType) {
        return memory.getReadOnlyMemoriesForAgentType(agentType);
    }

    public Set<ReadOnlyMemory> getReadOnlyMemories() {
        return memory.getReadOnlyMemories();
    }

    public boolean isFactKeyForValueInMemory(FactKey<?> factKey) {
        return memory.isFactKeyForValueInMemory(factKey);
    }

    public boolean isFactKeyForSetInMemory(FactKey<?> factKey) {
        return memory.isFactKeyForSetInMemory(factKey);
    }

    public int getAgentId() {
        return memory.getAgentId();
    }

    /**
     * Returns fact value for desire parameters of parenting intention
     *
     * @param factKey
     * @param <V>
     * @return
     */
    public <V> Optional<V> returnFactValueOfParentIntentionForGivenKey(FactKey<V> factKey) {
        if (parentsDesireParameters.isPresent()) {
            return parentsDesireParameters.get().returnFactValueForGivenKey(factKey);
        }
        MyLogger.getLogger().warning("There are no parameters from parent intention present.");
        return Optional.empty();
    }

    /**
     * Returns fact value set for desire parameters of parenting intention
     *
     * @param factKey
     * @param <V>
     * @param <S>
     * @return
     */
    public <V, S extends Set<V>> Optional<S> returnFactSetValueOfParentIntentionForGivenKey(FactKey<V> factKey) {
        if (parentsDesireParameters.isPresent()) {
            return parentsDesireParameters.get().returnFactSetValueForGivenKey(factKey);
        }
        MyLogger.getLogger().warning("There are no parameters from parent intention present.");
        return Optional.empty();
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
