package cz.jan.maly.main;

import cz.jan.maly.service.implementation.AgentLocationInitializer;
import cz.jan.maly.service.implementation.AgentPlayerInitializer;
import cz.jan.maly.service.implementation.AgentUnitFactory;
import cz.jan.maly.service.implementation.BotFacade;

import java.beans.IntrospectionException;
import java.io.IOException;

public class SimpleBot extends BotFacade {

    private SimpleBot() {
        super(AgentUnitFactory::new, AgentPlayerInitializer::new, AgentLocationInitializer::new);
    }

    public static void main(String[] args) throws IOException, InterruptedException, IntrospectionException {
        new SimpleBot().run();
    }

}