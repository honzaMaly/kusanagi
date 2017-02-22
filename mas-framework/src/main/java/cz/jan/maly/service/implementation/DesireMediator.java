package cz.jan.maly.service.implementation;

import cz.jan.maly.model.QueuedItemInterface;
import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.SharedDesireForAgents;
import cz.jan.maly.model.planing.SharedDesireInRegister;
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
     * @param sharedDesire
     * @param responseReceiver
     * @return
     */
    public boolean registerDesire(SharedDesireInRegister sharedDesire, ResponseReceiverInterface<Boolean> responseReceiver) {
        synchronized (queuedItems) {
            return queuedItems.add(new QueuedItemInterface<Boolean>() {
                @Override
                public Boolean executeCode() {
                    return workingRegister.addedDesire(sharedDesire);
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
     * @param sharedDesire
     * @param responseReceiver
     * @return
     */
    public boolean unregisterDesire(SharedDesireInRegister sharedDesire, ResponseReceiverInterface<Boolean> responseReceiver) {
        synchronized (queuedItems) {
            return queuedItems.add(new QueuedItemInterface<Boolean>() {
                @Override
                public Boolean executeCode() {
                    return workingRegister.removedDesire(sharedDesire);
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
    public boolean addCommitmentToDesire(Agent agentWhoWantsToCommitTo, SharedDesireForAgents desireForOthersHeWantsToCommitTo, ResponseReceiverInterface<Optional<SharedDesireForAgents>> responseReceiver) {
        synchronized (queuedItems) {
            return queuedItems.add(new QueuedItemInterface<Optional<SharedDesireForAgents>>() {
                @Override
                public Optional<SharedDesireForAgents> executeCode() {
                    return workingRegister.commitToDesire(agentWhoWantsToCommitTo, desireForOthersHeWantsToCommitTo);
                }

                @Override
                public ResponseReceiverInterface<Optional<SharedDesireForAgents>> getReceiverOfResponse() {
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
    public boolean removeCommitmentToDesire(Agent agentWhoWantsToRemoveCommitment, SharedDesireForAgents desireHeWantsToRemoveCommitmentTo, ResponseReceiverInterface<Boolean> responseReceiver) {
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
