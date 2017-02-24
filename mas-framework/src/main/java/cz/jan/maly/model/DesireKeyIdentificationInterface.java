package cz.jan.maly.model;

import cz.jan.maly.model.metadata.DesireKey;

/**
 * Contract defining method to be implemented by each class which wants enable user to get desire key associated with class
 * Created by Jan on 24-Feb-17.
 */
public interface DesireKeyIdentificationInterface {

    /**
     * Returns DesireKey associated with this instance
     *
     * @return
     */
    DesireKey getDesireKey();

}
