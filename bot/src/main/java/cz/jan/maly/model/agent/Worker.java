package cz.jan.maly.model.agent;

import bwapi.UnitType;
import cz.jan.maly.model.KeyToFact;
import cz.jan.maly.model.agent.action.ActInGameAction;
import cz.jan.maly.model.agent.action.GetGameObservationAction;
import cz.jan.maly.model.agent.action.game.GatherResources;
import cz.jan.maly.model.game.wrappers.AUnit;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.jan.maly.model.facts.Facts.*;

/**
 * Created by Jan on 17-Dec-16.
 */
public class Worker extends AgentWithGameRepresentation {

    protected Worker(long timeBetweenCycles, AUnit unit) {
        super(timeBetweenCycles, unit);
    }

    @Override
    protected void initializeKnowledgeOnCreation() {
        agentsKnowledge.getAgentsOwnFactByKey(HEALTH).get().setContent(unit.getHP());
    }

    @Override
    protected AgentsKnowledge setupAgentsKnowledge() {
        Set<KeyToFact> ownFactsToUse = new HashSet<>(), factsByOtherAgentToUse = new HashSet<>();
        ownFactsToUse.add(HEALTH);
        ownFactsToUse.add(MINING_MINERAL);
        ownFactsToUse.add(SET_OF_MINERALS);
        factsByOtherAgentToUse.add(SET_OF_MINERALS);
        return new AgentsKnowledge(this, ownFactsToUse, factsByOtherAgentToUse);
    }

    @Override
    protected AgentActionCycleAbstract composeWorkflow() {

        //observe surrounding for minerals -> get minerals from general knowledge -> naturals found, go mine closest
        ActInGameAction mineMinerals = new ActInGameAction(this, new GatherResources(unit, agentsKnowledge.getAgentsOwnFactByKey(MINING_MINERAL).get()));
        GetGameObservationAction lookForMinerals = new GetGameObservationAction(this, (game, agentsKnowledgeToUpdateAgentsKnowledge, unit) -> {
            List<AUnit> mineralsInSight = unit.u().getUnitsInRadius(unit.u().getType().sightRange()).stream()
                    .filter(unit1 -> unit1.getType().equals(UnitType.Resource_Mineral_Field))
                    .map(AUnit::createFrom)
                    .collect(Collectors.toList());
            agentsKnowledge.getAgentsOwnFactByKey(SET_OF_MINERALS).get().setContent(mineralsInSight);
        });
        return lookForMinerals;
    }

    @Override
    protected void actOnAgentRemoval(Agent agent) {

    }
}
