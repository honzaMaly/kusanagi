package cz.jan.maly.model.planing;

import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.ReadOnlyMemory;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Class extending Desire describes template for internal desires agents may want to commit to. Concrete implementation
 * of this are used in planning tree.
 * Created by Jan on 22-Feb-17.
 */
public abstract class InternalDesire<T extends Intention<? extends InternalDesire<?>>> extends Desire
        implements FactContainerInterface, OnChangeActor, OnDestructionActor {
    final CommitmentDeciderInitializer removeCommitment;
    @Getter
    final boolean isAbstract;
    final WorkingMemory memory;
    private final CommitmentDecider commitmentDecider;
    private final Optional<DesireParameters> parentsDesireParameters;
    @Getter
    private final Optional<ReactionOnChangeStrategy> reactionOnChangeStrategy;

    InternalDesire(DesireKey desireKey, WorkingMemory memory, CommitmentDeciderInitializer commitmentDecider,
                   CommitmentDeciderInitializer removeCommitment, boolean isAbstract, ReactionOnChangeStrategy reactionOnChangeStrategy) {
        super(desireKey, memory);
        this.commitmentDecider = commitmentDecider.initializeCommitmentDecider(desireParameters);
        this.memory = memory;
        this.removeCommitment = removeCommitment;
        this.isAbstract = isAbstract;
        this.parentsDesireParameters = Optional.empty();
        this.reactionOnChangeStrategy = Optional.ofNullable(reactionOnChangeStrategy);
    }

    InternalDesire(DesireKey desireKey, WorkingMemory memory, CommitmentDeciderInitializer commitmentDecider,
                   CommitmentDeciderInitializer removeCommitment, boolean isAbstract,
                   DesireParameters parentsDesireParameters, ReactionOnChangeStrategy reactionOnChangeStrategy) {
        super(desireKey, memory);
        this.commitmentDecider = commitmentDecider.initializeCommitmentDecider(desireParameters);
        this.memory = memory;
        this.removeCommitment = removeCommitment;
        this.isAbstract = isAbstract;
        this.parentsDesireParameters = Optional.of(parentsDesireParameters);
        this.reactionOnChangeStrategy = Optional.ofNullable(reactionOnChangeStrategy);
    }

    InternalDesire(DesireParameters desireParameters, WorkingMemory memory, CommitmentDeciderInitializer commitmentDecider,
                   CommitmentDeciderInitializer removeCommitment, boolean isAbstract, int originatorId,
                   ReactionOnChangeStrategy reactionOnChangeStrategy) {
        super(desireParameters, originatorId);
        this.memory = memory;
        this.commitmentDecider = commitmentDecider.initializeCommitmentDecider(desireParameters);
        this.removeCommitment = removeCommitment;
        this.isAbstract = isAbstract;
        this.parentsDesireParameters = Optional.empty();
        this.reactionOnChangeStrategy = Optional.ofNullable(reactionOnChangeStrategy);
    }

    @Override
    public void actOnRemoval() {
        actOnChange(memory, desireParameters);
    }

    public boolean shouldCommit(List<DesireKey> madeCommitmentToTypes, List<DesireKey> didNotMakeCommitmentToTypes,
                                List<DesireKey> typesAboutToMakeDecision) {

        //TODO - HACK! does not change return value and enables reaction
        return commitmentDecider.shouldCommit(madeCommitmentToTypes, didNotMakeCommitmentToTypes, typesAboutToMakeDecision,
                memory) && actOnChange(memory, desireParameters);
    }

    public boolean shouldCommit(List<DesireKey> madeCommitmentToTypes, List<DesireKey> didNotMakeCommitmentToTypes,
                                List<DesireKey> typesAboutToMakeDecision, int numberOfCommittedAgents) {

        //TODO - HACK! does not change return value and enables reaction
        return commitmentDecider.shouldCommit(madeCommitmentToTypes, didNotMakeCommitmentToTypes, typesAboutToMakeDecision,
                memory, numberOfCommittedAgents) && actOnChange(memory, desireParameters);
    }

    public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
        return memory.returnFactValueForGivenKey(factKey);
    }

    public <V, S extends Stream<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
        return memory.returnFactSetValueForGivenKey(factKey);
    }

    public Optional<ReadOnlyMemory> getReadOnlyMemoryForAgent(int agentId) {
        return memory.getReadOnlyMemoryForAgent(agentId);
    }

    public Stream<ReadOnlyMemory> getReadOnlyMemoriesForAgentType(AgentType agentType) {
        return memory.getReadOnlyMemoriesForAgentType(agentType.getAgentTypeID());
    }

    public Stream<ReadOnlyMemory> getReadOnlyMemories() {
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
    public <V, S extends Stream<V>> Optional<S> returnFactSetValueOfParentIntentionForGivenKey(FactKey<V> factKey) {
        if (parentsDesireParameters.isPresent()) {
            return parentsDesireParameters.get().returnFactSetValueForGivenKey(factKey);
        }
        MyLogger.getLogger().warning("There are no parameters from parent intention present.");
        return Optional.empty();
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
