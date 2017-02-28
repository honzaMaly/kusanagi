package cz.jan.maly.model.planing.tree;

/**
 * Interface to be implemented by visitor of decision tree. Execution pay 3 different types of visit to tree. Using
 * design pattern Visitor enables to have logic to handle flow implemented by concrete visitor.
 * Created by Jan on 21-Feb-17.
 */
public interface TreeVisitorInterface {

    void visit(DesireNodeAtTopLevel node);

    void visit(IntentionNodeAtTopLevel.WithDesireForOthers node);

    void visit(IntentionNodeAtTopLevel.WithAbstractPlan node);

    void visit(IntentionNodeAtTopLevel.WithPlan node);

}
