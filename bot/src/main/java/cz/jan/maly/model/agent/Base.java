package cz.jan.maly.model.agent;

import bwapi.UnitType;
import cz.jan.maly.model.agent.action.GetGameObservationAction;
import cz.jan.maly.model.agent.action.UpdateCommonKnowledgeAction;
import cz.jan.maly.model.agent.data.AgentsKnowledgeBase;
import cz.jan.maly.model.agent.implementation.AgentWithGameRepresentation;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.sflo.FormulaInterface;
import cz.jan.maly.model.sflo.factories.FormulaFactoryEnum;
import cz.jan.maly.utils.MyLogger;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.jan.maly.model.facts.Facts.*;

/**
 * Base is agent with Hatchery unit representation. It keeps reference on everything related to base.
 * Created by Jan on 17-Dec-16.
 */
public class Base extends AgentWithGameRepresentation {

    protected Base(long timeBetweenCycles, AUnit unit) {
        super(timeBetweenCycles, unit);
        MyLogger.getLogger().info(unit.toString()+" registered.");
    }

    @Override
    protected AgentsKnowledgeBase setupAgentsKnowledge() {
        Set<KeyToFact> ownFactsToUse = new HashSet<>(), factsByOtherAgentToUse = new HashSet<>();
        ownFactsToUse.add(BUILDINGS_IN_EXPANSION);
        ownFactsToUse.add(AVAILABLE_LARVAE);
        ownFactsToUse.add(HEALTH);
        ownFactsToUse.add(ENEMIES_AROUND_BASE);
        return new AgentsKnowledgeBase(this, ownFactsToUse, factsByOtherAgentToUse);
    }

    @Override
    protected AgentActionCycleAbstract actionsDefinedByUser() {

        UpdateCommonKnowledgeAction updateCommonKnowledgeAction = new UpdateCommonKnowledgeAction(this);

        LinkedHashMap<FormulaInterface, AgentActionCycleAbstract> doAfterObservation = new LinkedHashMap<>();
        doAfterObservation.put(FormulaFactoryEnum.TRUTH.createExpression(), updateCommonKnowledgeAction);

        GetGameObservationAction sense = new GetGameObservationAction(this, doAfterObservation, (game, agentsKnowledgeToUpdateAgentsKnowledge, unit) -> {
            agentsKnowledgeBase.getAgentsOwnFactByKey(HEALTH).get().setContent(unit.getHP());
            Set<AUnit> larvae = unit.u().getUnitsInRadius(unit.u().getType().sightRange()).stream()
                    .filter(unit1 -> unit1.getType().equals(UnitType.Zerg_Larva))
                    .map(AUnit::createFrom)
                    .collect(Collectors.toSet());
            agentsKnowledgeBase.getAgentsOwnFactByKey(AVAILABLE_LARVAE).get().setContent(larvae);
            Set<AUnit> buildings = unit.u().getUnitsInRadius(unit.u().getType().sightRange()).stream()
                    .filter(unit1 -> unit1.getType().equals(UnitType.Zerg_Spawning_Pool))
                    .map(AUnit::createFrom)
                    .collect(Collectors.toSet());
            agentsKnowledgeBase.getAgentsOwnFactByKey(BUILDINGS_IN_EXPANSION).get().setContent(buildings);
            Boolean areThereAnyEnemies = unit.u().getUnitsInRadius(unit.u().getType().sightRange()).stream()
                    .anyMatch(unit1 -> unit1.getPlayer().isEnemy(unit.getPlayer()));
            agentsKnowledgeBase.getAgentsOwnFactByKey(ENEMIES_AROUND_BASE).get().setContent(areThereAnyEnemies);
        });

        return sense;
    }

    @Override
    protected void actOnAgentRemoval(Agent agent) {

    }

    @Override
    protected void initializeKnowledgeOnCreation() {

    }
}
