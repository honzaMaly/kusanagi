package cz.jan.maly.service;

/**
 * Observer Design Pattern - Subject
 * Created by Jan on 30-Oct-16.
 */
public interface Subject {

    /**
     * methods to register observers
     */
    void register(Observer observer);

    /**
     * methods to unregister observers
     * @param observer
     */
    void unregister(Observer observer);

    /**
     * Method to notify observers of change
     */
    void notifyObservers();

}
