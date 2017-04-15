package cz.jan.maly.model.agent;

import cz.jan.maly.model.ResponseReceiverInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.service.implementation.BotFacade;
import cz.jan.maly.utils.MyLogger;

/**
 * AbstractAgent is agent which makes no observation and send no commands to game. It only reasons and share desires
 * Created by Jan on 05-Apr-17.
 */
class AbstractAgent<K extends AgentType> extends Agent<K> {
    AbstractAgent(K agentType, BotFacade botFacade) {
        super(agentType, botFacade.getMasFacade());
    }

    @Override
    public boolean sendCommandToExecute(ActCommand<?> command, ResponseReceiverInterface<Boolean> responseReceiver) {
        MyLogger.getLogger().warning("Trying to send command on behalf of abstract agent.");
        throw new RuntimeException("Trying to send command on behalf of abstract agent.");
    }
}
