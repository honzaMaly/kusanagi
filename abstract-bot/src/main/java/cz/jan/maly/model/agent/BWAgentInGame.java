package cz.jan.maly.model.agent;

import bwapi.Game;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.service.implementation.BotFacade;

import static cz.jan.maly.model.FactsKeys.IS_UNIT;

/**
 * Extension of BWAgent to keep reference on actual unit in game
 * Created by Jan on 15-Mar-17.
 */
public class BWAgentInGame extends BWAgent {

    public BWAgentInGame(AgentType<Game> agentType, BotFacade botFacade, AUnit unit) {
        super(agentType, botFacade);

        //add itself to knowledge
        beliefs.updateFact(new Fact<>(unit, IS_UNIT));
    }
}
