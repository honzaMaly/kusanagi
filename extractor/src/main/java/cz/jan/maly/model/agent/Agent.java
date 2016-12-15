package cz.jan.maly.model.agent;

import cz.jan.maly.service.AgentsManager;
import lombok.Getter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Agent abstraction class handles execution of main routine (in form of sequence) in agent.
 * Concrete agent has to describe creation of sequence of actions to execute, type of agent's knowledge and method to decided
 * if agent is still alive
 * Created by Jan on 09-Dec-16.
 */
public abstract class Agent {
    protected final AgentsKnowledge agentsKnowledge;
    private Boolean isAlive = true;
    protected final Set<Agent> agentsToNotifyInCaseOfTermination = new HashSet<>();

    /**
     * Starting action is actually root of the tree. This tree is executed by this agent from root to one of the leafs in each cycle.
     * Path depends on current knowledge. This kind of representation enable one for example to change behaviour when actual
     * race of opponent is discovered. This put emphasis on implementing simple action over action which performs different behaviour
     * given current knowledge
     */
    private final AgentActionCycleAbstract startingActionOfWorkflow;

    private final long timeBetweenCycles;
    private final Set<Agent> receivedNotificationFromAgents = new HashSet<>();
    private final Worker worker = new Worker();
    private Boolean hasStarted = false;

    protected Agent(AgentsKnowledge agentsKnowledge, long timeBetweenCycles) {
        this.agentsKnowledge = agentsKnowledge;
        this.timeBetweenCycles = timeBetweenCycles;
        this.startingActionOfWorkflow = composeWorkflow();
        AgentsManager.getInstance().addAgent(this);
    }

    public void receivedNotificationFromAgent(Agent agent){
        synchronized (receivedNotificationFromAgents){
            agentsToNotifyInCaseOfTermination.add(agent);
        }
    }

    public AgentsKnowledge getAgentsKnowledge() {
        return agentsKnowledge;
    }

    /**
     * Method to be called to activate agent
     */
    public void act(){
        synchronized (hasStarted){
            if (!hasStarted){
                hasStarted = true;
                worker.run();
            }
        }
    }

    /**
     * Worker execute workflow of this agent. It makes sure that worker sleeps for defined amount of time after
     * cycle was executed or is awaken sooner in case of receiving notification from another agent to act
     */
    private class Worker implements Runnable {
        private final Set<Agent> receivedNotificationsFrom = new HashSet<>();

        @Override
        public void run() {
            Optional<AgentActionCycleAbstract> nextAction = Optional.ofNullable(startingActionOfWorkflow);
            while (true) {
                synchronized (receivedNotificationFromAgents){
                    receivedNotificationsFrom.addAll(receivedNotificationFromAgents);
                    receivedNotificationFromAgents.clear();
                }

                //pick next action. if notification was received and next action is empty new cycle should start immediately
                boolean isNextActionLeaf = nextAction.get().isLeaf();
                nextAction = nextAction.get().executeAction(receivedNotificationsFrom);
                if (!nextAction.isPresent()){
                    receivedNotificationsFrom.clear();
                    nextAction = Optional.ofNullable(startingActionOfWorkflow);

                    //if cycle reached end naturally, sleep for time interval or till notification
                    if (isNextActionLeaf){

                        //todo thread for watch

                    }
                }
                synchronized (isAlive) {
                    if (!isAlive) {
                        removeAgent();
                        break;
                    }
                }
            }
        }

    }

    /**
     * This is crucial method to be implemented by each agent as it should return valid workflow for agent to execute
     *
     * @return
     */
    protected abstract AgentActionCycleAbstract composeWorkflow();

    /**
     * Another agent ask this instance to be notified in case of termination
     * @param agent
     * @return
     */
    public synchronized boolean wasAgentRegisterInNotifyListForCaseOfTermination(Agent agent) {
        synchronized (isAlive) {
            if (!isAlive) {
                return false;
            }
            synchronized (agentsToNotifyInCaseOfTermination) {
                agentsToNotifyInCaseOfTermination.add(agent);
            }
            return true;
        }
    }

    /**
     * Act on agent removal
     *
     * @param agent
     */
    public void agentWasRemoved(Agent agent) {
        synchronized (agentsToNotifyInCaseOfTermination) {
            agentsToNotifyInCaseOfTermination.remove(agent);
        }
        actOnAgentRemoval(agent);
    }

    /**
     * Abstract method to decide how to act on agent removal - for example to terminate as well
     *
     * @param agent
     */
    protected abstract void actOnAgentRemoval(Agent agent);

    public synchronized boolean isAgentAlive(){
        return isAlive;
    }

    /**
     * Method to be called when one want to terminate agent
     */
    public synchronized void terminateAgent() {
        this.isAlive = false;
    }

    private void removeAgent() {
        synchronized (agentsToNotifyInCaseOfTermination) {
            agentsToNotifyInCaseOfTermination.forEach(this::agentWasRemoved);
        }

        //

        AgentsManager.getInstance().removeAgent(this);
    }

}
