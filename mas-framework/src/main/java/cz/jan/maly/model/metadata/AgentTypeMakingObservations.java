package cz.jan.maly.model.metadata;

import cz.jan.maly.model.planing.command.ObservingCommand;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Extension of type. It also defines command to make observation
 * Created by Jan on 03-Apr-17.
 */
public class AgentTypeMakingObservations<E> extends AgentType {
    @Getter
    private ObservingCommand<E> observingCommand;

    @Getter
    private final int skipTurnsToMakeObservation;

    /**
     * Define agent type. Together with initial desires
     *
     * @param name
     * @param desiresForOthers
     * @param desiresWithAbstractIntention
     * @param desiresWithIntentionToAct
     * @param desiresWithIntentionToReason
     * @param usingTypesForFacts
     * @param usingTypesForFactSets
     */
    protected AgentTypeMakingObservations(String name, Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention,
                                          Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason,
                                          Set<FactKey<?>> usingTypesForFacts, Set<FactKey<?>> usingTypesForFactSets,
                                          ConfigurationInitializationStrategy initializationStrategy,
                                          ObservingCommand<E> observingCommand, int skipTurnsToMakeObservation) {
        super(name, desiresForOthers, desiresWithAbstractIntention, desiresWithIntentionToAct, desiresWithIntentionToReason,
                usingTypesForFacts, usingTypesForFactSets, initializationStrategy);
        this.observingCommand = observingCommand;
        this.skipTurnsToMakeObservation = skipTurnsToMakeObservation;
    }

    //builder with default fields
    public static class AgentTypeMakingObservationsBuilder extends AgentTypeBuilder {
        private Set<DesireKey> desiresForOthers = new HashSet<>();
        private Set<DesireKey> desiresWithAbstractIntention = new HashSet<>();
        private Set<DesireKey> desiresWithIntentionToAct = new HashSet<>();
        private Set<DesireKey> desiresWithIntentionToReason = new HashSet<>();
        private Set<FactKey<?>> usingTypesForFacts = new HashSet<>();
        private Set<FactKey<?>> usingTypesForFactSets = new HashSet<>();
        private int skipTurnsToMakeObservation = 5;
    }
}
