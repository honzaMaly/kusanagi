package cz.jan.maly.service.implementation;

import bwapi.Game;
import cz.jan.maly.model.QueuedItemInterfaceWithResponse;
import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ObservingCommand;
import cz.jan.maly.service.CommandManager;
import cz.jan.maly.service.ObservingCommandManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service let queued instances of commands be executed on frame. Only one action is executed at time.
 * Service watch for execution time to manage executions of actions based on priority in queue and remaining
 * time - it make maximum to make sure that limit is not overstep.
 * Created by Jan on 28-Dec-16.
 */
public class GameCommandExecutor implements CommandManager<ActCommand<?>>, ObservingCommandManager<Game, ObservingCommand<Game>> {

    //FIFO
    private final List<QueuedItemInterfaceWithResponseWithCommandClassGetter> queuedItems = new ArrayList<>();
    private final Game game;
    //structures to keep durations of command execution
    private final Map<AgentType, Map<Class, Long>> lastDurationOfCommandTypeExecutionForAgentType = new HashMap<>();
    //counter of frames
    private int countOfPassedFrames = 0;

    GameCommandExecutor(Game game) {
        this.game = game;
    }

    //Not synchronized. Speed is primal concern not accuracy
    int getCountOfPassedFrames() {
        return countOfPassedFrames;
    }

    /**
     * Add request to internal queue
     *
     * @param request
     * @return
     */
    private boolean addToQueue(QueuedItemInterfaceWithResponseWithCommandClassGetter request) {
        synchronized (queuedItems) {
            return queuedItems.add(request);
        }
    }

    /**
     * Method to add item to queue with code to execute action in GAME
     *
     * @param command
     * @param responseReceiver
     * @return
     */
    public boolean addCommandToObserve(ObservingCommand<Game> command, WorkingMemory memory, ResponseReceiverInterface<Boolean> responseReceiver, AgentType agentType) {
        return addToQueue(new QueuedItemInterfaceWithResponseWithCommandClassGetter() {
            @Override
            Class getClassOfCommand() {
                return command.getClass();
            }

            @Override
            AgentType getAgentType() {
                return agentType;
            }

            @Override
            public Boolean executeCode() {
                return executeCommand(command, memory, game);
            }

            @Override
            public ResponseReceiverInterface<Boolean> getReceiverOfResponse() {
                return responseReceiver;
            }
        });
    }

    /**
     * Method to add item to queue with code to execute action in GAME
     *
     * @param command
     * @param responseReceiver
     * @return
     */
    public boolean addCommandToAct(ActCommand<?> command, WorkingMemory memory, ResponseReceiverInterface<Boolean> responseReceiver, AgentType agentType) {
        return addToQueue(new QueuedItemInterfaceWithResponseWithCommandClassGetter() {
            @Override
            Class getClassOfCommand() {
                return command.getClass();
            }

            @Override
            AgentType getAgentType() {
                return agentType;
            }

            @Override
            public Boolean executeCode() {
                return executeCommand(command, memory);
            }

            @Override
            public ResponseReceiverInterface<Boolean> getReceiverOfResponse() {
                return responseReceiver;
            }
        });
    }

    /**
     * Method to be called on each frame. It executes commands in queue based on their priority.
     * First command in queue is executed always - to make sure that commands do not stuck in queue
     */
    void actOnFrame() {
        long currentTime = System.currentTimeMillis(), end = currentTime + BotFacade.getMaxFrameExecutionTime(), start = currentTime;
        int startIndex = 0;
        executeCommand();
        currentTime = System.currentTimeMillis();
        while (end > currentTime) {
            startIndex = executeCommand(end - currentTime, startIndex);
            currentTime = System.currentTimeMillis();
        }

        //this is not vital to have it synchronized. primary concern is speed
        this.countOfPassedFrames = game.getFrameCount();

//        long tameItTook = (System.currentTimeMillis() - start);
//        MyLogger.getLogger().info("Frame commands " + countOfPassedFrames + " executed in " + tameItTook + " ms");
//        game.printf("Frame commands " + countOfPassedFrames + " executed in " + tameItTook + " ms");
    }

    /**
     * Execute first command
     */
    private void executeCommand() {
        if (!queuedItems.isEmpty()) {
            QueuedItemInterfaceWithResponseWithCommandClassGetter queuedItem;
            synchronized (queuedItems) {
                queuedItem = queuedItems.remove(0);
            }
            long timeItTook = executeCommand(queuedItem);
            lastDurationOfCommandTypeExecutionForAgentType.computeIfAbsent(queuedItem.getAgentType(), at -> new HashMap<>()).put(queuedItem.getClassOfCommand(), timeItTook);
        }
    }

    private long executeCommand(QueuedItemInterfaceWithResponseWithCommandClassGetter queuedItem) {
        long start = System.currentTimeMillis();
        queuedItem.executeItem();
        return System.currentTimeMillis() - start;
    }

    /**
     * Find first queued command which was executed last time in less time than is remaining time
     *
     * @param remainingTime
     * @param startIndex
     */
    private int executeCommand(long remainingTime, int startIndex) {
        long end = System.currentTimeMillis() + remainingTime;
        for (int i = startIndex; i < queuedItems.size(); i++) {
            if (System.currentTimeMillis() >= end) {
                break;
            }
            QueuedItemInterfaceWithResponseWithCommandClassGetter queuedItem;
            synchronized (queuedItems) {
                queuedItem = queuedItems.get(i);
            }
            long executionTime = lastDurationOfCommandTypeExecutionForAgentType.getOrDefault(queuedItem.getAgentType(), new HashMap<>()).getOrDefault(queuedItem.getClassOfCommand(), BotFacade.getMaxFrameExecutionTime());
            if (executionTime < end - System.currentTimeMillis()) {
                synchronized (queuedItems) {
                    queuedItem = queuedItems.remove(i);
                }
                executionTime = executeCommand(queuedItem);
                lastDurationOfCommandTypeExecutionForAgentType.computeIfAbsent(queuedItem.getAgentType(), at -> new HashMap<>()).put(queuedItem.getClassOfCommand(), executionTime);
                return i;
            }
        }
        return 0;
    }

    /**
     * Extension of item in queue
     */
    private static abstract class QueuedItemInterfaceWithResponseWithCommandClassGetter implements QueuedItemInterfaceWithResponse<Boolean> {

        /**
         * Get class of command
         *
         * @return
         */
        abstract Class getClassOfCommand();

        /**
         * Get agent type
         *
         * @return
         */
        abstract AgentType getAgentType();
    }


}
