package cz.jan.maly.model.agents;

import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.*;
import cz.jan.maly.model.planing.command.ActCommandForIntention;
import cz.jan.maly.model.planing.command.ObservingCommand;
import cz.jan.maly.model.planing.command.ReasoningCommandForIntention;
import cz.jan.maly.model.planing.tree.Tree;
import cz.jan.maly.model.planing.tree.visitors.CommandExecutor;
import cz.jan.maly.model.planing.tree.visitors.CommitmentDecider;
import cz.jan.maly.model.planing.tree.visitors.CommitmentRemovalDecider;
import cz.jan.maly.service.MASFacade;
import cz.jan.maly.service.implementation.DesireMediator;
import cz.jan.maly.service.implementation.KnowledgeMediator;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

import java.util.Optional;

/**
 * Created by Jan on 09-Feb-17.
 */
public abstract class Agent<E> implements AgentTypeBehaviourFactory, ResponseReceiverInterface<Boolean> {

    @Getter
    private final int id;

    @Getter
    private final AgentType agentType;

    @Getter
    private final DesireMediator desireMediator;
    @Getter
    private final KnowledgeMediator knowledgeMediator;

    private final WorkingMemory beliefs;
    private final Tree tree = new Tree(this);
    private final CommandExecutor commandExecutor = new CommandExecutor(tree, this);
    private final CommitmentDecider commitmentDecider = new CommitmentDecider(tree, this);
    private final CommitmentRemovalDecider commitmentRemovalDecider = new CommitmentRemovalDecider(tree, this);
    private final ObservingCommand<E> observingCommand;

    //to handle main routine of agent
    private boolean isAlive = true;
    private final Object isAliveLockMonitor = new Object();

    protected Agent(AgentType agentType, ObservingCommand<E> observingCommand, MASFacade masFacade) {
        this.observingCommand = observingCommand;
        this.id = masFacade.getAgentsRegister().getFreeId();
        this.desireMediator = masFacade.getDesireMediator();
        this.knowledgeMediator = masFacade.getKnowledgeMediator();
        this.agentType = agentType;

        //todo init beliefs

        //run main routine in its own thread
        Worker worker = new Worker(this);
        worker.start();
    }

    @Override
    public void receiveResponse(Boolean response) {
        //agent is removed from desire register
    }

    /**
     * Worker execute workflow of this agent.
     */
    private class Worker extends Thread implements ResponseReceiverInterface<Boolean> {
        private final Agent agent;
        private final Object lockMonitor = new Object();

        private Worker(Agent agent) {
            this.agent = agent;
        }

        @Override
        public void run() {

            //init agent
            doRoutine();
            tree.initTopLevelDesires(desireMediator.getSnapshotOfRegister());

            while (true) {

                //check if agent is still alive
                synchronized (isAliveLockMonitor) {
                    if (!isAlive) {
                        break;
                    }
                }

                //execute routine
                commitmentDecider.visitTree();
                commandExecutor.visitTree();
                commitmentRemovalDecider.visitTree();
                doRoutine();
                tree.updateTopLevelDesires(desireMediator.getSnapshotOfRegister());
            }

            //remove agent
            desireMediator.removeAgentFromRegister(agent, agent);
            tree.removeCommitmentToSharedDesires();
        }

        private void doRoutine() {
            if (requestObservation(observingCommand, this)) {
                synchronized (lockMonitor) {
                    try {
                        lockMonitor.wait();
                    } catch (InterruptedException e) {
                        MyLogger.getLogger().warning(this.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
                    }
                }
            }
            //todo add shared knowledge
        }

        @Override
        public void receiveResponse(Boolean response) {

            //notify waiting method
            synchronized (lockMonitor) {
                if (!response) {
                    MyLogger.getLogger().warning(this.getClass().getSimpleName() + " could not execute observing command");
                }
                lockMonitor.notify();
            }
        }
    }

    /**
     * Execute reasoning command
     *
     * @param command
     */
    public void executeCommand(ReasoningCommandForIntention command) {
        if (!MASFacade.REASONING_EXECUTOR.executeCommand(command, beliefs)) {
            MyLogger.getLogger().warning(this.getClass().getSimpleName() + ", " + agentType.getName() + " could not execute reasoning command");
        }
    }

    /**
     * Execute acting command
     *
     * @param command
     * @param responseReceiver
     */
    public abstract boolean sendCommandToExecute(ActCommandForIntention<?> command, ResponseReceiverInterface<Boolean> responseReceiver);

    /**
     * Execute observing command
     *
     * @param observingCommand
     * @param responseReceiver
     */
    protected abstract boolean requestObservation(ObservingCommand<E> observingCommand, ResponseReceiverInterface<Boolean> responseReceiver);

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

    /**
     * Method to be called when one want to terminate agent
     */
    public void terminateAgent() {
        synchronized (isAliveLockMonitor) {
            this.isAlive = false;
        }
    }
}
