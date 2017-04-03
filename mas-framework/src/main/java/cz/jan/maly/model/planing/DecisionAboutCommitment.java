package cz.jan.maly.model.planing;

import cz.jan.maly.model.metadata.DesireKey;

import java.util.Set;

/**
 * Contract for classes with method to decide commitment (as they need additional data for decision)
 * Created by Jan on 02-Mar-17.
 */
public interface DecisionAboutCommitment {

    /**
     * Method get object with parameters to initialize data container
     *
     * @return
     */
    Set<DesireKey> getParametersToLoad();

}
