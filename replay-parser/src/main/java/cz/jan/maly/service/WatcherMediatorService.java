package cz.jan.maly.service;

import cz.jan.maly.model.watcher.AgentWatcher;
import cz.jan.maly.model.watcher.FactConverter;
import cz.jan.maly.model.watcher.FactConverterByAgentType;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Contract for service to track watchers
 * Created by Jan on 18-Apr-17.
 */
public interface WatcherMediatorService {

    void addWatcher(AgentWatcher watcher);

    void removeWatcher(AgentWatcher watcher);

    void clearAllAgents();

    void watchAgents();

    /**
     * Convert fact to feature value
     *
     * @param convertingStrategy
     * @param <V>
     * @return
     */
    <V extends Stream<Optional<?>>> double getFeatureValueOfFact(FactConverter<V> convertingStrategy);

    /**
     * Convert fact set to feature value
     *
     * @param convertingStrategy
     * @param <V>
     * @return
     */
    <V extends Stream<Optional<Stream<?>>>> double getFeatureValueOfFactSet(FactConverter<V> convertingStrategy);

    /**
     * Convert fact to feature value
     *
     * @param convertingStrategy
     * @param <V>
     * @return
     */
    <V extends Stream<Optional<?>>> double getFeatureValueOfFact(FactConverterByAgentType<V> convertingStrategy);

    /**
     * Convert fact set to feature value
     *
     * @param convertingStrategy
     * @param <V>
     * @return
     */
    <V extends Stream<Optional<Stream<?>>>> double getFeatureValueOfFactSet(FactConverterByAgentType<V> convertingStrategy);

}
