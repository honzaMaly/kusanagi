package cz.jan.maly.model.planing.heap;

import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.DesireParameters;
import cz.jan.maly.utils.MyLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Generic node manipulator
 * Created by Jan on 14-Mar-17.
 */
class ChildNodeManipulation<V extends Node<?> & VisitorAcceptor & IntentionNodeInterface, K extends Node<?> & DesireNodeInterface<?>> {
    final Map<DesireParameters, K> desiresNodesByKey = new HashMap<>();
    final Map<DesireParameters, V> intentionNodesByKey = new HashMap<>();

    /**
     * Get set of desires key for uncommitted nodes. Remove all uncommitted nodes
     *
     * @return
     */
    Set<DesireKey> removeDesiresForUncommittedNodesAndReturnTheirKeys() {
        Set<DesireKey> desireKeys = desiresNodesByKey.keySet().stream()
                .map(DesireParameters::getDesireKey)
                .collect(Collectors.toSet());
        desiresNodesByKey.clear();
        return desireKeys;
    }

    /**
     * Add desire node
     *
     * @param desireNode
     */
    void addDesireNode(K desireNode) {
        desiresNodesByKey.put(desireNode.desireParameters, desireNode);
    }

    /**
     * Replace desire by intention
     *
     * @param desireNode
     * @param intentionNode
     */
    void replaceDesireByIntention(K desireNode, V intentionNode) {
        if (desiresNodesByKey.containsKey(desireNode.desireParameters)) {
            desiresNodesByKey.remove(desireNode.desireParameters);
            intentionNodesByKey.put(intentionNode.desireParameters, intentionNode);
        } else {
            MyLogger.getLogger().warning("Could not replace desire by intention, desire node is missing.");
            throw new RuntimeException("Could not replace desire by intention, desire node is missing.");
        }
    }

    /**
     * Replace desire by intention
     *
     * @param desireNode
     * @param intentionNode
     */
    void replaceIntentionByDesire(V intentionNode, K desireNode) {
        if (intentionNodesByKey.containsKey(intentionNode.desireParameters)) {
            intentionNodesByKey.remove(intentionNode.desireParameters);
            desiresNodesByKey.put(desireNode.desireParameters, desireNode);
        } else {
            MyLogger.getLogger().warning("Could not replace intention by desire, intention node is missing.");
            throw new RuntimeException("Could not replace intention by desire, intention node is missing.");
        }
    }

}
