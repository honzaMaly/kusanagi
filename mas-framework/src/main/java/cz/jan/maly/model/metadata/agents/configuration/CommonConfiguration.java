package cz.jan.maly.model.metadata.agents.configuration;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.ReactionOnChangeStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Common configuration class
 * Created by Jan on 03-Apr-17.
 */
@Getter
@AllArgsConstructor
public class CommonConfiguration {
    private CommitmentDeciderInitializer decisionInDesire;
    private CommitmentDeciderInitializer decisionInIntention;
    private Set<DesireKey> typesOfDesiresToConsiderWhenCommitting;
    private Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment;
    private ReactionOnChangeStrategy reactionOnChangeStrategy;
    private ReactionOnChangeStrategy reactionOnChangeStrategyInIntention;

    //builder with default fields
    static class CommonConfigurationBuilder {
        private Set<DesireKey> typesOfDesiresToConsiderWhenCommitting = new HashSet<>();
        private Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment = new HashSet<>();
    }
}
