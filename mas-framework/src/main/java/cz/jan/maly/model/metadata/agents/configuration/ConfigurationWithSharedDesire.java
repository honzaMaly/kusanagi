package cz.jan.maly.model.metadata.agents.configuration;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.ReactionOnChangeStrategy;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * WithSharedDesire configuration class
 * Created by Jan on 03-Apr-17.
 */
@Getter
public class ConfigurationWithSharedDesire extends CommonConfiguration {
    private DesireKey sharedDesireKey;
    private int counts;

    @Builder
    private ConfigurationWithSharedDesire(CommitmentDeciderInitializer decisionInDesire,
                                          CommitmentDeciderInitializer decisionInIntention,
                                          Set<DesireKey> typesOfDesiresToConsiderWhenCommitting,
                                          Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment,
                                          DesireKey sharedDesireKey, int counts,
                                          ReactionOnChangeStrategy reactionOnChangeStrategy,
                                          ReactionOnChangeStrategy reactionOnChangeStrategyInIntention) {
        super(decisionInDesire, decisionInIntention, typesOfDesiresToConsiderWhenCommitting,
                typesOfDesiresToConsiderWhenRemovingCommitment, reactionOnChangeStrategy, reactionOnChangeStrategyInIntention);
        this.sharedDesireKey = sharedDesireKey;
        this.counts = counts;
    }

    //builder with default fields
    public static class ConfigurationWithSharedDesireBuilder extends CommonConfiguration.CommonConfigurationBuilder {
        private int counts = Integer.MAX_VALUE;
        private Set<DesireKey> typesOfDesiresToConsiderWhenCommitting = new HashSet<>();
        private Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment = new HashSet<>();
    }

}
