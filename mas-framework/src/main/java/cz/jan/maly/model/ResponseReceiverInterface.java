package cz.jan.maly.model;

/**
 * Interface for response receiver defining method to send response
 * Created by Jan on 17-Feb-17.
 */
public interface ResponseReceiverInterface<V> {

    /**
     * Method is called on receiver to end him response
     * @param response
     */
    void receiveResponse(V response);

}
