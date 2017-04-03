package cz.jan.maly.model.agent;

import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.service.implementation.BotFacade;
import cz.jan.maly.service.implementation.GameCommandExecutor;

/**
 * Implementation of BW Agent making no game observations
 * Created by Jan on 14-Mar-17.
 */
public class BWAgent extends Agent<AgentType> {
    private final GameCommandExecutor gameCommandExecutor;

    protected BWAgent(AgentType agentType, BotFacade botFacade) {
        super(agentType, botFacade.getMasFacade());
        this.gameCommandExecutor = botFacade.getGameCommandExecutor();
    }

    @Override
    public boolean sendCommandToExecute(ActCommand<?> command, ResponseReceiverInterface<Boolean> responseReceiver) {
        return gameCommandExecutor.addCommandToAct(command, beliefs, responseReceiver, agentType);
    }
}
