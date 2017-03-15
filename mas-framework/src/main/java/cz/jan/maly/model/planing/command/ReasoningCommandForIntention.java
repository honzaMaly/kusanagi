package cz.jan.maly.model.planing.command;

import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.planing.CommandForIntention;
import cz.jan.maly.model.planing.IntentionCommand;

/**
 * Template for command with reasoning to make
 * Created by jean on 06/03/2017.
 */
public abstract class ReasoningCommandForIntention extends CommandForIntention<IntentionCommand.OwnReasoning, WorkingMemory<?>> {
    protected ReasoningCommandForIntention(IntentionCommand.OwnReasoning intention) {
        super(intention);
    }
}
