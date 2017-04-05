package cz.jan.maly.model.agent;

import bwapi.Game;
import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.AgentTypeMakingObservations;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ObservingCommand;
import cz.jan.maly.service.implementation.BotFacade;
import cz.jan.maly.service.implementation.GameCommandExecutor;

/**
 * AgentObservingGame is agent which makes observations of BW game
 * Created by Jan on 15-Mar-17.
 */
class AgentObservingGame<K extends AgentTypeMakingObservations<Game>> extends Agent.MakingObservation<Game> {
    private final GameCommandExecutor gameCommandExecutor;

    AgentObservingGame(K agentType, BotFacade botFacade) {
        super(agentType, botFacade.getMasFacade());
        this.gameCommandExecutor = botFacade.getGameCommandExecutor();
    }

    @Override
    public boolean sendCommandToExecute(ActCommand<?> command, ResponseReceiverInterface<Boolean> responseReceiver) {
        return gameCommandExecutor.addCommandToAct(command, beliefs, responseReceiver, agentType);
    }

    @Override
    protected boolean requestObservation(ObservingCommand<Game> observingCommand, ResponseReceiverInterface<Boolean> responseReceiver) {
        return gameCommandExecutor.addCommandToObserve(observingCommand, beliefs, responseReceiver, agentType);
    }
}
