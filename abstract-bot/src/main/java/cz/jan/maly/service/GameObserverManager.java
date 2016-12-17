package cz.jan.maly.service;

import bwapi.Game;
import cz.jan.maly.model.GameObserver;
import cz.jan.maly.model.ServiceInterface;
import cz.jan.maly.model.ServiceOnFrame;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class to manage access to game data for GameObservers (trough queue)
 * Created by Jan on 07-Dec-16.
 */
public class GameObserverManager implements ServiceInterface, ServiceOnFrame {
    private static GameObserverManager instance = null;
    //FIFO
    private List<GameObserver> queueOfObservers = new ArrayList<>();

    protected GameObserverManager() {
        // Exists only to defeat instantiation.
    }
    public static GameObserverManager getInstance() {
        if(instance == null) {
            instance = new GameObserverManager();
        }
        return instance;
    }

    public synchronized boolean isObserverOfGamePutInQueue(GameObserver gameObserver){
        return queueOfObservers.add(gameObserver);
    }

    @Override
    public void reinitializedServiceForNewGame() {
        synchronized (queueOfObservers){
            queueOfObservers.clear();
        }
    }

    @Override
    public synchronized void act(long remainingTime, Game game) {

        //find first service which was executed last time in less time than is remaining time
        for (int i = 0; i < queueOfObservers.size(); i++) {
            if (queueOfObservers.get(i).getExecutionTime()<remainingTime){
                queueOfObservers.remove(i).makeObservation(game);
                break;
            }
        }
    }
}
