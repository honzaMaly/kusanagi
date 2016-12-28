package cz.jan.maly.service.implementation;

import bwapi.Game;
import cz.jan.maly.model.agent.GameCommand;

import java.util.ArrayList;
import java.util.List;

import static cz.jan.maly.utils.FrameworkUtils.getMaxFrameExecutionTime;

/**
 * Service let queued instances of GameCommand act on frame. Only one action is executed at time.
 * Service watch for execution time to manage execution of actions based on priority in queue and remaining
 * time - it make sure that limit is not overstep.
 * Created by Jan on 28-Dec-16.
 */
public class GameCommandExecutor {

    //FIFO
    private final List<GameCommand> gameCommands = new ArrayList<>();

    //counter of frames
    private long countOfPassedFrames = 0;

    public synchronized boolean addCommandToExecute(GameCommand command) {
        return gameCommands.add(command);
    }

    public synchronized long getCountOfPassedFrames() {
        return countOfPassedFrames;
    }

    public synchronized void incrementCountOfPassedFrames() {
        countOfPassedFrames++;
    }

    /**
     * Method to be called on each frame. It executes commands in queue based on their priority.
     * First command in queue is executed always - to make sure that commands do not stuck in queue
     *
     * @param game
     */
    public void actOnFrame(Game game) {
        boolean isFirstAction = true;
        long currentTime = System.currentTimeMillis(), end = currentTime + getMaxFrameExecutionTime();
        while (end > currentTime) {
            if (isFirstAction) {
                act(game);
                isFirstAction = false;
            } else {
                act(end - currentTime, game);
            }
            currentTime = System.currentTimeMillis();
        }
        incrementCountOfPassedFrames();
    }

    /**
     * Execute first command
     *
     * @param game
     */
    private void act(Game game) {
        if (!gameCommands.isEmpty()) {
            synchronized (gameCommands) {
                gameCommands.remove(0).commandGame(game);
            }
        }
    }

    /**
     * Find first queued command which was executed last time in less time than is remaining time
     *
     * @param remainingTime
     * @param game
     */
    private void act(long remainingTime, Game game) {
        for (int i = 0; i < gameCommands.size(); i++) {
            synchronized (gameCommands) {
                if (gameCommands.get(i).getExecutionTime() < remainingTime) {
                    gameCommands.remove(i).commandGame(game);
                    break;
                }
            }
        }
    }

}
