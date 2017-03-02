package cz.jan.maly.model.planing;

import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.QueuedItemInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.CommandManagerKey;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Template class Command defines method to be called by system to execute some action
 * by agent (not exclusively) on system behalf. It works with current internal_beliefs.
 * Created by Jan on 15-Feb-17.
 */
public abstract class Command<K extends CommandManagerKey> implements FactContainerInterface {
    private final Map<FactKey, Object> factParameterMap = new HashMap<>();
    private final Map<FactKey, Set> factSetParameterMap = new HashMap<>();

    @Getter
    private final K commandManagerKey;

    private final Intention intention;

    protected Command(Intention intention, Set<FactKey<?>> parametersTypesForFact, Set<FactKey<?>> parametersTypesForFactSets, Agent agent, K commandManagerKey) {
        this.commandManagerKey = commandManagerKey;

        //fill maps with actual parameters from internal_beliefs
        parametersTypesForFact.forEach(factKey -> {
            Optional<?> value = agent.getBeliefs().returnFactValueForGivenKey(factKey);
            value.ifPresent(o -> factParameterMap.put(factKey, o));
        });
        parametersTypesForFactSets.forEach(factKey -> {
            Optional<Set> value = agent.getBeliefs().returnFactSetValueForGivenKey(factKey);
            value.ifPresent(o -> factSetParameterMap.put(factKey, o));
        });

        this.intention = intention;
    }

    /**
     * Method called by CommitmentDecider to get action to make on agent's behalf
     */
    public abstract QueuedItemInterface<Boolean> execute();

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

    /**
     * Get content of fact for given key
     *
     * @param factKey
     * @param <V>
     * @return
     */
    public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
        Object value = factParameterMap.get(factKey);
        if (value != null) {
            return Optional.of((V) value);
        }
        return Optional.empty();
    }

    /**
     * Get set of content of fact for given key
     *
     * @param factKey
     * @param <V>
     * @return
     */
    public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
        Set values = factSetParameterMap.get(factKey);
        if (values != null) {
            return Optional.of((S) values);
        }
        return Optional.empty();
    }

}
