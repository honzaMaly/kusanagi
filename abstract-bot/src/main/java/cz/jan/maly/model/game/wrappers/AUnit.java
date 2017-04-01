package cz.jan.maly.model.game.wrappers;

import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;
import bwta.BWTA;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Wrapper for Unit with fields visible by all players
 * Created by Jan on 28-Mar-17.
 */
public class AUnit {
    private static final Set<UnitType> resourcesTypes = Arrays.stream(new UnitType[]{UnitType.Resource_Mineral_Field,
            UnitType.Resource_Mineral_Field_Type_2, UnitType.Resource_Mineral_Field_Type_3, UnitType.Resource_Vespene_Geyser})
            .collect(Collectors.toSet());

    final Unit unit;

    private final int unitId;

    private final UnitType type;

    public AUnitTypeWrapper getType() {
        return WrapperTypeFactory.createFrom(type);
    }

    @Getter
    private final boolean exists;

    @Getter
    private final APosition position;

    @Getter
    private final boolean isFlying;

    @Getter
    private final boolean isSieged;

    @Getter
    private final boolean isStuck;

    @Getter
    private final boolean isLifted;

    @Getter
    private final boolean isMoving;

    @Getter
    private final int shields;

    @Getter
    private final double velocityY;

    @Getter
    private final int hitPoints;

    @Getter
    private final boolean isAccelerating;

    @Getter
    private final double velocityX;

    @Getter
    private final boolean isIdle;

    @Getter
    private final boolean isBeingGathered;

    @Getter
    private final boolean isBraking;

    @Getter
    private final boolean isEnsnared;

    @Getter
    private final boolean isLockedDown;

    @Getter
    private final boolean isParasited;

    @Getter
    private final boolean isIrradiated;

    @Getter
    private final boolean isBurrowed;

    @Getter
    private final boolean isAttacking;

    @Getter
    private final boolean isCloaked;

    @Getter
    private final boolean isConstructing;

    @Getter
    private final boolean isCarryingGas;

    @Getter
    private final boolean isInvincible;

    @Getter
    private final boolean isDetected;

    @Getter
    private final boolean isAttackFrame;

    @Getter
    private final boolean isGatheringGas;

    @Getter
    private final boolean isMaelstrommed;

    @Getter
    private final boolean isBeingHealed;

    @Getter
    private final boolean isMorphing;

    @Getter
    private final boolean isUnderDarkSwarm;

    @Getter
    private final boolean isTargetable;

    @Getter
    private final boolean isStasised;

    @Getter
    private final boolean isRepairing;

    @Getter
    private final boolean isPlagued;

    @Getter
    private final boolean isStimmed;

    @Getter
    private final boolean isUnderStorm;

    @Getter
    private final boolean isVisible;

    @Getter
    private final boolean isPowered;

    @Getter
    private final boolean isStartingAttack;

    @Getter
    private final boolean isUnderAttack;

    @Getter
    private final boolean isBeingConstructed;

    @Getter
    private final boolean isGatheringMinerals;

    @Getter
    private final boolean isCarryingMinerals;

    @Getter
    private final APlayer player;

    @Getter
    private final Optional<bwta.Region> unitRegion;

    final List<Unit> enemyUnitsInWeaponRange;

    final List<Integer> enemyUnitsInWeaponRangeIds;

    final List<Unit> friendlyUnitsInRadiusOfSight;

    final List<Integer> friendlyUnitsInRadiusOfSightIds;

    final List<Unit> resourceUnitsInRadiusOfSight;

    final List<Integer> resourceUnitsInRadiusOfSightIds;

    final List<Unit> enemyUnitsInRadiusOfSight;

    final List<Integer> enemyUnitsInRadiusOfSightIds;

