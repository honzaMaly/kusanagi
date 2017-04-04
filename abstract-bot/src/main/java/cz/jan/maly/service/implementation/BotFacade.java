package cz.jan.maly.service.implementation;

import bwapi.*;
import bwta.BWTA;
import cz.jan.maly.model.agent.BWAgentInGame;
import cz.jan.maly.model.game.wrappers.*;
import cz.jan.maly.service.AgentUnitHandler;
import cz.jan.maly.service.MASFacade;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Facade for bot.
 * Created by Jan on 28-Dec-16.
 */
@Getter
public class BotFacade extends DefaultBWListener {

    //keep track of agent units
    private final Map<Integer, BWAgentInGame> agentsWithGameRepresentation = new HashMap<>();

    //facade for MAS
    private MASFacade masFacade;

    @Setter
    @Getter
    private static int gameDefaultSpeed = 100;

    @Setter
    @Getter
    private static long maxFrameExecutionTime = 30;

    @Setter
    @Getter
    private static long refreshInfoAboutOwnUnitAfterFrames = 2;

    @Setter
    @Getter
    private static long refreshInfoAboutEnemyUnitAfterFrames = 4;

    @Setter
    @Getter
    private static long refreshInfoAboutResourceUnitAfterFrames = 20;

    //executor of game commands
    private GameCommandExecutor gameCommandExecutor;

    //fields provided by user
    private final AgentUnitHandler agentUnitFactory;
//    private final AbstractAgentInitializerInterface abstractAgentInitializer;

    //game related fields
    private Mirror mirror = new Mirror();

    @Getter
    private Game game;

    @Getter
    private Player self;

    public BotFacade(AgentUnitHandler agentUnitFactory) {
        this.agentUnitFactory = agentUnitFactory;
//        this.abstractAgentInitializer = abstractAgentInitializer;
        MyLogger.setLoggingLevel(Level.WARNING);
    }

    @Override
    public void onStart() {
        UnitWrapperFactory.clearCache();
        masFacade = new MASFacade();

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

        //init types
        AUpgradeTypeWrapper.initTypes();
        AUnitTypeWrapper.initTypes();
        ATechTypeWrapper.initTypes();
        AWeaponTypeWrapper.initTypes();

        //speed up game to setup value
        game.setLocalSpeed(getGameDefaultSpeed());
        MyLogger.getLogger().info("Local game speed set to " + getGameDefaultSpeed());
    }

    @Override
    public synchronized void onUnitCreate(Unit unit) {
        if (self.getID() == unit.getPlayer().getID()) {
            Optional<BWAgentInGame> agent = agentUnitFactory.createAgentForUnit(unit, this, game.getFrameCount());
            agent.ifPresent(bwAgentInGame -> {
                agentsWithGameRepresentation.put(unit.getID(), bwAgentInGame);
                masFacade.addAgentToSystem(bwAgentInGame);

                //start agent
                bwAgentInGame.run();
            });
        }
    }

    @Override
    public void onUnitDestroy(Unit unit) {
        if (self.getID() == unit.getPlayer().getID()) {
            Optional<BWAgentInGame> agent = Optional.ofNullable(agentsWithGameRepresentation.remove(unit.getID()));
            agent.ifPresent(bwAgentInGame -> masFacade.removeAgentFromSystem(bwAgentInGame));
        }
        UnitWrapperFactory.unitDied(unit);
    }

    @Override
    public void onUnitMorph(Unit unit) {
        if (self.getID() == unit.getPlayer().getID()) {
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
        try {
            gameCommandExecutor.actOnFrame();
        }
        // === Catch any exception that occur not to "kill" the bot with one trivial error ===================
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO handle more events - unit renegade, visibility
}
