package cz.jan.maly.model.agent;

import cz.jan.maly.model.Knowledge;

/**
 * Interface to be implemented by class which is interested in observations of some observer
 * Created by Jan on 09-Dec-16.
 */
public interface SubscriberOfGameObserver {

    void processObservation(GameObservation observation);

}
