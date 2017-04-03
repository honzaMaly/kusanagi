package cz.jan.maly.model.game.wrappers;

import bwapi.Unit;
import cz.jan.maly.service.implementation.BotFacade;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * UnitWrapperFactory wraps units and handles refreshment of fields in wrappers to keep them current
 * Created by Jan on 01-Apr-17.
 */
public class UnitWrapperFactory {
    private static final Map<Integer, UnitValue<AUnit.Enemy>> enemyUnits = new ConcurrentHashMap<>();
    private static final Map<Integer, UnitValue<AUnit>> resourceUnits = new ConcurrentHashMap<>();
    private static final Map<Integer, UnitValue<AUnitWithCommands>> playersUnits = new ConcurrentHashMap<>();
    private static final Set<Integer> idsOfDeadUnits = (new ConcurrentHashMap<Integer, Object>()).keySet();

    //todo move units from maps if they become hostile/friendly?

    /**
     * Clear cache
     */
    public static void clearCache() {
        enemyUnits.clear();
        resourceUnits.clear();
        playersUnits.clear();
        idsOfDeadUnits.clear();
    }

    /**
     * Returns true if unit is dead
     *
     * @param unitId
     * @return
     */
    static boolean isDead(Integer unitId) {
        return idsOfDeadUnits.contains(unitId);
    }

    /**
     * Register unit as dead
     *
     * @param unit
     */
    public static void unitDied(Unit unit) {
        idsOfDeadUnits.add(unit.getID());
    }

    /**
     * Wrap player's units to AUnitWithCommands
     *
     * @param unit
     * @param frameCount
     * @return
     */
    public static AUnitWithCommands getCurrentWrappedUnitToCommand(Unit unit, int frameCount, boolean isCreatingUnit) {
        UnitValue<AUnitWithCommands> unitValue = playersUnits.get(unit.getID());

        //unit is present
        if (unitValue != null) {

            //update unit only if it is not current
            if (unitValue.lastUpdate + BotFacade.getRefreshInfoAboutOwnUnitAfterFrames() < frameCount) {
                unitValue.unit = new AUnitWithCommands(unit, isCreatingUnit);
                unitValue.lastUpdate = frameCount;
                wrapReferencedUnitsForUnit(unitValue.unit, frameCount, isCreatingUnit);
            }
            return unitValue.unit;
        }

        //register new unit
        unitValue = new UnitValue<>(new AUnitWithCommands(unit, isCreatingUnit), frameCount);
        playersUnits.put(unit.getID(), unitValue);
        wrapReferencedUnitsForUnit(unitValue.unit, frameCount, isCreatingUnit);
        return unitValue.unit;
    }

    /**
     * Wraps enemy unit
     *
     * @param unit
     * @param frameCount
     */
    private static void wrapEnemyUnits(Unit unit, int frameCount, boolean isCreatingUnit) {
        UnitValue<AUnit.Enemy> unitValue = enemyUnits.get(unit.getID());

        //unit is present
        if (unitValue != null) {

            //update unit only if it is not current
            if (unitValue.lastUpdate + BotFacade.getRefreshInfoAboutEnemyUnitAfterFrames() < frameCount) {
                unitValue.unit = new AUnit.Enemy(unit, isCreatingUnit);
                unitValue.lastUpdate = frameCount;
                wrapReferencedUnitsForUnit(unitValue.unit, frameCount, isCreatingUnit);
            }
        } else {

            //register new unit
            unitValue = new UnitValue<>(new AUnit.Enemy(unit, isCreatingUnit), frameCount);
            enemyUnits.put(unit.getID(), unitValue);
            wrapReferencedUnitsForUnit(unitValue.unit, frameCount, isCreatingUnit);
        }
    }

    /**
     * Wraps enemy unit
     *
     * @param unit
     * @param frameCount
     */
    private static void wrapResourceUnits(Unit unit, int frameCount, boolean isCreatingUnit) {
        UnitValue<AUnit> unitValue = resourceUnits.get(unit.getID());

        //unit is present
        if (unitValue != null) {

            //update unit only if it is not current
            if (unitValue.lastUpdate + BotFacade.getRefreshInfoAboutResourceUnitAfterFrames() < frameCount) {
                unitValue.unit = new AUnit(unit, isCreatingUnit);
                unitValue.lastUpdate = frameCount;
            }
        } else {

            //register new unit
            unitValue = new UnitValue<>(new AUnit(unit, isCreatingUnit), frameCount);
            resourceUnits.put(unit.getID(), unitValue);
        }
    }

