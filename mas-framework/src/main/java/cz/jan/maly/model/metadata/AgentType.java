package cz.jan.maly.model.metadata;

import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.metadata.agents.*;
import cz.jan.maly.model.planing.*;
import cz.jan.maly.model.planing.command.ActCommandForIntention;
import cz.jan.maly.model.planing.command.ObservingCommand;
import cz.jan.maly.model.planing.command.ReasoningCommandForIntention;
import lombok.Getter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Class describing metadata for agent type - used for identification and parameter type definition - fact types and
 * fact set types in memory (internal beliefs) as well as supported (implemented) desires by agent type and ways of their
 * creation using desire key
 * Created by Jan on 15-Feb-17.
 */
public abstract class AgentType<E> extends Key {

    @Getter
    private final ObservingCommand<E> observingCommand;

    //initial desires for this agent type
    private final Set<DesireKey> desiresForOthers;
    private final Set<DesireKey> desiresWithAbstractIntention;
    private final Set<DesireKey> desiresWithIntentionToAct;
    private final Set<DesireKey> desiresWithIntentionToReason;

    private final Set<DesireKey> supportedDesiresOfOtherAgents = new HashSet<>();

    @Getter
    private final Set<FactKey<?>> usingTypesForFacts = new HashSet<>();

    @Getter
    private final Set<FactKey<?>> usingTypesForFactSets = new HashSet<>();

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

