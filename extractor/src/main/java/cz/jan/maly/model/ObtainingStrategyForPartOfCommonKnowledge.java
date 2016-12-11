package cz.jan.maly.model;

import cz.jan.maly.model.agent.PartOfCommonKnowledgeRequestedByAgent;
import cz.jan.maly.model.game.CommonKnowledge;

/**
 * Interface describing strategy to obtain part of common knowledge required by agent.
 * Created by Jan on 07-Dec-16.
 */
public interface ObtainingStrategyForPartOfCommonKnowledge {

    PartOfCommonKnowledgeRequestedByAgent composeKnowledge(CommonKnowledge workingCommonKnowledge);

}
