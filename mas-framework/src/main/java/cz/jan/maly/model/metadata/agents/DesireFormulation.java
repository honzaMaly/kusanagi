package cz.jan.maly.model.metadata.agents;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.agents.configuration.CommonConfiguration;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithAbstractPlan;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.planing.CommitmentDeciderInitializer;
import cz.jan.maly.model.planing.IntentionCommand;
import cz.jan.maly.model.planing.ReactionOnChangeStrategy;
import cz.jan.maly.model.planing.command.CommandForIntention;
import cz.jan.maly.model.planing.command.CommandFormulationStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Template for desire formulations of specific type (child dependant), this contains only common structures and methods
 * Created by Jan on 11-Mar-17.
 */
public abstract class DesireFormulation {
    private final Map<DesireKey, CommitmentDeciderInitializer> decisionsByDesire = new HashMap<>();
    private final Map<DesireKey, CommitmentDeciderInitializer> decisionsByIntention = new HashMap<>();
    private final Map<DesireKey, ReactionOnChangeStrategy> reactionsByIntention = new HashMap<>();
    private final Map<DesireKey, ReactionOnChangeStrategy> reactionsByDesire = new HashMap<>();

    CommitmentDeciderInitializer getDecisionInIntention(DesireKey key) {
        return decisionsByIntention.get(key);
    }

    CommitmentDeciderInitializer getDecisionInDesire(DesireKey key) {
        return decisionsByDesire.get(key);
    }

    ReactionOnChangeStrategy getReactionInIntention(DesireKey key) {
        return reactionsByIntention.get(key);
    }

    ReactionOnChangeStrategy getReactionInDesire(DesireKey key) {
        return reactionsByDesire.get(key);
    }

    /**
     * Add configuration for desire
     *
     * @param key
     * @param configuration
     */
    void addDesireFormulationConfiguration(DesireKey key, CommonConfiguration configuration) {
        this.decisionsByDesire.put(key, configuration.getDecisionInDesire());
        this.decisionsByIntention.put(key, configuration.getDecisionInIntention());
        this.reactionsByIntention.put(key, configuration.getReactionOnChangeStrategyInIntention());
        this.reactionsByDesire.put(key, configuration.getReactionOnChangeStrategy());
    }

    /**
     * Returns true if desire key is supported
     *
     * @param desireKey
     * @return
     */
    public abstract boolean supportsDesireType(DesireKey desireKey);

    protected boolean supportsType(DesireKey desireKey) {
        return decisionsByDesire.containsKey(desireKey);
    }

    public Set<DesireKey> supportedDesireTypes() {
        return decisionsByDesire.keySet();
    }

    /**
     * Defines common structure to add configuration for abstract plan
     */
    protected static abstract class WithAbstractPlan extends DesireFormulation {
        final Map<DesireKey, Set<DesireKey>> desiresForOthersByKey = new HashMap<>();
        final Map<DesireKey, Set<DesireKey>> desiresWithAbstractIntentionByKey = new HashMap<>();
        final Map<DesireKey, Set<DesireKey>> desiresWithIntentionToActByKey = new HashMap<>();
        final Map<DesireKey, Set<DesireKey>> desiresWithIntentionToReasonByKey = new HashMap<>();

        public Map<DesireKey, Set<DesireKey>> desiresForOthers() {
            return desiresForOthersByKey;
        }

        public Map<DesireKey, Set<DesireKey>> desiresWithAbstractIntention() {
            return desiresWithAbstractIntentionByKey;
        }

        public Map<DesireKey, Set<DesireKey>> desiresWithIntentionToAct() {
            return desiresWithIntentionToActByKey;
        }

        public Map<DesireKey, Set<DesireKey>> desiresWithIntentionToReason() {
            return desiresWithIntentionToReasonByKey;
        }

        /**
         * Add configuration for desire
         *
         * @param key
         * @param configuration
         */
        public void addDesireFormulationConfiguration(DesireKey key, ConfigurationWithAbstractPlan configuration) {
            addDesireFormulationConfiguration(key, (CommonConfiguration) configuration);
            desiresForOthersByKey.put(key, configuration.getDesiresForOthers());
            desiresWithAbstractIntentionByKey.put(key, configuration.getDesiresWithAbstractIntention());
            desiresWithIntentionToActByKey.put(key, configuration.getDesiresWithIntentionToAct());
            desiresWithIntentionToReasonByKey.put(key, configuration.getDesiresWithIntentionToReason());
        }
    }

    /**
     * Defines common structure to add configuration for intention with command
     */
    static abstract class WithCommand<K extends CommandFormulationStrategy<? extends CommandForIntention<?>, ? extends IntentionCommand<?, ?>>> extends DesireFormulation {
        final Map<DesireKey, K> commandsByKey = new HashMap<>();

        /**
         * Add configuration for desire
         *
         * @param key
         * @param configuration
         */
        public void addDesireFormulationConfiguration(DesireKey key, ConfigurationWithCommand<K> configuration) {
            addDesireFormulationConfiguration(key, (CommonConfiguration) configuration);
            commandsByKey.put(key, configuration.getCommandCreationStrategy());
        }
    }

}
