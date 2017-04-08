package cz.jan.maly.service.implementation;

/**
 * Class to manage agents in system. It keeps reference to existing agents, add them ids
 * Created by Jan on 24-Feb-17.
 */
public class AgentsRegister {
    private final Object lock = new Object();
    //adding ids using integer incremental counter should not be a problem
    private int idCounter = 0;

    /**
     * Get free id for agent to be used
     *
     * @return
     */
    public int getFreeId() {
        synchronized (lock) {
            idCounter++;
            return idCounter;
        }
    }
}
