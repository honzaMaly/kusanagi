package cz.jan.maly.service;

import cz.jan.maly.model.ServiceInterface;
import cz.jan.maly.model.agent.action.GetGameObservationAction;

/**
 * Singleton class to manage access to game data for GameObservers (trough queue)
 * Created by Jan on 07-Dec-16.
 */
public class GameObserverManager implements ServiceInterface {
    private static GameObserverManager instance = null;

    protected GameObserverManager() {
        // Exists only to defeat instantiation.
    }
    public static GameObserverManager getInstance() {
        if(instance == null) {
            instance = new GameObserverManager();
        }
        return instance;
    }

    //todo handle queue

    public boolean isObserverOfGamePutInQueue(GetGameObservationAction gameObserver){
        return true;
    }

    @Override
    public void reinitializedServiceForNewGame() {
        //todo completely forget queue
    }
}
