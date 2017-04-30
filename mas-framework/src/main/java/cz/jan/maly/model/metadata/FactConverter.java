package cz.jan.maly.model.metadata;

import cz.jan.maly.model.FeatureRawValueObtainingStrategy;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.containers.*;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Template for feature
 * Created by Jan on 14-Apr-17.
 */
public abstract class FactConverter<V, K> implements Converter {

    private final DataForDecision dataForDecision;
    private final FeatureRawValueObtainingStrategy<V> strategyToObtainValue;
    @Getter
    private double value = 0;
    private final int id;

    FactConverter(DataForDecision dataForDecision, FeatureRawValueObtainingStrategy<V> strategyToObtainValue, int id) {
        this.dataForDecision = dataForDecision;
        this.strategyToObtainValue = strategyToObtainValue;
        this.id = id;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FactConverter<?, ?> that = (FactConverter<?, ?>) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Returns true if local value is different from value updated by data from register
     *
     * @param register
     * @return
     */
    public abstract void hasUpdatedValueFromRegisterChanged(K register);

    /**
     * Return true if feature value has changed for new belief
     *
     * @param belief
     * @return
     */
    void hasValueChanged(V belief) {
        double oldValue = this.value;
        this.value = strategyToObtainValue.returnRawValue(belief);
        if (oldValue != this.value && !dataForDecision.isBeliefsChanged()) {
            dataForDecision.setBeliefsChanged(true);
        }
    }

    /**
     * For belief
     *
     * @param <V>
     */
    public static class BeliefFromKey<V> extends FactConverter<Optional<V>, DesireKey> {
        private final FactKey<V> factKey;

        public BeliefFromKey(DataForDecision dataForDecision, DesireKey desireKey, FactWithOptionalValue<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue(), container.getID());
            this.factKey = container.getFactKey();
            hasUpdatedValueFromRegisterChanged(desireKey);
        }

        @Override
        public void hasUpdatedValueFromRegisterChanged(DesireKey register) {
            hasValueChanged(register.returnFactValueForGivenKey(factKey));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            BeliefFromKey<?> that = (BeliefFromKey<?>) o;

            return factKey.equals(that.factKey);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + factKey.hashCode();
            return result;
        }
    }

    /**
     * For belief set
     *
     * @param <V>
     */
    public static class BeliefSetFromKey<V> extends FactConverter<Optional<Stream<V>>, DesireKey> {
        private final FactKey<V> factKey;

        public BeliefSetFromKey(DataForDecision dataForDecision, DesireKey desireKey, FactWithOptionalValueSet<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue(), container.getID());
            this.factKey = container.getFactKey();
            hasUpdatedValueFromRegisterChanged(desireKey);
        }

