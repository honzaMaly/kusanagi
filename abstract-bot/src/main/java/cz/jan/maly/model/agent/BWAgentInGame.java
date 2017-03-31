package cz.jan.maly.model.agent;

import bwapi.Game;
import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
import cz.jan.maly.model.knowledge.Fact;
import cz.jan.maly.model.metadata.AgentType;
import cz.jan.maly.model.planing.command.ObservingCommand;
import cz.jan.maly.service.implementation.BotFacade;

import static cz.jan.maly.model.FactsKeys.IS;
import static cz.jan.maly.model.FactsKeys.REPRESENTS;

/**
 * Extension of BWAgent to keep reference on actual unit in GAME
 * Created by Jan on 15-Mar-17.
 */
public class BWAgentInGame extends BWAgent {

    //single definition of command to observe to be used by all agents of this type
    private static final ObservingCommand<Game> OBSERVING_COMMAND = (memory, environment) -> {
        if (!memory.returnFactValueForGivenKey(IS).isPresent()) {
            throw new RuntimeException("Trying to access commendable unit but it is not present.");
        }

        //update fields by creating new instance
        AUnitWithCommands unitWithCommands = memory.returnFactValueForGivenKey(IS).get().makeObservationOfEnvironment();

        //add updated version of itself to knowledge
        memory.updateFact(new Fact<>(unitWithCommands, IS));
        memory.updateFact(new Fact<>(unitWithCommands, REPRESENTS));
    };

    public BWAgentInGame(AgentType<Game> agentType, BotFacade botFacade, AUnitWithCommands unit) {
        super(agentType, botFacade);

        //add itself to knowledge
        beliefs.updateFact(new Fact<>(unit, IS));
        beliefs.updateFact(new Fact<>(unit, REPRESENTS));
    }

    /**
     * Get observing command
     *
     * @return
     */
    public static ObservingCommand<Game> observingCommand() {
        return OBSERVING_COMMAND;
    }
}
