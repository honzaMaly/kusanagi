package cz.jan.maly.model.metadata.agents.configuration;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.Commitment;
import cz.jan.maly.model.planing.RemoveCommitment;
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
    private Commitment decisionInDesire;
    private RemoveCommitment decisionInIntention;
    private Set<DesireKey> typesOfDesiresToConsiderWhenCommitting;
    private Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment;

    //builder with default fields
    static class CommonConfigurationBuilder {
        private Set<DesireKey> typesOfDesiresToConsiderWhenCommitting = new HashSet<>();
        private Set<DesireKey> typesOfDesiresToConsiderWhenRemovingCommitment = new HashSet<>();
    }
}
