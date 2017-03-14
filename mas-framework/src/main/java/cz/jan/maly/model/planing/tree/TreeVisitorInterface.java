package cz.jan.maly.model.planing.tree;

import cz.jan.maly.model.planing.command.ActCommandForIntention;
import cz.jan.maly.model.planing.command.ReasoningCommandForIntention;

/**
 * Interface to be implemented by visitor of decision tree. Execution pay 3 different types of visit to tree. Using
 * design pattern Visitor enables to have logic to handle flow implemented by concrete visitor.
 * Created by Jan on 21-Feb-17.
 */
public interface TreeVisitorInterface {

    /**
     * Visit tree
     */
    void visitTree();

    void visit(IntentionNodeAtTopLevel.WithAbstractPlan<?, ?> node);

    void visit(IntentionNodeNotTopLevel.WithAbstractPlan<?, ?, ?> node);

    void visitNodeWithActingCommand(IntentionNodeNotTopLevel.WithCommand<?, ?, ActCommandForIntention.Own> node);

    void visitNodeWithReasoningCommand(IntentionNodeNotTopLevel.WithCommand<?, ?, ReasoningCommandForIntention> node);

    void visit(IntentionNodeAtTopLevel.WithDesireForOthers node);

    void visit(IntentionNodeNotTopLevel.WithDesireForOthers<?> node);

    void visit(IntentionNodeAtTopLevel.WithCommand.OwnReasoning node);

    void visit(IntentionNodeAtTopLevel.WithCommand.OwnActing node);

    void visit(IntentionNodeAtTopLevel.WithCommand.FromAnotherAgent node);

}
