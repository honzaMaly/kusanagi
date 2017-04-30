package cz.jan.maly.model.watcher.agent_watcher_type_extension;

import cz.jan.maly.model.game.wrappers.AUnitOfPlayer;
import cz.jan.maly.model.game.wrappers.AUnitWithCommands;
import cz.jan.maly.model.metadata.AgentTypeID;
import cz.jan.maly.model.metadata.FactKey;
import cz.jan.maly.model.watcher.AgentWatcherType;
import cz.jan.maly.model.watcher.Beliefs;
import cz.jan.maly.model.watcher.updating_strategies.AgentEnvironmentObservation;
import cz.jan.maly.model.watcher.updating_strategies.Reasoning;
import cz.jan.maly.service.WatcherMediatorService;
import lombok.Builder;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

import static cz.jan.maly.model.bot.FactKeys.*;

/**
 * Extension of AgentWatcherType to UnitWatcherType
 * Created by Jan on 28-Apr-17.
 */
public class UnitWatcherType extends AgentWatcherType {

    @Getter
    private static final AgentEnvironmentObservation agentEnvironmentObservation = (aUnit, beliefs, frame) -> {
        AUnitWithCommands unitWithCommands = aUnit.makeObservationOfEnvironment(frame);
        beliefs.updateFact(REPRESENTS_UNIT, unitWithCommands);
        return unitWithCommands;
    };

    @Builder
    private UnitWatcherType(AgentTypeID agentTypeID, Set<FactKey<?>> factKeys, Set<FactKey<?>> factSetsKeys,
                            List<PlanWatcherInitializationStrategy> planWatchers, ReasoningForAgentWithUnitRepresentation reasoning) {
        super(agentTypeID, factKeys, factSetsKeys, planWatchers, reasoning);
        this.getFactSetsKeys().addAll(Arrays.asList(ENEMY_BUILDING, ENEMY_AIR,
                ENEMY_GROUND, OWN_BUILDING, OWN_AIR, OWN_GROUND));
        this.getFactKeys().add(REPRESENTS_UNIT);
        this.getFactKeys().add(LOCATION);
    }

    /**
     * Builder with default values
     */
    public static class UnitWatcherTypeBuilder extends AgentWatcherTypeBuilder {
        private Set<FactKey<?>> factKeys = new HashSet<>();
        private Set<FactKey<?>> factSetsKeys = new HashSet<>();
        private List<PlanWatcherInitializationStrategy> planWatchers = new ArrayList<>();
    }

    /**
     * Extension of reasoning to provide common beliefs updates
     */
    public static class ReasoningForAgentWithUnitRepresentation implements Reasoning {
        private final Reasoning reasoning;

        public ReasoningForAgentWithUnitRepresentation(Reasoning reasoning) {
            this.reasoning = reasoning;
        }

        /**
         * Updates beliefs
         *
         * @param beliefs
         */
        void updateBeliefsAboutUnitsInSurroundingArea(Beliefs beliefs) {
            AUnitOfPlayer unit = beliefs.returnFactValueForGivenKey(REPRESENTS_UNIT).get();

            //enemies
            beliefs.updateFactSetByFacts(ENEMY_BUILDING, unit.getEnemyUnitsInRadiusOfSight().stream().filter(enemy -> enemy.getType().isBuilding()).collect(Collectors.toSet()));
            beliefs.updateFactSetByFacts(ENEMY_GROUND, unit.getEnemyUnitsInRadiusOfSight().stream().filter(enemy -> !enemy.getType().isBuilding() && !enemy.getType().isFlyer()).collect(Collectors.toSet()));
            beliefs.updateFactSetByFacts(ENEMY_AIR, unit.getEnemyUnitsInRadiusOfSight().stream().filter(enemy -> !enemy.getType().isBuilding() && enemy.getType().isFlyer()).collect(Collectors.toSet()));

            //friendlies
            beliefs.updateFactSetByFacts(OWN_BUILDING, unit.getFriendlyUnitsInRadiusOfSight().stream().filter(own -> own.getType().isBuilding()).collect(Collectors.toSet()));
            beliefs.updateFactSetByFacts(OWN_GROUND, unit.getFriendlyUnitsInRadiusOfSight().stream().filter(own -> !own.getType().isBuilding() && !own.getType().isFlyer()).collect(Collectors.toSet()));
            beliefs.updateFactSetByFacts(OWN_AIR, unit.getFriendlyUnitsInRadiusOfSight().stream().filter(own -> !own.getType().isBuilding() && own.getType().isFlyer()).collect(Collectors.toSet()));

            beliefs.updateFact(LOCATION, unit.getNearestBaseLocation().orElse(LOCATION.getInitValue()));
        }

        @Override
        public void updateBeliefs(Beliefs beliefs, WatcherMediatorService mediatorService) {
            updateBeliefsAboutUnitsInSurroundingArea(beliefs);
            reasoning.updateBeliefs(beliefs, mediatorService);
        }
    }
}
