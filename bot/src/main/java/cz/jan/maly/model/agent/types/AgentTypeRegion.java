package cz.jan.maly.model.agent.types;

import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.AgentTypeID;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import lombok.Builder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.jan.maly.model.bot.FactKeys.IS_REGION;

/**
 * Type definition - of agent representing region
 * Created by Jan on 05-Apr-17.
 */
public class AgentTypeRegion extends AgentType {
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
    public AgentTypeRegion(AgentTypeID agentTypeID, Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention, Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason, Set<FactKey<?>> usingTypesForFacts, Set<FactKey<?>> usingTypesForFactSets, ConfigurationInitializationStrategy initializationStrategy) {
        super(agentTypeID, desiresForOthers, desiresWithAbstractIntention, desiresWithIntentionToAct, desiresWithIntentionToReason,

                //add facts related to agent
                Stream.concat(usingTypesForFacts.stream(), Arrays.stream(new FactKey<?>[]{IS_REGION})).collect(Collectors.toSet()),

                usingTypesForFactSets, initializationStrategy);
    }

    //builder with default fields
    public static class AgentTypeRegionBuilder extends AgentType.AgentTypeBuilder {
        private Set<DesireKey> desiresForOthers = new HashSet<>();
        private Set<DesireKey> desiresWithAbstractIntention = new HashSet<>();
        private Set<DesireKey> desiresWithIntentionToAct = new HashSet<>();
        private Set<DesireKey> desiresWithIntentionToReason = new HashSet<>();
        private Set<FactKey<?>> usingTypesForFacts = new HashSet<>();
        private Set<FactKey<?>> usingTypesForFactSets = new HashSet<>();
    }
}
