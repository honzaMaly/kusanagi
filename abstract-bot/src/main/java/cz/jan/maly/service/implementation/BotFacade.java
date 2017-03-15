package cz.jan.maly.service.implementation;

import bwapi.*;
import bwta.BWTA;
import cz.jan.maly.model.agent.BWAgentInGame;
import cz.jan.maly.service.AgentUnitFactoryInterface;
import cz.jan.maly.service.MASFacade;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Facade for bot.
 * Created by Jan on 28-Dec-16.
 */
@Getter
public class BotFacade extends DefaultBWListener {

    //keep track of agent units
    private final Map<Unit, BWAgentInGame> agentsWithGameRepresentation = new HashMap<>();

    //facade for MAS
    private MASFacade<Game> masFacade;

    @Setter
    @Getter
    private static int gameDefaultSpeed = 100;

    @Setter
    @Getter
    private static long maxFrameExecutionTime = 30;

    //executor of game commands
    private GameCommandExecutor gameCommandExecutor;

    //fields provided by user
    private final AgentUnitFactoryInterface agentUnitFactory;
//    private final AbstractAgentInitializerInterface abstractAgentInitializer;

    //game related fields
    private Mirror mirror = new Mirror();
    private Game game;

    private Player self;

    public BotFacade(AgentUnitFactoryInterface agentUnitFactory) {
        this.agentUnitFactory = agentUnitFactory;
//        this.abstractAgentInitializer = abstractAgentInitializer;
    }

    @Override
    public void onStart() {

        masFacade = new MASFacade<>();

        //initialize game related data
        game = mirror.getGame();
        self = game.self();

        //initialize command executor
        gameCommandExecutor = new GameCommandExecutor(game);

        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        MyLogger.getLogger().info("Analyzing map");
        BWTA.readMap();
        BWTA.analyze();

        MyLogger.getLogger().info("Map data ready");

        //create all abstract agents
//        abstractAgentInitializer.initializeAbstractAgentOnStartOfTheGame();

        //speed up game to setup value
        game.setLocalSpeed(getGameDefaultSpeed());
        MyLogger.getLogger().info("Local game speed set to " + getGameDefaultSpeed());
    }

    @Override
    public void onUnitCreate(Unit unit) {
        if (unit.getPlayer().equals(self)) {
            Optional<BWAgentInGame> agent = agentUnitFactory.createAgentForUnit(unit, this);
            agent.ifPresent(bwAgentInGame -> {
                agentsWithGameRepresentation.put(unit, bwAgentInGame);
                masFacade.addAgentToSystem(bwAgentInGame);
            });
        }
    }

    @Override
    public void onUnitDestroy(Unit unit) {
        if (unit.getPlayer().equals(self)) {
            Optional<BWAgentInGame> agent = Optional.ofNullable(agentsWithGameRepresentation.remove(unit));
            agent.ifPresent(bwAgentInGame1 -> masFacade.removeAgentFromSystem(bwAgentInGame1));
        }
    }

    @Override
    public void onUnitMorph(Unit unit) {
        if (unit.getPlayer().equals(self)) {
            onUnitDestroy(unit);
            onUnitCreate(unit);
        }
    }

    public void run() throws IOException, InterruptedException {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onEnd(boolean b) {
        agentsWithGameRepresentation.clear();
        masFacade.terminate();
    }

    @Override
    public void onFrame() {
        gameCommandExecutor.actOnFrame();
    }
}
