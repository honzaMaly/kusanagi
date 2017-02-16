package cz.jan.maly.model.agent;

import bwapi.UnitType;
import cz.jan.maly.model.agent.action.ActInGameAction;
import cz.jan.maly.model.agent.action.GetGameObservationAction;
import cz.jan.maly.model.agent.action.GetPartOfCommonKnowledgeAction;
import cz.jan.maly.model.agent.action.ReasonAboutKnowledgeAction;
import cz.jan.maly.model.agent.action.game.Build;
import cz.jan.maly.model.agent.action.game.GatherResources;
import cz.jan.maly.model.agent.data.AgentsKnowledgeBase;
import cz.jan.maly.model.agent.implementation.AgentWithGameRepresentation;
import cz.jan.maly.model.metadata.Fact;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitType;
import cz.jan.maly.sfol.FormulaInterface;
import cz.jan.maly.sfol.factories.FormulaFactoryEnum;
import cz.jan.maly.sfol.factories.SingleBooleanFactFormulaFactoryEnums;
import cz.jan.maly.sfol.factories.SingleFactSetFormulaFactoryEnums;
import cz.jan.maly.sfol.factories.UnaryFormulaFactoryEnums;
import cz.jan.maly.utils.MyLogger;

import java.util.*;
import java.util.stream.Collectors;

import static cz.jan.maly.model.facts.Facts.*;

/**
 * Created by Jan on 17-Dec-16.
 */
public class Worker extends AgentWithGameRepresentation {
    private static final Random RANDOM = new Random();

    protected Worker(long timeBetweenCycles, AUnit unit) {
        super(timeBetweenCycles, unit);
        MyLogger.getLogger().info(unit.toString()+" registered.");
    }

    @Override
    protected void initializeKnowledgeOnCreation() {
        agentsKnowledgeBase.getAgentsOwnFactByKey(HEALTH).get().setContent(unit.getHP());
    }

    @Override
    protected AgentsKnowledgeBase setupAgentsKnowledge() {
        Set<KeyToFact> ownFactsToUse = new HashSet<>(), factsByOtherAgentToUse = new HashSet<>();
        ownFactsToUse.add(HEALTH);
        ownFactsToUse.add(MINERALS_IN_SIGHT);
        ownFactsToUse.add(IS_GATHERING_MINERALS);
        ownFactsToUse.add(MINING_MINERAL);
        ownFactsToUse.add(POSITION);
        ownFactsToUse.add(BUILD);
        return new AgentsKnowledgeBase(this, ownFactsToUse, factsByOtherAgentToUse);
    }

    @Override
    protected AgentActionCycleAbstract actionsDefinedByUser() {

        ActInGameAction buildBuilding = new ActInGameAction(this, new Build(agentsKnowledgeBase.getAgentsOwnFactByKey(BUILD).get(), agentsKnowledgeBase.getAgentsOwnFactByKey(POSITION).get()), true);

        ActInGameAction mineMinerals = new ActInGameAction(this, new GatherResources(agentsKnowledgeBase.getAgentsOwnFactByKey(MINING_MINERAL).get()), false);

        LinkedHashMap<FormulaInterface, AgentActionCycleAbstract> doAfterChoosingMineral = new LinkedHashMap<>();
        doAfterChoosingMineral.put(UnaryFormulaFactoryEnums.NEGATION.createExpression(SingleFactSetFormulaFactoryEnums.IS_EMPTY.createExpression(agentsKnowledgeBase.getAgentsOwnFactByKey(MINING_MINERAL).get())), mineMinerals);

        ReasonAboutKnowledgeAction chooseMinerals = new ReasonAboutKnowledgeAction(this, doAfterChoosingMineral, agentsKnowledgeToUpdate -> {
            List<AUnit> mineralsInSight = agentsKnowledgeBase.getAgentsOwnFactByKey(MINERALS_IN_SIGHT).get().getContent();
            if (!mineralsInSight.isEmpty()) {

                //pick random mineral in sight
                agentsKnowledgeBase.getAgentsOwnFactByKey(MINING_MINERAL).get().setContent(mineralsInSight.get(RANDOM.nextInt(mineralsInSight.size())));
                MyLogger.getLogger().info(unit.toString()+" going for minerals.");
            }
        });

        LinkedHashMap<FormulaInterface, AgentActionCycleAbstract> doAfterKnowledgeObtain = new LinkedHashMap<>();
        doAfterKnowledgeObtain.put(UnaryFormulaFactoryEnums.NEGATION.createExpression(SingleFactSetFormulaFactoryEnums.IS_EMPTY.createExpression(agentsKnowledgeBase.getAgentsOwnFactByKey(BUILD).get())), buildBuilding);
        doAfterKnowledgeObtain.put(UnaryFormulaFactoryEnums.NEGATION.createExpression(SingleBooleanFactFormulaFactoryEnums.IDENTITY.createExpression(agentsKnowledgeBase.getAgentsOwnFactByKey(IS_GATHERING_MINERALS).get())), chooseMinerals);

        //get knowledge from other agents
        GetPartOfCommonKnowledgeAction getPartOfCommonKnowledgeAction = new GetPartOfCommonKnowledgeAction(this, doAfterKnowledgeObtain, (workingCommonKnowledge, agentsKnowledgeToUpdate) -> {
            Optional<Fact<Map<Integer,AUnitType>>> toBuild = workingCommonKnowledge.getCloneOfFactOfAgentByKey(Commander.getInstance(), AGENTS_TO_BUILD);
            if (toBuild.isPresent()){
                AUnitType buildingType = toBuild.get().getContent().get(getId());
                if (buildingType != null) {
                    agentsKnowledgeBase.getAgentsOwnFactByKey(BUILD).get().setContent(buildingType);
                }
            }
        });

        LinkedHashMap<FormulaInterface, AgentActionCycleAbstract> doAfterObservation = new LinkedHashMap<>();
        doAfterObservation.put(FormulaFactoryEnum.TRUTH.createExpression(), getPartOfCommonKnowledgeAction);

        GetGameObservationAction sense = new GetGameObservationAction(this, doAfterObservation, (game, agentsKnowledgeToUpdateAgentsKnowledge, unit) -> {
            List<AUnit> mineralsInSight = unit.u().getUnitsInRadius(unit.u().getType().sightRange()).stream()
                    .filter(unit1 -> unit1.getType().equals(UnitType.Resource_Mineral_Field))
                    .map(AUnit::createFrom)
                    .collect(Collectors.toList());
            agentsKnowledgeBase.getAgentsOwnFactByKey(MINERALS_IN_SIGHT).get().setContent(mineralsInSight);
            agentsKnowledgeBase.getAgentsOwnFactByKey(HEALTH).get().setContent(unit.getHP());
            agentsKnowledgeBase.getAgentsOwnFactByKey(IS_GATHERING_MINERALS).get().setContent(unit.isGatheringMinerals());
            agentsKnowledgeBase.getAgentsOwnFactByKey(POSITION).get().setContent(unit.u().getTilePosition());
        });
        return sense;
    }

    @Override
    protected void actOnAgentRemoval(Agent agent) {

    }

}
