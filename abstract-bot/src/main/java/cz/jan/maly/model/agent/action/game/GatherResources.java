package cz.jan.maly.model.agent.action.game;

import bwapi.Game;
import cz.jan.maly.model.metadata.Fact;
import cz.jan.maly.model.game.wrappers.AUnit;

/**
 * Tell unit to gather resources defined in memory
 * Created by Jan on 17-Dec-16.
 */
public class GatherResources implements Action {
    private final Fact<AUnit> resourceToGather;

    public GatherResources(Fact<AUnit> resourceToGather) {
        this.resourceToGather = resourceToGather;
    }

    @Override
    public void executeAction(AUnit unit, Game game) {
        unit.gather(resourceToGather.getContent());
    }
}
