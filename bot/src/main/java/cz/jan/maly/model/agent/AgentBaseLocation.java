package cz.jan.maly.model.agent;

import bwta.BaseLocation;
import cz.jan.maly.model.agent.types.AgentTypeBaseLocation;
import cz.jan.maly.model.game.wrappers.ABaseLocationWrapper;
import cz.jan.maly.service.implementation.BotFacade;

import static cz.jan.maly.model.bot.FactKeys.*;

/**
 * Agent for base location in game
 * INSTANCE OF THIS AGENT SHOULD NOT SEND ANY COMMAND TO GAME. ONLY REASON AND OBSERVE
 * Created by Jan on 05-Apr-17.
 */
public class AgentBaseLocation extends AgentObservingGame<AgentTypeBaseLocation> {

    public AgentBaseLocation(AgentTypeBaseLocation agentType, BotFacade botFacade, BaseLocation location) {
        super(agentType, botFacade);
        //add itself to knowledge
        beliefs.updateFact(IS_BASE_LOCATION, ABaseLocationWrapper.wrap(location));
        AgentTypeBaseLocation.updateKnowledgeAboutResources(location, beliefs, 0);
        beliefs.updateFact(MADE_OBSERVATION_IN_FRAME, 0);
        beliefs.updateFact(IS_MINERAL_ONLY, location.isMineralOnly());
        beliefs.updateFact(IS_ISLAND, location.isIsland());
        beliefs.updateFact(IS_START_LOCATION, location.isStartLocation());
        beliefs.updateFact(BASE_TO_MOVE, beliefs.returnFactValueForGivenKey(IS_BASE_LOCATION).get());
    }
}
