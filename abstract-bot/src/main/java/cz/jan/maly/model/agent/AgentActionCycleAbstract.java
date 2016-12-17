package cz.jan.maly.model.agent;

import cz.jan.maly.model.sflo.TermInterface;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Abstract class to remove repetitive code which would appear in each concrete implementation of AgentActionCycleAbstract
 * referencing to actions which should be executed next
 * Created by Jan on 14-Dec-16.
 */
public abstract class AgentActionCycleAbstract {
    protected final LinkedHashMap<TermInterface, AgentActionCycleAbstract> followingActionsWithConditions;
    protected final Agent agent;

    public AgentActionCycleAbstract(Agent agent, LinkedHashMap<TermInterface, AgentActionCycleAbstract> followingActionsWithConditions) {
        this.agent = agent;
        this.followingActionsWithConditions = followingActionsWithConditions;
    }

    public AgentActionCycleAbstract(Agent agent) {
        this.agent = agent;
        this.followingActionsWithConditions = new LinkedHashMap<>();
    }

    public abstract Optional<AgentActionCycleAbstract> executeAction();

    /**
     * Method to decide which action to choose next based on current state of knowledge
     *
     * @return
     */
    protected Optional<AgentActionCycleAbstract> decideNextAction() {
        Optional<TermInterface> firstTruthfulTerm = followingActionsWithConditions.keySet().stream()
                .filter(TermInterface::evaluate).findFirst();
        if (firstTruthfulTerm.isPresent()) {
            return Optional.ofNullable(followingActionsWithConditions.get(firstTruthfulTerm.get()));
        }
        return Optional.empty();
    }

    public int getLongestLengthToEnd() {
        int length = followingActionsWithConditions.values().stream()
                .map(agentActionCycleAbstract -> agentActionCycleAbstract.getLongestLengthToEnd())
                .max(Integer::compareTo)
                .orElse(0);
        return length + 1;
    }

}
