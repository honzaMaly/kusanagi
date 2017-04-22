package cz.jan.maly.service.implementation;

import cz.jan.maly.model.metadata.containers.FactWithOptionalValueSets;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValueSetsForAgentType;
import cz.jan.maly.model.metadata.containers.FactWithSetOfOptionalValues;
import cz.jan.maly.model.metadata.containers.FactWithSetOfOptionalValuesForAgentType;
import cz.jan.maly.model.watcher.AgentWatcher;
import cz.jan.maly.service.WatcherMediatorService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Implementation of WatcherMediatorService
 * Created by Jan on 18-Apr-17.
 */
public class WatcherMediatorServiceImpl implements WatcherMediatorService {
    private final Set<AgentWatcher<?>> watchers = new HashSet<>();

    @Override
    public void addWatcher(AgentWatcher watcher) {
        watchers.add(watcher);
    }

    @Override
    public void removeWatcher(AgentWatcher watcher) {
        watchers.remove(watcher);
    }

    @Override
    public void clearAllAgents() {
        watchers.clear();
    }

    @Override
    public void watchAgents() {

        //TODO
        watchers.forEach(agentWatcher -> agentWatcher.handleTrajectoriesOfPlans(this));

    }

    @Override
    public <V> double getFeatureValueOfFact(FactWithSetOfOptionalValues<V> convertingStrategy) {
        Stream<Optional<V>> stream = watchers.stream()
                .map(AgentWatcher::getBeliefs)
                .filter(beliefs -> beliefs.isFactKeyForValueInMemory(convertingStrategy.getFactKey()))
                .map(beliefs -> beliefs.returnFactValueForGivenKey(convertingStrategy.getFactKey()));
        return convertingStrategy.getStrategyToObtainValue().returnRawValue(stream);
    }

    @Override
    public <V> double getFeatureValueOfFactSet(FactWithOptionalValueSets<V> convertingStrategy) {
        Stream<Optional<Stream<V>>> stream = watchers.stream()
                .map(AgentWatcher::getBeliefs)
                .filter(beliefs -> beliefs.isFactKeyForSetInMemory(convertingStrategy.getFactKey()))
                .map(beliefs -> beliefs.returnFactSetValueForGivenKey(convertingStrategy.getFactKey()));
        return convertingStrategy.getStrategyToObtainValue().returnRawValue(stream);
    }

    @Override
    public <V> double getFeatureValueOfFact(FactWithSetOfOptionalValuesForAgentType<V> convertingStrategy) {
        Stream<Optional<V>> stream = watchers.stream()
                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getID() == convertingStrategy.getAgentType().getID())
                .map(AgentWatcher::getBeliefs)
                .map(beliefs -> beliefs.returnFactValueForGivenKey(convertingStrategy.getFactKey()));
        return convertingStrategy.getStrategyToObtainValue().returnRawValue(stream);
    }

    @Override
    public <V> double getFeatureValueOfFactSet(FactWithOptionalValueSetsForAgentType<V> convertingStrategy) {
        Stream<Optional<Stream<V>>> stream = watchers.stream()
                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().getID() == convertingStrategy.getAgentType().getID())
                .map(AgentWatcher::getBeliefs)
                .map(beliefs -> beliefs.returnFactSetValueForGivenKey(convertingStrategy.getFactKey()));
        return convertingStrategy.getStrategyToObtainValue().returnRawValue(stream);
    }

}
