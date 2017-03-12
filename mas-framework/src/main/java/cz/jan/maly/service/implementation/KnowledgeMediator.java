package cz.jan.maly.service.implementation;

import cz.jan.maly.model.QueuedItemInterfaceWithResponse;
import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.ReadOnlyMemory;
import cz.jan.maly.model.servicies.beliefs.ReadOnlyMemoryRegister;
import cz.jan.maly.model.servicies.beliefs.WorkingMemoryRegister;
import cz.jan.maly.service.MediatorTemplate;
import cz.jan.maly.utils.FrameworkUtils;

/**
 * KnowledgeMediator instance enables agents to share knowledge of agents by keeping each agent's internal knowledge.
 * Class defines method to access queue.
 * Created by Jan on 24-Feb-17.
 */
public class KnowledgeMediator extends MediatorTemplate<ReadOnlyMemoryRegister, WorkingMemoryRegister> {

    public KnowledgeMediator() {
        super(new WorkingMemoryRegister(), FrameworkUtils::getLengthOfIntervalBeforeUpdatingRegisterWithMemory);
    }

    /**
     * Method to add item to queue with code to register desire
     *
     * @param readOnlyMemory
     * @param responseReceiver
     * @return
     */
    public boolean registerDesire(ReadOnlyMemory readOnlyMemory, Agent owner, ResponseReceiverInterface<Boolean> responseReceiver) {
        synchronized (queuedItems) {
            return queuedItems.add(new QueuedItemInterfaceWithResponse<Boolean>() {
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
    }

}
