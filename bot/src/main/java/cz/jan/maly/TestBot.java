package cz.jan.maly;

import cz.jan.maly.model.agent.AgentUnitFactory;
import cz.jan.maly.service.implementation.MASService;

import java.io.IOException;
import java.util.ArrayList;

public class TestBot extends MASService {

    public TestBot() {
        super(new AgentUnitFactory(), () -> new ArrayList<>());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new TestBot().run();
    }
}