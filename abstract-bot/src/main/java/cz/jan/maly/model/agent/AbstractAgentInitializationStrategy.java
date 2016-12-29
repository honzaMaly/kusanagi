package cz.jan.maly.model.agent;

import cz.jan.maly.model.agent.implementation.AgentWithoutGameRepresentation;
import cz.jan.maly.service.implementation.MASService;

/**
 * Strategy describing strategy to create AbstractAgent
 * Created by Jan on 29-Dec-16.
 */
public interface AbstractAgentInitializationStrategy {

    /**
     * Method return instance of abstract agent
     * @param service
     * @return
     */
    AgentWithoutGameRepresentation createAbstractAgent(MASService service);

}
