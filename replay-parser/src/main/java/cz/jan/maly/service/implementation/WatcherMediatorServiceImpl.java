package cz.jan.maly.service.implementation;

import cz.jan.maly.model.watcher.*;
import cz.jan.maly.service.WatcherMediatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

/**
 * Implementation of WatcherMediatorService
 * Created by Jan on 18-Apr-17.
 */
@Slf4j
@Service
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
        watchers.forEach(agentWatcher -> agentWatcher.planWhereStatusHasChanged(this));

    }

    @Override
    public <V extends Stream<Optional<?>>> double getFeatureValueOfFact(FactConverter<V> convertingStrategy) {
        Stream<Optional<?>> stream = watchers.stream()
                .map(AgentWatcher::getBeliefs)
                .filter(beliefs -> beliefs.isFactKeyForValueInMemory(convertingStrategy.getFactKey()))
                .map(beliefs -> beliefs.returnFactValueForGivenKey(convertingStrategy.getFactKey()));
        return convertingStrategy.convert((V) stream);
    }

    @Override
    public <V extends Stream<Optional<Stream<?>>>> double getFeatureValueOfFactSet(FactConverter<V> convertingStrategy) {
        Stream<Optional<Stream<?>>> stream = watchers.stream()
                .map(AgentWatcher::getBeliefs)
                .filter(beliefs -> beliefs.isFactKeyForSetInMemory(convertingStrategy.getFactKey()))
                .map(beliefs -> beliefs.returnFactSetValueForGivenKey(convertingStrategy.getFactKey()));
        return convertingStrategy.convert((V) stream);
    }

    @Override
    public <V extends Stream<Optional<?>>> double getFeatureValueOfFact(FactConverterByAgentType<V> convertingStrategy) {
        Stream<Optional<?>> stream = watchers.stream()
                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().equals(convertingStrategy.getType()))
                .map(AgentWatcher::getBeliefs)
                .map(beliefs -> beliefs.returnFactValueForGivenKey(convertingStrategy.getFactKey()));
        return convertingStrategy.convert((V) stream);
    }

    @Override
    public <V extends Stream<Optional<Stream<?>>>> double getFeatureValueOfFactSet(FactConverterByAgentType<V> convertingStrategy) {
        Stream<Optional<Stream<?>>> stream = watchers.stream()
                .filter(agentWatcher -> agentWatcher.getAgentWatcherType().equals(convertingStrategy.getType()))
                .map(AgentWatcher::getBeliefs)
                .map(beliefs -> beliefs.returnFactSetValueForGivenKey(convertingStrategy.getFactKey()));
        return convertingStrategy.convert((V) stream);
    }

}
