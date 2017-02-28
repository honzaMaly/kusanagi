package cz.jan.maly.model.planing.tree;

/**
 * Contract for each node representing intention
 * Created by Jan on 28-Feb-17.
 */
interface IntentionNodeInterface {

    /**
     * Remove commitment to this intention and replace itself by desire
     *
     * @return
     */
    boolean removeCommitment();

}
