package cz.jan.maly.model.agent;

import bwta.BaseLocation;
import cz.jan.maly.model.agent.types.AgentTypeBaseLocation;
import cz.jan.maly.model.game.wrappers.ABaseLocationWrapper;
import cz.jan.maly.service.implementation.BotFacade;

import static cz.jan.maly.model.BasicFactsKeys.IS_BASE_LOCATION;
import static cz.jan.maly.model.BasicFactsKeys.MADE_OBSERVATION_IN_FRAME;

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
    }
}
