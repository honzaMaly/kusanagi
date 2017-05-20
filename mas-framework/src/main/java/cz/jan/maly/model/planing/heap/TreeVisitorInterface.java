package cz.jan.maly.model.planing.heap;

import cz.jan.maly.model.planing.command.ActCommand;
import cz.jan.maly.model.planing.command.ReasoningCommand;

/**
 * Interface to be implemented by visitor of decision heapOfTrees. Execution pay 3 different types of visit to heapOfTrees. Using
 * design pattern Visitor enables to have logic to handle flow implemented by concrete visitor.
 * Created by Jan on 21-Feb-17.
 */
public interface TreeVisitorInterface {

    /**
     * Visit heapOfTrees
     */
    void visitTree();

    void visit(IntentionNodeAtTopLevel.WithAbstractPlan<?, ?> node);

    void visit(IntentionNodeNotTopLevel.WithAbstractPlan<?, ?, ?> node);

    void visitNodeWithActingCommand(IntentionNodeNotTopLevel.WithCommand<?, ?, ActCommand.Own> node);

    void visitNodeWithReasoningCommand(IntentionNodeNotTopLevel.WithCommand<?, ?, ReasoningCommand> node);

    void visit(IntentionNodeAtTopLevel.WithDesireForOthers node);

    void visit(IntentionNodeNotTopLevel.WithDesireForOthers<?> node);

    void visit(IntentionNodeAtTopLevel.WithCommand.OwnReasoning node);

    void visit(IntentionNodeAtTopLevel.WithCommand.OwnActing node);

    void visit(IntentionNodeAtTopLevel.WithCommand.FromAnotherAgent node);

}
