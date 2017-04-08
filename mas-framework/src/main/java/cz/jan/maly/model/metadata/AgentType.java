package cz.jan.maly.model.metadata;

import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.agents.*;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithAbstractPlan;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithSharedDesire;
import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.planing.OwnDesire;
import cz.jan.maly.model.planing.SharedDesireForAgents;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Class describing metadata for agent type - used for identification and parameter type definition - fact types and
 * fact set types in memory (internal beliefs) as well as supported (implemented) desires by agent type and ways of their
 * creation using desire key
 * Created by Jan on 15-Feb-17.
 */
public class AgentType extends Key {
    private final OwnDesireWithAbstractIntentionFormulation.Stacked
            ownDesireWithAbstractIntentionFormulation = new OwnDesireWithAbstractIntentionFormulation.Stacked();
    private final OwnDesireWithIntentionWithActingCommandFormulation.Stacked
            ownDesireWithIntentionWithActingCommandFormulation = new OwnDesireWithIntentionWithActingCommandFormulation.Stacked();
    private final OwnDesireWithIntentionWithReasoningCommandFormulation.Stacked
            ownDesireWithIntentionWithReasoningCommandFormulation = new OwnDesireWithIntentionWithReasoningCommandFormulation.Stacked();
    private final AnotherAgentsDesireWithAbstractIntentionFormulation
            anotherAgentsDesireWithAbstractIntentionFormulation = new AnotherAgentsDesireWithAbstractIntentionFormulation();
    private final AnotherAgentsDesireWithIntentionWithActingCommandFormulation
            anotherAgentsDesireWithIntentionWithActingCommandFormulation = new AnotherAgentsDesireWithIntentionWithActingCommandFormulation();
    private final OwnDesireWithSharedDesireFormulation.Stacked
            ownDesireWithSharedDesireFormulation = new OwnDesireWithSharedDesireFormulation.Stacked();
    //initial desires for this agent type
    private Set<DesireKey> desiresForOthers;
    private Set<DesireKey> desiresWithAbstractIntention;
    private Set<DesireKey> desiresWithIntentionToAct;
    private Set<DesireKey> desiresWithIntentionToReason;
    private Set<DesireKey> supportedDesiresOfOtherAgents = new HashSet<>();
    @Getter
    private Set<FactKey<?>> usingTypesForFacts;
    @Getter
    private Set<FactKey<?>> usingTypesForFactSets;
    private boolean isConfigurationInitialized = false;

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
     * @param initializationStrategy
     */
    protected AgentType(String name, Set<DesireKey> desiresForOthers,
                        Set<DesireKey> desiresWithAbstractIntention, Set<DesireKey> desiresWithIntentionToAct,
                        Set<DesireKey> desiresWithIntentionToReason, Set<FactKey<?>> usingTypesForFacts,
                        Set<FactKey<?>> usingTypesForFactSets, ConfigurationInitializationStrategy initializationStrategy) {
        super(name, AgentType.class);
        this.desiresForOthers = desiresForOthers;
        this.desiresWithAbstractIntention = desiresWithAbstractIntention;
        this.desiresWithIntentionToAct = desiresWithIntentionToAct;
        this.desiresWithIntentionToReason = desiresWithIntentionToReason;
        this.usingTypesForFacts = usingTypesForFacts;
        this.usingTypesForFactSets = usingTypesForFactSets;

        //initialize configuration first, then get all facts required for correct behaviour to be present in agent
        initializationStrategy.initializeConfiguration(this);
        this.isConfigurationInitialized = true;

        //infer desires for other agents
        supportedDesiresOfOtherAgents.addAll(anotherAgentsDesireWithAbstractIntentionFormulation.supportedDesireTypes());
        supportedDesiresOfOtherAgents.addAll(anotherAgentsDesireWithIntentionWithActingCommandFormulation.supportedDesireTypes());

        //when having abstract plan, can agent make desires
        checkSupport(ownDesireWithAbstractIntentionFormulation.desiresWithIntentionToReason(), ownDesireWithIntentionWithReasoningCommandFormulation);
        checkSupport(anotherAgentsDesireWithAbstractIntentionFormulation.desiresWithIntentionToReason(), ownDesireWithIntentionWithReasoningCommandFormulation);
        checkSupport(ownDesireWithAbstractIntentionFormulation.desiresForOthers(), ownDesireWithSharedDesireFormulation);
        checkSupport(anotherAgentsDesireWithAbstractIntentionFormulation.desiresForOthers(), ownDesireWithSharedDesireFormulation);
        checkSupport(ownDesireWithAbstractIntentionFormulation.desiresWithAbstractIntention(), ownDesireWithAbstractIntentionFormulation);
        checkSupport(anotherAgentsDesireWithAbstractIntentionFormulation.desiresWithAbstractIntention(), ownDesireWithAbstractIntentionFormulation);
        checkSupport(ownDesireWithAbstractIntentionFormulation.desiresWithIntentionToAct(), ownDesireWithIntentionWithActingCommandFormulation);
        checkSupport(anotherAgentsDesireWithAbstractIntentionFormulation.desiresWithIntentionToAct(), ownDesireWithIntentionWithActingCommandFormulation);

        //check if starting desires are present
        checkSupport(desiresWithIntentionToReason, ownDesireWithIntentionWithReasoningCommandFormulation);
        checkSupport(desiresForOthers, ownDesireWithSharedDesireFormulation);
        checkSupport(desiresWithAbstractIntention, ownDesireWithAbstractIntentionFormulation);
        checkSupport(desiresWithIntentionToAct, ownDesireWithIntentionWithActingCommandFormulation);
    }

