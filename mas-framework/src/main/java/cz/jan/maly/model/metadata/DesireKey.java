package cz.jan.maly.model.metadata;

import lombok.Getter;

import java.util.Set;

/**
 * Class describing metadata for desire - used for identification and parameter type definition.
 * Created by Jan on 14-Feb-17.
 */
public class DesireKey extends Key {

    @Getter
    private final Set<FactKey<?>> parametersTypesForFacts;

    @Getter
    private final Set<FactKey<?>> parametersTypesForFactSets;

    public DesireKey(String name, Set<FactKey<?>> parametersTypesForFacts, Set<FactKey<?>> parametersTypesForFactSets) {
        super(name, DesireKey.class);
        this.parametersTypesForFacts = parametersTypesForFacts;
        this.parametersTypesForFactSets = parametersTypesForFactSets;
    }

}