    protected AgentType(String name, ObservingCommand<E> observingCommand, Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention, Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason) {
        super(name, AgentType.class);
        this.observingCommand = observingCommand;
        this.desiresForOthers = desiresForOthers;
        this.desiresWithAbstractIntention = desiresWithAbstractIntention;
        this.desiresWithIntentionToAct = desiresWithIntentionToAct;
        this.desiresWithIntentionToReason = desiresWithIntentionToReason;

        //initialize configuration first, then get all facts required for correct behaviour to be present in agent
        initializeConfiguration();

        //desires for other agents
        supportedDesiresOfOtherAgents.addAll(anotherAgentsDesireWithAbstractIntentionFormulation.supportedDesireTypes());
        supportedDesiresOfOtherAgents.addAll(anotherAgentsDesireWithIntentionWithActingCommandFormulation.supportedDesireTypes());

        //what facts need to be in memory
        usingTypesForFacts.addAll(ownDesireWithAbstractIntentionFormulation.getRequiredFactsToSupportFormulation());
        usingTypesForFacts.addAll(ownDesireWithAbstractIntentionFormulation.getRequiredFactsToSupportFormulationInStack());
        usingTypesForFacts.addAll(ownDesireWithIntentionWithActingCommandFormulation.getRequiredFactsToSupportFormulation());
        usingTypesForFacts.addAll(ownDesireWithIntentionWithActingCommandFormulation.getRequiredFactsToSupportFormulationInStack());
        usingTypesForFacts.addAll(ownDesireWithIntentionWithReasoningCommandFormulation.getRequiredFactsToSupportFormulation());
        usingTypesForFacts.addAll(ownDesireWithIntentionWithReasoningCommandFormulation.getRequiredFactsToSupportFormulationInStack());
        usingTypesForFacts.addAll(ownDesireWithSharedDesireFormulation.getRequiredFactsToSupportFormulation());
        usingTypesForFacts.addAll(ownDesireWithSharedDesireFormulation.getRequiredFactsToSupportFormulationInStack());
        usingTypesForFacts.addAll(anotherAgentsDesireWithAbstractIntentionFormulation.getRequiredFactsToSupportFormulation());
        usingTypesForFacts.addAll(anotherAgentsDesireWithIntentionWithActingCommandFormulation.getRequiredFactsToSupportFormulation());

        usingTypesForFactSets.addAll(ownDesireWithAbstractIntentionFormulation.getRequiredFactsSetsToSupportFormulation());
        usingTypesForFactSets.addAll(ownDesireWithAbstractIntentionFormulation.getRequiredFactsSetsToSupportFormulationInStack());
        usingTypesForFactSets.addAll(ownDesireWithIntentionWithActingCommandFormulation.getRequiredFactsSetsToSupportFormulation());
        usingTypesForFactSets.addAll(ownDesireWithIntentionWithActingCommandFormulation.getRequiredFactsSetsToSupportFormulationInStack());
        usingTypesForFactSets.addAll(ownDesireWithIntentionWithReasoningCommandFormulation.getRequiredFactsSetsToSupportFormulation());
        usingTypesForFactSets.addAll(ownDesireWithIntentionWithReasoningCommandFormulation.getRequiredFactsSetsToSupportFormulationInStack());
        usingTypesForFactSets.addAll(ownDesireWithSharedDesireFormulation.getRequiredFactsSetsToSupportFormulation());
        usingTypesForFactSets.addAll(ownDesireWithSharedDesireFormulation.getRequiredFactsSetsToSupportFormulationInStack());
        usingTypesForFactSets.addAll(anotherAgentsDesireWithAbstractIntentionFormulation.getRequiredFactsSetsToSupportFormulation());
        usingTypesForFactSets.addAll(anotherAgentsDesireWithIntentionWithActingCommandFormulation.getRequiredFactsSetsToSupportFormulation());

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

    private void checkSupport(Set<DesireKey> keysToSupport, DesireFormulation desireFormulation) {
        Optional<DesireKey> first = keysToSupport.stream()
                .filter(key -> !desireFormulation.supportsDesireType(key))
                .findAny();
        if (first.isPresent()) {
            throw new IllegalArgumentException(first.get().getName() + " can be instantiated in abstract plan for " + getName());
        }
    }

    public Optional<DesireFromAnotherAgent.WithAbstractIntention> formAnotherAgentsDesireWithAbstractIntention(SharedDesireForAgents desireForAgents) {
        return anotherAgentsDesireWithAbstractIntentionFormulation.formDesire(desireForAgents);
    }

    public Optional<DesireFromAnotherAgent.WithIntentionWithPlan> formAnotherAgentsDesireWithCommand(SharedDesireForAgents desireForAgents) {
        return anotherAgentsDesireWithIntentionWithActingCommandFormulation.formDesire(desireForAgents);
    }

    public OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey parentKey, DesireKey key, Memory memory) {
        Optional<OwnDesire.WithAbstractIntention> withAbstractIntention = ownDesireWithAbstractIntentionFormulation.formDesire(parentKey, key, memory);
        if (!withAbstractIntention.isPresent()) {
            throw new IllegalArgumentException(this.getName() + " does not support creation of OwnDesire.WithAbstractIntention instance for desire key: " + key.getName());
        }
        return withAbstractIntention.get();
    }

