package cz.jan.maly.service;

/**
 * Observer Design Pattern - Observer
 * Created by Jan on 30-Oct-16.
 */
public interface Observer {

    /**
     * Method to update the observer, used by subject
     */
    void update(String parsedFileName);

}
