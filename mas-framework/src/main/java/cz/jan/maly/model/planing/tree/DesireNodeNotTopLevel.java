package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.Intention;
import cz.jan.maly.model.planing.InternalDesire;

/**
 * Template for desire not in top level
 * Created by Jan on 28-Feb-17.
 */
abstract class DesireNodeNotTopLevel<T extends InternalDesire<? extends Intention>, K extends Node & IntentionNodeWithChildes> extends Node.NotTopLevel<K> {
    final T desire;

    DesireNodeNotTopLevel(K parent, T desire) {
        super(parent, desire.getDesireParameters());
        this.desire = desire;
    }


}
