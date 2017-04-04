package cz.jan.maly.model.agent;

import bwapi.Game;
import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.model.metadata.AgentTypeMakingObservations;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ObservingCommand;
import cz.jan.maly.service.implementation.BotFacade;
import cz.jan.maly.service.implementation.GameCommandExecutor;
import cz.jan.maly.utils.MyLogger;

import static cz.jan.maly.model.BasicFactsKeys.IS_UNIT;
import static cz.jan.maly.model.BasicFactsKeys.REPRESENTS_UNIT;

/**
 * BWAgentInGame is agent which makes observations of game
 * Created by Jan on 15-Mar-17.
 */
public class BWAgentInGame extends Agent.MakingObservation<Game> {
    private final GameCommandExecutor gameCommandExecutor;

    //single definition of command to observe to be used by all agents of this type
    private static final ObservingCommand<Game> OBSERVING_COMMAND = (memory, environment) -> {
        if (!memory.returnFactValueForGivenKey(IS_UNIT).isPresent()) {
            MyLogger.getLogger().warning("Trying to access commendable unit but it is not present.");
            throw new RuntimeException("Trying to access commendable unit but it is not present.");
        }

        //update fields by creating new instance
        AUnitWithCommands unitWithCommands = memory.returnFactValueForGivenKey(IS_UNIT).get().makeObservationOfEnvironment(environment.getFrameCount());

        //add updated version of itself to knowledge
        memory.updateFact(new Fact<>(unitWithCommands, IS_UNIT));
        memory.updateFact(new Fact<>(unitWithCommands, REPRESENTS_UNIT));
        return true;
    };

    public BWAgentInGame(AgentTypeMakingObservations<Game> agentType, BotFacade botFacade, AUnitWithCommands unit) {
        super(agentType, botFacade.getMasFacade());
        this.gameCommandExecutor = botFacade.getGameCommandExecutor();

        //add itself to knowledge
        beliefs.updateFact(new Fact<>(unit, IS_UNIT));
        beliefs.updateFact(new Fact<>(unit, REPRESENTS_UNIT));
    }

    @Override
    public boolean sendCommandToExecute(ActCommand<?> command, ResponseReceiverInterface<Boolean> responseReceiver) {
        return gameCommandExecutor.addCommandToAct(command, beliefs, responseReceiver, agentType);
    }

    @Override
    protected boolean requestObservation(ObservingCommand<Game> observingCommand, ResponseReceiverInterface<Boolean> responseReceiver) {
        return gameCommandExecutor.addCommandToObserve(observingCommand, beliefs, responseReceiver, agentType);
    }

    /**
     * Get observing command
     *
     * @return
     */
    public static ObservingCommand<Game> observingCommand() {
        return OBSERVING_COMMAND;
    }
}
