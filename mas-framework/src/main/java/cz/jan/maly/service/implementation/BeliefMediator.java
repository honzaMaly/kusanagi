package cz.jan.maly.service.implementation;

import cz.jan.maly.model.QueuedItemInterfaceWithResponse;
import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.ReadOnlyMemory;
import cz.jan.maly.model.servicies.beliefs.ReadOnlyMemoryRegister;
import cz.jan.maly.model.servicies.beliefs.WorkingMemoryRegister;
import cz.jan.maly.service.MASFacade;
import cz.jan.maly.service.MediatorTemplate;

/**
 * KnowledgeMediator instance enables agents to share knowledge of agents by keeping each agent's internal knowledge.
 * Class defines method to access queue.
 * Created by Jan on 24-Feb-17.
 */
public class BeliefMediator extends MediatorTemplate<ReadOnlyMemoryRegister, WorkingMemoryRegister> {

    public BeliefMediator() {
        super(new WorkingMemoryRegister(), MASFacade::getLengthOfIntervalBeforeUpdatingRegisterWithMemory);
    }

    /**
     * Method to add item to queue with code to register knowledge
     *
     * @param readOnlyMemory
     * @param responseReceiver
     * @return
     */
    public boolean registerBelief(ReadOnlyMemory readOnlyMemory, Agent owner, ResponseReceiverInterface<Boolean> responseReceiver) {
        return addToQueue(new QueuedItemInterfaceWithResponse<Boolean>() {
            @Override
            public Boolean executeCode() {
                return workingRegister.addAgentsMemory(readOnlyMemory, owner);
            }

            @Override
            public ResponseReceiverInterface<Boolean> getReceiverOfResponse() {
                return responseReceiver;
            }
        });
    }

    /**
     * Method to add item to queue with code to remove agent
     *
     * @param owner
     * @param responseReceiver
     * @return
     */
    public boolean removeAgent(Agent owner, ResponseReceiverInterface<Boolean> responseReceiver) {
        return addToQueue(new QueuedItemInterfaceWithResponse<Boolean>() {
            @Override
            public Boolean executeCode() {
                return workingRegister.removeAgentsMemory(owner);
            }

            @Override
            public ResponseReceiverInterface<Boolean> getReceiverOfResponse() {
                return responseReceiver;
            }
        });
    }

}
