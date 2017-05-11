package cz.jan.maly.model.agent.types;

import bwapi.Game;
import bwta.BaseLocation;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.game.wrappers.ABaseLocationWrapper;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.UnitWrapperFactory;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.AgentTypeMakingObservations;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
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
 * Type definition - agent type for base location
 * <p>
 * Created by Jan on 05-Apr-17.
 */
public class AgentTypeBaseLocation extends AgentTypeMakingObservations<Game> {
    //single definition of command to observe to be used by all agents of this type
    private static final ObservingCommand<Game> OBSERVING_COMMAND = (memory, environment) -> {
        Optional<ABaseLocationWrapper> baseLocation = memory.returnFactValueForGivenKey(IS_BASE_LOCATION);
        if (!baseLocation.isPresent()) {
            MyLogger.getLogger().warning("Trying to access commendable unit but it is not present.");
            throw new RuntimeException("Trying to access commendable unit but it is not present.");
        }
        updateKnowledgeAboutResources(baseLocation.get().getWrappedPosition(), memory, environment.getFrameCount());
        memory.updateFact(MADE_OBSERVATION_IN_FRAME, environment.getFrameCount());
        return true;
    };

    /**
     * Define agent type. Together with initial desires
     *
     * @param desiresForOthers
     * @param desiresWithAbstractIntention
     * @param desiresWithIntentionToAct
     * @param desiresWithIntentionToReason
     * @param usingTypesForFacts
     * @param usingTypesForFactSets
     * @param initializationStrategy
     */
    @Builder
    private AgentTypeBaseLocation(Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention,
                                  Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason,
                                  Set<FactKey<?>> usingTypesForFacts, Set<FactKey<?>> usingTypesForFactSets,
                                  AgentType.ConfigurationInitializationStrategy initializationStrategy, int skipTurnsToMakeObservation) {
        super(AgentTypes.BASE_LOCATION, desiresForOthers, desiresWithAbstractIntention, desiresWithIntentionToAct, desiresWithIntentionToReason,

                //add facts related to agent
                Stream.concat(usingTypesForFacts.stream(), Arrays.stream(new FactKey<?>[]{IS_BASE_LOCATION,
                        MADE_OBSERVATION_IN_FRAME, IS_MINERAL_ONLY, IS_ISLAND, IS_START_LOCATION, IS_BASE_LOCATION}))
                        .collect(Collectors.toSet()),

                //add fact set related to resources
                Stream.concat(usingTypesForFactSets.stream(), Arrays.stream(new FactKey<?>[]{MINERAL,
                        GEYSER})).collect(Collectors.toSet()),

                initializationStrategy, OBSERVING_COMMAND, skipTurnsToMakeObservation);
    }

    /**
     * Method to update base info about resources. DO NOT CALL OUTSIDE MAIN GAME THREAD!
     *
     * @param location
     * @param memory
     * @param frameCount
     */
    public static void updateKnowledgeAboutResources(BaseLocation location, WorkingMemory memory, int frameCount) {
        Set<AUnit> minerals = location.getMinerals().stream()
                .map(unit -> UnitWrapperFactory.wrapResourceUnits(unit, frameCount, false))
                .collect(Collectors.toSet());
        memory.updateFactSetByFacts(MINERAL, minerals);

        Set<AUnit> geysers = location.getGeysers().stream()
                .map(unit -> UnitWrapperFactory.wrapResourceUnits(unit, frameCount, false))
                .collect(Collectors.toSet());
        memory.updateFactSetByFacts(GEYSER, geysers);
    }

    //builder with default fields
    public static class AgentTypeBaseLocationBuilder extends AgentTypeMakingObservationsBuilder {
        private Set<DesireKey> desiresForOthers = new HashSet<>();
        private Set<DesireKey> desiresWithAbstractIntention = new HashSet<>();
        private Set<DesireKey> desiresWithIntentionToAct = new HashSet<>();
        private Set<DesireKey> desiresWithIntentionToReason = new HashSet<>();
        private Set<FactKey<?>> usingTypesForFacts = new HashSet<>();
        private Set<FactKey<?>> usingTypesForFactSets = new HashSet<>();
        private int skipTurnsToMakeObservation = 5;
    }

}
