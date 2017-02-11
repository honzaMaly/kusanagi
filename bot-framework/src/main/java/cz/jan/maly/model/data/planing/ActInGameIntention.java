package cz.jan.maly.model.data.planing;

import cz.jan.maly.model.data.knowledge_representation.FactKey;
import cz.jan.maly.model.data.tasks.ActInGameTask;

import java.util.List;

/**
 * Created by Jan on 11-Feb-17.
 */
public class ActInGameIntention extends Intention<ActInGameTask> {

    public ActInGameIntention(ActInGameDesire desire, List<Desire> relatedDesiresToHave, List<FactKey> factKeysUsed) {
        super(desire, relatedDesiresToHave, factKeysUsed);
    }

}
