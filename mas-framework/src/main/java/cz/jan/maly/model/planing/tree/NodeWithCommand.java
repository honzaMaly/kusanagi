package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.Command;

/**
 * Contract for nodes with command
 * Created by Jan on 03-Mar-17.
 */
interface NodeWithCommand {

    /**
     * Return command associated with node
     *
     * @return
     */
    Command getCommand();

}
