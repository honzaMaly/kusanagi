package cz.jan.maly.service;

import bwapi.Game;

/**
 * Service with method which tell observers and action makers to act on frame.
 * Only one action is executed at time. Service acts as long as max execution time to act per frame
 * is not overstep
 * Created by Jan on 17-Dec-16.
 */
public class OnFrameExecutor {
    public static final long maxFrameExecutionTime = 75;
    private static OnFrameExecutor instance = null;

    protected OnFrameExecutor() {
        // Exists only to defeat instantiation.
    }

    public static OnFrameExecutor getInstance() {
        if (instance == null) {
            instance = new OnFrameExecutor();
        }
        return instance;
    }

    /**
     * Method to be called on new frame
     *
     * @param game
     */
    public void actOnFrame(Game game) {
        long currentTime = System.currentTimeMillis(), end = currentTime + maxFrameExecutionTime;
        int counter = 0;
        while (end > currentTime) {
            if (counter % 2 == 0) {
                GameObserverManager.getInstance().act(end - currentTime, game);
            } else {
                GameIssueCommandManager.getInstance().act(end - currentTime, game);
            }
            currentTime = System.currentTimeMillis();
            counter++;
        }
    }

}