    public OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey key, Memory memory) {
        Optional<OwnDesire.WithAbstractIntention> withAbstractIntention = ownDesireWithAbstractIntentionFormulation.formDesire(key, memory);
        if (!withAbstractIntention.isPresent()) {
            throw new IllegalArgumentException(this.getName() + " does not support creation of OwnDesire.WithAbstractIntention instance for desire key: " + key.getName());
        }
        return withAbstractIntention.get();
    }

    public OwnDesire.Acting formOwnActingDesire(DesireKey parentKey, DesireKey key, Memory memory) {
        Optional<OwnDesire.Acting> acting = ownDesireWithIntentionWithActingCommandFormulation.formDesire(parentKey, key, memory);
        if (!acting.isPresent()) {
            throw new IllegalArgumentException(this.getName() + " does not support creation of OwnDesire.Acting instance for desire key: " + key.getName());
        }
        return acting.get();
    }

    public OwnDesire.Acting formOwnActingDesire(DesireKey key, Memory memory) {
        Optional<OwnDesire.Acting> acting = ownDesireWithIntentionWithActingCommandFormulation.formDesire(key, memory);
        if (!acting.isPresent()) {
            throw new IllegalArgumentException(this.getName() + " does not support creation of OwnDesire.Acting instance for desire key: " + key.getName());
        }
        return acting.get();
    }

    public OwnDesire.Reasoning formOwnReasoningDesire(DesireKey parentKey, DesireKey key, Memory memory) {
        Optional<OwnDesire.Reasoning> reasoning = ownDesireWithIntentionWithReasoningCommandFormulation.formDesire(parentKey, key, memory);
        if (!reasoning.isPresent()) {
            throw new IllegalArgumentException(this.getName() + " does not support creation of OwnDesire.Reasoning instance for desire key: " + key.getName());
        }
        return reasoning.get();
    }

    public OwnDesire.Reasoning formOwnReasoningDesire(DesireKey key, Memory memory) {
        Optional<OwnDesire.Reasoning> reasoning = ownDesireWithIntentionWithReasoningCommandFormulation.formDesire(key, memory);
        if (!reasoning.isPresent()) {
            throw new IllegalArgumentException(this.getName() + " does not support creation of OwnDesire.Reasoning instance for desire key: " + key.getName());
        }
        return reasoning.get();
    }

    public DesireForOthers formDesireForOthers(DesireKey parentKey, DesireKey key, Memory memory) {
        Optional<DesireForOthers> desireForOthers = ownDesireWithSharedDesireFormulation.formDesire(parentKey, key, memory);
        if (!desireForOthers.isPresent()) {
            throw new IllegalArgumentException(this.getName() + " does not support creation of DesireForOthers instance for desire key: " + key.getName());
        }
        return desireForOthers.get();
    }

    public DesireForOthers formDesireForOthers(DesireKey key, Memory memory) {
        Optional<DesireForOthers> desireForOthers = ownDesireWithSharedDesireFormulation.formDesire(key, memory);
        if (!desireForOthers.isPresent()) {
            throw new IllegalArgumentException(this.getName() + " does not support creation of DesireForOthers instance for desire key: " + key.getName());
        }
        return desireForOthers.get();
    }

    /**
     * This method is used to fill data structures with configuration using protected methods "addConfiguration". Method
     * is called only one time in constructor. One should avoid adding additional configuration outside of this method
     * as other structures to initialize agent memory are initialize only once after calling this method.
     */
    protected abstract void initializeConfiguration();

    /**
     * Add configuration for desire creation for another agent desire
     *
     * @param key
     * @param decisionParametersForDesire
     * @param decisionInDesire
     * @param decisionParametersForIntention
     * @param intentionParameters
     * @param decisionInIntention
     * @param desiresForOthers
     * @param desiresWithAbstractIntention
     * @param desiresWithIntentionToAct
     * @param desiresWithIntentionToReason
     */
    protected void addConfigurationForAnotherAgentsDesireWithAbstractIntentionFormulation(DesireKey key, DecisionParameters decisionParametersForDesire,
                                                                                          Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                                                          RemoveCommitment decisionInIntention, IntentionParameters intentionParameters,
                                                                                          Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention,
                                                                                          Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason) {
        anotherAgentsDesireWithAbstractIntentionFormulation.addDesireFormulationConfiguration(key,
                decisionParametersForDesire, decisionInDesire, decisionParametersForIntention,
                decisionInIntention, intentionParameters, desiresForOthers, desiresWithAbstractIntention,
                desiresWithIntentionToAct, desiresWithIntentionToReason);
    }

    /**
     * Add configuration for own desire creation
     *
     * @param key
     * @param decisionParametersForDesire
     * @param decisionInDesire
     * @param decisionParametersForIntention
     * @param intentionParameters
     * @param decisionInIntention
     * @param desiresForOthers
     * @param desiresWithAbstractIntention
     * @param desiresWithIntentionToAct
     * @param desiresWithIntentionToReason
     */
    protected void addConfigurationForOwnDesireDesireWithAbstractIntentionFormulation(DesireKey key, DecisionParameters decisionParametersForDesire,
                                                                                      Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                                                      RemoveCommitment decisionInIntention, IntentionParameters intentionParameters,
                                                                                      Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention,
                                                                                      Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason) {
        ownDesireWithAbstractIntentionFormulation.addDesireFormulationConfiguration(key,
                decisionParametersForDesire, decisionInDesire, decisionParametersForIntention,
                decisionInIntention, intentionParameters, desiresForOthers, desiresWithAbstractIntention,
                desiresWithIntentionToAct, desiresWithIntentionToReason);
    }

    /**
     * Add configuration for own desire creation considering parent
     *
     * @param key
     * @param parent
     * @param decisionParametersForDesire
     * @param decisionInDesire
     * @param decisionParametersForIntention
     * @param intentionParameters
     * @param decisionInIntention
     * @param desiresForOthers
     * @param desiresWithAbstractIntention
     * @param desiresWithIntentionToAct
     * @param desiresWithIntentionToReason
     */
    protected void addDesireFormulationConfiguration(DesireKey parent, DesireKey key, DecisionParameters decisionParametersForDesire,
                                                     Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                     RemoveCommitment decisionInIntention, IntentionParameters intentionParameters,
                                                     Set<DesireKey> desiresForOthers, Set<DesireKey> desiresWithAbstractIntention,
                                                     Set<DesireKey> desiresWithIntentionToAct, Set<DesireKey> desiresWithIntentionToReason) {
        ownDesireWithAbstractIntentionFormulation.addDesireFormulationConfiguration(parent, key,
                decisionParametersForDesire, decisionInDesire, decisionParametersForIntention,
                decisionInIntention, intentionParameters, desiresForOthers, desiresWithAbstractIntention,
                desiresWithIntentionToAct, desiresWithIntentionToReason);
    }

    /**
     * Add configuration for own desire with acting command
     *
     * @param key
     * @param decisionParametersForDesire
     * @param decisionInDesire
     * @param decisionParametersForIntention
     * @param intentionParameters
     * @param decisionInIntention
     * @param command
     */
    protected void addDesireFormulationConfigurationForOwnDesireWithActingCommand(DesireKey key, DecisionParameters decisionParametersForDesire,
                                                                                  Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                                                  RemoveCommitment decisionInIntention, IntentionParameters intentionParameters,
                                                                                  ActCommandForIntention.Own command) {
        ownDesireWithIntentionWithActingCommandFormulation.addDesireFormulationConfiguration(key, decisionParametersForDesire,
                decisionInDesire, decisionParametersForIntention, decisionInIntention, intentionParameters, command);
    }

    /**
     * Add configuration for own desire with acting command. When creating consider parent
     *
     * @param key
     * @param parent
     * @param decisionParametersForDesire
     * @param decisionInDesire
     * @param decisionParametersForIntention
     * @param intentionParameters
     * @param decisionInIntention
     * @param command
     */
    protected void addDesireFormulationConfigurationForOwnDesireWithActingCommandStacked(DesireKey parent, DesireKey key, DecisionParameters decisionParametersForDesire,
                                                                                         Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                                                         RemoveCommitment decisionInIntention, IntentionParameters intentionParameters, ActCommandForIntention.Own command) {
        ownDesireWithIntentionWithActingCommandFormulation.addDesireFormulationConfiguration(parent, key, decisionParametersForDesire,
                decisionInDesire, decisionParametersForIntention, decisionInIntention, intentionParameters, command);
    }

    /**
     * Add configuration for own desire with reasoning command
     *
     * @param key
     * @param decisionParametersForDesire
     * @param decisionInDesire
     * @param decisionParametersForIntention
     * @param intentionParameters
     * @param decisionInIntention
     * @param command
     */
    protected void addDesireFormulationConfigurationForOwnDesireWithReasoningCommand(DesireKey key, DecisionParameters decisionParametersForDesire,
                                                                                     Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                                                     RemoveCommitment decisionInIntention, IntentionParameters intentionParameters,
                                                                                     ReasoningCommandForIntention command) {
        ownDesireWithIntentionWithReasoningCommandFormulation.addDesireFormulationConfiguration(key, decisionParametersForDesire,
                decisionInDesire, decisionParametersForIntention, decisionInIntention, intentionParameters, command);
    }

    /**
     * Add configuration for own desire with reasoning command. When creating consider parent
     *
     * @param key
     * @param parent
     * @param decisionParametersForDesire
     * @param decisionInDesire
     * @param decisionParametersForIntention
     * @param intentionParameters
     * @param decisionInIntention
     * @param command
     */
    protected void addDesireFormulationConfigurationForOwnDesireWithReasoningCommandStacked(DesireKey parent, DesireKey key, DecisionParameters decisionParametersForDesire,
                                                                                            Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                                                            RemoveCommitment decisionInIntention, IntentionParameters intentionParameters, ReasoningCommandForIntention command) {
        ownDesireWithIntentionWithReasoningCommandFormulation.addDesireFormulationConfiguration(parent, key, decisionParametersForDesire,
                decisionInDesire, decisionParametersForIntention, decisionInIntention, intentionParameters, command);
    }

    /**
     * Add configuration for another agent's desire with acting command
     *
     * @param key
     * @param decisionParametersForDesire
     * @param decisionInDesire
     * @param decisionParametersForIntention
     * @param intentionParameters
     * @param decisionInIntention
     * @param command
     */
    protected void addDesireFormulationConfigurationForAnotherAgentsDesireWithActingCommand(DesireKey key, DecisionParameters decisionParametersForDesire,
                                                                                            Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                                                            RemoveCommitment decisionInIntention, IntentionParameters intentionParameters,
                                                                                            ActCommandForIntention.DesiredByAnotherAgent command) {
        anotherAgentsDesireWithIntentionWithActingCommandFormulation.addDesireFormulationConfiguration(key, decisionParametersForDesire,
                decisionInDesire, decisionParametersForIntention, decisionInIntention, intentionParameters, command);
    }

    /**
     * Add configuration for desire with shared desire
     *
     * @param key
     * @param decisionParametersForDesire
     * @param decisionInDesire
     * @param decisionParametersForIntention
     * @param intentionParameters
     * @param decisionInIntention
     * @param sharedDesireKey
     * @param counts
     */
    protected void addDesireFormulationConfigurationWithSharedDesire(DesireKey key, DecisionParameters decisionParametersForDesire,
                                                                     Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                                     RemoveCommitment decisionInIntention, IntentionParameters intentionParameters,
                                                                     DesireKey sharedDesireKey, int counts) {
        ownDesireWithSharedDesireFormulation.addDesireFormulationConfiguration(key, decisionParametersForDesire,
                decisionInDesire, decisionParametersForIntention, decisionInIntention, intentionParameters, sharedDesireKey, counts);
    }

    /**
     * Add configuration for desire
     *
     * @param key
     * @param parent
     * @param decisionParametersForDesire
     * @param decisionInDesire
     * @param decisionParametersForIntention
     * @param intentionParameters
     * @param decisionInIntention
     * @param sharedDesireKey
     * @param counts
     */
    protected void addDesireFormulationConfigurationWithSharedDesireStacked(DesireKey parent, DesireKey key, DecisionParameters decisionParametersForDesire,
                                                                            Commitment decisionInDesire, DecisionParameters decisionParametersForIntention,
                                                                            RemoveCommitment decisionInIntention, IntentionParameters intentionParameters,
                                                                            DesireKey sharedDesireKey, int counts) {
        ownDesireWithSharedDesireFormulation.addDesireFormulationConfiguration(parent, key, decisionParametersForDesire,
                decisionInDesire, decisionParametersForIntention, decisionInIntention, intentionParameters, sharedDesireKey, counts);
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
}
