package cz.jan.maly.service;

import cz.jan.maly.model.metadata.containers.FactWithOptionalValueSets;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValueSetsForAgentType;
import cz.jan.maly.model.metadata.containers.FactWithSetOfOptionalValues;
import cz.jan.maly.model.metadata.containers.FactWithSetOfOptionalValuesForAgentType;
import cz.jan.maly.model.watcher.AgentWatcher;

import java.util.stream.Stream;

/**
 * Contract for service to track watchers
 * Created by Jan on 18-Apr-17.
 */
public interface WatcherMediatorService {

    /**
     * Add new watcher (for example new unit was created)
     *
     * @param watcher
     */
    void addWatcher(AgentWatcher<?> watcher);

    /**
     * Remove watcher (for example unit was killed)
     *
     * @param watcher
     */
    void removeWatcher(AgentWatcher<?> watcher);

    /**
     * Get stream of watchers
     *
     * @return
     */
    Stream<AgentWatcher<?>> getStreamOfWatchers();

    /**
     * Remove all agents from register and save theirs trajectories
     */
    void clearAllAgentsAndSaveTheirTrajectories();


    /**
     * Method to tell all agents to observe environment and system (this is called each frame)
     */
    void tellAgentsToObserveSystemAndHandlePlans();

    /**
     * Convert fact to feature value
     *
     * @param convertingStrategy
     * @param <V>
     * @return
     */
    <V> double getFeatureValueOfFact(FactWithSetOfOptionalValues<V> convertingStrategy);

    /**
     * Convert fact set to feature value
     *
     * @param convertingStrategy
     * @param <V>
     * @return
     */
    <V> double getFeatureValueOfFactSet(FactWithOptionalValueSets<V> convertingStrategy);

    /**
     * Convert fact to feature value
     *
     * @param convertingStrategy
     * @param <V>
     * @return
     */
    <V> double getFeatureValueOfFact(FactWithSetOfOptionalValuesForAgentType<V> convertingStrategy);

    /**
     * Convert fact set to feature value
     *
     * @param convertingStrategy
     * @param <V>
     * @return
     */
    <V> double getFeatureValueOfFactSet(FactWithOptionalValueSetsForAgentType<V> convertingStrategy);

}
