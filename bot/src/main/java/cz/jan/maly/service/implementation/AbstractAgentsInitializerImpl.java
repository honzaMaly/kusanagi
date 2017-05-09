package cz.jan.maly.service.implementation;

import cz.jan.maly.model.agent.AbstractAgent;
import cz.jan.maly.model.bot.AgentTypes;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.service.AbstractAgentsInitializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of AbstractAgentsInitializer
 * Created by Jan on 09-May-17.
 */
public class AbstractAgentsInitializerImpl implements AbstractAgentsInitializer {

    @Override
    public List<AbstractAgent> initializeAbstractAgents(BotFacade botFacade) {
        List<AbstractAgent> abstractAgents = new ArrayList<>();
        abstractAgents.add(new AbstractAgent(ECO_MANAGER, botFacade));

        //todo others

        return abstractAgents;
    }

    //todo
    private static final AgentType ECO_MANAGER = AgentType.builder()
            .agentTypeID(AgentTypes.ECO_MANAGER)
            .initializationStrategy(type -> {

                //todo

            })
            .build();

}
