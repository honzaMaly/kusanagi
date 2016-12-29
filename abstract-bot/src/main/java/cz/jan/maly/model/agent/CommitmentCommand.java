package cz.jan.maly.model.agent;

/**
 * Interface to be implemented by class of action which is interested in making requests on commitments - make commitment, add request for agents to commit,...
 * Created by Jan on 29-Dec-16.
 */
public interface CommitmentCommand {

    /**
     * Method to notify sender of request to make commitment with result of this operation
     *
     * @param wasSuccessful
     */
    void handleResultOfCommitmentRequest(boolean wasSuccessful);

}
