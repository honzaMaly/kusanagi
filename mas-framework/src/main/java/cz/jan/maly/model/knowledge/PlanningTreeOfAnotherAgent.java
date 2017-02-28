package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.PlanningTreeInterface;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;

import java.util.Map;
import java.util.Set;

/**
 * Read only copy of agent's planning tree to be shared with other agents
 * Created by Jan on 28-Feb-17.
 */
public class PlanningTreeOfAnotherAgent implements PlanningTreeInterface {
    private final Map<DesireKey, Long> keysOfCommittedDesiresInTreeCounts;
    private final Map<DesireKey, Long> keysOfDesiresInTreeCounts;
    private final Set<DesireParameters> parametersOfCommittedDesiresOnTopLevel;
    private final Set<DesireParameters> parametersOfDesiresOnTopLevel;

    public PlanningTreeOfAnotherAgent(Map<DesireKey, Long> keysOfCommittedDesiresInTreeCounts, Map<DesireKey, Long> keysOfDesiresInTreeCounts, Set<DesireParameters> parametersOfCommittedDesiresOnTopLevel, Set<DesireParameters> parametersOfDesiresOnTopLevel) {
        this.keysOfCommittedDesiresInTreeCounts = keysOfCommittedDesiresInTreeCounts;
        this.keysOfDesiresInTreeCounts = keysOfDesiresInTreeCounts;
        this.parametersOfCommittedDesiresOnTopLevel = parametersOfCommittedDesiresOnTopLevel;
        this.parametersOfDesiresOnTopLevel = parametersOfDesiresOnTopLevel;
    }

    @Override
    public Map<DesireKey, Long> collectKeysOfCommittedDesiresInTreeCounts() {
        return keysOfCommittedDesiresInTreeCounts;
    }

    @Override
    public Map<DesireKey, Long> collectKeysOfDesiresInTreeCounts() {
        return keysOfDesiresInTreeCounts;
    }

    @Override
    public Set<DesireParameters> getParametersOfCommittedDesiresOnTopLevel() {
        return parametersOfCommittedDesiresOnTopLevel;
    }

    @Override
    public Set<DesireParameters> getParametersOfDesiresOnTopLevel() {
        return parametersOfDesiresOnTopLevel;
    }
}
