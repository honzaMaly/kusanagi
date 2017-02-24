package cz.jan.maly.service.implementation;

import cz.jan.maly.model.QueuedItemInterface;
import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.knowledge.ReadOnlyMemory;
import cz.jan.maly.model.servicies.internal_beliefs.ReadOnlyMemoryRegister;
import cz.jan.maly.model.servicies.internal_beliefs.WorkingMemoryRegister;
import cz.jan.maly.service.MediatorTemplate;
import cz.jan.maly.utils.FrameworkUtils;

/**
 * KnowledgeMediator instance enables agents to share knowledge of agents by keeping each agent's internal knowledge.
 * Class defines method to access queue.
 * Created by Jan on 24-Feb-17.
 */
public class KnowledgeMediator extends MediatorTemplate<ReadOnlyMemoryRegister, WorkingMemoryRegister> {

    protected KnowledgeMediator() {
        super(new WorkingMemoryRegister(), FrameworkUtils::getLengthOfIntervalBeforeUpdatingRegisterWithMemory);
    }

    /**
     * Method to add item to queue with code to register desire
     *
     * @param readOnlyMemory
     * @param responseReceiver
     * @return
     */
    public boolean registerDesire(ReadOnlyMemory readOnlyMemory, ResponseReceiverInterface<Boolean> responseReceiver) {
        synchronized (queuedItems) {
            return queuedItems.add(new QueuedItemInterface<Boolean>() {
                @Override
                public Boolean executeCode() {
                    return workingRegister.addAgentsMemory(readOnlyMemory);
                }

                @Override
                public ResponseReceiverInterface<Boolean> getReceiverOfResponse() {
                    return responseReceiver;
                }
            });
        }
    }

}