        @Override
        public void hasUpdatedValueFromRegisterChanged(DesireKey register) {
            hasValueChanged(register.returnFactSetValueForGivenKey(factKey));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BeliefFromKey<?> that = (BeliefFromKey<?>) o;

            return factKey.equals(that.factKey);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + factKey.hashCode();
            return result;
        }
    }

    /**
     * For belief
     *
     * @param <V>
     */
    public static class BeliefFromDesire<V> extends FactConverter<Optional<V>, DesireParameters> {
        private final FactKey<V> factKey;

        public BeliefFromDesire(DataForDecision dataForDecision, DesireParameters desireParameters,
                                FactWithOptionalValue<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue(), container.getID());
            this.factKey = container.getFactKey();
            hasUpdatedValueFromRegisterChanged(desireParameters);
        }

        @Override
        public void hasUpdatedValueFromRegisterChanged(DesireParameters register) {
            hasValueChanged(register.returnFactValueForGivenKey(factKey));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BeliefFromKey<?> that = (BeliefFromKey<?>) o;

            return factKey.equals(that.factKey);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + factKey.hashCode();
            return result;
        }
    }

    /**
     * For belief set
     *
     * @param <V>
     */
    public static class BeliefSetFromDesire<V> extends FactConverter<Optional<Stream<V>>, DesireParameters> {
        private final FactKey<V> factKey;

        public BeliefSetFromDesire(DataForDecision dataForDecision, DesireParameters desireParameters,
                                   FactWithOptionalValueSet<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue(), container.getID());
            this.factKey = container.getFactKey();
            hasUpdatedValueFromRegisterChanged(desireParameters);
        }

        @Override
        public void hasUpdatedValueFromRegisterChanged(DesireParameters register) {
            hasValueChanged(register.returnFactSetValueForGivenKey(factKey));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BeliefFromKey<?> that = (BeliefFromKey<?>) o;

            return factKey.equals(that.factKey);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + factKey.hashCode();
            return result;
        }
    }

    /**
     * For belief
     *
     * @param <V>
     */
    public static class Belief<V> extends FactConverter<Optional<V>, WorkingMemory> {
        private final FactKey<V> factKey;

        public Belief(DataForDecision dataForDecision, FactWithOptionalValue<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue(), container.getID());
            this.factKey = container.getFactKey();
        }

        @Override
        public void hasUpdatedValueFromRegisterChanged(WorkingMemory register) {
            hasValueChanged(register.returnFactValueForGivenKey(factKey));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BeliefFromKey<?> that = (BeliefFromKey<?>) o;

            return factKey.equals(that.factKey);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + factKey.hashCode();
            return result;
        }
    }

    /**
     * For global beliefs
     *
     * @param <V>
     */
    public static class GlobalBelief<V> extends FactConverter<Stream<Optional<V>>, WorkingMemory> {
        private final FactKey<V> factKey;

        public GlobalBelief(DataForDecision dataForDecision, FactWithSetOfOptionalValues<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue(), container.getID());
            this.factKey = container.getFactKey();
        }

        @Override
        public void hasUpdatedValueFromRegisterChanged(WorkingMemory register) {
            hasValueChanged(register.getReadOnlyMemories().filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(factKey))
                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(factKey))
            );
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BeliefFromKey<?> that = (BeliefFromKey<?>) o;

            return factKey.equals(that.factKey);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + factKey.hashCode();
            return result;
        }
    }

    /**
     * For global beliefs of agent type
     *
     * @param <V>
     */
    public static class GlobalBeliefForAgentType<V> extends FactConverter<Stream<Optional<V>>, WorkingMemory> {
        private final FactKey<V> factKey;
        private final AgentTypeID agentType;

        public GlobalBeliefForAgentType(DataForDecision dataForDecision, FactWithSetOfOptionalValuesForAgentType<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue(), container.getID());
            this.factKey = container.getFactKey();
            this.agentType = container.getAgentType();
        }

        @Override
        public void hasUpdatedValueFromRegisterChanged(WorkingMemory register) {
            hasValueChanged(register.getReadOnlyMemoriesForAgentType(agentType).filter(readOnlyMemory -> readOnlyMemory.isFactKeyForValueInMemory(factKey))
                    .map(readOnlyMemory -> readOnlyMemory.returnFactValueForGivenKey(factKey)));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GlobalBeliefSetForAgentType<?> that = (GlobalBeliefSetForAgentType<?>) o;

            if (!factKey.equals(that.factKey)) return false;
            return agentType.equals(that.agentType);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + factKey.hashCode();
            result = 31 * result + agentType.hashCode();
            return result;
        }
    }

    /**
     * For belief set
     *
     * @param <V>
     */
    public static class BeliefSet<V> extends FactConverter<Optional<Stream<V>>, WorkingMemory> {
        private final FactKey<V> factKey;

        public BeliefSet(DataForDecision dataForDecision, FactWithOptionalValueSet<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue(), container.getID());
            this.factKey = container.getFactKey();
        }

        @Override
        public void hasUpdatedValueFromRegisterChanged(WorkingMemory register) {
            hasValueChanged(register.returnFactSetValueForGivenKey(factKey));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            BeliefSet<?> beliefSet = (BeliefSet<?>) o;

            return factKey.equals(beliefSet.factKey);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + factKey.hashCode();
            return result;
        }
    }

    /**
     * For global belief sets
     *
     * @param <V>
     */
    public static class GlobalBeliefSet<V> extends FactConverter<Stream<Optional<Stream<V>>>, WorkingMemory> {
        private final FactKey<V> factKey;

        public GlobalBeliefSet(DataForDecision dataForDecision, FactWithOptionalValueSets<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue(), container.getID());
            this.factKey = container.getFactKey();
        }

        @Override
        public void hasUpdatedValueFromRegisterChanged(WorkingMemory register) {
            hasValueChanged(register.getReadOnlyMemories().filter(readOnlyMemory -> readOnlyMemory.isFactKeyForSetInMemory(factKey))
                    .map(readOnlyMemory -> readOnlyMemory.returnFactSetValueForGivenKey(factKey))
            );
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            GlobalBeliefSet<?> that = (GlobalBeliefSet<?>) o;

            return factKey.equals(that.factKey);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + factKey.hashCode();
            return result;
        }
    }

    /**
     * For global belief sets of agent type
     *
     * @param <V>
     */
    public static class GlobalBeliefSetForAgentType<V> extends FactConverter<Stream<Optional<Stream<V>>>, WorkingMemory> {
        private final FactKey<V> factKey;
        private final AgentTypeID agentType;

        public GlobalBeliefSetForAgentType(DataForDecision dataForDecision, FactWithOptionalValueSetsForAgentType<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue(), container.getID());
            this.factKey = container.getFactKey();
            this.agentType = container.getAgentType();
        }

        @Override
        public void hasUpdatedValueFromRegisterChanged(WorkingMemory register) {
            hasValueChanged(register.getReadOnlyMemoriesForAgentType(agentType).filter(readOnlyMemory -> readOnlyMemory.isFactKeyForSetInMemory(factKey))
                    .map(readOnlyMemory -> readOnlyMemory.returnFactSetValueForGivenKey(factKey)));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GlobalBeliefSetForAgentType<?> that = (GlobalBeliefSetForAgentType<?>) o;

            if (!factKey.equals(that.factKey)) return false;
            return agentType.equals(that.agentType);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + factKey.hashCode();
            result = 31 * result + agentType.hashCode();
            return result;
        }
    }

    /**
     * For belief
     */
    public static class BeliefFromKeyPresence extends FactConverter<Boolean, List<DesireKey>> {
        private final DesireKey desireKey;

        public BeliefFromKeyPresence(DataForDecision dataForDecision, DesireKey desireKey) {
            super(dataForDecision, aBoolean -> {
                if (aBoolean) {
                    return 1;
                }
                return 0;
            }, desireKey.getID());
            this.desireKey = desireKey;
        }

        @Override
        public void hasUpdatedValueFromRegisterChanged(List<DesireKey> list) {
            hasValueChanged(list.contains(desireKey));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            BeliefFromKeyPresence that = (BeliefFromKeyPresence) o;

            return desireKey.equals(that.desireKey);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + desireKey.hashCode();
            return result;
        }
    }

}
