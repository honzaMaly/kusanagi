package cz.jan.maly.model.agent;

import bwapi.Race;
import cz.jan.maly.model.agent.types.AgentTypePlayer;
import cz.jan.maly.model.game.wrappers.APlayer;
import cz.jan.maly.model.game.wrappers.ARace;
import cz.jan.maly.service.implementation.BotFacade;

import static cz.jan.maly.model.bot.FactKeys.ENEMY_RACE;
import static cz.jan.maly.model.bot.FactKeys.IS_PLAYER;

/**
 * Agent to represent "player" - to access field of Player
 * Created by Jan on 05-Apr-17.
 */
public class AgentPlayer extends AgentObservingGame<AgentTypePlayer> {
    public AgentPlayer(AgentTypePlayer agentType, BotFacade botFacade, APlayer aPlayer, Race enemyStartingRace) {
        super(agentType, botFacade);

        //add itself to knowledge
        beliefs.updateFact(IS_PLAYER, aPlayer);
        if (!enemyStartingRace.equals(Race.Unknown)) {
            beliefs.updateFact(ENEMY_RACE, ARace.getRace(enemyStartingRace));
        }
    }
}
