package cz.jan.maly.main;

import cz.jan.maly.service.implementation.AgentUnitHandlerImpl;
import cz.jan.maly.service.implementation.BotFacade;
import cz.jan.maly.service.implementation.LocationInitializerImpl;
import cz.jan.maly.service.implementation.PlayerInitializerImpl;

import java.beans.IntrospectionException;
import java.io.IOException;

public class SimpleBot extends BotFacade {

    private SimpleBot() {
        super(AgentUnitHandlerImpl::new, PlayerInitializerImpl::new, LocationInitializerImpl::new);
    }

    public static void main(String[] args) throws IOException, InterruptedException, IntrospectionException {
        new SimpleBot().run();
    }

}