    AUnit(Unit unit) {
        this.unit = unit;
        this.unitRegion = Optional.ofNullable(BWTA.getRegion(unit.getTilePosition()));

        //units in weapon range
        this.enemyUnitsInWeaponRange = unit.getUnitsInWeaponRange(unit.getType().airWeapon()).stream()
                .filter(u -> !u.getPlayer().equals(unit.getPlayer()) && !u.getPlayer().isNeutral())
                .filter(u -> !resourcesTypes.contains(u.getType()))
                .collect(Collectors.toList());
        this.enemyUnitsInWeaponRange.addAll(unit.getUnitsInWeaponRange(unit.getType().groundWeapon()).stream()
                .filter(u -> !u.getPlayer().equals(unit.getPlayer()) && !u.getPlayer().isNeutral())
                .filter(u -> !resourcesTypes.contains(u.getType()))
                .collect(Collectors.toList()));
        this.enemyUnitsInWeaponRangeIds = this.enemyUnitsInWeaponRange.stream()
                .map(Unit::getID)
                .collect(Collectors.toList());

        //units in radius of sight
        this.friendlyUnitsInRadiusOfSight = unit.getUnitsInRadius(unit.getType().sightRange()).stream()
                .filter(u -> u.getPlayer().equals(unit.getPlayer()))
                .collect(Collectors.toList());
        this.resourceUnitsInRadiusOfSight = unit.getUnitsInRadius(unit.getType().sightRange()).stream()
                .filter(u -> resourcesTypes.contains(u.getType()) && u.getPlayer().isNeutral())
                .collect(Collectors.toList());
        this.enemyUnitsInRadiusOfSight = unit.getUnitsInRadius(unit.getType().sightRange()).stream()
                .filter(u -> !u.getPlayer().equals(unit.getPlayer()) && !u.getPlayer().isNeutral())
                .collect(Collectors.toList());

        //ids
        this.friendlyUnitsInRadiusOfSightIds = this.friendlyUnitsInRadiusOfSight.stream()
                .map(Unit::getID)
                .collect(Collectors.toList());
        this.resourceUnitsInRadiusOfSightIds = this.resourceUnitsInRadiusOfSight.stream()
                .map(Unit::getID)
                .collect(Collectors.toList());
        this.enemyUnitsInRadiusOfSightIds = this.enemyUnitsInRadiusOfSight.stream()
                .map(Unit::getID)
                .collect(Collectors.toList());

        this.position = new APosition(unit.getPosition());
        this.type = unit.getType();
        this.exists = unit.exists();
        this.velocityX = unit.getVelocityX();
        this.isIdle = unit.isIdle();
        this.isFlying = unit.isFlying();
        this.isSieged = unit.isSieged();
        this.isStuck = unit.isStuck();
        this.isLifted = unit.isLifted();
        this.isMoving = unit.isMoving();
        this.shields = unit.getShields();
        this.velocityY = unit.getVelocityY();
        this.hitPoints = unit.getHitPoints();
        this.isAccelerating = unit.isAccelerating();
        this.isBeingGathered = unit.isBeingGathered();
        this.isBraking = unit.isBraking();
        this.isEnsnared = unit.isEnsnared();
        this.isLockedDown = unit.isLockedDown();
        this.isParasited = unit.isParasited();
        this.isIrradiated = unit.isIrradiated();
        this.isBurrowed = unit.isBurrowed();
        this.isAttacking = unit.isAttacking();
        this.isCloaked = unit.isCloaked();
        this.isConstructing = unit.isConstructing();
        this.isCarryingGas = unit.isCarryingGas();
        this.isInvincible = unit.isInvincible();
        this.isDetected = unit.isDetected();
        this.isAttackFrame = unit.isAttackFrame();
        this.isGatheringGas = unit.isGatheringGas();
        this.isMaelstrommed = unit.isMaelstrommed();
        this.isBeingHealed = unit.isBeingHealed();
        this.isMorphing = unit.isMorphing();
        this.isUnderDarkSwarm = unit.isUnderDarkSwarm();
        this.isTargetable = unit.isTargetable();
        this.isStasised = unit.isStasised();
        this.isRepairing = unit.isRepairing();
        this.isPlagued = unit.isPlagued();
        this.isStimmed = unit.isStimmed();
        this.isUnderStorm = unit.isUnderStorm();
        this.isVisible = unit.isVisible();
        this.isPowered = unit.isPowered();
        this.isStartingAttack = unit.isStartingAttack();
        this.isUnderAttack = unit.isUnderAttack();
        this.isBeingConstructed = unit.isBeingConstructed();
        this.isGatheringMinerals = unit.isGatheringMinerals();
        this.isCarryingMinerals = unit.isCarryingMinerals();
        this.player = APlayer.wrapPlayer(unit.getPlayer()).get();
        unitId = unit.getID();
    }

