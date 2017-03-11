package cz.jan.maly.model.metadata;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

/**
 * Class describes metadata required to form intention - its parameters
 * Created by Jan on 10-Mar-17.
 */
@Builder
public class IntentionParameters {

    @Getter
    private final Set<FactKey<?>> parametersTypesForFacts;

    @Getter
    private final Set<FactKey<?>> parametersTypesForFactSets;

    public IntentionParameters(Set<FactKey<?>> parametersTypesForFacts, Set<FactKey<?>> parametersTypesForFactSets) {
        this.parametersTypesForFacts = parametersTypesForFacts;
        this.parametersTypesForFactSets = parametersTypesForFactSets;
    }
}
