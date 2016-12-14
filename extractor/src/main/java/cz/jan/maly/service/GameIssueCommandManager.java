package cz.jan.maly.service;

import cz.jan.maly.model.ServiceInterface;

/**
 * Singleton class to issue commands to game (trough queue)
 * Created by Jan on 14-Dec-16.
 */
public class GameIssueCommandManager implements ServiceInterface {
    private static GameIssueCommandManager instance = null;

    protected GameIssueCommandManager() {
        // Exists only to defeat instantiation.
    }
    public static GameIssueCommandManager getInstance() {
        if(instance == null) {
            instance = new GameIssueCommandManager();
        }
        return instance;
    }

    @Override
    public void reinitializedServiceForNewGame() {

        //todo completely forget queue

    }
}
