package cz.jan.maly;

import cz.jan.maly.service.AgentUnitFactory;
import cz.jan.maly.service.implementation.BotFacade;

import java.io.IOException;
import java.util.ArrayList;

public class SimpleBot extends BotFacade {

    private SimpleBot() {
        super(new AgentUnitFactory(), ArrayList::new);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new SimpleBot().run();
    }
}