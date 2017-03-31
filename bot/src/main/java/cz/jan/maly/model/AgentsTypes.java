package cz.jan.maly.model;

import bwapi.Game;
import cz.jan.maly.model.agent.BWAgentInGame;
import cz.jan.maly.model.metadata.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import static cz.jan.maly.model.DesiresKeys.MINE_MINERALS;
import static cz.jan.maly.model.FactsKeys.*;

/**
 * Created by Jan on 15-Mar-17.
 */
public class AgentsTypes {

    public static final AgentType<Game> WORKER = new AgentType<Game>("WORKER", BWAgentInGame.observingCommand(),
            new HashSet<>(), new HashSet<>(), Arrays.stream(new DesireKey[]{MINE_MINERALS}).collect(Collectors.toSet()), new HashSet<>()) {
        @Override
        protected void initializeConfiguration() {
            addDesireFormulationConfigurationForOwnDesireWithActingCommand(MINE_MINERALS,
                    new DecisionParameters(Arrays.stream(new FactKey<?>[]{IS_MINING_MINERAL}).collect(Collectors.toSet()),
                            Arrays.stream(new FactKey<?>[]{MINERAL}).collect(Collectors.toSet()),
                            new HashSet<>()),
                    (dataForDecision, desire) -> dataForDecision.returnFactValueForGivenKey(IS_MINING_MINERAL).isPresent() && dataForDecision.returnFactValueForGivenKey(IS_MINING_MINERAL).get() != null && !dataForDecision.returnFactSetValueForGivenKey(MINERAL).get().isEmpty(),
                    new DecisionParameters(new HashSet<>(), new HashSet<>(), new HashSet<>()),
                    (dataForDecision, intention) -> false,
                    new IntentionParameters(Arrays.stream(new FactKey<?>[]{IS}).collect(Collectors.toSet()), new HashSet<>()),
                    null);
        }
    };

}
