package cz.jan.maly.service;

/**
 * Contract to be implement by each terminable service
 * Created by Jan on 14-Mar-17.
 */
public interface TerminableService {

    /**
     * Tell service to terminate
     */
    void terminate();
}
