package cz.jan.maly.model.agent.action.game;

import bwapi.Game;
import cz.jan.maly.model.data.Fact;
import cz.jan.maly.model.game.wrappers.AUnit;
import cz.jan.maly.model.game.wrappers.AUnitType;

/**
 * Tell unit to morph to type defined in memory
 * Created by Jan on 19-Dec-16.
 */
public class Morph implements Action {
    private final Fact<AUnitType> morphTo;

    public Morph(Fact<AUnitType> morphTo) {
        this.morphTo = morphTo;
    }

    @Override
    public void executeAction(AUnit unit, Game game) {
        unit.morph(morphTo.getContent());
    }
}
