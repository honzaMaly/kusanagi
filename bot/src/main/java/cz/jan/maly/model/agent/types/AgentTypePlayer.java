package cz.jan.maly.model.agent.types;

import bwapi.Game;
import cz.jan.maly.model.game.wrappers.APlayer;
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
 * Type definition - agent type for player
 * Created by Jan on 05-Apr-17.
 */
public class AgentTypePlayer extends AgentTypeMakingObservations<Game> {
    //single definition of command to observe to be used by all agents of this type
    private static final ObservingCommand<Game> OBSERVING_COMMAND = (memory, environment) -> {
        Optional<APlayer> aPlayer = memory.returnFactValueForGivenKey(IS_PLAYER);
        if (!aPlayer.isPresent()) {
            MyLogger.getLogger().warning("Trying to access player but it is not present.");
            throw new RuntimeException("Trying to access player but it is not present.");
        }

        //update fields by creating new instance
        APlayer player = aPlayer.get().makeObservationOfEnvironment();

        //add updated version of itself to knowledge
        memory.updateFact(IS_PLAYER, player);
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
    private AgentTypePlayer(AgentTypeID agentTypeID, Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention,
                            Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason,
                            Set<FactKey<?>> usingTypesForFacts, Set<FactKey<?>> usingTypesForFactSets,
                            AgentType.ConfigurationInitializationStrategy initializationStrategy, int skipTurnsToMakeObservation) {
        super(agentTypeID, desiresForOthers, desiresWithAbstractIntention, desiresWithIntentionToAct, desiresWithIntentionToReason,

                //add facts related to agent - IS_UNIT, REPRESENTS_UNIT
                Stream.concat(usingTypesForFacts.stream(), Arrays.stream(new FactKey<?>[]{IS_PLAYER, ENEMY_RACE,
                        MADE_OBSERVATION_IN_FRAME})).collect(Collectors.toSet()),
                usingTypesForFactSets, initializationStrategy, OBSERVING_COMMAND, skipTurnsToMakeObservation);
    }

    //builder with default fields
    public static class AgentTypePlayerBuilder extends AgentTypeMakingObservationsBuilder {
        private Set<DesireKey> desiresForOthers = new HashSet<>();
        private Set<DesireKey> desiresWithAbstractIntention = new HashSet<>();
        private Set<DesireKey> desiresWithIntentionToAct = new HashSet<>();
        private Set<DesireKey> desiresWithIntentionToReason = new HashSet<>();
        private Set<FactKey<?>> usingTypesForFacts = new HashSet<>();
        private Set<FactKey<?>> usingTypesForFactSets = new HashSet<>();
        private int skipTurnsToMakeObservation = 5;
    }

}
