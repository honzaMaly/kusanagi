package cz.jan.maly.model.decision;

/**
 * Contract to return commitment associated with strategy implementing this interface
 * Created by Jan on 23-Apr-17.
 */
interface NextActionStrategy {

    /**
     * Return next commitment
     * @return
     */
    boolean commit();

}
