package cz.jan.maly;

import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import cz.jan.maly.model.agent.AgentUnitFactory;
import cz.jan.maly.service.OnFrameExecutor;

import java.io.IOException;

public class TestBot1 extends DefaultBWListener {

    private Mirror mirror = new Mirror();

    private Game game;
    private Player self;

    public void run() throws IOException, InterruptedException {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
        if (unit.getPlayer().equals(self)) {
            AgentUnitFactory.createAgentForUnit(unit);
        }
        System.out.println("New unit discovered " + unit.getType());
    }

    @Override
    public void onStart() {
        game = mirror.getGame();
        self = game.self();

        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");

        int i = 0;
        for (BaseLocation baseLocation : BWTA.getBaseLocations()) {
            System.out.println("Base location #" + (++i) + ". Printing location's region polygon:");
            for (Position position : baseLocation.getRegion().getPolygon().getPoints()) {
                System.out.print(position + ", ");
            }
            System.out.println();
        }

    }

    @Override
    public void onFrame() {
        OnFrameExecutor.getInstance().actOnFrame(game);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new TestBot1().run();
    }
}