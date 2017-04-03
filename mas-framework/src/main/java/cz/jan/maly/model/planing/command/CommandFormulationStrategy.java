package cz.jan.maly.model.planing.command;

import cz.jan.maly.model.planing.IntentionCommand;

/**
 * Contract for acting command creation strategy
 * Created by Jan on 01-Apr-17.
 */
public interface CommandFormulationStrategy<V extends CommandForIntention<?>, T extends IntentionCommand<?, ?>> {
    /**
     * Form command from intention
     *
     * @param intention
     * @return
     */
    V formCommand(T intention);
}
