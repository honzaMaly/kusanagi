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
public abstract class Feature<V, K> {

    private final DataForDecision dataForDecision;
    private final FeatureRawValueObtainingStrategy<V> strategyToObtainValue;
    @Getter
    private double value = 0;

    Feature(DataForDecision dataForDecision, FeatureRawValueObtainingStrategy<V> strategyToObtainValue) {
        this.dataForDecision = dataForDecision;
        this.strategyToObtainValue = strategyToObtainValue;
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
     */
    public static class BeliefFromKeyPresence extends Feature<Boolean, List<DesireKey>> {
        private final DesireKey desireKey;

        public BeliefFromKeyPresence(DataForDecision dataForDecision, DesireKey desireKey) {
            super(dataForDecision, aBoolean -> {
                if (aBoolean) {
                    return 1;
                }
                return 0;
            });
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

            BeliefFromKeyPresence that = (BeliefFromKeyPresence) o;

            return desireKey.equals(that.desireKey);
        }

        @Override
        public int hashCode() {
            return desireKey.hashCode();
        }
    }

    /**
     * For belief
     *
     * @param <V>
     */
    public static class BeliefFromKey<V> extends Feature<Optional<V>, DesireKey> {
        private final FactKey<V> factKey;

        public BeliefFromKey(DataForDecision dataForDecision, DesireKey desireKey, FactWithOptionalValue<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue());
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

            BeliefFromKey<?> that = (BeliefFromKey<?>) o;

            return factKey.equals(that.factKey);
        }

        @Override
        public int hashCode() {
            return factKey.hashCode();
        }
    }

    /**
     * For belief set
     *
     * @param <V>
     */
    public static class BeliefSetFromKey<V> extends Feature<Optional<Stream<V>>, DesireKey> {
        private final FactKey<V> factKey;

        public BeliefSetFromKey(DataForDecision dataForDecision, DesireKey desireKey, FactWithOptionalValueSet<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue());
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
            return factKey.hashCode();
        }
    }

    /**
     * For belief
     *
     * @param <V>
     */
    public static class BeliefFromDesire<V> extends Feature<Optional<V>, DesireParameters> {
        private final FactKey<V> factKey;

        public BeliefFromDesire(DataForDecision dataForDecision, DesireParameters desireParameters,
                                FactWithOptionalValue<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue());
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
            return factKey.hashCode();
        }
    }

    /**
     * For belief set
     *
     * @param <V>
     */
    public static class BeliefSetFromDesire<V> extends Feature<Optional<Stream<V>>, DesireParameters> {
        private final FactKey<V> factKey;

        public BeliefSetFromDesire(DataForDecision dataForDecision, DesireParameters desireParameters,
                                   FactWithOptionalValueSet<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue());
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
            return factKey.hashCode();
        }
    }

    /**
     * For belief
     *
     * @param <V>
     */
    public static class Belief<V> extends Feature<Optional<V>, WorkingMemory> {
        private final FactKey<V> factKey;

        public Belief(DataForDecision dataForDecision, FactWithOptionalValue<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue());
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
            return factKey.hashCode();
        }
    }

    /**
     * For global beliefs
     *
     * @param <V>
     */
    public static class GlobalBelief<V> extends Feature<Stream<Optional<V>>, WorkingMemory> {
        private final FactKey<V> factKey;

        public GlobalBelief(DataForDecision dataForDecision, FactWithSetOfOptionalValues<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue());
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
            return factKey.hashCode();
        }
    }

    /**
     * For global beliefs of agent type
     *
     * @param <V>
     */
    public static class GlobalBeliefForAgentType<V> extends Feature<Stream<Optional<V>>, WorkingMemory> {
        private final FactKey<V> factKey;
        private final AgentType agentType;

        public GlobalBeliefForAgentType(DataForDecision dataForDecision, FactWithSetOfOptionalValuesForAgentType<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue());
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
            int result = factKey.hashCode();
            result = 31 * result + agentType.hashCode();
            return result;
        }
    }

    /**
     * For belief set
     *
     * @param <V>
     */
    public static class BeliefSet<V> extends Feature<Optional<Stream<V>>, WorkingMemory> {
        private final FactKey<V> factKey;

        public BeliefSet(DataForDecision dataForDecision, FactWithOptionalValueSet<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue());
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

            BeliefFromKey<?> that = (BeliefFromKey<?>) o;

            return factKey.equals(that.factKey);
        }

        @Override
        public int hashCode() {
            return factKey.hashCode();
        }
    }

    /**
     * For global belief sets
     *
     * @param <V>
     */
    public static class GlobalBeliefSet<V> extends Feature<Stream<Optional<Stream<V>>>, WorkingMemory> {
        private final FactKey<V> factKey;

        public GlobalBeliefSet(DataForDecision dataForDecision, FactWithOptionalValueSets<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue());
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

            BeliefFromKey<?> that = (BeliefFromKey<?>) o;

            return factKey.equals(that.factKey);
        }

        @Override
        public int hashCode() {
            return factKey.hashCode();
        }
    }

    /**
     * For global belief sets of agent type
     *
     * @param <V>
     */
    public static class GlobalBeliefSetForAgentType<V> extends Feature<Stream<Optional<Stream<V>>>, WorkingMemory> {
        private final FactKey<V> factKey;
        private final AgentType agentType;

        public GlobalBeliefSetForAgentType(DataForDecision dataForDecision, FactWithOptionalValueSetsForAgentType<V> container) {
            super(dataForDecision, container.getStrategyToObtainValue());
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
            int result = factKey.hashCode();
            result = 31 * result + agentType.hashCode();
            return result;
        }
    }

}
