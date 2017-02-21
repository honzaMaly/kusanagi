package cz.jan.maly.model.planing;

/**
 * Interface to be implemented by desire and intention instances. As both should implement method to decide if agent
 * should commit to desire / remove its commitment to intention
 * Created by Jan on 21-Feb-17.
 */
public interface Commitment {

    /**
     * Returns if commitment holds in case of intention / should commit to desire and make intention from it
     *
     * @return
     */
    boolean isCommitted();

}
