package cz.jan.maly.model.planing.command;

import cz.jan.maly.model.planing.IntentionCommand;

/**
 * Template for command with action to execute
 * Created by Jan on 11-Mar-17.
 */
public abstract class ActCommand<T extends IntentionCommand<?, ? extends ActCommand<T>>> extends CommandForIntention<T> {
    private ActCommand(T intention) {
        super(intention);
    }

    /**
     * Template for command initiated by another agent's desire
     */
    public static abstract class DesiredByAnotherAgent extends ActCommand<IntentionCommand.FromAnotherAgent> {
        protected DesiredByAnotherAgent(IntentionCommand.FromAnotherAgent intention) {
            super(intention);
        }
    }

    /**
     * Template for command initiated by own desire
     */
    public static abstract class Own extends ActCommand<IntentionCommand.OwnActing> {
        protected Own(IntentionCommand.OwnActing intention) {
            super(intention);
        }
    }

}
