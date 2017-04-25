package cz.jan.maly.service;

import cz.jan.maly.model.metadata.containers.FactWithOptionalValueSets;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValueSetsForAgentType;
import cz.jan.maly.model.metadata.containers.FactWithSetOfOptionalValues;
import cz.jan.maly.model.metadata.containers.FactWithSetOfOptionalValuesForAgentType;
import cz.jan.maly.model.watcher.AgentWatcher;

/**
 * Contract for service to track watchers
 * Created by Jan on 18-Apr-17.
 */
public interface WatcherMediatorService {

    void addWatcher(AgentWatcher watcher);

    void removeWatcher(AgentWatcher watcher);

    /**
     * Remove all agents from register and save theirs trajectories
     */
    void clearAllAgentsAndSaveTheirTrajectories();

    void watchAgents();

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
