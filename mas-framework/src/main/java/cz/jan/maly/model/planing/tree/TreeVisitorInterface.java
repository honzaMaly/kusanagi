package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.agents.Agent;

/**
 * Interface to be implemented by visitor of decision tree. Execution pay 3 different types of visit to tree. Using
 * design pattern Visitor enables to have logic to handle flow implemented by concrete visitor.
 * Created by Jan on 21-Feb-17.
 */
public interface TreeVisitorInterface {

    /**
     * Visit tree
     */
    void visitTree(Tree tree, Agent agent);

    void visit(IntentionNodeAtTopLevel.WithAbstractPlan<?, ?> node, Agent agent);

    void visit(IntentionNodeAtTopLevel.WithPlan<?, ?> node, Agent agent);

    void visit(IntentionNodeNotTopLevel.WithAbstractPlan<?, ?, ?> node, Agent agent);

    void visit(IntentionNodeNotTopLevel.WithPlan<?> node, Agent agent);

    void visit(IntentionNodeAtTopLevel.WithDesireForOthers node, Agent agent);

    void visit(IntentionNodeNotTopLevel.WithDesireForOthers<?> node, Agent agent);

}
