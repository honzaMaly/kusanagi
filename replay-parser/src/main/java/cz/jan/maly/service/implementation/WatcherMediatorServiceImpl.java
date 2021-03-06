package cz.jan.maly.service.implementation;

import cz.jan.maly.model.metadata.AgentTypeID;
import cz.jan.maly.model.metadata.DesireKeyID;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValueSets;
import cz.jan.maly.model.metadata.containers.FactWithOptionalValueSetsForAgentType;
import cz.jan.maly.model.metadata.containers.FactWithSetOfOptionalValues;
import cz.jan.maly.model.metadata.containers.FactWithSetOfOptionalValuesForAgentType;
import cz.jan.maly.model.tracking.Trajectory;
import cz.jan.maly.model.watcher.AgentWatcher;
import cz.jan.maly.service.StorageService;
import cz.jan.maly.service.WatcherMediatorService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of WatcherMediatorService
 * Created by Jan on 18-Apr-17.
 */
public class WatcherMediatorServiceImpl implements WatcherMediatorService {
    private static WatcherMediatorService instance = null;
    private final Set<AgentWatcher<?>> watchers = new HashSet<>(), allWatchers = new HashSet<>();
    private final StorageService storageService = StorageServiceImp.getInstance();

    private WatcherMediatorServiceImpl() {
        //singleton
    }

    public static WatcherMediatorService getInstance() {
        if (instance == null) {
            instance = new WatcherMediatorServiceImpl();
        }
        return instance;
    }

    @Override
    public void addWatcher(AgentWatcher watcher) {
        watchers.add(watcher);
        allWatchers.add(watcher);
    }

    @Override
    public void removeWatcher(AgentWatcher watcher) {
        watchers.remove(watcher);
    }

    @Override
    public Stream<AgentWatcher<?>> getStreamOfWatchers() {
        return watchers.stream();
    }

    @Override
    public void clearAllAgentsAndSaveTheirTrajectories() {

        //save trajectories
        Map<AgentTypeID, Map<DesireKeyID, List<Trajectory>>> toPersist = new HashMap<>();
        allWatchers.stream()
                //collect watchers by type
                .collect(Collectors.groupingBy(AgentWatcher::getAgentWatcherType,
                        Collectors.mapping(Function.identity(), Collectors.toList())))
                //for each of them get map of trajectories with desires
                .forEach((agentWatcherType, agentWatchers) -> agentWatchers.stream()
                        .map(agentWatcher -> (AgentWatcher<?>) agentWatcher)
                        .flatMap(AgentWatcher::getTrajectories)
                        .forEach(desireKeyIDListEntry -> toPersist.computeIfAbsent(agentWatcherType, agentTypeID -> new HashMap<>()).computeIfAbsent(desireKeyIDListEntry.getKey(), desireKeyID -> new ArrayList<>()).addAll(desireKeyIDListEntry.getValue())));
        //save merged entries
        toPersist.forEach((agentTypeID, desireKeyIDListMap) -> desireKeyIDListMap.forEach((desireKeyID, trajectories) -> storageService.saveTrajectory(agentTypeID, desireKeyID, trajectories)));

        //remove agents from register
        watchers.clear();
        allWatchers.clear();
    }

    @Override
    public void tellAgentsToObserveSystemAndHandlePlans() {

        //handle trajectories first to keep causality for actions
        watchers.parallelStream().forEach(agentWatcher -> agentWatcher.handleTrajectoriesOfPlans(this));
        watchers.parallelStream().forEach(agentWatcher -> agentWatcher.reason(this));
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
