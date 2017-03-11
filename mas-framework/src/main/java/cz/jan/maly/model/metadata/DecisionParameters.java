package cz.jan.maly.model.metadata;

import lombok.Getter;

import java.util.Set;

/**
 * Class defines data to be loaded in DataForDecision instance
 * Created by Jan on 02-Mar-17.
 */
public class DecisionParameters {

    @Getter
    private final Set<FactKey<?>> parametersTypesForFacts;

    @Getter
    private final Set<FactKey<?>> parametersTypesForFactSets;

    @Getter
    private final Set<DesireKey> typesOfDesiresToConsider;

    public DecisionParameters(Set<FactKey<?>> parametersTypesForFacts, Set<FactKey<?>> parametersTypesForFactSets, Set<DesireKey> typesOfDesiresToConsider) {
        this.parametersTypesForFacts = parametersTypesForFacts;
        this.parametersTypesForFactSets = parametersTypesForFactSets;
        this.typesOfDesiresToConsider = typesOfDesiresToConsider;
    }
}
