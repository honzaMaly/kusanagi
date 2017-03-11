package cz.jan.maly.model.planing;

import cz.jan.maly.model.DesireKeyIdentificationInterface;
import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.DecisionContainerParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.IntentionParameters;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static cz.jan.maly.utils.FrameworkUtils.CLONER;

/**
 * Class describing template for intention. Intention instance represents metadata and high level abstraction of
 * what agent has committed to achieve to. It may contain other desires related to this intention to be consider.
 * Created by Jan on 10-Feb-17.
 */
public abstract class Intention<T extends InternalDesire> implements FactContainerInterface, RemoveCommitment, DesireKeyIdentificationInterface, DecisionAboutCommitment {
    private final Map<FactKey, Object> factParameterMap = new HashMap<>();
    private final Map<FactKey, Set<?>> factSetParameterMap = new HashMap<>();
    private final T originalDesire;
    private final RemoveCommitment removeCommitment;
    private final DecisionContainerParameters decisionParameters;

    Intention(T originalDesire, IntentionParameters intentionParameters, Memory memory, RemoveCommitment removeCommitment, DecisionContainerParameters decisionParameters) {
        this.originalDesire = originalDesire;
        this.removeCommitment = removeCommitment;
        this.decisionParameters = decisionParameters;

        //fill maps with actual parameters from internal_beliefs
        intentionParameters.getParametersTypesForFacts().forEach(factKey -> {
            Optional<?> value = memory.returnFactValueForGivenKey(factKey);
            value.ifPresent(o -> factParameterMap.put(factKey, CLONER.deepClone(o)));
        });
        intentionParameters.getParametersTypesForFactSets().forEach(factKey -> {
            Optional<Set<?>> value = memory.returnFactSetValueForGivenKey(factKey);
            value.ifPresent(set -> factSetParameterMap.put(factKey, CLONER.deepClone(set)));
        });
    }

    @Override
    public DecisionContainerParameters getParametersToLoad() {
        return decisionParameters;
    }

    /**
     * Should agent remove commitment to this intention?
     * @param dataForDecision
     * @return
     */
    public boolean shouldRemoveCommitment(DataForDecision dataForDecision) {
        return removeCommitment.shouldRemoveCommitment(dataForDecision);
    }

    @Override
    public DesireKey getDesireKey() {
        return originalDesire.getDesireKey();
    }

    public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
        Object value = factParameterMap.get(factKey);
        if (value != null) {
            return Optional.of((V) value);
        }
        return Optional.empty();
    }

    public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
        Set values = factSetParameterMap.get(factKey);
        if (values != null) {
            return Optional.of((S) values);
        }
        return Optional.empty();
    }

    /**
     * Return fact value from desire. This value is intended as read only!
     *
     * @param factKey
     * @param <V>
     * @return
     */
    public <V> Optional<V> returnFactValueForGivenKeyForOriginalDesire(FactKey<V> factKey) {
        return originalDesire.returnFactValueForGivenKey(factKey);
    }

    /**
     * Return fact value set from desire. This value is intended as read only!
     *
     * @param factKey
     * @param <V>
     * @param <S>
     * @return
     */
    public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKeyForOriginalDesire(FactKey<V> factKey) {
        return originalDesire.returnFactSetValueForGivenKey(factKey);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intention)) return false;

        Intention<?> intention = (Intention<?>) o;

        if (!factParameterMap.equals(intention.factParameterMap)) return false;
        if (!factSetParameterMap.equals(intention.factSetParameterMap)) return false;
        return originalDesire.equals(intention.originalDesire);
    }

    @Override
    public int hashCode() {
        int result = factParameterMap.hashCode();
        result = 31 * result + factSetParameterMap.hashCode();
        result = 31 * result + originalDesire.hashCode();
        return result;
    }
}