    public boolean isFullyHealthy() {
        return getHitPoints() >= getType().getMaxHitPoints();
    }

    public int getHPPercent() {
        return 100 * getHitPoints() / getType().getMaxHitPoints();
    }

    public boolean isWounded() {
        return getHitPoints() < getType().getMaxHitPoints();
    }

    /**
     * Returns true if this unit is capable of attacking <b>otherUnit</b>. For example Zerglings can't attack
     * flying targets and Corsairs can't attack ground targets.
     */
    public boolean canAttackThisKindOfUnit(AUnitOfPlayer otherUnit) {

        // Enemy is GROUND unit
        if (!otherUnit.getType().isFlyer()) {
            return getType().canAttackGroundUnits();
        } // Enemy is AIR unit
        else {
            return getType().canAttackAirUnits();
        }
    }

    /**
     * Returns <b>true</b> if this unit can attack <b>targetUnit</b> in terms of both min and max range
     * conditions fulfilled.
     *
     * @param safetyMargin allowed error (in tiles) applied to the max distance condition
     */
    public boolean hasRangeToAttack(AUnit targetUnit, double safetyMargin) {
        AWeaponTypeWrapper weaponAgainstThisUnit = getWeaponAgainst(targetUnit);
        double dist = getPosition().distanceTo(targetUnit);
        return !weaponAgainstThisUnit.isForType(WeaponType.None)
                && weaponAgainstThisUnit.getMaxRange() <= (dist + safetyMargin)
                && weaponAgainstThisUnit.getMinRange() >= dist;
    }

    /**
     * Returns weapon that would be used to attack given target.
     * If no such weapon, then WeaponTypes.None will be returned.
     */
    public AWeaponTypeWrapper getWeaponAgainst(AUnit target) {
        if (!target.getType().isFlyer()) {
            return getType().getGroundWeapon();
        } else {
            return getType().getAirWeapon();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AUnit aUnit = (AUnit) o;

        return unitId == aUnit.unitId;
    }

    @Override
    public int hashCode() {
        return unitId;
    }

    /**
     * Returns true if unit is starting an attack or already in the attack frame animation.
     */
    public boolean isJustShooting() {
        return isAttackFrame() || isStartingAttack();
    }

    /**
     * Enemy
     */
    public static class Enemy extends AUnit {
        Enemy(Unit unit) {
            super(unit);
        }

        public List<AUnitOfPlayer> getEnemyUnitsInWeaponRange() {
            return enemyUnitsInWeaponRangeIds.stream()
                    .map(UnitWrapperFactory::getWrappedPlayersUnit)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }

        public List<AUnit.Enemy> getFriendlyUnitsInRadiusOfSight() {
            return friendlyUnitsInRadiusOfSightIds.stream()
                    .map(UnitWrapperFactory::getWrappedEnemyUnit)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }

        public List<AUnit> getResourceUnitsInRadiusOfSight() {
            return resourceUnitsInRadiusOfSightIds.stream()
                    .map(UnitWrapperFactory::getWrappedResourceUnit)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }

        public List<AUnitOfPlayer> getEnemyUnitsInRadiusOfSight() {
            return enemyUnitsInRadiusOfSightIds.stream()
                    .map(UnitWrapperFactory::getWrappedPlayersUnit)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }

    }

    /**
     * Player's unit
     */
    public static class Players extends AUnit {
        Players(Unit unit) {
            super(unit);
        }

        public List<AUnit.Enemy> getEnemyUnitsInWeaponRange() {
            return enemyUnitsInWeaponRangeIds.stream()
                    .map(UnitWrapperFactory::getWrappedEnemyUnit)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }

        public List<AUnitOfPlayer> getFriendlyUnitsInRadiusOfSight() {
            return friendlyUnitsInRadiusOfSightIds.stream()
                    .map(UnitWrapperFactory::getWrappedPlayersUnit)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }

        public List<AUnit> getResourceUnitsInRadiusOfSight() {
            return resourceUnitsInRadiusOfSightIds.stream()
                    .map(UnitWrapperFactory::getWrappedResourceUnit)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }

        public List<AUnit.Enemy> getEnemyUnitsInRadiusOfSight() {
            return enemyUnitsInRadiusOfSightIds.stream()
                    .map(UnitWrapperFactory::getWrappedEnemyUnit)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }

    }

}
