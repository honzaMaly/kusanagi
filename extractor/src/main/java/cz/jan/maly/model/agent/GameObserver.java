package cz.jan.maly.model.agent;

import java.util.HashSet;
import java.util.Set;

/**
 * Class representing observes of particular part of the game. It collects knowledge and notify all its subscribers
 * Created by Jan on 07-Dec-16.
 */
public abstract class GameObserver {
    protected final Set<SubscriberOfGameObserver> subscribers = new HashSet<>();

    public void register(SubscriberOfGameObserver subscriberOfGameObserver){
        subscribers.add(subscriberOfGameObserver);
    }

    public void unregister(SubscriberOfGameObserver subscriberOfGameObserver){
        subscribers.remove(subscriberOfGameObserver);
    }

}
