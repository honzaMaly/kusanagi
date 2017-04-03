package cz.jan.maly.model;

import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.knowledge.WorkingMemory;
import cz.jan.maly.model.metadata.DesireKey;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.metadata.agents.configuration.ConfigurationWithCommand;
import cz.jan.maly.model.planing.command.ActCommand;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static cz.jan.maly.model.DesiresKeys.MINE_MINERALS;
import static cz.jan.maly.model.FactsKeys.IS;
import static cz.jan.maly.model.FactsKeys.REPRESENTS;

/**
 * Created by Jan on 15-Mar-17.
 */
public class AgentsTypes {

    public static final AgentTypeObservingGame WORKER = AgentTypeObservingGame.builder()
            .name("WORKER")
            .desiresWithIntentionToAct(new HashSet<>(Arrays.asList(new DesireKey[]{MINE_MINERALS})))
            .usingTypesForFacts(new HashSet<>(Arrays.asList(new FactKey<?>[]{IS, REPRESENTS})))
            .initializationStrategy(type -> {

                //go mining
                ConfigurationWithCommand.WithActingCommandDesiredBySelf miningConfiguration = ConfigurationWithCommand
                        .WithActingCommandDesiredBySelf.builder()
                        .commandCreationStrategy(intention -> new ActCommand.Own(intention) {
                            @Override
                            public boolean act(WorkingMemory memory) {
                                List<AUnit> resources = intention.returnFactValueForGivenKey(IS).get().getResourceUnitsInRadiusOfSight();
                                return intention.returnFactValueForGivenKey(IS).get().gather(resources.get(0));
                            }
                        })
                        .decisionInDesire((desire, dataForDecision) -> !desire.returnFactValueForGivenKey(IS).get().getResourceUnitsInRadiusOfSight().isEmpty())
                        .decisionInIntention((intention, dataForDecision) -> false)
                        .build();
                type.addConfiguration(MINE_MINERALS, miningConfiguration);

                //todo abstract plan to mine the closest resources

            })
            .build();

}
