package cz.jan.maly;

import cz.jan.maly.model.agent.AgentUnitFactory;
import cz.jan.maly.service.AbstractAgentInitializerInterface;
import cz.jan.maly.service.implementation.MASService;

import java.io.IOException;

public class TestBot extends MASService {

    public TestBot() {
        super(new AgentUnitFactory(), new AbstractAgentInitializerInterface() {
            @Override
            public void initializeAbstractAgentOnStartOfTheGame() {
                //todo
            }
        });
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new TestBot().run();
    }
}