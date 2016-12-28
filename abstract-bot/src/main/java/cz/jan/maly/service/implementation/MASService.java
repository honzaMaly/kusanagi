package cz.jan.maly.service.implementation;

import bwapi.*;
import bwta.BWTA;
import cz.jan.maly.model.agent.Agent;
import cz.jan.maly.model.agent.implementation.AgentWithGameRepresentation;
import cz.jan.maly.service.AbstractAgentInitializerInterface;
import cz.jan.maly.service.AgentUnitFactoryInterface;
import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

import java.io.IOException;
import java.util.Optional;

import static cz.jan.maly.utils.FrameworkUtils.getGameDefaultSpeed;

/**
 * MAS service is facade of framework. It registers all managers (for agents' references) and listen to game to take
 * appropriate actions.
 * Created by Jan on 28-Dec-16.
 */
@Getter
public class MASService extends DefaultBWListener {
    private AgentsManager agentsManager;
    private GameCommandExecutor gameCommandExecutor;
    private MediatorForSharingRequests mediatorForSharingRequests;
    private MediatorFoSharingKnowledge mediatorFoSharingKnowledge;

    //fields provided by user
    private final AgentUnitFactoryInterface agentUnitFactory;
    private final AbstractAgentInitializerInterface abstractAgentInitializer;

    //game related fields
    private Mirror mirror = new Mirror();
    private Game game;

    //todo strategy to identify player - for replays
    private Player self;

    public MASService(AgentUnitFactoryInterface agentUnitFactory, AbstractAgentInitializerInterface abstractAgentInitializer) {
        this.agentUnitFactory = agentUnitFactory;
        this.abstractAgentInitializer = abstractAgentInitializer;
    }

    @Override
    public void onStart() {

        //start framework
        agentsManager = new AgentsManager();
        gameCommandExecutor = new GameCommandExecutor();
        mediatorForSharingRequests = new MediatorForSharingRequests();
        mediatorFoSharingKnowledge = new MediatorFoSharingKnowledge();

        //initialize game related data
        game = mirror.getGame();
        self = game.self();

        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        MyLogger.getLogger().info("Analyzing map");
        BWTA.readMap();
        BWTA.analyze();
        MyLogger.getLogger().info("Map data ready");

        //create all abstract agents
        abstractAgentInitializer.initializeAbstractAgentOnStartOfTheGame();

        //speed up game to setup value
        game.setLocalSpeed(getGameDefaultSpeed());
        MyLogger.getLogger().info("Local game speed set to " + getGameDefaultSpeed());
    }

    @Override
    public void onUnitCreate(Unit unit) {
        if (unit.getPlayer().equals(self)) {
            agentUnitFactory.createAgentForUnit(unit);
            MyLogger.getLogger().info("New unit created " + unit.getType());
        }
    }

    @Override
    public void onUnitDestroy(Unit unit) {
        if (unit.getPlayer().equals(self)) {
            Optional<AgentWithGameRepresentation> agent = agentsManager.getRelevantAgentWithGameRepresentation(agentsToFilterFrom -> agentsToFilterFrom
                    .filter(agentWithGameRepresentation -> agentWithGameRepresentation.getUnit().u().equals(unit))
                    .findFirst(), AgentWithGameRepresentation.class);
            agent.ifPresent(Agent::terminateAgent);
            MyLogger.getLogger().info("Unit destroyed " + unit.getType());
        }
    }

    @Override
    public void onUnitMorph(Unit unit) {
        if (unit.getPlayer().equals(self)) {
            Optional<AgentWithGameRepresentation> agent = agentsManager.getRelevantAgentWithGameRepresentation(agentsToFilterFrom -> agentsToFilterFrom
                    .filter(agentWithGameRepresentation -> agentWithGameRepresentation.getUnit().u().equals(unit))
                    .findFirst(), AgentWithGameRepresentation.class);
            agent.ifPresent(Agent::terminateAgent);

            //create new agent
            agentUnitFactory.createAgentForUnit(unit);
            MyLogger.getLogger().info("New unit morphed " + unit.getType());
        }
    }

    public void run() throws IOException, InterruptedException {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onEnd(boolean b) {
        agentsManager.terminateAllAgents();
    }
}
