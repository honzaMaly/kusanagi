package cz.jan.maly.model.agent;

import bwapi.Game;
import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.planing.command.ActCommandForIntention;
import cz.jan.maly.model.planing.command.ObservingCommand;
import cz.jan.maly.service.implementation.BotFacade;
import cz.jan.maly.service.implementation.GameCommandExecutor;

/**
 * Implementation of BW Agent
 * Created by Jan on 14-Mar-17.
 */
public class BWAgent extends Agent<Game> {
    private final GameCommandExecutor gameCommandExecutor;

    protected BWAgent(AgentType<Game> agentType, BotFacade botFacade) {
        super(agentType, botFacade.getMasFacade());
        this.gameCommandExecutor = botFacade.getGameCommandExecutor();
    }

    @Override
    public boolean sendCommandToExecute(ActCommandForIntention<?> command, ResponseReceiverInterface<Boolean> responseReceiver) {
        return gameCommandExecutor.addCommandToAct(command, beliefs, responseReceiver, agentType);
    }

    @Override
    protected boolean requestObservation(ObservingCommand<Game> observingCommand, ResponseReceiverInterface<Boolean> responseReceiver) {
        return gameCommandExecutor.addCommandToObserve(observingCommand, beliefs, responseReceiver, agentType);
    }
}
