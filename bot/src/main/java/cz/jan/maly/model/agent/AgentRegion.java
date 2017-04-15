package cz.jan.maly.model.agent;

import bwta.Region;
import cz.jan.maly.model.agent.types.AgentTypeRegion;
import cz.jan.maly.service.implementation.BotFacade;

import static cz.jan.maly.model.bot.BasicFactsKeys.IS_REGION;

/**
 * Agent representing region
 * INSTANCE OF THIS AGENT SHOULD NOT SEND ANY COMMAND TO GAME. ONLY REASON
 * Created by Jan on 05-Apr-17.
 */
public class AgentRegion extends AbstractAgent<AgentTypeRegion> {
    public AgentRegion(AgentTypeRegion agentType, BotFacade botFacade, Region region) {
        super(agentType, botFacade);

        //add itself to knowledge
        beliefs.updateFact(IS_REGION, region);
    }
}
