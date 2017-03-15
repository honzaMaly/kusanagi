package cz.jan.maly.model.game.wrappers;

import bwapi.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper for BWMirror Unit class that makes units much easier to use.<br /><br />
 * Atlantis uses wrappers for BWMirror native classes which can't be extended.<br /><br />
 * <b>AUnit</b> class contains numerous helper methods, but if you think some methods are missing
 * you can create missing method here and you can reference original Unit class via u() method.
 *
 * @author Rafal Poniatowski <ravaelles@gmail.com>
 */
public class AUnit extends APositionedObject implements Comparable<AUnit>, UnitActions {

    private static final Map<Unit, AUnit> instances = new HashMap<>();

    private Unit u;
    private AUnitType _lastCachedType;

    // =========================================================

    private AUnit(Unit u) {
        if (u == null) {
            throw new RuntimeException("AUnit constructor: unit is null");
        }

        this.u = u;
        this.innerID = firstFreeID++;
        this._lastCachedType = AUnitType.createFrom(u.getType());

        // Repair & Heal
        this._repairableMechanically = isBuilding() || isVehicle();
        this._healable = isInfantry() || isWorker();

        // Military building
        this._isMilitaryBuildingAntiGround = isType(
                AUnitType.Terran_Bunker, AUnitType.Protoss_Photon_Cannon, AUnitType.Zerg_Sunken_Colony
        );
        this._isMilitaryBuildingAntiAir = isType(
                AUnitType.Terran_Bunker, AUnitType.Terran_Missile_Turret,
                AUnitType.Protoss_Photon_Cannon, AUnitType.Zerg_Spore_Colony
        );
    }

    /**
     * Atlantis uses wrapper for BWMirror native classes which aren't extended.<br />
     * <b>AUnit</b> class contains numerous helper methods, but if you think some methods are missing
     * you can create missing method here and you can reference original Unit class via u() method.
     */
    public static AUnit createFrom(Unit u) {
        if (u == null) {
            throw new RuntimeException("AUnit constructor: unit is null");
        }

        if (instances.containsKey(u)) {
            return instances.get(u);
        } else {
            AUnit unit = new AUnit(u);
            instances.put(u, unit);
            return unit;
        }
    }

    // =========================================================

    /**
     * Returns unit type from BWMirror OR if type is Unknown (behind fog of war) it will return last cached
     * type.
     */
    public AUnitType getType() {
        AUnitType type = AUnitType.createFrom(u.getType());
        if (type.equals(AUnitType.Unknown)) {
            return _lastCachedType;
        } else {
            _lastCachedType = type;
            return type;
        }
    }

    @Override
    public APosition getPosition() {
        return APosition.createFrom(u.getPosition());
    }

    /**
     * <b>AVOID USAGE AS MUCH AS POSSIBLE</b> outside AUnit class.
     * AUnit class should be used always in place of Unit.
     */
    @Override
    public Unit u() {
        return u;
    }

    /**
     * This method exists only to allow reference in UnitActions class.
     */
    @Override
    public AUnit unit() {
        return this;
    }