    /**
     * Wraps referenced units by enemy
     *
     * @param unit
     * @param frameCount
     */
    private static void wrapReferencedUnitsForUnit(AUnit.Enemy unit, int frameCount, boolean isCreatingUnit) {

        //each enemy unit of enemy is player's in 1v1 game
        Set<Unit> playersUnits = new HashSet<>();
        playersUnits.addAll(unit.enemyUnitsInRadiusOfSight);
        playersUnits.addAll(unit.enemyUnitsInWeaponRange);
        playersUnits.forEach(u -> getCurrentWrappedUnitToCommand(u, frameCount, isCreatingUnit));

        //wrap enemies units
        unit.friendlyUnitsInRadiusOfSight.forEach(u -> wrapEnemyUnits(u, frameCount, isCreatingUnit));

        //wrap resources
        unit.resourceUnitsInRadiusOfSight.forEach(u -> wrapResourceUnits(u, frameCount, isCreatingUnit));
    }

    /**
     * Wraps referenced units by player's unit
     *
     * @param unit
     * @param frameCount
     */
    private static void wrapReferencedUnitsForUnit(AUnitWithCommands unit, int frameCount, boolean isCreatingUnit) {

        //wrap enemy units
        Set<Unit> enemyUnits = new HashSet<>();
        enemyUnits.addAll(unit.enemyUnitsInRadiusOfSight);
        enemyUnits.addAll(unit.enemyUnitsInWeaponRange);
        enemyUnits.forEach(u -> wrapEnemyUnits(u, frameCount, isCreatingUnit));

        //wrap friendly units
        Set<Unit> playerUnits = new HashSet<>();
        playerUnits.addAll(unit.friendlyUnitsInRadiusOfSight);
        playerUnits.addAll(unit.loadedUnits);
        unit.transport.ifPresent(playerUnits::add);
        playerUnits.forEach(u -> getCurrentWrappedUnitToCommand(u, frameCount, isCreatingUnit));

        //wrap resources
        unit.resourceUnitsInRadiusOfSight.forEach(u -> wrapResourceUnits(u, frameCount, isCreatingUnit));
    }

    /**
     * Get wrapped enemy unit for passed unit
     *
     * @param unitId
     * @return
     */
    static Optional<AUnit.Enemy> getWrappedEnemyUnit(Integer unitId) {
        if (enemyUnits.containsKey(unitId)) {
            return Optional.ofNullable(enemyUnits.get(unitId).unit);
        }
        return Optional.empty();
    }

    /**
     * Get wrapped resource unit for passed unit
     *
     * @param unitId
     * @return
     */
    static Optional<AUnit> getWrappedResourceUnit(Integer unitId) {
        if (resourceUnits.containsKey(unitId)) {
            return Optional.ofNullable(resourceUnits.get(unitId).unit);
        }
        return Optional.empty();
    }

    /**
     * Get wrapped player's unit for passed unit
     *
     * @param unitId
     * @return
     */
    static Optional<AUnitOfPlayer> getWrappedPlayersUnit(Integer unitId) {
        if (playersUnits.containsKey(unitId)) {
            return Optional.ofNullable(playersUnits.get(unitId).unit);
        }
        return Optional.empty();
    }

    /**
     * Get wrapped unit for passed unit
     *
     * @param unitId
     * @return
     */
    static Optional<AUnit> getWrappedUnit(Integer unitId) {
        if (enemyUnits.containsKey(unitId)) {
            return Optional.ofNullable(enemyUnits.get(unitId).unit);
        }
        if (resourceUnits.containsKey(unitId)) {
            return Optional.ofNullable(resourceUnits.get(unitId).unit);
        }
        if (playersUnits.containsKey(unitId)) {
            return Optional.ofNullable(playersUnits.get(unitId).unit);
        }
        return Optional.empty();
    }

    /**
     * Keep unit and frame of its last update together
     */
    private static class UnitValue<T extends AUnit> {
        T unit;
        int lastUpdate;

        UnitValue(T unit, int lastUpdate) {
            this.unit = unit;
            this.lastUpdate = lastUpdate;
        }
    }
}
