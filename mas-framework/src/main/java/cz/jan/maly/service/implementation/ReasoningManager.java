package cz.jan.maly.service.implementation;

import cz.jan.maly.model.agents.Agent;
import cz.jan.maly.model.planing.Command;
import cz.jan.maly.service.CommandManager;

import static cz.jan.maly.utils.FrameworkUtils.REASONING_MANAGER;

/**
 * Manager to execute reasoning command on agent's behalf
 * Created by jean on 06/03/2017.
 */
public class ReasoningManager extends CommandManager {

    public ReasoningManager() {
        super(REASONING_MANAGER);
    }

    @Override
    public boolean executeCommand(Command commandToExecute) {
        return commandToExecute.execute();
    }
}
