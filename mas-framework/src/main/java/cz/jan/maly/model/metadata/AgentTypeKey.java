package cz.jan.maly.model.metadata;

import lombok.Getter;

import java.util.Set;

/**
 * Class describing metadata for agent type - used for identification and parameter type definition - fact types and
 * fact set types in memory (beliefs) as well as supported (implemented) desires by agent type and desires it may request
 * Created by Jan on 15-Feb-17.
 */
public abstract class AgentTypeKey extends Key {

    @Getter
    private final Set<FactKey<?>> usingTypesForFacts;

    @Getter
    private final Set<FactKey<?>> usingTypesForFactSets;

    @Getter
    private final Set<DesireKey> supportedDesires;

    @Getter
    private final Set<DesireKey> requestingDesires;

    AgentTypeKey(String name, Set<FactKey<?>> usingTypesForFacts, Set<FactKey<?>> usingTypesForFactSets, Set<DesireKey> supportedDesires, Set<DesireKey> requestingDesires) {
        super(name, AgentTypeKey.class);
        this.usingTypesForFacts = usingTypesForFacts;
        this.usingTypesForFactSets = usingTypesForFactSets;
        this.supportedDesires = supportedDesires;
        this.requestingDesires = requestingDesires;
    }
}
