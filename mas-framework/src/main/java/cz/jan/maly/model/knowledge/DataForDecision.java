package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.FactContainerInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.DecisionContainerParameters;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

import static cz.jan.maly.utils.FrameworkUtils.CLONER;

/**
 * Container with data to be used to make commitment on (additional to data defined by intention/desire)
 * Created by Jan on 02-Mar-17.
 */
public class DataForDecision implements FactContainerInterface {

    //additional facts retrieved from memory to make decision on
    private final Map<FactKey, Object> factParameterMap = new HashMap<>();
    private final Map<FactKey, Set> factSetParameterMap = new HashMap<>();

    //what was already decided on same level - types of desires
    @Getter
    private final List<DesireKey> madeCommitmentToTypes;
    @Getter
    private final List<DesireKey> didNotMakeCommitmentToTypes;

    //desires/intention types to come
    @Getter
    private final List<DesireKey> typesAboutToMakeDecision;

    @Getter
    private final Optional<DesireKey> parentsType;

    @Getter
    private final boolean isAtTopLevel;

    public DataForDecision(DecisionContainerParameters parameters, Agent agent, List<DesireKey> madeCommitmentToTypes, List<DesireKey> didNotMakeCommitmentToTypes, List<DesireKey> typesAboutToMakeDecision, Optional<DesireKey> parentsType) {
        this.isAtTopLevel = !parentsType.isPresent();

        //filter keys
        this.madeCommitmentToTypes = madeCommitmentToTypes.stream()
                .filter(desireKey -> parameters.getTypesOfDesiresToConsider().contains(desireKey))
                .collect(Collectors.toList());
        this.didNotMakeCommitmentToTypes = didNotMakeCommitmentToTypes.stream()
                .filter(desireKey -> parameters.getTypesOfDesiresToConsider().contains(desireKey))
                .collect(Collectors.toList());
        this.typesAboutToMakeDecision = typesAboutToMakeDecision.stream()
                .filter(desireKey -> parameters.getTypesOfDesiresToConsider().contains(desireKey))
                .collect(Collectors.toList());

        this.parentsType = parentsType;

        //fill maps with actual parameters from internal_beliefs
        parameters.getParametersTypesForFacts().forEach(factKey -> {
            Optional<?> value = agent.getBeliefs().returnFactValueForGivenKey(factKey);
            value.ifPresent(o -> factParameterMap.put(factKey, CLONER.deepClone(o)));
        });
        parameters.getParametersTypesForFactSets().forEach(factKey -> {
            Optional<Set> value = agent.getBeliefs().returnFactSetValueForGivenKey(factKey);
            value.ifPresent(set -> factSetParameterMap.put(factKey, CLONER.deepClone(set)));
        });
    }

    public <V> Optional<V> returnFactValueForGivenKey(FactKey<V> factKey) {
        Object value = factParameterMap.get(factKey);
        if (value != null) {
            return Optional.of((V) value);
        }
        return Optional.empty();
    }

    public <V, S extends Set<V>> Optional<S> returnFactSetValueForGivenKey(FactKey<V> factKey) {
        Set values = factSetParameterMap.get(factKey);
        if (values != null) {
            return Optional.of((S) values);
        }
        return Optional.empty();
    }

}
