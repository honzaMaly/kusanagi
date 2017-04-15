package cz.jan.maly.model.agent;

import cz.jan.maly.model.agent.types.AgentTypeUnit;
import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
import cz.jan.maly.service.implementation.BotFacade;

import static cz.jan.maly.model.bot.BasicFactsKeys.IS_UNIT;
import static cz.jan.maly.model.bot.BasicFactsKeys.REPRESENTS_UNIT;

/**
 * Agent for unit in game
 * Created by Jan on 05-Apr-17.
 */
public class AgentUnit extends AgentObservingGame<AgentTypeUnit> {

    public AgentUnit(AgentTypeUnit agentType, BotFacade botFacade, AUnitWithCommands unit) {
        super(agentType, botFacade);

        //add itself to knowledge
        beliefs.updateFact(IS_UNIT, unit);
        beliefs.updateFact(REPRESENTS_UNIT, unit);
    }
}
