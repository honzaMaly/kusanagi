package cz.jan.maly.model.metadata;

import cz.jan.maly.model.metadata.agents.AnotherAgentsDesireWithAbstractIntentionFormulation;
import cz.jan.maly.model.metadata.agents.OwnDesireWithAbstractIntentionStackedFormulation;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Class describing metadata for agent type - used for identification and parameter type definition - fact types and
 * fact set types in memory (internal beliefs) as well as supported (implemented) desires by agent type
 * Created by Jan on 15-Feb-17.
 */
public abstract class AgentType extends Key {

    @Getter
    private final Set<FactKey<?>> usingTypesForFacts = new HashSet<>();

    @Getter
    private final Set<FactKey<?>> usingTypesForFactSets = new HashSet<>();

    private final OwnDesireWithAbstractIntentionStackedFormulation ownDesireWithAbstractIntentionFormulation = new OwnDesireWithAbstractIntentionStackedFormulation();

    private final AnotherAgentsDesireWithAbstractIntentionFormulation anotherAgentsDesireWithAbstractIntentionFormulation = new AnotherAgentsDesireWithAbstractIntentionFormulation();

    //todo other factories, implement creation factory interface, add methods to set configuration

    protected AgentType(String name) {
        super(name, AgentType.class);
    }

}
