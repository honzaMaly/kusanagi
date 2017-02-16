package cz.jan.maly.model.agent;

import cz.jan.maly.model.agent.action.GetGameObservationAction;
import cz.jan.maly.model.agent.action.GetPartOfCommonKnowledgeAction;
import cz.jan.maly.model.agent.action.ReasonAboutKnowledgeAction;
import cz.jan.maly.model.agent.action.UpdateCommonKnowledgeAction;
import cz.jan.maly.model.agent.data.AgentsKnowledgeBase;
import cz.jan.maly.model.metadata.Fact;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitType;
import cz.jan.maly.sfol.FormulaInterface;
import cz.jan.maly.sfol.factories.*;
import cz.jan.maly.service.implementation.AgentsManager;
import cz.jan.maly.utils.MyLogger;

import java.util.*;
import java.util.stream.Collectors;

import static cz.jan.maly.model.facts.Facts.*;

/**
 * Main commander deciding what to build
 * Created by Jan on 17-Dec-16.
 */
public class Commander extends Agent {
    private static Commander instance = null;

    public static Commander getInstance() {
        if (instance == null) {
            instance = new Commander(0);
        }
        return instance;
    }

    protected Commander(long timeBetweenCycles) {
        super(timeBetweenCycles);
        MyLogger.getLogger().info("Commander registered.");
        act();
    }

    @Override
    protected AgentsKnowledgeBase setupAgentsKnowledge() {
        Set<KeyToFact> ownFactsToUse = new HashSet<>(), factsByOtherAgentToUse = new HashSet<>();
        ownFactsToUse.add(MINED_MINERAL);
        factsByOtherAgentToUse.add(SET_OF_AVAILABLE_WORKERS);
        factsByOtherAgentToUse.add(SET_OF_AVAILABLE_LARVAE);
        factsByOtherAgentToUse.add(AVAILABLE_LARVAE);
        factsByOtherAgentToUse.add(BUILDINGS_IN_EXPANSION);
        ownFactsToUse.add(AGENTS_TO_MORPH);
        ownFactsToUse.add(AGENTS_TO_BUILD);
        return new AgentsKnowledgeBase(this, ownFactsToUse, factsByOtherAgentToUse);
    }

