package cz.jan.maly.model.metadata;

import lombok.Getter;

import java.util.Set;

/**
 * Class describes metadata required to form command to act by agent - its parameters
 * Created by Jan on 11-Mar-17.
 */
public class ActCommandParameters {

    @Getter
    private final Set<FactKey<?>> parametersTypesForFacts;

    @Getter
    private final Set<FactKey<?>> parametersTypesForFactSets;

    public ActCommandParameters(Set<FactKey<?>> parametersTypesForFacts, Set<FactKey<?>> parametersTypesForFactSets) {
        this.parametersTypesForFacts = parametersTypesForFacts;
        this.parametersTypesForFactSets = parametersTypesForFactSets;
    }
}
