package cz.jan.maly.service.implementation;

import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.planing.command.ReasoningCommandForIntention;
import cz.jan.maly.service.CommandManager;

/**
 * Manager to execute reasoning command on agent's behalf
 * Created by jean on 06/03/2017.
 */
public class ReasoningExecutor implements CommandManager<ReasoningCommandForIntention, WorkingMemory> {

    @Override
    public boolean executeCommand(ReasoningCommandForIntention commandToExecute, WorkingMemory memory) {
        return commandToExecute.act(memory);
    }
}
