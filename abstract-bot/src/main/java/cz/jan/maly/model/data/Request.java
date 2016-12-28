package cz.jan.maly.model.data;

import cz.jan.maly.model.agent.Agent;

import java.util.*;

import static cz.jan.maly.utils.FrameworkUtils.CLONER;

/**
 * Class request is used as "proposal" to other agent to do something by committing to this request.
 * Instance store facts for this request (for example position to attack) as well as committed agents to this request.
 * Created by Jan on 21-Dec-16.
 */
public class Request {
    protected final Map<KeyToFact, Fact> factsAboutThisProposal = new HashMap<>();
    protected final Set<Agent> committedAgents = new HashSet<>();
    protected final boolean canCommitOneAgentOnly;
    private final Agent requestFrom;
    private final int id;

    //it makes copy of sets
    public Request(Map<KeyToFact, Fact> facts, Set<Agent> agents, boolean canCommitOneAgentOnly, Agent requestFrom, int id) {
        this.canCommitOneAgentOnly = canCommitOneAgentOnly;
        this.requestFrom = requestFrom;
        this.id = id;
        facts.forEach((keyToFact, fact) -> factsAboutThisProposal.put(keyToFact, keyToFact.createFact(fact)));
        committedAgents.addAll(agents);
    }

    //it makes copy of sets
    public Request(Map<KeyToFact, Fact> facts, boolean canCommitOneAgentOnly, Agent requestFrom, int id) {
        this.canCommitOneAgentOnly = canCommitOneAgentOnly;
        this.requestFrom = requestFrom;
        this.id = id;
        facts.forEach((keyToFact, fact) -> factsAboutThisProposal.put(keyToFact, keyToFact.createFact(fact)));
    }

    /**
     * Return set of committed agents to this request
     *
     * @return
     */
    public Set<Agent> committedAgents() {
        return committedAgents;
    }

    /**
     * Removes agent commitment to this request
     *
     * @param agent
     * @return
     */
    public boolean removeCommitment(Agent agent) {
        if (committedAgents.contains(agent))
            return committedAgents.remove(agent);
        return true;
    }

    /**
     * Make copy of this proposal to be used by other agents in different threads
     *
     * @return
     */
    public Request copyRequest() {
        return new Request(factsAboutThisProposal, committedAgents, canCommitOneAgentOnly, requestFrom, id);
    }

    /**
     * Try to commit agent in argument to this request.
     *
     * @param agent
     * @return
     */
    public boolean commitAgent(Agent agent) {
        if (canCommitOneAgentOnly & !committedAgents.isEmpty()) {
            return false;
        }
        return committedAgents.add(agent);
    }

    /**
     * Can commit agent
     *
     * @return
     */
    public boolean canCommitAgent() {
        if (canCommitOneAgentOnly & !committedAgents.isEmpty()) {
            return false;
        }
        return true;
    }

    public Agent getRequestFrom() {
        return requestFrom;
    }

    /**
     * Method returns optional of required fact. If fact is present, clone of fact is return in wrapper
     *
     * @param keyToFact
     * @return
     */
    public <V> Optional<Fact<V>> getCloneOfFactByKey(KeyToFact<V> keyToFact) {
        Fact<V> fact = factsAboutThisProposal.get(keyToFact);
        if (fact != null) {
            return Optional.ofNullable(CLONER.deepClone(fact));
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (id != request.id) return false;
        if (canCommitOneAgentOnly != request.canCommitOneAgentOnly) return false;
        if (!factsAboutThisProposal.keySet().equals(request.factsAboutThisProposal.keySet())) return false;
        return requestFrom.equals(request.requestFrom);
    }

    @Override
    public int hashCode() {
        int result = factsAboutThisProposal.keySet().hashCode();
        result = 31 * result + id;
        result = 31 * result + (canCommitOneAgentOnly ? 1 : 0);
        result = 31 * result + requestFrom.hashCode();
        return result;
    }
}
