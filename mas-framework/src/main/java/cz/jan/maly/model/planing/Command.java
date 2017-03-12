package cz.jan.maly.model.planing;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.FactKey;

import java.util.Optional;
import java.util.Set;

/**
 * Template class for Command defines common data structure for some executable object by agent
 * Created by Jan on 15-Feb-17.
 */
public abstract class Command<T extends IntentionCommand<?, ? extends Command<T, K>, K>, K extends Memory<?>> {
    private final T intention;

    protected Command(T intention) {
        this.intention = intention;
    }

    /**
     * Method to be called by Command Executor to execute command
     *
     * @param memory
     * @return
     */
    public abstract boolean act(K memory);

    /**
     * Get content of fact for given key from intention
     *
     * @param factKey
     * @param <V>
     * @return
     */
    protected <V> Optional<V> returnFactValueForGivenKeyFromIntention(FactKey<V> factKey) {
        return intention.returnFactValueForGivenKey(factKey);
    }

    /**
     * Get set of content of fact for given key from intention
     *
     * @param factKey
     * @param <V>
     * @return
     */
    protected <V> Optional<Set<V>> returnFactSetValueForGivenKeyFromIntention(FactKey<V> factKey) {
        return intention.returnFactSetValueForGivenKey(factKey);
    }

    /**
     * Get content of fact for given key from desire
     *
     * @param factKey
     * @param <V>
     * @return
     */
    protected <V> Optional<V> returnFactValueForGivenKeyFromDesire(FactKey<V> factKey) {
        return intention.returnFactValueForGivenKeyForOriginalDesire(factKey);
    }

    /**
     * Get set of content of fact for given key from desire
     *
     * @param factKey
     * @param <V>
     * @return
     */
    protected <V> Optional<Set<V>> returnFactSetValueForGivenKeyFromDesire(FactKey<V> factKey) {
        return intention.returnFactSetValueForGivenKeyForOriginalDesire(factKey);
    }

}
