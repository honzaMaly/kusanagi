package cz.jan.maly.service.implementation;

import bwapi.*;
import bwta.BWTA;
import cz.jan.maly.model.agent.AgentPlayer;
import cz.jan.maly.model.agent.AgentUnit;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.game.wrappers.*;
import cz.jan.maly.service.AgentUnitHandler;
import cz.jan.maly.service.LocationInitializer;
import cz.jan.maly.service.MASFacade;
import cz.jan.maly.service.PlayerInitializer;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Facade for bot.
 * Created by Jan on 28-Dec-16.
 */
@Getter
public class BotFacade extends DefaultBWListener {

    //TODO !!!THIS IS HACK DO NOT USE INSIDE OTHER COMMAND INTERACTING WITH GAME!!!
    //class to handle additional commands with observations requests
    public static AdditionalCommandToObserveGameProcessor ADDITIONAL_OBSERVATIONS_PROCESSOR;

    //keep track of agent units
    private final Map<Integer, AgentUnit> agentsWithGameRepresentation = new HashMap<>();

    //facade for MAS
    private MASFacade masFacade;

    @Setter
    @Getter
    private static int gameDefaultSpeed = 20;

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
    private final AgentUnitFactoryCreationStrategy agentUnitFactoryCreationStrategy;
    private final PlayerInitializer playerInitializer;
    private final LocationInitializer locationInitializer;

    //this is created with new game
    private AgentUnitHandler agentUnitFactory;

    //game related fields
    private Mirror mirror = new Mirror();

    @Getter
    private Game game;

    @Getter
    private Player self;

    public BotFacade(AgentUnitFactoryCreationStrategy agentUnitFactoryCreationStrategy, PlayerInitializer playerInitializer, LocationInitializer locationInitializer) {
        this.agentUnitFactoryCreationStrategy = agentUnitFactoryCreationStrategy;
        this.playerInitializer = playerInitializer;
        this.locationInitializer = locationInitializer;
        MyLogger.setLoggingLevel(Level.WARNING);
    }

    @Override
    public void onStart() {
        UnitWrapperFactory.clearCache();
        WrapperTypeFactory.clearCache();

        //initialize game related data
        game = mirror.getGame();
        self = game.self();

        //initialize command executor
        gameCommandExecutor = new GameCommandExecutor(game);
        masFacade = new MASFacade(() -> gameCommandExecutor.getCountOfPassedFrames());
        ADDITIONAL_OBSERVATIONS_PROCESSOR = new AdditionalCommandToObserveGameProcessor(gameCommandExecutor);
        agentUnitFactory = agentUnitFactoryCreationStrategy.createFactory();

        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        MyLogger.getLogger().info("Analyzing map");
        BWTA.readMap();
        BWTA.analyze();

        MyLogger.getLogger().info("Map data ready");

        //reference on agents
        List<Agent<?>> agentsToRun = new ArrayList<>();

        //todo - create abstract agents?

        //init player as another agent
        Optional<APlayer> player = APlayer.wrapPlayer(self);
        if (!player.isPresent()) {
            MyLogger.getLogger().warning("Could not initiate player.");
            throw new RuntimeException("Could not initiate player.");
        }
        AgentPlayer agentPlayer = playerInitializer.createAgentForPlayer(player.get(), this, game.enemy().getRace());
        masFacade.addAgentToSystem(agentPlayer);
        agentsToRun.add(agentPlayer);

        //init base location as agents
        agentsToRun.addAll(BWTA.getBaseLocations().stream()
                .map(location -> locationInitializer.createAgent(location, this))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet()));

        //init regions
        agentsToRun.addAll(BWTA.getRegions().stream()
                .map(location -> locationInitializer.createAgent(location, this))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet()));

        //speed up game to setup value
        game.setLocalSpeed(getGameDefaultSpeed());
        MyLogger.getLogger().info("Local game speed set to " + getGameDefaultSpeed());

        //run new agents
        agentsToRun.forEach(Agent::run);
    }

    @Override
    public void onUnitCreate(Unit unit) {
        if (self.getID() == unit.getPlayer().getID()) {
            Optional<AgentUnit> agent = agentUnitFactory.createAgentForUnit(unit, this, game.getFrameCount());
            agent.ifPresent(agentObservingGame -> {
                agentsWithGameRepresentation.put(unit.getID(), agentObservingGame);
                masFacade.addAgentToSystem(agentObservingGame);

                //start agent
                agentObservingGame.run();
            });
        }
    }

    @Override
    public void onUnitDestroy(Unit unit) {
        if (self.getID() == unit.getPlayer().getID()) {
            Optional<AgentUnit> agent = Optional.ofNullable(agentsWithGameRepresentation.remove(unit.getID()));
            agent.ifPresent(agentObservingGame -> masFacade.removeAgentFromSystem(agentObservingGame));
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

    /**
     * Contract for strategy to create new AgentUnitFactory for new game
     */
    public interface AgentUnitFactoryCreationStrategy {

        /**
         * Creates new factory
         *
         * @return
         */
        AgentUnitHandler createFactory();
    }
}
