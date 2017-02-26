package cz.jan.maly.model.planing.tree;

/**
 * Interface to be implemented by visitor of decision tree. Execution pay 3 different types of visit to tree. Using
 * design pattern Visitor enables to have logic to handle flow implemented by concrete visitor.
 * Created by Jan on 21-Feb-17.
 */
public interface TreeVisitorInterface {

    void visit(LeafNodeWithPlan.WithOwnDesire leafNodeWithOwnDesire);

    void visit(LeafNodeWithPlan.WithAnotherAgentDesire leafNodeWithAnotherAgentDesire);

    void visit(LeafNodeWithDesireForOtherAgents leafNodeWithDesireForOtherAgents);

    void visit(WithOwnDesireIntermediateNode withOwnDesireIntermediateNode);

    void visit(WithAnotherAgentDesireIntermediateNode withAnotherAgentDesireIntermediateNode);

}
