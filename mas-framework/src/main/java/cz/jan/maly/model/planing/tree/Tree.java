package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.PlanningTreeInterface;
import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.knowledge.PlanningTreeOfAnotherAgent;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.model.planing.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Facade for planning tree. Tree manages nodes at top level.
 * Created by Jan on 28-Feb-17.
 */
public class Tree implements PlanningTreeInterface, Parent<DesireNodeAtTopLevel<?>, IntentionNodeAtTopLevel<?, ?>> {
    private final Map<Intention<?>, IntentionNodeAtTopLevel<?, ?>> intentionsInTopLevel = new HashMap<>();
    private final Map<InternalDesire<?>, DesireNodeAtTopLevel<?>> desiresInTopLevel = new HashMap<>();

    //todo generify as only desires from another agent can be removed without deciding commitment
    private final Map<InternalDesire<?>, Intention<?>> desireIntentionAssociation = new HashMap<>();

    //todo each turn reinit not committed own desires with current data. Do that on all levels

    private final Set<SharedDesireInRegister> sharedDesiresForOtherAgents = new HashSet<>();
    private final Set<SharedDesireForAgents> committedSharedDesiresByOtherAgents = new HashSet<>();

    private final Agent agent;

    public Tree(Agent agent) {
        this.agent = agent;
    }

    @Override
    public void addSharedDesireForOtherAgents(SharedDesireInRegister sharedDesire) {
        sharedDesiresForOtherAgents.add(sharedDesire);
    }

    @Override
    public void removeSharedDesireForOtherAgents(SharedDesireInRegister sharedDesire) {
        sharedDesiresForOtherAgents.remove(sharedDesire);
    }

    void addCommittedSharedDesireByOtherAgents(SharedDesireForAgents sharedDesire) {
        committedSharedDesiresByOtherAgents.add(sharedDesire);
    }

    /**
     * Removes desire (in case of agent commitment to it - intention) from tree
     *
     * @param desireToRemove
     */
    public void removeDesire(InternalDesire<?> desireToRemove) {
        if (desireIntentionAssociation.containsKey(desireToRemove)) {
            intentionsInTopLevel.remove(desireIntentionAssociation.remove(desireToRemove));
        } else {
            desiresInTopLevel.remove(desireToRemove);
        }
    }

    /**
     * Returns read only copy of this tree to be shared with other agents
     *
     * @return
     */
    public PlanningTreeOfAnotherAgent getReadOnlyCopy() {
        return new PlanningTreeOfAnotherAgent(collectKeysOfCommittedDesiresInTreeCounts(),
                collectKeysOfDesiresInTreeCounts(),
                getParametersOfCommittedDesiresOnTopLevel(),
                getParametersOfDesiresOnTopLevel(), committedSharedDesiresParametersByOtherAgents(), sharedDesiresParameters());
    }

    /**
     * Replace desire by intention
     *
     * @param desireNode
     * @param intentionNode
     */
    void replaceDesireByIntention(DesireNodeAtTopLevel<?> desireNode, IntentionNodeAtTopLevel<?, ?> intentionNode) {
        if (desiresInTopLevel.containsKey(desireNode.desire)) {
            desiresInTopLevel.remove(desireNode.desire);
            intentionsInTopLevel.put(intentionNode.intention, intentionNode);
            desireIntentionAssociation.put(desireNode.desire, intentionNode.intention);
        } else {
            throw new RuntimeException("Could not replace desire by intention, desire node is missing.");
        }
    }

    /**
     * Replace desire by intention
     *
     * @param desireNode
     * @param intentionNode
     */
    void replaceIntentionByDesire(IntentionNodeAtTopLevel<?, ?> intentionNode, DesireNodeAtTopLevel<?> desireNode) {
        if (intentionsInTopLevel.containsKey(intentionNode.intention)) {
            intentionsInTopLevel.remove(intentionNode.intention);
            desiresInTopLevel.put(desireNode.desire, desireNode);
            desireIntentionAssociation.remove(desireNode.desire);
        } else {
            throw new RuntimeException("Could not replace intention by desire, intention node is missing.");
        }
    }

    public List<DesireNodeAtTopLevel<?>> getNodesWithDesire() {
        return desiresInTopLevel.values().stream()
                .collect(Collectors.toList());
    }

    public List<IntentionNodeAtTopLevel<?, ?>> getNodesWithIntention() {
        return intentionsInTopLevel.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DesireKey> getDesireKeyAssociatedWithParent() {
        return Optional.empty();
    }

    @Override
    public Agent getAgent() {
        return agent;
    }

    @Override
    public Set<DesireParameters> committedSharedDesiresParametersByOtherAgents() {
        return committedSharedDesiresByOtherAgents.stream()
                .map(Desire::getDesireParameters)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<DesireParameters> sharedDesiresParameters() {
        return sharedDesiresForOtherAgents.stream()
                .map(Desire::getDesireParameters)
                .collect(Collectors.toSet());
    }

    @Override
    public int countOfCommittedSharedDesiresByOtherAgents() {
        return committedSharedDesiresByOtherAgents.size();
    }

    @Override
    public int countOfSharedDesires() {
        return sharedDesiresForOtherAgents.size();
    }

    @Override
    public Map<DesireKey, Long> collectKeysOfCommittedDesiresInTreeCounts() {
        List<DesireKey> list = new ArrayList<>();
        intentionsInTopLevel.values().forEach(intentionNode -> intentionNode.collectKeysOfCommittedDesiresInSubtree(list));
        return list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    @Override
    public Map<DesireKey, Long> collectKeysOfDesiresInTreeCounts() {
        List<DesireKey> list = desiresInTopLevel.values().stream()
                .map(Node::getDesireKey)
                .collect(Collectors.toList());
        intentionsInTopLevel.values().forEach(intentionNode -> intentionNode.collectKeysOfDesiresInSubtree(list));
        return list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    @Override
    public Set<DesireParameters> getParametersOfCommittedDesiresOnTopLevel() {
        return intentionsInTopLevel.values().stream()
                .map(intentionNode -> intentionNode.desireParameters)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<DesireParameters> getParametersOfDesiresOnTopLevel() {
        return desiresInTopLevel.values().stream()
                .map(desiresNode -> desiresNode.desireParameters)
                .collect(Collectors.toSet());
    }
}
