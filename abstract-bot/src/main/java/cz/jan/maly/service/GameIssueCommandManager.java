package cz.jan.maly.service;

import bwapi.Game;
import cz.jan.maly.model.GameActionMaker;
import cz.jan.maly.model.ServiceInterface;
import cz.jan.maly.model.ServiceOnFrame;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to issue commands to game (trough queue)
 * Created by Jan on 14-Dec-16.
 */
public class GameIssueCommandManager implements ServiceInterface, ServiceOnFrame {
    private static GameIssueCommandManager instance = null;
    //FIFO
    private List<GameActionMaker> queueOfActionMakers = new ArrayList<>();

    protected GameIssueCommandManager() {
        // Exists only to defeat instantiation.
    }
    public static GameIssueCommandManager getInstance() {
        if(instance == null) {
            instance = new GameIssueCommandManager();
        }
        return instance;
    }

    public boolean isActionToExecuteInGamePutInQueue(GameActionMaker gameActionMaker){
        return queueOfActionMakers.add(gameActionMaker);
    }

    @Override
    public void reinitializedServiceForNewGame() {
        synchronized (queueOfActionMakers){
            queueOfActionMakers.clear();
        }
    }

    @Override
    public void act(long remainingTime, Game game) {

        //find first service which was executed last time in less time than is remaining time
        for (int i = 0; i < queueOfActionMakers.size(); i++) {
            if (queueOfActionMakers.get(i).getExecutionTime()<remainingTime){
                queueOfActionMakers.remove(i).executeActionInGame();
                break;
            }
        }
    }
}
