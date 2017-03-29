package cz.jan.maly.model.game.wrappers;

import bwapi.Unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Wrapper for Unit in game
 * Created by Jan on 28-Mar-17.
 */
public class AUnit implements UnitActions {
    private static final Map<Unit, AUnit> instances = new HashMap<>();
    final Unit unit;
    private AUnitWrapper unitWrapped;

    private AUnit(Unit unit) {
        this.unit = unit;
        this.unitWrapped = new AUnitWrapper(unit);
    }

    /**
     * Get wrapped unit for given unit
     *
     * @param unit
     * @return
     */
    public static Optional<AUnitWrapper> getUnitWrapped(Unit unit) {
        Optional<AUnit> unitWrapped = wrapUnit(unit);
        if (unitWrapped.isPresent()) {
            return Optional.of(unitWrapped.get().unitWrapped);
        }
        return Optional.empty();
    }

    /**
     * Method to refresh fields in wrapper for unit
     */
    public void makeObservationOfEnvironment() {
        this.unitWrapped = new AUnitWrapper(unit);
    }

    /**
     * Wrapped unit to command is returned
     *
     * @param unitToWrap
     * @return
     */
    public static Optional<AUnit> wrapUnit(Unit unitToWrap) {
        if (unitToWrap == null) {
            return Optional.empty();
        }
        if (instances.containsKey(unitToWrap)) {
            return Optional.of(instances.get(unitToWrap));
        } else {
            return Optional.of(instances.put(unitToWrap, new AUnit(unitToWrap)));
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
