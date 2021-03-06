package cz.jan.maly.service.implementation;

import cz.jan.maly.model.QueuedItemInterfaceWithResponse;
import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.SharedDesire;
import cz.jan.maly.model.planing.SharedDesireForAgents;
import cz.jan.maly.model.planing.SharedDesireInRegister;
import cz.jan.maly.model.servicies.desires.ReadOnlyDesireRegister;
import cz.jan.maly.model.servicies.desires.WorkingDesireRegister;
import cz.jan.maly.service.MASFacade;
import cz.jan.maly.service.MediatorTemplate;

import java.util.Optional;
import java.util.Set;

/**
 * DesireMediator instance enables agents to propose desires for other agents to commit to. It keeps status what is available
 * and which agent is committed to what. This information are available then to other agents. Class defines method to access
 * queue.
 * <p>
 * Created by Jan on 17-Feb-17.
 */
public class DesireMediator extends MediatorTemplate<ReadOnlyDesireRegister, WorkingDesireRegister> {

    public DesireMediator() {
        super(new WorkingDesireRegister(), MASFacade::getLengthOfIntervalBeforeUpdatingRegisterWithDesires);
    }

    /**
     * Method to add item to queue with code to register desire
     *
     * @param sharedDesire
     * @param responseReceiver
     * @return
     */
    public boolean registerDesire(SharedDesireInRegister sharedDesire, ResponseReceiverInterface<Boolean> responseReceiver) {
        return addToQueue(new QueuedItemInterfaceWithResponse<Boolean>() {
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

    /**
     * Method to remove agent from register of desires
     *
     * @param agent
     * @param responseReceiver
     * @return
     */
    public void removeAgentFromRegister(Agent agent, ResponseReceiverInterface<Boolean> responseReceiver) {
        addToQueue(new QueuedItemInterfaceWithResponse<Boolean>() {
            @Override
            public Boolean executeCode() {
                return workingRegister.removeAgent(agent);
            }

            @Override
            public ResponseReceiverInterface<Boolean> getReceiverOfResponse() {
                return responseReceiver;
            }
        });
    }

    /**
     * Method to add item to queue with code to unregister desire
     *
     * @param sharedDesire
     * @param responseReceiver
     * @return
     */
    public boolean unregisterDesire(SharedDesire sharedDesire, ResponseReceiverInterface<Boolean> responseReceiver) {
        return addToQueue(new QueuedItemInterfaceWithResponse<Boolean>() {
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

    /**
     * Method to add item to queue with code to unregister desires
     *
     * @param sharedDesires
     * @param responseReceiver
     * @return
     */
    public boolean unregisterDesires(Set<SharedDesire> sharedDesires, ResponseReceiverInterface<Boolean> responseReceiver) {
        return addToQueue(new QueuedItemInterfaceWithResponse<Boolean>() {
            @Override
            public Boolean executeCode() {
                sharedDesires.forEach(workingRegister::removedDesire);
                return true;
            }

            @Override
            public ResponseReceiverInterface<Boolean> getReceiverOfResponse() {
                return responseReceiver;
            }
        });
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
        return addToQueue(new QueuedItemInterfaceWithResponse<Optional<SharedDesireForAgents>>() {
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

    /**
     * Method to add item to queue with code to remove commitment to desire
     *
     * @param agentWhoWantsToRemoveCommitment
     * @param desireHeWantsToRemoveCommitmentTo
     * @param responseReceiver
     * @return
     */
    public boolean removeCommitmentToDesire(Agent agentWhoWantsToRemoveCommitment, SharedDesireForAgents desireHeWantsToRemoveCommitmentTo, ResponseReceiverInterface<Boolean> responseReceiver) {
        return addToQueue(new QueuedItemInterfaceWithResponse<Boolean>() {
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

    /**
     * Method to add item to queue with code to remove commitment to desire
     *
     * @param agentWhoWantsToRemoveCommitment
     * @param desiresHeWantsToRemoveCommitmentTo
     * @param responseReceiver
     * @return
     */
    public void removeCommitmentToDesires(Agent agentWhoWantsToRemoveCommitment, Set<SharedDesireForAgents> desiresHeWantsToRemoveCommitmentTo, ResponseReceiverInterface<Boolean> responseReceiver) {
        addToQueue(new QueuedItemInterfaceWithResponse<Boolean>() {
            @Override
            public Boolean executeCode() {
                desiresHeWantsToRemoveCommitmentTo.forEach(desireHeWantsToRemoveCommitmentTo -> workingRegister.removeCommitmentToDesire(agentWhoWantsToRemoveCommitment, desireHeWantsToRemoveCommitmentTo));
                return true;
            }

            @Override
            public ResponseReceiverInterface<Boolean> getReceiverOfResponse() {
                return responseReceiver;
            }
        });
    }

}
