package cz.jan.maly.model.metadata.agents.configuration;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.Commitment;
import cz.jan.maly.model.planing.RemoveCommitment;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * WithAbstractPlan configuration class
 * Created by Jan on 03-Apr-17.
 */
@Getter
public class ConfigurationWithAbstractPlan extends CommonConfiguration {
    private Set<DesireKey> desiresForOthers;
    private Set<DesireKey> desiresWithAbstractIntention;
    private Set<DesireKey> desiresWithIntentionToAct;
    private Set<DesireKey> desiresWithIntentionToReason;

    @Builder
    private ConfigurationWithAbstractPlan(Commitment decisionInDesire, RemoveCommitment decisionInIntention,
                                          Set<DesireKey> typesOfDesiresToConsiderWhenCommitting, Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment,
                                          Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention,
                                          Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason) {
        super(decisionInDesire, decisionInIntention, typesOfDesiresToConsiderWhenCommitting, typesOfDesiresToConsiderWhenRemovingCommitment);
        this.desiresForOthers = desiresForOthers;
        this.desiresWithAbstractIntention = desiresWithAbstractIntention;
        this.desiresWithIntentionToAct = desiresWithIntentionToAct;
        this.desiresWithIntentionToReason = desiresWithIntentionToReason;
    }

    //builder with default fields
    public static class ConfigurationWithAbstractPlanBuilder extends CommonConfiguration.CommonConfigurationBuilder {
        private Set<DesireKey> desiresForOthers = new HashSet<>();
        private Set<DesireKey> desiresWithAbstractIntention = new HashSet<>();
        private Set<DesireKey> desiresWithIntentionToAct = new HashSet<>();
        private Set<DesireKey> desiresWithIntentionToReason = new HashSet<>();
        private Set<DesireKey> typesOfDesiresToConsiderWhenCommitting = new HashSet<>();
        private Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment = new HashSet<>();
    }

}