    /**
     * Check if instance of DesireFormulation supports given keys
     *
     * @param keysToSupport
     * @param desireFormulation
     */
    private void checkSupport(Set<DesireKey> keysToSupport, DesireFormulation desireFormulation) {
        Optional<DesireKey> first = keysToSupport.stream()
                .filter(key -> !desireFormulation.supportsDesireType(key))
                .findAny();
        if (first.isPresent()) {
            MyLogger.getLogger().warning(first.get().getName() + " can't be instantiated in abstract plan for " + getName());
            throw new IllegalArgumentException(first.get().getName() + " can't be instantiated in abstract plan for " + getName());
        }
    }

    /**
     * Check if instance of DesireFormulation supports given keys
     *
     * @param keysToSupport
     * @param desireFormulation
     */
    private <T extends DesireFormulation & OwnInternalDesireFormulationStacked<?>> void checkSupport(Map<DesireKey, Set<DesireKey>> keysToSupport, T desireFormulation) {
        Optional<DesireKey> first = keysToSupport.entrySet()
                .stream()
                .map(entry -> entry.getValue().stream()
                        .filter(value -> !desireFormulation.supportsDesireType(entry.getKey(), value))
                        .findFirst())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
        if (first.isPresent()) {
            MyLogger.getLogger().warning(first.get().getName() + " can't be instantiated in abstract plan for " + getName());
            throw new IllegalArgumentException(first.get().getName() + " can't be instantiated in abstract plan for " + getName());
        }
    }

    /**
     * Create instance of desire
     *
     * @param desireForAgents
     * @param memory
     * @return
     */
    public Optional<DesireFromAnotherAgent.WithAbstractIntention> formAnotherAgentsDesireWithAbstractIntention(SharedDesireForAgents desireForAgents, WorkingMemory memory) {
        return anotherAgentsDesireWithAbstractIntentionFormulation.formDesire(desireForAgents, memory);
    }

    /**
     * Create instance of desire
     *
     * @param desireForAgents
     * @param memory
     * @return
     */
    public Optional<DesireFromAnotherAgent.WithIntentionWithPlan> formAnotherAgentsDesireWithCommand(SharedDesireForAgents desireForAgents, WorkingMemory memory) {
        return anotherAgentsDesireWithIntentionWithActingCommandFormulation.formDesire(desireForAgents, memory);
    }