    @Override
    protected AgentActionCycleAbstract actionsDefinedByUser() {

        UpdateCommonKnowledgeAction updateCommonKnowledgeAction = new UpdateCommonKnowledgeAction(this);

        LinkedHashMap<FormulaInterface, AgentActionCycleAbstract> doAfterMorphing = new LinkedHashMap<>();
        doAfterMorphing.put(FactCardinalityOfMapFormulaFactoryEnums.GREATER.createExpression(agentsKnowledgeBase.getAgentsOwnFactByKey(AGENTS_TO_MORPH).get(), 0), updateCommonKnowledgeAction);

        //build new base - we have at least 5 drones and 350m
        //build overlord - we have full pop and less then 3 overlords +100m
        //build pool if we have no pool and at least five drones +200m
        //build drone if we have less then 6 drones + 50m
        //build drone we have other expansion and less then 10 drones +50m
        //build zerglings +50m

        //todo build order

        ReasonAboutKnowledgeAction buildPool = new ReasonAboutKnowledgeAction(this, doAfterMorphing, agentsKnowledgeToUpdate -> {
            List<Integer> workers = agentsKnowledgeBase.getFactInCacheByKey(SET_OF_AVAILABLE_WORKERS).get().getContent().stream()
                    .collect(Collectors.toList());
            if (!workers.isEmpty()) {
                Map<Integer, AUnitType> toBuild = agentsKnowledgeBase.getAgentsOwnFactByKey(AGENTS_TO_BUILD).get().getContent();
                toBuild.put(workers.get(RANDOM.nextInt(workers.size())), AUnitType.Zerg_Spawning_Pool);
                agentsKnowledgeBase.getAgentsOwnFactByKey(AGENTS_TO_MORPH).get().setContent(toBuild);
            }
        });

        ReasonAboutKnowledgeAction morphToDrone = new ReasonAboutKnowledgeAction(this, doAfterMorphing, agentsKnowledgeToUpdate -> {
            List<Integer> larvae = agentsKnowledgeBase.getFactInCacheByKey(SET_OF_AVAILABLE_LARVAE).get().getContent().stream()
                    .collect(Collectors.toList());
            if (!larvae.isEmpty()) {
                Map<Integer, AUnitType> toMorph = agentsKnowledgeBase.getAgentsOwnFactByKey(AGENTS_TO_MORPH).get().getContent();
                toMorph.put(larvae.get(RANDOM.nextInt(larvae.size())), AUnitType.Zerg_Drone);
                agentsKnowledgeBase.getAgentsOwnFactByKey(AGENTS_TO_MORPH).get().setContent(toMorph);
            }
        });

        LinkedHashMap<FormulaInterface, AgentActionCycleAbstract> doAfterLoadingOthersAgentsMemory = new LinkedHashMap<>();
        doAfterLoadingOthersAgentsMemory.put(
                BinaryFormulaFactoryEnums.AND.createExpression(FactCardinalityOfMapFormulaFactoryEnums.LESS.createExpression(agentsKnowledgeBase.getAgentsOwnFactByKey(AGENTS_TO_BUILD).get(), 1),
                        BinaryFormulaFactoryEnums.AND.createExpression(ConstantNumericalFactFormulaFactoryEnums.GREATER_EQUAL.createExpression(agentsKnowledgeBase.getAgentsOwnFactByKey(MINED_MINERAL).get(), 200),
                                FactCardinalityOfSetFormulaFactoryEnums.LESS.createExpression(agentsKnowledgeBase.getFactInCacheByKey(BUILDINGS_IN_EXPANSION).get(), 1))), buildPool);
        doAfterLoadingOthersAgentsMemory.put(FactCardinalityOfMapFormulaFactoryEnums.LESS.createExpression(agentsKnowledgeBase.getAgentsOwnFactByKey(AGENTS_TO_MORPH).get(), 1), morphToDrone);

        GetPartOfCommonKnowledgeAction getPartOfCommonKnowledgeAction = new GetPartOfCommonKnowledgeAction(this, doAfterLoadingOthersAgentsMemory, (workingCommonKnowledge, agentsKnowledgeToUpdate) -> {

            //todo this should be probably in its own action type

            Set<Integer> workers = AgentsManager.getInstance().getRelevantAgents(agentsToFilterFrom -> agentsToFilterFrom.stream().filter(agent -> agent instanceof Worker).collect(Collectors.toSet())).stream()
                    .map(Agent::getId)
                    .collect(Collectors.toSet());
            agentsKnowledgeBase.getFactInCacheByKey(SET_OF_AVAILABLE_WORKERS).get().setContent(workers);

            Set<Agent> bases = AgentsManager.getInstance().getRelevantAgents(agentsToFilterFrom -> agentsToFilterFrom.stream().filter(agent -> agent instanceof Base).collect(Collectors.toSet()));
            Set<AUnit> larvae = new HashSet<>();
            bases.forEach(agent -> {
                Optional<Fact<Set<AUnit>>> larvaeInBase = workingCommonKnowledge.getCloneOfFactOfAgentByKey(agent, AVAILABLE_LARVAE);
                larvaeInBase.ifPresent(setFact -> larvae.addAll(setFact.getContent()));
            });
            agentsKnowledgeBase.getFactInCacheByKey(AVAILABLE_LARVAE).get().setContent(larvae);

            Set<AUnit> buildings = new HashSet<>();
            bases.forEach(agent -> {
                Optional<Fact<Set<AUnit>>> bInBase = workingCommonKnowledge.getCloneOfFactOfAgentByKey(agent, BUILDINGS_IN_EXPANSION);
                bInBase.ifPresent(setFact -> buildings.addAll(setFact.getContent()));

            });
            agentsKnowledgeBase.getFactInCacheByKey(BUILDINGS_IN_EXPANSION).get().setContent(buildings);

            Set<Integer> larvaeA = AgentsManager.getInstance().getRelevantAgents(agentsToFilterFrom -> agentsToFilterFrom.stream()
                    .filter(agent -> agent instanceof Larva)
                    .collect(Collectors.toSet())).stream()
                    .map(Agent::getId)
                    .collect(Collectors.toSet());
            agentsKnowledgeBase.getFactInCacheByKey(SET_OF_AVAILABLE_LARVAE).get().setContent(larvaeA);

        });

        LinkedHashMap<FormulaInterface, AgentActionCycleAbstract> doAfterSensing = new LinkedHashMap<>();
        doAfterSensing.put(FormulaFactoryEnum.TRUTH.createExpression(), getPartOfCommonKnowledgeAction);

        GetGameObservationAction sense = new GetGameObservationAction(this, doAfterSensing, (game, agentsKnowledgeToUpdateAgentsKnowledge, unit) -> {
            agentsKnowledgeBase.getAgentsOwnFactByKey(MINED_MINERAL).get().setContent(game.self().minerals());
        });

        return sense;
    }

    @Override
    protected void actOnAgentRemoval(Agent agent) {

    }
}
