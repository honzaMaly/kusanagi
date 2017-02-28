package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jan on 28-Feb-17.
 */
public class Tree {
    private final Map<Intention<?>, IntentionNodeAtTopLevel<?, ?>> intentionsInTopLevel = new HashMap<>();
    private final Map<InternalDesire<?>, DesireNodeAtTopLevel<?>> desiresInTopLevel = new HashMap<>();
    private final Map<InternalDesire<?>, Intention<?>> desireIntentionAssociation = new HashMap<>();
    final Agent agent;

    public Tree(Agent agent) {
        this.agent = agent;
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

    public void addDesire(DesireFromAnotherAgent.WithAbstractIntention desire) {
        desiresInTopLevel.put(desire, new DesireNodeAtTopLevel.FromAnotherAgent.WithAbstractIntention(this, desire));
    }

    public void addDesire(DesireFromAnotherAgent.WithIntentionWithPlan desire) {
        desiresInTopLevel.put(desire, new DesireNodeAtTopLevel.FromAnotherAgent.WithIntentionWithPlan(this, desire));
    }

    public void addDesire(OwnDesire.WithAbstractIntention desire) {
        desiresInTopLevel.put(desire, new DesireNodeAtTopLevel.Own.WithAbstractIntention(this, desire));
    }

    public void addDesire(OwnDesire.WithIntentionWithPlan desire) {
        desiresInTopLevel.put(desire, new DesireNodeAtTopLevel.Own.WithIntentionWithPlan(this, desire));
    }

    public void addDesire(DesireForOthers desire) {
        desiresInTopLevel.put(desire, new DesireNodeAtTopLevel.ForOthers(this, desire));
    }

    /**
     * Method to visit childes. Desires nodes are visited first.
     *
     * @param treeVisitorInterface
     */
    public void payVisitToChildes(TreeVisitorInterface treeVisitorInterface) {
        desiresInTopLevel.values().forEach(node -> node.accept(treeVisitorInterface));
        intentionsInTopLevel.values().forEach(node -> node.accept(treeVisitorInterface));
    }


    //todo aggregated data, update them

}
