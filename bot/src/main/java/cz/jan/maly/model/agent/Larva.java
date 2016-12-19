package cz.jan.maly.model.agent;

import cz.jan.maly.model.Fact;
import cz.jan.maly.model.KeyToFact;
import cz.jan.maly.model.agent.action.ActInGameAction;
import cz.jan.maly.model.agent.action.GetPartOfCommonKnowledgeAction;
import cz.jan.maly.model.agent.action.game.Morph;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitType;
import cz.jan.maly.model.sflo.TermInterface;
import cz.jan.maly.model.sflo.factories.SingleFactTermFactoryEnums;
import cz.jan.maly.model.sflo.factories.UnaryTermFactoryEnums;
import cz.jan.maly.service.MyLogger;

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
        agentsKnowledge.getAgentsOwnFactByKey(HEALTH).get().setContent(unit.getHP());
    }

    @Override
    protected AgentsKnowledge setupAgentsKnowledge() {
        Set<KeyToFact> ownFactsToUse = new HashSet<>(), factsByOtherAgentToUse = new HashSet<>();
        ownFactsToUse.add(HEALTH);
        ownFactsToUse.add(MORPH_TO);
        return new AgentsKnowledge(this, ownFactsToUse, factsByOtherAgentToUse);
    }

    @Override
    protected AgentActionCycleAbstract composeWorkflow() {

        //morph to
        ActInGameAction morphTo = new ActInGameAction(this, new Morph(agentsKnowledge.getAgentsOwnFactByKey(MORPH_TO).get()), true);

        LinkedHashMap<TermInterface, AgentActionCycleAbstract> doAfterCheckingMorph = new LinkedHashMap<>();
        doAfterCheckingMorph.put(UnaryTermFactoryEnums.NEGATION.createExpression(SingleFactTermFactoryEnums.IS_EMPTY.createExpression(agentsKnowledge.getAgentsOwnFactByKey(MORPH_TO).get())), morphTo);


        //read from agents
        GetPartOfCommonKnowledgeAction getPartOfCommonKnowledgeAction = new GetPartOfCommonKnowledgeAction(this, doAfterCheckingMorph, (workingCommonKnowledge, agentsKnowledgeToUpdate) -> {

            Optional<Fact<Map<Integer, AUnitType>>> morphsRequests = workingCommonKnowledge.getCloneOfFactOfAgentByKey(Commander.getInstance(), AGENTS_TO_MORPH);
            if (morphsRequests.isPresent()) {
                AUnitType typeToMorphTo = morphsRequests.get().getContent().get(getId());
                if (typeToMorphTo != null) {
                    agentsKnowledge.getAgentsOwnFactByKey(MORPH_TO).get().setContent(typeToMorphTo);
                }
            }

        });

        return getPartOfCommonKnowledgeAction;
    }

    @Override
    protected void actOnAgentRemoval(Agent agent) {

    }
}
