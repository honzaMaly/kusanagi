package cz.jan.maly.model.agents;

import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.planing.OwnDesire;
import cz.jan.maly.model.planing.SharedDesireForAgents;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;
import cz.jan.maly.model.planing.tree.Tree;
import cz.jan.maly.model.planing.tree.visitors.CommandExecutor;
import cz.jan.maly.model.planing.tree.visitors.CommitmentDecider;
import cz.jan.maly.model.planing.tree.visitors.CommitmentRemovalDecider;
import cz.jan.maly.service.implementation.AgentsRegister;
import cz.jan.maly.service.implementation.DesireMediator;
import cz.jan.maly.service.implementation.KnowledgeMediator;
import cz.jan.maly.service.implementation.ReasoningExecutor;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

import java.util.Optional;

/**
 * Created by Jan on 09-Feb-17.
 */
public abstract class Agent implements AgentTypeBehaviourFactory {

    //instance of reasoning manager, it can be shared by agents as it is stateless
    private static final ReasoningExecutor REASONING_EXECUTOR = new ReasoningExecutor();

    //register of agents - to assign ids to them
    private static final AgentsRegister AGENTS_REGISTER = new AgentsRegister();

    //shared desire mediator
    public static final DesireMediator DESIRE_MEDIATOR = new DesireMediator();

    //shared knowledge mediator
    private static final KnowledgeMediator KNOWLEDGE_MEDIATOR = new KnowledgeMediator();

    @Getter
    private final int id;

    @Getter
    private final AgentType agentType;

    private final Tree tree = new Tree(this);
    private final WorkingMemory beliefs;
    private final CommandExecutor commandExecutor = new CommandExecutor(tree, this);
    private final CommitmentDecider commitmentDecider = new CommitmentDecider(tree, this);
    private final CommitmentRemovalDecider commitmentRemovalDecider = new CommitmentRemovalDecider(tree, this);

    protected Agent(AgentType agentType) {
        this.id = AGENTS_REGISTER.getFreeId();
        this.agentType = agentType;

//        //init desires from types provided by user
//        getInitialOwnDesireWithAbstractIntentionTypes().forEach(desireKey -> tree.addDesire(formOwnDesireWithAbstractIntention(desireKey)));
//        getInitialOwnDesireWithIntentionWithPlanTypes().forEach(desireKey -> tree.addDesire(formOwnDesireWithIntentionWithPlan(desireKey)));
//        getDesireForOthersTypes().forEach(desireKey -> tree.addDesire(formDesireForOthers(desireKey)));
    }

    /**
     * Execute reasoning command
     *
     * @param command
     */
    public void executeCommand(ReasoningCommand command) {
        if (!REASONING_EXECUTOR.executeCommand(command, beliefs)) {
            MyLogger.getLogger().warning(this.getClass().getSimpleName() + ", " + agentType.getName() + " could not execute reasoning command");
        }
    }

    /**
     * Execute acting command
     *
     * @param command
     * @param responseReceiver
     */
    public abstract boolean sendCommandToExecute(ActCommand<?> command, ResponseReceiverInterface<Boolean> responseReceiver);

    /**
     * Get memory of agent
     *
     * @return
     */
    public Memory getBeliefs() {
        return beliefs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agent)) return false;

        Agent agent = (Agent) o;

        return id == agent.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey desireKey) {
        return agentType.formOwnDesireWithAbstractIntention(desireKey, beliefs);
    }

    @Override
    public OwnDesire.WithAbstractIntention formOwnDesireWithAbstractIntention(DesireKey desireKey, DesireKey parentDesireKey) {
        return agentType.formOwnDesireWithAbstractIntention(parentDesireKey, desireKey, beliefs);
    }

    @Override
    public OwnDesire.Reasoning formOwnDesireWithReasoningCommand(DesireKey desireKey) {
        return agentType.formOwnReasoningDesire(desireKey, beliefs);
    }

    @Override
    public OwnDesire.Reasoning formOwnDesireWithReasoningCommand(DesireKey desireKey, DesireKey parentDesireKey) {
        return agentType.formOwnReasoningDesire(parentDesireKey, desireKey, beliefs);
    }

    @Override
    public OwnDesire.Acting formOwnDesireWithActingCommand(DesireKey desireKey) {
        return agentType.formOwnActingDesire(desireKey, beliefs);
    }

    @Override
    public OwnDesire.Acting formOwnDesireWithActingCommand(DesireKey desireKey, DesireKey parentDesireKey) {
        return agentType.formOwnActingDesire(parentDesireKey, desireKey, beliefs);
    }

    @Override
    public DesireForOthers formDesireForOthers(DesireKey desireKey) {
        return agentType.formDesireForOthers(desireKey, beliefs);
    }

    @Override
    public DesireForOthers formDesireForOthers(DesireKey desireKey, DesireKey parentDesireKey) {
        return agentType.formDesireForOthers(parentDesireKey, desireKey, beliefs);
    }

    @Override
    public Optional<DesireFromAnotherAgent.WithAbstractIntention> formDesireFromOtherAgentWithAbstractIntention(SharedDesireForAgents desireForAgents) {
        return agentType.formAnotherAgentsDesireWithAbstractIntention(desireForAgents);
    }

    @Override
    public Optional<DesireFromAnotherAgent.WithIntentionWithPlan> formDesireFromOtherAgentWithIntentionWithPlan(SharedDesireForAgents desireForAgents) {
        return agentType.formAnotherAgentsDesireWithCommand(desireForAgents);
    }
}
