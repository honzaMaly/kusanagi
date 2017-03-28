package cz.jan.maly.model.game.wrappers;

import bwapi.Unit;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for Unit in game
 * Created by Jan on 28-Mar-17.
 */
public class AUnit implements UnitActions {

    private static final Map<Unit, AUnit> instances = new HashMap<>();

    final Unit unit;

    @Getter
    private AUnitWrapper aUnitWrapper;

    public AUnit(Unit unit) {
        this.unit = unit;
        this.aUnitWrapper = new AUnitWrapper(unit);
    }

    public AUnitWrapper makeObservationOfEnvironment() {
        this.aUnitWrapper = new AUnitWrapper(unit);
        return this.aUnitWrapper;
    }

    /**
     * Wrapped unit to command is returned
     *
     * @param unitToWrap
     * @return
     */
    public static AUnit wrapUnit(Unit unitToWrap) {
        if (unitToWrap == null) {
            return null;
        }
        if (instances.containsKey(unitToWrap)) {
            return instances.get(unitToWrap);
        } else {
            return instances.put(unitToWrap, new AUnit(unitToWrap));
        }
    }

    /**
     * This method exists only to allow reference in UnitActions class.
     */
    @Override
    public AUnit unit() {
        return this;
    }

}
