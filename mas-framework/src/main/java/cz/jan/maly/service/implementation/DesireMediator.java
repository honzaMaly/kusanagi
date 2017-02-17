package cz.jan.maly.service.implementation;

import cz.jan.maly.model.QueuedItemInterface;
import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.DesireForOthers;
import cz.jan.maly.model.planing.DesireFromAnotherAgent;
import cz.jan.maly.model.servicies.desires.ReadOnlyDesireRegister;
import cz.jan.maly.model.servicies.desires.WorkingDesireRegister;
import cz.jan.maly.service.MediatorTemplate;
import cz.jan.maly.utils.FrameworkUtils;

import java.util.Optional;

/**
 * DesireMediator instance enables agents to propose desires to commit to to other agents. It keeps status what is available
 * and which agent is committed to what. This information are available then to other agents. Class defines method to access
 * queue.
 * <p>
 * Created by Jan on 17-Feb-17.
 */
public class DesireMediator extends MediatorTemplate<ReadOnlyDesireRegister, WorkingDesireRegister> {

    public DesireMediator() {
        super(new WorkingDesireRegister(), FrameworkUtils::getLengthOfIntervalBeforeUpdatingRegisterWithDesires);
    }

    /**
     * Method to add item to queue with code to register desire
     *
     * @param desireForOthers
     * @param responseReceiver
     * @return
     */
    public boolean registerDesire(DesireForOthers desireForOthers, ResponseReceiverInterface<Boolean> responseReceiver) {
        synchronized (queuedItems) {
            return queuedItems.add(new QueuedItemInterface<Boolean>() {
                @Override
                public Boolean executeCode() {
                    return workingRegister.addedDesire(desireForOthers);
                }

                @Override
                public ResponseReceiverInterface<Boolean> getReceiverOfResponse() {
                    return responseReceiver;
                }
            });
        }
    }

    /**
     * Method to add item to queue with code to unregister desire
     *
     * @param desireForOthers
     * @param responseReceiver
     * @return
     */
    public boolean unregisterDesire(DesireForOthers desireForOthers, ResponseReceiverInterface<Boolean> responseReceiver) {
        synchronized (queuedItems) {
            return queuedItems.add(new QueuedItemInterface<Boolean>() {
                @Override
                public Boolean executeCode() {
                    return workingRegister.removedDesire(desireForOthers);
                }

                @Override
                public ResponseReceiverInterface<Boolean> getReceiverOfResponse() {
                    return responseReceiver;
                }
            });
        }
    }

    /**
     * Method to add item to queue with code to make commitment to desire
     *
     * @param agentWhoWantsToCommitTo
     * @param desireForOthersHeWantsToCommitTo
     * @param responseReceiver
     * @return
     */
    public boolean addCommitmentToDesire(Agent agentWhoWantsToCommitTo, DesireForOthers desireForOthersHeWantsToCommitTo, ResponseReceiverInterface<Optional<DesireFromAnotherAgent>> responseReceiver) {
        synchronized (queuedItems) {
            return queuedItems.add(new QueuedItemInterface<Optional<DesireFromAnotherAgent>>() {
                @Override
                public Optional<DesireFromAnotherAgent> executeCode() {
                    return workingRegister.commitToDesire(agentWhoWantsToCommitTo, desireForOthersHeWantsToCommitTo);
                }

                @Override
                public ResponseReceiverInterface<Optional<DesireFromAnotherAgent>> getReceiverOfResponse() {
                    return responseReceiver;
                }
            });
        }
    }

    /**
     * Method to add item to queue with code to remove commitment to desire
     *
     * @param agentWhoWantsToRemoveCommitment
     * @param desireHeWantsToRemoveCommitmentTo
     * @param responseReceiver
     * @return
     */
    public boolean removeCommitmentToDesire(Agent agentWhoWantsToRemoveCommitment, DesireFromAnotherAgent desireHeWantsToRemoveCommitmentTo, ResponseReceiverInterface<Boolean> responseReceiver) {
        synchronized (queuedItems) {
            return queuedItems.add(new QueuedItemInterface<Boolean>() {
                @Override
                public Boolean executeCode() {
                    return workingRegister.removeCommitmentToDesire(agentWhoWantsToRemoveCommitment, desireHeWantsToRemoveCommitmentTo);
                }

                @Override
                public ResponseReceiverInterface<Boolean> getReceiverOfResponse() {
                    return responseReceiver;
                }
            });
        }
    }

}
