package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.IntentionCommand;
import cz.jan.maly.model.planing.InternalDesire;
import cz.jan.maly.model.planing.command.CommandForIntention;

/**
 * Contract for nodes with command
 * Created by Jan on 03-Mar-17.
 */
interface NodeWithCommand<T extends CommandForIntention<? extends IntentionCommand<? extends InternalDesire<? extends IntentionCommand<?, ?>>, T>>> {

    /**
     * Return command associated with node
     *
     * @return
     */
    T getCommand();

}
