package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.metadata.DesireKey;

import java.util.List;
import java.util.Optional;

/**
 * Contract for parent - it has at least one children
 * Created by Jan on 02-Mar-17.
 */
public interface Parent<V extends Node<?> & DesireNodeInterface, K extends Node<?> & IntentionNodeInterface & VisitorAcceptor> {

    /**
     * Get nodes for desires
     *
     * @return
     */
    List<V> getNodesWithDesire();

    /**
     * Get nodes for intentions
     *
     * @return
     */
    List<K> getNodesWithIntention();

    /**
     * Return desire key
     *
     * @return
     */
    Optional<DesireKey> getDesireKeyAssociatedWithParent();

}
