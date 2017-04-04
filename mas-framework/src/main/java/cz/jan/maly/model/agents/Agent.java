package cz.jan.maly.model.agents;

import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.metadata.AgentTypeMakingObservations;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.planing.OwnDesire;
import cz.jan.maly.model.planing.SharedDesireForAgents;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ObservingCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;
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
 * Template for agent. Main routine of agent runs in its own thread.
 * Created by Jan on 09-Feb-17.
 */
public abstract class Agent<E extends AgentType> implements AgentTypeBehaviourFactory, ResponseReceiverInterface<Boolean>, Runnable {

    //lock
    final Object lockMonitor = new Object();

    @Getter
    private final int id;

    @Getter
    protected final E agentType;

    @Getter
    private final DesireMediator desireMediator;
    @Getter
    private final KnowledgeMediator knowledgeMediator;

    protected final WorkingMemory beliefs;
    private final Tree tree = new Tree(this);
    private final CommandExecutor commandExecutor = new CommandExecutor(tree, this);
    private final CommitmentDecider commitmentDecider = new CommitmentDecider(tree);
    private final CommitmentRemovalDecider commitmentRemovalDecider = new CommitmentRemovalDecider(tree);

    //to handle main routine of agent
    private boolean isAlive = true;
    private final Object isAliveLockMonitor = new Object();

    protected Agent(E agentType, MASFacade masFacade) {
        this.id = masFacade.getAgentsRegister().getFreeId();
        this.desireMediator = masFacade.getDesireMediator();
        this.knowledgeMediator = masFacade.getKnowledgeMediator();
        this.agentType = agentType;
        this.beliefs = new WorkingMemory(tree, this.agentType, this.id);
    }

    @Override
    public void run() {

        //run main routine in its own thread
        Worker worker = new Worker();
        worker.start();
        MyLogger.getLogger().info("Agent has started.");
    }

    @Override
    public void receiveResponse(Boolean response) {
        //agent is removed from desire register
    }

    void doRoutine(Worker worker) {
        shareKnowledge(worker);
    }

    void shareKnowledge(Worker worker) {
        if (knowledgeMediator.registerKnowledge(beliefs.cloneMemory(), this, worker)) {
            synchronized (lockMonitor) {
                try {
                    lockMonitor.wait();
                } catch (InterruptedException e) {
                    MyLogger.getLogger().warning(worker.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
                }
            }
        }
        beliefs.addKnowledge(knowledgeMediator.getSnapshotOfRegister());
    }

    private void removeAgent() {
        desireMediator.removeAgentFromRegister(this, this);
        tree.removeCommitmentToSharedDesires();
    }

    /**
     * Worker execute workflow of this agent.
     */
    class Worker extends Thread implements ResponseReceiverInterface<Boolean> {

        @Override
        public void run() {

            //init agent
            doRoutine(this);
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
                doRoutine(this);
                tree.updateDesires(desireMediator.getSnapshotOfRegister());
            }

            removeAgent();
        }

        @Override
        public void receiveResponse(Boolean response) {

            //notify waiting method
            synchronized (lockMonitor) {
                if (!response) {
                    MyLogger.getLogger().warning(this.getClass().getSimpleName() + " could not execute command");
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
    public void executeCommand(ReasoningCommand command) {
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
        return agentType.formAnotherAgentsDesireWithAbstractIntention(desireForAgents, beliefs);
    }

    @Override
    public Optional<DesireFromAnotherAgent.WithIntentionWithPlan> formDesireFromOtherAgentWithIntentionWithPlan(SharedDesireForAgents desireForAgents) {
        return agentType.formAnotherAgentsDesireWithCommand(desireForAgents, beliefs);
    }

    /**
     * Method to be called when one want to terminate agent
     */
    public void terminateAgent() {
        synchronized (isAliveLockMonitor) {
            this.isAlive = false;
        }
    }

    /**
     * Extension of agent which also makes observations
     *
     * @param <E>
     */
    public static abstract class MakingObservation<E> extends Agent<AgentTypeMakingObservations<E>> {
        protected MakingObservation(AgentTypeMakingObservations<E> agentType, MASFacade masFacade) {
            super(agentType, masFacade);
        }

        /**
         * Execute observing command
         *
         * @param observingCommand
         * @param responseReceiver
         */
        protected abstract boolean requestObservation(ObservingCommand<E> observingCommand, ResponseReceiverInterface<Boolean> responseReceiver);

        private void makeObservation(Worker worker) {
            if (requestObservation(agentType.getObservingCommand(), worker)) {
                synchronized (lockMonitor) {
                    try {
                        lockMonitor.wait();
                    } catch (InterruptedException e) {
                        MyLogger.getLogger().warning(worker.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
                    }
                }
            }
        }

        @Override
        void doRoutine(Worker worker) {
            makeObservation(worker);
            shareKnowledge(worker);
        }

    }

}
