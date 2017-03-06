package cz.jan.maly.model;

/**
 * Interface to be implemented by each queued item. It defines method whit code to execute and two methods to execute item
 * by queue manager and send result to receiver of result for executed operation
 * Created by Jan on 17-Feb-17.
 */
public interface QueuedItemInterfaceWithResponse<V> {

    /**
     * Execute code and returns result of executed method
     *
     * @return
     */
    V executeCode();

    /**
     * Get receiver to send respond with result to
     *
     * @return
     */
    ResponseReceiverInterface<V> getReceiverOfResponse();

    /**
     * Method to be called que manager. Default behaviour is to execute item and send result to receiver
     */
    default void executeItem() {
        getReceiverOfResponse().receiveResponse(executeCode());
    }

}
