package cz.jan.maly.service.implementation;

import bwapi.Game;
import cz.jan.maly.model.QueuedItemInterfaceWithResponse;
import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.knowledge.Memory;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.planing.command.ActCommandForIntention;
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
public class GameCommandExecutor implements CommandManager<ActCommandForIntention<?>, Memory<?, ?>>, ObservingCommandManager<Game, ObservingCommand<Game>> {

    //FIFO
    private final List<QueuedItemInterfaceWithResponseWithCommandClassGetter> queuedItems = new ArrayList<>();

    //counter of frames
    private long countOfPassedFrames = 0;

    private final Game game;

    public GameCommandExecutor(Game game) {
        this.game = game;
    }

    public synchronized long getCountOfPassedFrames() {
        return countOfPassedFrames;
    }

    private synchronized void incrementCountOfPassedFrames() {
        countOfPassedFrames++;
    }

    //structures to keep durations of command execution
    private final Map<AgentType<Game>, Map<Class, Long>> lastDurationOfCommandTypeExecutionForAgentType = new HashMap<>();

    /**
     * Method to add item to queue with code to execute action in game
     *
     * @param command
     * @param responseReceiver
     * @return
     */
    public boolean addCommandToObserve(ObservingCommand<Game> command, WorkingMemory memory, ResponseReceiverInterface<Boolean> responseReceiver, AgentType<Game> agentType) {
        synchronized (queuedItems) {
            return queuedItems.add(new QueuedItemInterfaceWithResponseWithCommandClassGetter() {
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
    }

    /**
     * Method to add item to queue with code to execute action in game
     *
     * @param command
     * @param responseReceiver
     * @return
     */
    public boolean addCommandToAct(ActCommandForIntention<?> command, WorkingMemory memory, ResponseReceiverInterface<Boolean> responseReceiver, AgentType<Game> agentType) {
        synchronized (queuedItems) {
            return queuedItems.add(new QueuedItemInterfaceWithResponseWithCommandClassGetter() {
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
    }

    /**
     * Method to be called on each frame. It executes commands in queue based on their priority.
     * First command in queue is executed always - to make sure that commands do not stuck in queue
     */
    void actOnFrame() {
        long currentTime = System.currentTimeMillis(), end = currentTime + BotFacade.getMaxFrameExecutionTime();
        executeCommand();
        currentTime = System.currentTimeMillis();
        while (end > currentTime) {
            executeCommand(end - currentTime);
            currentTime = System.currentTimeMillis();
        }
        incrementCountOfPassedFrames();
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
            executeCommand(queuedItem);
        }
    }

    //todo maybe have another thread as consumer processing execution times???
    private void executeCommand(QueuedItemInterfaceWithResponseWithCommandClassGetter queuedItem) {
        long start = System.currentTimeMillis();
        queuedItem.executeItem();
        lastDurationOfCommandTypeExecutionForAgentType.putIfAbsent(queuedItem.getAgentType(), new HashMap<>()).put(queuedItem.getClassOfCommand(), System.currentTimeMillis() - start);
    }

    /**
     * Find first queued command which was executed last time in less time than is remaining time
     *
     * @param remainingTime
     */
    private void executeCommand(long remainingTime) {
        long end = System.currentTimeMillis() + remainingTime;
        for (int i = 0; i < queuedItems.size(); i++) {
            synchronized (queuedItems) {
                QueuedItemInterfaceWithResponseWithCommandClassGetter queuedItem = queuedItems.get(i);
                long executionTime = lastDurationOfCommandTypeExecutionForAgentType.getOrDefault(queuedItem.getAgentType(), new HashMap<>()).getOrDefault(queuedItem.getClassOfCommand(), BotFacade.getMaxFrameExecutionTime());
                if (executionTime < remainingTime) {
                    executeCommand(queuedItems.remove(i));
                }
            }
            if (System.currentTimeMillis() >= end) {
                break;
            }
        }
    }

    @Override
    public boolean executeCommand(ActCommandForIntention<?> commandToExecute, Memory<?, ?> memory) {
        return commandToExecute.act(memory);
    }

    @Override
    public boolean executeCommand(ObservingCommand<Game> commandToExecute, WorkingMemory memory, Game environment) {
        return commandToExecute.observe(memory, environment);
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
