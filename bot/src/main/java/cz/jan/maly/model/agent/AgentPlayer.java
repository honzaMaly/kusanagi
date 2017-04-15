package cz.jan.maly.model.agent;

import bwapi.Race;
import cz.jan.maly.model.agent.types.AgentTypePlayer;
import cz.jan.maly.model.game.wrappers.APlayer;
import cz.jan.maly.service.implementation.BotFacade;

import static cz.jan.maly.model.bot.BasicFactsKeys.ENEMY_RACE;
import static cz.jan.maly.model.bot.BasicFactsKeys.IS_PLAYER;

/**
 * Agent to represent "player" - to access field of Player
 * Created by Jan on 05-Apr-17.
 */
public class AgentPlayer extends AgentObservingGame<AgentTypePlayer> {
    public AgentPlayer(AgentTypePlayer agentType, BotFacade botFacade, APlayer aPlayer, Race enemyStartingRace) {
        super(agentType, botFacade);

        //add itself to knowledge
        beliefs.updateFact(IS_PLAYER, aPlayer);
        beliefs.updateFact(ENEMY_RACE, enemyStartingRace);
    }
}
