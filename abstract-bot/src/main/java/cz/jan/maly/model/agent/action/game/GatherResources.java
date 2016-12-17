package cz.jan.maly.model.agent.action.game;

import cz.jan.maly.model.Fact;
import cz.jan.maly.model.game.wrappers.AUnit;

/**
 * Tell unit to gather resources defined in memory
 * Created by Jan on 17-Dec-16.
 */
public class GatherResources extends Action {
    private final Fact<AUnit> resourceToGather;

    public GatherResources(AUnit unit, Fact<AUnit> resourceToGather) {
        super(unit);
        this.resourceToGather = resourceToGather;
    }

    @Override
    public void executeAction() {
        unit.gather(resourceToGather.getContent());
    }
}
