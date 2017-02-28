package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.Intention;
import cz.jan.maly.model.planing.InternalDesire;

/**
 * Template for intention not in top level
 * Created by Jan on 28-Feb-17.
 */
abstract class IntentionNodeNotTopLevel<V extends Intention<? extends InternalDesire>, T extends InternalDesire<V>, K extends Node & IntentionNodeWithChildes> extends Node.NotTopLevel<K> implements IntentionNodeInterface {
    final V intention;

    private IntentionNodeNotTopLevel(K parent, T desire) {
        super(parent, desire.getDesireParameters());
        this.intention = desire.formIntention();
    }

}
