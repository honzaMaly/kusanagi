package cz.jan.maly.model.agent;

import bwapi.UnitType;
import cz.jan.maly.model.Fact;
import cz.jan.maly.model.KeyToFact;
import cz.jan.maly.model.agent.action.ActInGameAction;
import cz.jan.maly.model.agent.action.GetGameObservationAction;
import cz.jan.maly.model.agent.action.GetPartOfCommonKnowledgeAction;
import cz.jan.maly.model.agent.action.ReasonAboutKnowledgeAction;
import cz.jan.maly.model.agent.action.game.Build;
import cz.jan.maly.model.agent.action.game.GatherResources;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitType;
import cz.jan.maly.model.sflo.TermInterface;
import cz.jan.maly.model.sflo.factories.SingleBooleanFactTermFactoryEnums;
import cz.jan.maly.model.sflo.factories.SingleFactTermFactoryEnums;
import cz.jan.maly.model.sflo.factories.TermFactoryEnum;
import cz.jan.maly.model.sflo.factories.UnaryTermFactoryEnums;
import cz.jan.maly.service.MyLogger;

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
        agentsKnowledge.getAgentsOwnFactByKey(HEALTH).get().setContent(unit.getHP());
    }

    @Override
    protected AgentsKnowledge setupAgentsKnowledge() {
        Set<KeyToFact> ownFactsToUse = new HashSet<>(), factsByOtherAgentToUse = new HashSet<>();
        ownFactsToUse.add(HEALTH);
        ownFactsToUse.add(MINERALS_IN_SIGHT);
        ownFactsToUse.add(IS_GATHERING_MINERALS);
        ownFactsToUse.add(MINING_MINERAL);
        ownFactsToUse.add(POSITION);
        ownFactsToUse.add(BUILD);
        return new AgentsKnowledge(this, ownFactsToUse, factsByOtherAgentToUse);
    }

    @Override
    protected AgentActionCycleAbstract composeWorkflow() {

        ActInGameAction buildBuilding = new ActInGameAction(this, new Build(agentsKnowledge.getAgentsOwnFactByKey(BUILD).get(), agentsKnowledge.getAgentsOwnFactByKey(POSITION).get()), true);

        ActInGameAction mineMinerals = new ActInGameAction(this, new GatherResources(agentsKnowledge.getAgentsOwnFactByKey(MINING_MINERAL).get()), false);

        LinkedHashMap<TermInterface, AgentActionCycleAbstract> doAfterChoosingMineral = new LinkedHashMap<>();
        doAfterChoosingMineral.put(UnaryTermFactoryEnums.NEGATION.createExpression(SingleFactTermFactoryEnums.IS_EMPTY.createExpression(agentsKnowledge.getAgentsOwnFactByKey(MINING_MINERAL).get())), mineMinerals);

        ReasonAboutKnowledgeAction chooseMinerals = new ReasonAboutKnowledgeAction(this, doAfterChoosingMineral, agentsKnowledgeToUpdate -> {
            List<AUnit> mineralsInSight = agentsKnowledge.getAgentsOwnFactByKey(MINERALS_IN_SIGHT).get().getContent();
            if (!mineralsInSight.isEmpty()) {

                //pick random mineral in sight
                agentsKnowledge.getAgentsOwnFactByKey(MINING_MINERAL).get().setContent(mineralsInSight.get(RANDOM.nextInt(mineralsInSight.size())));
                MyLogger.getLogger().info(unit.toString()+" going for minerals.");
            }
        });

        LinkedHashMap<TermInterface, AgentActionCycleAbstract> doAfterKnowledgeObtain = new LinkedHashMap<>();
        doAfterKnowledgeObtain.put(UnaryTermFactoryEnums.NEGATION.createExpression(SingleFactTermFactoryEnums.IS_EMPTY.createExpression(agentsKnowledge.getAgentsOwnFactByKey(BUILD).get())), buildBuilding);
        doAfterKnowledgeObtain.put(UnaryTermFactoryEnums.NEGATION.createExpression(SingleBooleanFactTermFactoryEnums.IDENTITY.createExpression(agentsKnowledge.getAgentsOwnFactByKey(IS_GATHERING_MINERALS).get())), chooseMinerals);

        //get knowledge from other agents
        GetPartOfCommonKnowledgeAction getPartOfCommonKnowledgeAction = new GetPartOfCommonKnowledgeAction(this, doAfterKnowledgeObtain, (workingCommonKnowledge, agentsKnowledgeToUpdate) -> {
            Optional<Fact<Map<Integer,AUnitType>>> toBuild = workingCommonKnowledge.getCloneOfFactOfAgentByKey(Commander.getInstance(), AGENTS_TO_BUILD);
            if (toBuild.isPresent()){
                AUnitType buildingType = toBuild.get().getContent().get(getId());
                if (buildingType != null) {
                    agentsKnowledge.getAgentsOwnFactByKey(BUILD).get().setContent(buildingType);
                }
            }
        });

        LinkedHashMap<TermInterface, AgentActionCycleAbstract> doAfterObservation = new LinkedHashMap<>();
        doAfterObservation.put(TermFactoryEnum.TRUTH.createExpression(), getPartOfCommonKnowledgeAction);

        GetGameObservationAction sense = new GetGameObservationAction(this, doAfterObservation, (game, agentsKnowledgeToUpdateAgentsKnowledge, unit) -> {
            List<AUnit> mineralsInSight = unit.u().getUnitsInRadius(unit.u().getType().sightRange()).stream()
                    .filter(unit1 -> unit1.getType().equals(UnitType.Resource_Mineral_Field))
                    .map(AUnit::createFrom)
                    .collect(Collectors.toList());
            agentsKnowledge.getAgentsOwnFactByKey(MINERALS_IN_SIGHT).get().setContent(mineralsInSight);
            agentsKnowledge.getAgentsOwnFactByKey(HEALTH).get().setContent(unit.getHP());
            agentsKnowledge.getAgentsOwnFactByKey(IS_GATHERING_MINERALS).get().setContent(unit.isGatheringMinerals());
            agentsKnowledge.getAgentsOwnFactByKey(POSITION).get().setContent(unit.u().getTilePosition());
        });
        return sense;
    }

    @Override
    protected void actOnAgentRemoval(Agent agent) {

    }

}