    /**
     * Create instance of desire
     *
     * @param parentKey
     * @param key
     * @param memory
     * @return
     */
    public OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey parentKey, DesireKey key, WorkingMemory memory, DesireParameters parentsDesireParameters) {
        Optional<OwnDesire.WithAbstractIntention> withAbstractIntention = ownDesireWithAbstractIntentionFormulation.formDesire(parentKey, key, memory, parentsDesireParameters);
        if (!withAbstractIntention.isPresent()) {
            MyLogger.getLogger().warning(this.getName() + " does not support creation of OwnDesire.WithAbstractIntention instance for desire key: " + key.getName());
            throw new IllegalArgumentException(this.getName() + " does not support creation of OwnDesire.WithAbstractIntention instance for desire key: " + key.getName());
        }
        return withAbstractIntention.get();
    }

    /**
     * Create instance of desire
     *
     * @param key
     * @param memory
     * @return
     */
    public OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey key, WorkingMemory memory) {
        Optional<OwnDesire.WithAbstractIntention> withAbstractIntention = ownDesireWithAbstractIntentionFormulation.formDesire(key, memory);
        if (!withAbstractIntention.isPresent()) {
            MyLogger.getLogger().warning(this.getName() + " does not support creation of OwnDesire.WithAbstractIntention instance for desire key: " + key.getName());
            throw new IllegalArgumentException(this.getName() + " does not support creation of OwnDesire.WithAbstractIntention instance for desire key: " + key.getName());
        }
        return withAbstractIntention.get();
    }

    /**
     * Create instance of desire
     *
     * @param parentKey
     * @param key
     * @param memory
     * @return
     */
    public OwnDesire.Acting formOwnActingDesire(DesireKey parentKey, DesireKey key, WorkingMemory memory, DesireParameters parentsDesireParameters) {
        Optional<OwnDesire.Acting> acting = ownDesireWithIntentionWithActingCommandFormulation.formDesire(parentKey, key, memory, parentsDesireParameters);
        if (!acting.isPresent()) {
            MyLogger.getLogger().warning(this.getName() + " does not support creation of OwnDesire.Acting instance for desire key: " + key.getName());
            throw new IllegalArgumentException(this.getName() + " does not support creation of OwnDesire.Acting instance for desire key: " + key.getName());
        }
        return acting.get();
    }

    /**
     * Create instance of desire
     *
     * @param key
     * @param memory
     * @return
     */
    public OwnDesire.Acting formOwnActingDesire(DesireKey key, WorkingMemory memory) {
        Optional<OwnDesire.Acting> acting = ownDesireWithIntentionWithActingCommandFormulation.formDesire(key, memory);
        if (!acting.isPresent()) {
            MyLogger.getLogger().warning(this.getName() + " does not support creation of OwnDesire.Acting instance for desire key: " + key.getName());
            throw new IllegalArgumentException(this.getName() + " does not support creation of OwnDesire.Acting instance for desire key: " + key.getName());
        }
        return acting.get();
    }

    /**
     * Create instance of desire
     *
     * @param parentKey
     * @param key
     * @param memory
     * @return
     */
    public OwnDesire.Reasoning formOwnReasoningDesire(DesireKey parentKey, DesireKey key, WorkingMemory memory, DesireParameters parentsDesireParameters) {
        Optional<OwnDesire.Reasoning> reasoning = ownDesireWithIntentionWithReasoningCommandFormulation.formDesire(parentKey, key, memory, parentsDesireParameters);
        if (!reasoning.isPresent()) {
            MyLogger.getLogger().warning(this.getName() + " does not support creation of OwnDesire.Reasoning instance for desire key: " + key.getName());
            throw new IllegalArgumentException(this.getName() + " does not support creation of OwnDesire.Reasoning instance for desire key: " + key.getName());
        }
        return reasoning.get();
    }

    /**
     * Create instance of desire
     *
     * @param key
     * @param memory
     * @return
     */
    public OwnDesire.Reasoning formOwnReasoningDesire(DesireKey key, WorkingMemory memory) {
        Optional<OwnDesire.Reasoning> reasoning = ownDesireWithIntentionWithReasoningCommandFormulation.formDesire(key, memory);
        if (!reasoning.isPresent()) {
            MyLogger.getLogger().warning(this.getName() + " does not support creation of OwnDesire.Reasoning instance for desire key: " + key.getName());
            throw new IllegalArgumentException(this.getName() + " does not support creation of OwnDesire.Reasoning instance for desire key: " + key.getName());
        }
        return reasoning.get();
    }

    /**
     * Create instance of desire
     *
     * @param parentKey
     * @param key
     * @param memory
     * @return
     */
    public DesireForOthers formDesireForOthers(DesireKey parentKey, DesireKey key, WorkingMemory memory, DesireParameters parentsDesireParameters) {
        Optional<DesireForOthers> desireForOthers = ownDesireWithSharedDesireFormulation.formDesire(parentKey, key, memory, parentsDesireParameters);
        if (!desireForOthers.isPresent()) {
            MyLogger.getLogger().warning(this.getName() + " does not support creation of DesireForOthers instance for desire key: " + key.getName());
            throw new IllegalArgumentException(this.getName() + " does not support creation of DesireForOthers instance for desire key: " + key.getName());
        }
        return desireForOthers.get();
    }

    /**
     * Create instance of desire
     *
     * @param key
     * @param memory
     * @return
     */
    public DesireForOthers formDesireForOthers(DesireKey key, WorkingMemory memory) {
        Optional<DesireForOthers> desireForOthers = ownDesireWithSharedDesireFormulation.formDesire(key, memory);
        if (!desireForOthers.isPresent()) {
            MyLogger.getLogger().warning(this.getName() + " does not support creation of DesireForOthers instance for desire key: " + key.getName());
            throw new IllegalArgumentException(this.getName() + " does not support creation of DesireForOthers instance for desire key: " + key.getName());
        }
        return desireForOthers.get();
    }

    /**
     * Add configuration for desire with abstract plan
     *
     * @param key
     * @param configuration
     * @param isForSelf
     */
    public void addConfiguration(DesireKey key, ConfigurationWithAbstractPlan configuration, boolean isForSelf) {
        if (isConfigurationInitialized) {
            MyLogger.getLogger().warning("Cannot add new configuration to initialized type.");
            throw new RuntimeException("Cannot add new configuration to initialized type.");
        }
        if (isForSelf) {
            ownDesireWithAbstractIntentionFormulation.addDesireFormulationConfiguration(key, configuration);
        } else {
            anotherAgentsDesireWithAbstractIntentionFormulation.addDesireFormulationConfiguration(key, configuration);
        }
    }

    /**
     * Add configuration for desire with abstract plan with parent
     *
     * @param key
     * @param parent
     * @param configuration
     */
    public void addConfiguration(DesireKey key, DesireKey parent, ConfigurationWithAbstractPlan configuration) {
        if (isConfigurationInitialized) {
            MyLogger.getLogger().warning("Cannot add new configuration to initialized type.");
            throw new RuntimeException("Cannot add new configuration to initialized type.");
        }
        ownDesireWithAbstractIntentionFormulation.addDesireFormulationConfiguration(parent, key, configuration);
    }

    /**
     * Add configuration for desire to act
     *
     * @param key
     * @param configuration
     */
    public void addConfiguration(DesireKey key, ConfigurationWithCommand.WithActingCommandDesiredBySelf configuration) {
        if (isConfigurationInitialized) {
            MyLogger.getLogger().warning("Cannot add new configuration to initialized type.");
            throw new RuntimeException("Cannot add new configuration to initialized type.");
        }
        ownDesireWithIntentionWithActingCommandFormulation.addDesireFormulationConfiguration(key, configuration);
    }

    /**
     * Add configuration for desire to act with parent
     *
     * @param key
     * @param parent
     * @param configuration
     */
    public void addConfiguration(DesireKey key, DesireKey parent, ConfigurationWithCommand.WithActingCommandDesiredBySelf configuration) {
        if (isConfigurationInitialized) {
            MyLogger.getLogger().warning("Cannot add new configuration to initialized type.");
            throw new RuntimeException("Cannot add new configuration to initialized type.");
        }
        ownDesireWithIntentionWithActingCommandFormulation.addDesireFormulationConfiguration(parent, key, configuration);
    }

    /**
     * Add configuration for desire to reason
     *
     * @param key
     * @param configuration
     */
    public void addConfiguration(DesireKey key, ConfigurationWithCommand.WithReasoningCommandDesiredBySelf configuration) {
        if (isConfigurationInitialized) {
            MyLogger.getLogger().warning("Cannot add new configuration to initialized type.");
            throw new RuntimeException("Cannot add new configuration to initialized type.");
        }
        ownDesireWithIntentionWithReasoningCommandFormulation.addDesireFormulationConfiguration(key, configuration);
    }

    /**
     * Add configuration for desire to reason with parent
     *
     * @param key
     * @param parent
     * @param configuration
     */
    public void addConfiguration(DesireKey key, DesireKey parent, ConfigurationWithCommand.WithReasoningCommandDesiredBySelf configuration) {
        if (isConfigurationInitialized) {
            MyLogger.getLogger().warning("Cannot add new configuration to initialized type.");
            throw new RuntimeException("Cannot add new configuration to initialized type.");
        }
        ownDesireWithIntentionWithReasoningCommandFormulation.addDesireFormulationConfiguration(parent, key, configuration);
    }

    /**
     * Add configuration for desire to act from another agent
     *
     * @param key
     * @param configuration
     */
    public void addConfiguration(DesireKey key, ConfigurationWithCommand.WithActingCommandDesiredByOtherAgent configuration) {
        if (isConfigurationInitialized) {
            MyLogger.getLogger().warning("Cannot add new configuration to initialized type.");
            throw new RuntimeException("Cannot add new configuration to initialized type.");
        }
        anotherAgentsDesireWithIntentionWithActingCommandFormulation.addDesireFormulationConfiguration(key, configuration);
    }

    /**
     * Add configuration for desire to share desire
     *
     * @param key
     * @param configuration
     */
    public void addConfiguration(DesireKey key, ConfigurationWithSharedDesire configuration) {
        if (isConfigurationInitialized) {
            MyLogger.getLogger().warning("Cannot add new configuration to initialized type.");
            throw new RuntimeException("Cannot add new configuration to initialized type.");
        }
        ownDesireWithSharedDesireFormulation.addDesireFormulationConfiguration(key, configuration);
    }

    /**
     * Add configuration for desire to share desire with parent
     *
     * @param key
     * @param parent
     * @param configuration
     */
    public void addConfiguration(DesireKey key, DesireKey parent, ConfigurationWithSharedDesire configuration) {
        if (isConfigurationInitialized) {
            MyLogger.getLogger().warning("Cannot add new configuration to initialized type.");
            throw new RuntimeException("Cannot add new configuration to initialized type.");
        }
        ownDesireWithSharedDesireFormulation.addDesireFormulationConfiguration(parent, key, configuration);
    }

    /**
     * Returns plan as set of desires for others to commit to
     *
     * @return
     */
    public Set<DesireKey> returnPlanAsSetOfDesiresForOthers() {
        return desiresForOthers;
    }

    /**
     * Returns plan as set of own desires with abstract intention
     *
     * @return
     */
    public Set<DesireKey> returnPlanAsSetOfDesiresWithAbstractIntention() {
        return desiresWithAbstractIntention;
    }

    /**
     * Returns plan as set of own desires with intention with act command
     *
     * @return
     */
    public Set<DesireKey> returnPlanAsSetOfDesiresWithIntentionToAct() {
        return desiresWithIntentionToAct;
    }

    /**
     * Returns plan as set of own desires with intention with reason command
     *
     * @return
     */
    public Set<DesireKey> returnPlanAsSetOfDesiresWithIntentionToReason() {
        return desiresWithIntentionToReason;
    }

    public Set<DesireKey> getSupportedDesiresOfOtherAgents() {
        return supportedDesiresOfOtherAgents;
    }

    /**
     * This contract is used to fill data structures with configuration using protected methods "addConfiguration". Method
     * is called only one time in constructor. One should avoid adding additional configuration outside of this method
     * as other structures to initialize agent memory are initialize only once after calling this method.
     */
    public interface ConfigurationInitializationStrategy {

        /**
         * Add configuration to type
         *
         * @param type
         */
        void initializeConfiguration(AgentType type);
    }

    //builder with default fields
    public static class AgentTypeBuilder {
        private Set<DesireKey> desiresForOthers = new HashSet<>();
        private Set<DesireKey> desiresWithAbstractIntention = new HashSet<>();
        private Set<DesireKey> desiresWithIntentionToAct = new HashSet<>();
        private Set<DesireKey> desiresWithIntentionToReason = new HashSet<>();
        private Set<FactKey<?>> usingTypesForFacts = new HashSet<>();
        private Set<FactKey<?>> usingTypesForFactSets = new HashSet<>();
    }

}
