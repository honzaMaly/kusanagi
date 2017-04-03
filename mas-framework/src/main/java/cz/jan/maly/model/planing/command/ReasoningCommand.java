package cz.jan.maly.model.planing.command;

import cz.jan.maly.model.planing.IntentionCommand;

/**
 * Template for command with reasoning to make
 * Created by jean on 06/03/2017.
 */
public abstract class ReasoningCommand extends CommandForIntention<IntentionCommand.OwnReasoning> {
    protected ReasoningCommand(IntentionCommand.OwnReasoning intention) {
        super(intention);
    }
}
