package cz.jan.maly.model.agent.types;

import bwapi.Game;
import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
import cz.jan.maly.model.metadata.*;
import cz.jan.maly.model.planing.command.ObservingCommand;
import cz.jan.maly.utils.MyLogger;
import lombok.Builder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.jan.maly.model.bot.FactKeys.*;

/**
 * Type definition - agent type for unit observing game
 * Created by Jan on 03-Apr-17.
 */
public class AgentTypeUnit extends AgentTypeMakingObservations<Game> {

    //single definition of command to observe to be used by all agents of this type
    private static final ObservingCommand<Game> OBSERVING_COMMAND = (memory, environment) -> {
        Optional<AUnitWithCommands> unitWithCommands = memory.returnFactValueForGivenKey(IS_UNIT);
        if (!unitWithCommands.isPresent()) {
            MyLogger.getLogger().warning("Trying to access commendable unit but it is not present.");
            throw new RuntimeException("Trying to access commendable unit but it is not present.");
        }

        //update fields by creating new instance
        AUnitWithCommands unit = unitWithCommands.get().makeObservationOfEnvironment(environment.getFrameCount());

        //add updated version of itself to knowledge
        memory.updateFact(IS_UNIT, unit);
        memory.updateFact(REPRESENTS_UNIT, unit);
        memory.updateFact(MADE_OBSERVATION_IN_FRAME, environment.getFrameCount());
        return true;
    };

    /**
     * Define agent type. Together with initial desires
     *
     * @param agentTypeID
     * @param desiresForOthers
     * @param desiresWithAbstractIntention
     * @param desiresWithIntentionToAct
     * @param desiresWithIntentionToReason
     * @param usingTypesForFacts
     * @param usingTypesForFactSets
     * @param initializationStrategy
     */
    @Builder
    private AgentTypeUnit(AgentTypeID agentTypeID, Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention,
                          Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason,
                          Set<FactKey<?>> usingTypesForFacts, Set<FactKey<?>> usingTypesForFactSets,
                          AgentType.ConfigurationInitializationStrategy initializationStrategy, int skipTurnsToMakeObservation) {
        super(agentTypeID, desiresForOthers, desiresWithAbstractIntention, desiresWithIntentionToAct, desiresWithIntentionToReason,

                //add facts related to agent - IS_UNIT, REPRESENTS_UNIT
                Stream.concat(usingTypesForFacts.stream(), Arrays.stream(new FactKey<?>[]{IS_UNIT, REPRESENTS_UNIT,
                        MADE_OBSERVATION_IN_FRAME})).collect(Collectors.toSet()),
                usingTypesForFactSets, initializationStrategy, OBSERVING_COMMAND, skipTurnsToMakeObservation);
    }

    //builder with default fields
    public static class AgentTypeUnitBuilder extends AgentTypeMakingObservationsBuilder {
        private Set<DesireKey> desiresForOthers = new HashSet<>();
        private Set<DesireKey> desiresWithAbstractIntention = new HashSet<>();
        private Set<DesireKey> desiresWithIntentionToAct = new HashSet<>();
        private Set<DesireKey> desiresWithIntentionToReason = new HashSet<>();
        private Set<FactKey<?>> usingTypesForFacts = new HashSet<>();
        private Set<FactKey<?>> usingTypesForFactSets = new HashSet<>();
        private int skipTurnsToMakeObservation = 5;
    }
}
