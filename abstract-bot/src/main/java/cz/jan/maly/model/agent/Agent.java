package cz.jan.maly.model.agent;

import cz.jan.maly.model.agent.data.ActionToEvaluateTogether;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.model.agent.data.AgentsKnowledgeBase;
import cz.jan.maly.model.agent.data.AgentsKnowledgeCache;
import cz.jan.maly.service.implementation.AgentsManager;
import cz.jan.maly.service.implementation.MASService;
import cz.jan.maly.utils.MyLogger;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Agent abstraction class handles execution of main routine (action workflow, in form of sequence) in agent.
 * Concrete agent has to describe actions to execute, type of agent's knowledge and requests, initialization and destruction
 * Created by Jan on 09-Dec-16.
 */
@EqualsAndHashCode(of = "id")
public abstract class Agent {

    protected final AgentsKnowledgeBase agentsKnowledgeBaseInternal;
    protected final AgentsKnowledgeCache agentsKnowledgeCache;

    //todo
    //this structure contains request made by agent
    //this structure contains request agent is committed to

    private Boolean isAlive = true;
    private boolean isRunning = true;
    private final int id;
    protected static final Random RANDOM = new Random();
    private final long timeBetweenCycles;
    private final Worker worker = new Worker();
    private final boolean isAbstract;

    //definition of workflow specific for agent
    private final Map<ActionCycleEnums, List<ActionToEvaluateTogether>> actionDefinition = new HashMap<>();
    private final List<ActionCycleEnums> actionTypeExecutionOrder;

    private final Map<ActionCycleEnums, Long> gameRelatedActionWasCalledInFrame = new HashMap<>();

    //to have access to different services
    protected final MASService service;

    protected Agent(long timeBetweenCycles, Set<KeyToFact> factsToUseInInternalKnowledge, Set<KeyToFact> factsToUseInCache, boolean isAbstract, MASService service) {
        this.agentsKnowledgeBaseInternal = new AgentsKnowledgeBase(factsToUseInInternalKnowledge, this);
        this.agentsKnowledgeCache = new AgentsKnowledgeCache(factsToUseInCache);
        this.timeBetweenCycles = timeBetweenCycles;
        this.isAbstract = isAbstract;
        this.service = service;
        initializeActionWorkflow();
        this.actionTypeExecutionOrder = actionDefinition.keySet().stream()
                .sorted(Comparator.comparingInt(Enum::ordinal))
                .collect(Collectors.toList());
        actionTypeExecutionOrder.stream()
                .filter(ActionCycleEnums::isCanBeExecutedOncePerFrameOnly)
                .forEach(actionCycleEnums -> gameRelatedActionWasCalledInFrame.put(actionCycleEnums, -1L));
        this.id = service.getAgentsManager().getFreeId();
        worker.start();
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public int getId() {
        return id;
    }

    /**
     * Worker execute workflow of this agent. It makes sure that worker sleeps for defined amount of time.
     * If action failed to be executed, cycle start over.
     */
    private class Worker extends Thread {

        @Override
        public void run() {
            while (true) {
                synchronized (isAlive) {
                    if (!isAlive) {
                        break;
                    }
                }

                //action of particular type are executed in order given by ActionCycleEnums ordinal
                boolean hasActionFailed = false;
                for (ActionCycleEnums nextAction : actionTypeExecutionOrder) {

                    //todo check if action is frame dependant. if so skip it if it was executed recently

                    //only actions in one ActionToEvaluateTogether can be executed at once and only those for which conditions are met
                    boolean wasAnyActionExecuted = false;
                    for (ActionToEvaluateTogether actionToEvaluateTogether : actionDefinition.get(nextAction)) {
                        while (actionToEvaluateTogether.hasNext()) {
                            AgentActionCycleAbstract action = actionToEvaluateTogether.next();
                            if (action.areConditionForExecutionMet()) {

                                //execute action. if execution fail, action type execution start over
                                if (!action.executedAction()) {
                                    hasActionFailed = true;
                                    break;
                                }
                                wasAnyActionExecuted = true;
                            }
                        }
                        if (wasAnyActionExecuted || hasActionFailed) {
                            break;
                        }
                    }
                    if (hasActionFailed) {
                        break;
                    }

                    //todo update last execution of action

                    //check this after each action
                    synchronized (isAlive) {
                        if (!isAlive) {
                            break;
                        }
                    }
                }

                //todo if cycle reached end naturally, sleep for time interval
                try {
                    Thread.sleep(timeBetweenCycles);
                } catch (InterruptedException e) {
                    MyLogger.getLogger().warning(e.getLocalizedMessage());
                }
            }
            isRunning = false;
            removeAgent();
        }

    }

    //todo
    /**
     * Method merge user defined actions with internal ones. To create map of actions to execute per type.
     *
     * @return
     */
    private void initializeActionWorkflow() {
        Map<ActionCycleEnums, List<ActionToEvaluateTogether>> actionsDefinedByUser = actionsDefinedByUser();
        if (actionsDefinedByUser.keySet().stream().anyMatch(ActionCycleEnums::isInternal)) {
            MyLogger.getLogger().info("Internal actions con not be define by user. Default values for those actions will be used instead.");
        }

        //todo. check that in game action are size one
        for (ActionCycleEnums actionCycleEnums : ActionCycleEnums.values()) {
            switch (actionCycleEnums) {
                case ACT_IN_GAME:

                    break;
                case READ_MAP:

                    break;
            }
        }
    }

    /**
     * This is crucial method to be implemented by each agent as it should return valid map of actions for agent to execute.
     * This method is on class as it works with inner knowledge and requests. Value - list in map, is evaluate from first
     * element until at least one action in ActionToEvaluateTogether is executed (or there are no more options).
     * After that program continue with next level if action was executed/no action was executed.
     * If action failed to be executed cycle start over.
     *
     * @return
     */
    protected abstract Map<ActionCycleEnums, List<ActionToEvaluateTogether>> actionsDefinedByUser();

    public boolean isAgentAlive() {
        return isRunning;
    }

    /**
     * Method to be called when one want to terminate agent
     */
    public synchronized void terminateAgent() {
        this.isAlive = false;
    }

    protected abstract void removeAgent();

}