    private static AUnit getBWMirrorUnit(Unit u) {
        for (AUnit unit : instances.values()) {
            if (unit.u.equals(u)) {
                return unit;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.getID();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof AUnit) {
            AUnit other = (AUnit) obj;
            return u == other.u;
        }

        return true;
    }

    // =========================================================
    // =========================================================
    // =========================================================

    private static int firstFreeID = 1;
    private int innerID;
    private boolean _repairableMechanically = false;
    private boolean _healable = false;
    private boolean _isMilitaryBuildingAntiGround = false;
    private boolean _isMilitaryBuildingAntiAir = false;

    // =========================================================
    @Override
    public String toString() {
        return "AUnit(" + getType().toString() + " #" + innerID + ")";
    }

    @Override
    public int compareTo(AUnit o) {
        return Integer.compare(this.getID(), ((AUnit) o).getID());
    }

    public boolean canBeHealed() {
        return _repairableMechanically || _healable;
    }

    public boolean isRepairableMechanically() {
        return _repairableMechanically;
    }

    public boolean isHealable() {
        return _healable;
    }

    /**
     * Returns true if given unit is OF TYPE BUILDING.
     */
    public boolean isBuilding() {
        return getType().isBuilding();
    }

    public boolean isWorker() {
        return isType(AUnitType.Terran_SCV, AUnitType.Protoss_Probe, AUnitType.Zerg_Drone);
    }

    public boolean isBunker() {
        return getType().equals(AUnitType.Terran_Bunker);
    }

    public boolean isBase() {
        return isType(AUnitType.Terran_Command_Center, AUnitType.Protoss_Nexus, AUnitType.Zerg_Hatchery,
                AUnitType.Zerg_Lair, AUnitType.Zerg_Hive);
    }

    public boolean isInfantry() {
        return getType().isOrganic();
    }

    public boolean isVehicle() {
        return getType().isMechanical();
    }

    /**
     * Returns true if given unit is considered to be "ranged" unit (not melee).
     */
    public boolean isRangedUnit() {
        return getType().isRangedUnit();
    }

    /**
     * Returns true if given unit is considered to be "melee" unit (not ranged).
     */
    public boolean isMeleeUnit() {
        return getType().isMeleeUnit();
    }

    // =========================================================
    // Auxiliary methods

    public boolean ofType(AUnitType type) {
        return getType().equals(type);
    }

    public boolean isType(AUnitType... types) {
        return getType().isType(types);
    }

    public boolean isFullyHealthy() {
        return getHitPoints() >= getMaxHitPoints();
    }

    public int getHPPercent() {
        return 100 * getHitPoints() / getMaxHitPoints();
    }

    public boolean isWounded() {
        return getHitPoints() < getMaxHP();
    }

    public int getHP() {
        return getHitPoints();
    }

    public int getMaxHP() {
        return getMaxHitPoints();
    }

    public String getShortName() {
        return getType().getShortName();
    }

    /**
     * Has separate name not to mistake attackUnit with attackPosition.
     */
    public boolean attackUnit(AUnit target) {
        return u.attack(target.u);
    }

    /**
     * Returns max shoot range (in build tiles) of this unit against land targets.
     */
    public double getShootRangeGround() {
        return getType().getGroundWeapon().maxRange() / 32;
    }

    /**
     * Returns max shoot range (in build tiles) of this unit against land targets.
     */
    public double getShootRangeAir() {
        return getType().getAirWeapon().maxRange() / 32;
    }

    /**
     * Returns max shoot range (in build tiles) of this unit against given <b>opponentUnit</b>.
     */
    public double getShootRangeAgainst(AUnit opponentUnit) {
        if (opponentUnit.isAirUnit()) {
            return getType().getAirWeapon().maxRange() / 32;
        } else {
            return getType().getGroundWeapon().maxRange() / 32;
        }
    }

    // =========================================================
    // Very specific auxiliary methods

    /**
     * Returns true if given unit is one of buildings like Bunker, Photon Cannon etc. For more details, you
     * have to specify at least one <b>true</b> to the params.
     */
    public boolean isMilitaryBuilding(boolean canShootGround, boolean canShootAir) {
        if (!isBuilding()) {
            return false;
        }
        if (canShootGround && _isMilitaryBuildingAntiGround) {
            return true;
        } else if (canShootAir && _isMilitaryBuildingAntiAir) {
            return true;
        }
        return false;
    }

    public boolean isGroundUnit() {
        return !getType().isAirUnit();
    }

    public boolean isAirUnit() {
        return getType().isAirUnit();
    }

    public boolean isSpiderMine() {
        return getType().equals(AUnitType.Terran_Vulture_Spider_Mine);
    }

    public boolean isLarvaOrEgg() {
        return getType().equals(AUnitType.Zerg_Larva) || getType().equals(AUnitType.Zerg_Egg);
    }

    public boolean isLarva() {
        return getType().equals(AUnitType.Zerg_Larva);
    }

    public boolean isEgg() {
        return getType().equals(AUnitType.Zerg_Egg);
    }

    /**
     * Not that we're racists, but spider mines and larvas aren't really units...
     */
    public boolean isNotActuallyUnit() {
        return isSpiderMine() || isLarvaOrEgg();
    }

    /**
     * Not that we're racists, but spider mines and larvas aren't really units...
     */
    public boolean isActualUnit() {
        return !isNotActuallyUnit();
    }

    // =========================================================
    // RANGE and ATTACK methods

    /**
     * Returns true if this unit is capable of attacking <b>otherUnit</b>. For example Zerglings can't attack
     * flying targets and Corsairs can't attack ground targets.
     *
     * @param includeCooldown if true, then unit will be considered able to attack only if the cooldown after
     *                        the last shot allows it
     */
    public boolean canAttackThisKindOfUnit(AUnit otherUnit, boolean includeCooldown) {

        // Enemy is GROUND unit
        if (otherUnit.isGroundUnit()) {
            return canAttackGroundUnits() && (!includeCooldown || getGroundWeaponCooldown() == 0);
        } // Enemy is AIR unit
        else {
            return canAttackAirUnits() && (!includeCooldown || getAirWeaponCooldown() == 0);
        }
    }

    /**
     * Returns <b>true</b> if this unit can attack <b>targetUnit</b> in terms of both min and max range
     * conditions fulfilled.
     *
     * @param safetyMargin allowed error (in tiles) applied to the max distance condition
     */
    public boolean hasRangeToAttack(AUnit targetUnit, double safetyMargin) {
        WeaponType weaponAgainstThisUnit = getWeaponAgainst(targetUnit);
        double dist = this.distanceTo(targetUnit);
        return weaponAgainstThisUnit != WeaponType.None
                && weaponAgainstThisUnit.maxRange() <= (dist + safetyMargin)
                && weaponAgainstThisUnit.minRange() >= dist;
    }

    /**
     * Returns weapon that would be used to attack given target.
     * If no such weapon, then WeaponTypes.None will be returned.
     */
    public WeaponType getWeaponAgainst(AUnit target) {
        if (target.isGroundUnit()) {
            return getGroundWeapon();
        } else {
            return getAirWeapon();
        }
    }

    /**
     * Returns true if unit is starting an attack or already in the attack frame animation.
     */
    public boolean isJustShooting() {
        return isAttackFrame() || isStartingAttack();
    }

    /**
     * Returns true if unit has anti-ground weapon.
     */
    public boolean canAttackGroundUnits() {
        return getType().getGroundWeapon() != WeaponType.None;
    }

    /**
     * Returns true if unit has anti-air weapon.
     */
    public boolean canAttackAirUnits() {
        return getType().getAirWeapon() != WeaponType.None;
    }

    public WeaponType getAirWeapon() {
        return getType().getAirWeapon();
    }

    public WeaponType getGroundWeapon() {
        return getType().getGroundWeapon();
    }

    public int getID() {
        return innerID;
    }

    // =========================================================
    // Method intermediates between BWMirror and Atlantis

    public Player getPlayer() {
        return u.getPlayer();
    }

    public int getX() {
        return u.getX();
    }

    public int getY() {
        return u.getY();
    }

    public boolean isCompleted() {
        return u.isCompleted();
    }

    public boolean exists() {
        return u.exists();
    }

    public boolean isConstructing() {
        return u.isConstructing();
    }

    public int getHitPoints() {
        return u.getHitPoints();
    }

    public int getMaxHitPoints() {
        return u.getType().maxHitPoints();
    }

    public boolean isIdle() {
        return u.isIdle() || u.getLastCommand().getUnitCommandType().equals(UnitCommandType.None);
    }

    public boolean isVisible() {
        return u.isVisible();
    }

    public boolean isGatheringMinerals() {
        return u.isGatheringMinerals();
    }

    public boolean isGatheringGas() {
        return u.isGatheringGas();
    }

    public boolean isCarryingMinerals() {
        return u.isCarryingMinerals();
    }

    public boolean isCarryingGas() {
        return u.isCarryingGas();
    }

    public boolean isRepairing() {
        return u.isRepairing();
    }

    public int getGroundWeaponCooldown() {
        return u.getGroundWeaponCooldown();
    }

    public int getAirWeaponCooldown() {
        return u.getAirWeaponCooldown();
    }

    public boolean isAttackFrame() {
        return u.isAttackFrame();
    }

    public boolean isStartingAttack() {
        return u.isStartingAttack();
    }

    public List<AUnitType> getTrainingQueue() {
        return (List<AUnitType>) AUnitType.convertToAUnitTypesCollection(u.getTrainingQueue());
    }

    public boolean isUpgrading() {
        return u.isUpgrading();
    }

    public AUnit getTarget() {
        return u.getTarget() != null ? AUnit.createFrom(u.getTarget()) : null;
    }

    public APosition getTargetPosition() {
        return APosition.createFrom(u.getTargetPosition());
    }

    public AUnit getOrderTarget() {
        return u.getOrderTarget() != null ? AUnit.createFrom(u.getOrderTarget()) : null;
    }

    public AUnit getBuildUnit() {
        return u.getBuildUnit() != null ? AUnit.createFrom(u.getBuildUnit()) : null;
    }

    public AUnitType getBuildType() {
        return u.getBuildType() != null ? AUnitType.createFrom(u.getBuildType()) : null;
    }

    public boolean isMorphing() {
        return u.isMorphing();
    }

    public boolean isMoving() {
        return u.isMoving();
    }

    public boolean isAttacking() {
        return u.isAttacking();
    }

    public boolean hasPathTo(Position point) {
        return u.hasPath(point);
    }

    public boolean isTraining() {
        return u.isTraining();
    }

    public boolean isBeingConstructed() {
        return u.isBeingConstructed();
    }

    public UnitCommand getLastCommand() {
        return u.getLastCommand();
    }

}