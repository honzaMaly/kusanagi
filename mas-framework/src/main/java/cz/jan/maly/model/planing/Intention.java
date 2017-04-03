package cz.jan.maly.model.planing;

import cz.jan.maly.model.DesireKeyIdentificationInterface;
import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.knowledge.DataForDecision;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Optional;
import java.util.Set;

/**
 * Class describing template for intention. Intention instance represents metadata and high level abstraction of
 * what agent has committed to achieve to. It may contain other desires related to this intention to be consider.
 * Created by Jan on 10-Feb-17.
 */
public abstract class Intention<T extends InternalDesire<?>> implements FactContainerInterface, DesireKeyIdentificationInterface, DecisionAboutCommitment {
    private final T originalDesire;
    private final RemoveCommitment removeCommitment;

    Intention(T originalDesire, RemoveCommitment removeCommitment) {
        this.originalDesire = originalDesire;
        this.removeCommitment = removeCommitment;
    }

    @Override
    public Set<DesireKey> getParametersToLoad() {
        return originalDesire.typesOfDesiresToConsiderWhenRemovingCommitment;
    }

    /**
     * Should agent remove commitment to this intention?
     *
     * @param dataForDecision
     * @return
     */
    public boolean shouldRemoveCommitment(DataForDecision dataForDecision) {
        return removeCommitment.shouldRemoveCommitment(this, dataForDecision);
    }

    @Override
    public DesireKey getDesireKey() {
        return originalDesire.getDesireKey();
    }

    public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
        return originalDesire.returnFactValueForGivenKey(factKey);
    }

    public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
        return originalDesire.returnFactSetValueForGivenKey(factKey);
    }

    public <V> Optional<V> returnFactValueForGivenKeyInDesireParameters(FactKey<V> factKey) {
        return originalDesire.returnFactValueForGivenKeyInParameters(factKey);
    }

    public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKeyInDesireParameters(FactKey<V> factKey) {
        return originalDesire.returnFactSetValueForGivenKeyInParameters(factKey);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intention)) return false;

        Intention<?> intention = (Intention<?>) o;
        return originalDesire.equals(intention.originalDesire);
    }

    @Override
    public int hashCode() {
        return originalDesire.hashCode();
    }
}
