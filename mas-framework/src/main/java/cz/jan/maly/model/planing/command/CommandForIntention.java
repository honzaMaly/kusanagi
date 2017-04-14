package cz.jan.maly.model.planing.command;

import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.planing.IntentionCommand;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Template class for CommandForIntention defines common data structure for some executable object by agent
 * Created by Jan on 15-Feb-17.
 */
public abstract class CommandForIntention<T extends IntentionCommand<?, ? extends CommandForIntention<T>>> implements CommandInterface, FactContainerInterface {
    private final T intention;

    protected CommandForIntention(T intention) {
        this.intention = intention;
    }

    @Override
    public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
        return intention.returnFactValueForGivenKey(factKey);
    }

    @Override
    public <V, S extends Stream<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
        return intention.returnFactSetValueForGivenKey(factKey);
    }

    /**
     * Get content of fact for given key from desire
     *
     * @param factKey
     * @param <V>
     * @return
     */
    protected <V> Optional<V> returnFactValueForGivenKeyInDesireParameters(FactKey<V> factKey) {
        return intention.returnFactValueForGivenKeyInDesireParameters(factKey);
    }

    /**
     * Get set of content of fact for given key from desire
     *
     * @param factKey
     * @param <V>
     * @return
     */
    protected <V> Optional<Stream<V>> returnFactSetValueForGivenKeyInDesireParameters(FactKey<V> factKey) {
        return intention.returnFactSetValueForGivenKeyInDesireParameters(factKey);
    }

}
