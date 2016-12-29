package cz.jan.maly.model.agent;

import cz.jan.maly.model.agent.action.ActInGameAction;
import cz.jan.maly.model.agent.action.GetPartOfCommonKnowledgeAction;
import cz.jan.maly.model.agent.action.game.Morph;
import cz.jan.maly.model.agent.data.AgentsKnowledgeBase;
import cz.jan.maly.model.agent.implementation.AgentWithGameRepresentation;
import cz.jan.maly.model.data.Fact;
import cz.jan.maly.model.data.KeyToFact;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitType;
import cz.jan.maly.model.sflo.FormulaInterface;
import cz.jan.maly.model.sflo.factories.SingleFactFormulaFactoryEnums;
import cz.jan.maly.model.sflo.factories.UnaryFormulaFactoryEnums;
import cz.jan.maly.utils.MyLogger;

import java.util.*;

import static cz.jan.maly.model.facts.Facts.*;

/**
 * Agent representing larva
 * Created by Jan on 18-Dec-16.
 */
public class Larva extends AgentWithGameRepresentation {

    protected Larva(long timeBetweenCycles, AUnit unit) {
        super(timeBetweenCycles, unit);
        MyLogger.getLogger().info(unit.toString() + " registered.");
    }

    @Override
    protected void initializeKnowledgeOnCreation() {
        agentsKnowledgeBase.getAgentsOwnFactByKey(HEALTH).get().setContent(unit.getHP());
    }

    @Override
    protected AgentsKnowledgeBase setupAgentsKnowledge() {
        Set<KeyToFact> ownFactsToUse = new HashSet<>(), factsByOtherAgentToUse = new HashSet<>();
        ownFactsToUse.add(HEALTH);
        ownFactsToUse.add(MORPH_TO);
        return new AgentsKnowledgeBase(this, ownFactsToUse, factsByOtherAgentToUse);
    }

    @Override
    protected AgentActionCycleAbstract actionsDefinedByUser() {

        //morph to
        ActInGameAction morphTo = new ActInGameAction(this, new Morph(agentsKnowledgeBase.getAgentsOwnFactByKey(MORPH_TO).get()), true);

        LinkedHashMap<FormulaInterface, AgentActionCycleAbstract> doAfterCheckingMorph = new LinkedHashMap<>();
        doAfterCheckingMorph.put(UnaryFormulaFactoryEnums.NEGATION.createExpression(SingleFactFormulaFactoryEnums.IS_EMPTY.createExpression(agentsKnowledgeBase.getAgentsOwnFactByKey(MORPH_TO).get())), morphTo);


        //read from agents
        GetPartOfCommonKnowledgeAction getPartOfCommonKnowledgeAction = new GetPartOfCommonKnowledgeAction(this, doAfterCheckingMorph, (workingCommonKnowledge, agentsKnowledgeToUpdate) -> {

            Optional<Fact<Map<Integer, AUnitType>>> morphsRequests = workingCommonKnowledge.getCloneOfFactOfAgentByKey(Commander.getInstance(), AGENTS_TO_MORPH);
            if (morphsRequests.isPresent()) {
                AUnitType typeToMorphTo = morphsRequests.get().getContent().get(getId());
                if (typeToMorphTo != null) {
                    agentsKnowledgeBase.getAgentsOwnFactByKey(MORPH_TO).get().setContent(typeToMorphTo);
                }
            }

        });

        return getPartOfCommonKnowledgeAction;
    }

    @Override
    protected void actOnAgentRemoval(Agent agent) {

    }
}
