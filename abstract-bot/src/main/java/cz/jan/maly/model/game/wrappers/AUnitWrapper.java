package cz.jan.maly.model.game.wrappers;

import bwapi.*;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Wrapper for BWMirror Unit fields.
 */
public class AUnitWrapper {
    final Unit unit;

    @Getter
    private final AUnitTypeWrapper type;

    @Getter
    private final Optional<AUnitWrapper> target;

    @Getter
    private final boolean exists;

    @Getter
    private final double velocityX;

    @Getter
    private final APosition position;

    @Getter
    private final boolean isLoaded;

    @Getter
    private final boolean isIdle;

    @Getter
    private final Optional<Order> order;

    @Getter
    private final boolean hasNuke;

    @Getter
    private final boolean isBlind;

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
    private final List<AUnitTypeWrapper> trainingQueue;

    @Getter
    private final int removeTimer;
    @Getter
    private final int spellCooldown;
    @Getter
    private final int energy;
    @Getter
    private final int scarabCount;
    @Getter
    private final int plagueTimer;
    @Getter
    private final int orderTimer;
    @Getter
    private final int stasisTimer;
    @Getter
    private final int ensnareTimer;
    @Getter
    private final Optional<AUnitWrapper> orderTarget;
    @Getter
    private final int shields;
    @Getter
    private final int lockdownTimer;
    @Getter
    private final int stimTimer;
    @Getter
    private final double velocityY;
    @Getter
    private final int hitPoints;
    @Getter
    private final boolean isAccelerating;
    @Getter
    private final Optional<AUnitWrapper> transport;
    @Getter
    private final List<AUnitWrapper> loadedUnits;
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
    private final boolean isUpgrading;
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
    private final boolean isTraining;
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
    private final boolean isResearching;
    @Getter
    private final boolean canAttackUnit;
    @Getter
    private final int remainingBuildTime;
    @Getter
    private final int remainingTrainTime;
    @Getter
    private final Optional<APosition> targetPosition;
    @Getter
    private final int remainingUpgradeTime;
    @Getter
    private final Optional<APosition> orderTargetPosition;
    @Getter
    private final boolean isBeingConstructed;
    @Getter
    private final int airWeaponCooldown;
    @Getter
    private final int defenseMatrixTimer;
    @Getter
    private final int remainingResearchTime;
    @Getter
    private final int spaceRemaining;
    @Getter
    private final int defenseMatrixPoints;
    @Getter
    private final int irradiateTimer;
    @Getter
    private final int lastCommandFrame;
    @Getter
    private final int acidSporeCount;
    @Getter
    private final int spiderMineCount;
    @Getter
    private final int maelstromTimer;
    @Getter
    private final int groundWeaponCooldown;
    @Getter
    private final boolean isGatheringMinerals;
    @Getter
    private final boolean isHoldingPosition;
    @Getter
    private final boolean isDefenseMatrixed;
    @Getter
    private final boolean isUnderDisruptionWeb;
    @Getter
    private final boolean isCarryingMinerals;
    @Getter
    private final APlayer player;
    @Getter
    private final Optional<Order> secondaryOrder;
    @Getter
    private final Optional<AUnitCommand> lastCommand;

    //TODO region/...

    AUnitWrapper(Unit u) {
        this.unit = u;

        //todo
//        //fields
//        //tech on units around, positions
//        this.useTech = u.useTech();
//        this.canBuild = u.canBuild();
//        //can build type, can build on surrounding positions
//        this.canBuild = u.canBuild();
//        this.canBuild = u.canBuild();
//        //on positions
//        this.canLand = u.canLand();
//        this.canLand = u.canLand();
//        //on position
//        this.canUnloadAtPosition = u.canUnloadAtPosition();
//        //on position, tech
//        this.canUseTechPosition = u.canUseTechPosition();
//        //surrounding positions
//        this.canUnloadAllPosition = u.canUnloadAllPosition();
//        //distance to all surrounding units, in weapon range, in sight range, in region
//        this.distance = u.getDistance();
//        this.unitsInRadius = u.getUnitsInRadius();
//        //surrounding units
//        this.canTargetUnit = u.canTargetUnit();
//        //units in transport
//        this.canUnload = u.canUnload();
//        this.canUnload = u.canUnload();
//        //can gather unit
//        this.canGather = u.canGather();
//        this.canGather = u.canGather();
//        //use tech on unit/position
//        this.canUseTechUnit = u.canUseTechUnit();
//        this.canUseTechUnit = u.canUseTechUnit();
//        this.canUseTechUnit = u.canUseTechUnit();
//        this.canUseTechUnit = u.canUseTechUnit();
//        //can rapair unit
//        this.canRepair = u.canRepair();
//        this.canRepair = u.canRepair();
//        //unit, position
//        this.canAttack = u.canAttack();
//        this.canAttack = u.canAttack();
//        this.canAttack = u.canAttack();
//
//        //tech, unit, position
//        this.canUseTech = u.canUseTech();
//        this.canUseTech = u.canUseTech();
//
//        //unit, position
//        this.canAttackMove = u.canAttackMove();
//        this.canAttackMove = u.canAttackMove();
//
//        //units in weapon range
//        this.unitsInWeaponRange = u.getUnitsInWeaponRange();
//        this.isInWeaponRange = u.isInWeaponRange();

        //todo region

        this.position = new APosition(u.getPosition());
        this.type = WrapperTypeFactory.createFrom(u.getType());
        this.target = AUnit.getUnitWrapped(u.getTarget());
        this.exists = u.exists();
        this.velocityX = u.getVelocityX();
        this.isLoaded = u.isLoaded();
        this.isIdle = u.isIdle();
        this.order = Optional.ofNullable(u.getOrder());
        this.hasNuke = u.hasNuke();
        this.isBlind = u.isBlind();
        this.isFlying = u.isFlying();
        this.isSieged = u.isSieged();
        this.isStuck = u.isStuck();
        this.isLifted = u.isLifted();
        this.isMoving = u.isMoving();
        this.trainingQueue = u.getTrainingQueue().stream()
                .map(WrapperTypeFactory::createFrom)
                .collect(Collectors.toList());
        this.removeTimer = u.getRemoveTimer();
        this.spellCooldown = u.getSpellCooldown();
        this.energy = u.getEnergy();
        this.scarabCount = u.getScarabCount();
        this.plagueTimer = u.getPlagueTimer();
        this.orderTimer = u.getOrderTimer();
        this.stasisTimer = u.getStasisTimer();
        this.ensnareTimer = u.getEnsnareTimer();
        this.orderTarget = AUnit.getUnitWrapped(u.getOrderTarget());
        this.shields = u.getShields();
        this.lockdownTimer = u.getLockdownTimer();
        this.stimTimer = u.getStimTimer();
        this.velocityY = u.getVelocityY();
        this.hitPoints = u.getHitPoints();
        this.lastCommand = AUnitCommand.creteOrEmpty(u.getLastCommand());
        this.isAccelerating = u.isAccelerating();
        this.transport = AUnit.getUnitWrapped(u.getTransport());
        this.loadedUnits = u.getLoadedUnits().stream()
                .map(AUnit::getUnitWrapped)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        this.isBeingGathered = u.isBeingGathered();
        this.isBraking = u.isBraking();
        this.isEnsnared = u.isEnsnared();
        this.isLockedDown = u.isLockedDown();
        this.isParasited = u.isParasited();
        this.isIrradiated = u.isIrradiated();
        this.isBurrowed = u.isBurrowed();
        this.isAttacking = u.isAttacking();
        this.isCloaked = u.isCloaked();
        this.isConstructing = u.isConstructing();
        this.isCarryingGas = u.isCarryingGas();
        this.isInvincible = u.isInvincible();
        this.isDetected = u.isDetected();
        this.isAttackFrame = u.isAttackFrame();
        this.isGatheringGas = u.isGatheringGas();
        this.isMaelstrommed = u.isMaelstrommed();
        this.isBeingHealed = u.isBeingHealed();
        this.isMorphing = u.isMorphing();
        this.isUnderDarkSwarm = u.isUnderDarkSwarm();
        this.isUpgrading = u.isUpgrading();
        this.isTargetable = u.isTargetable();
        this.isStasised = u.isStasised();
        this.isRepairing = u.isRepairing();
        this.isPlagued = u.isPlagued();
        this.isStimmed = u.isStimmed();
        this.isTraining = u.isTraining();
        this.isUnderStorm = u.isUnderStorm();
        this.isVisible = u.isVisible();
        this.isPowered = u.isPowered();
        this.isStartingAttack = u.isStartingAttack();
        this.isUnderAttack = u.isUnderAttack();
        this.isResearching = u.isResearching();
        this.canAttackUnit = u.canAttackUnit();
        this.remainingBuildTime = u.getRemainingBuildTime();
        this.remainingTrainTime = u.getRemainingTrainTime();
        this.targetPosition = APosition.creteOrEmpty(u.getTargetPosition());
        this.secondaryOrder = Optional.ofNullable(u.getSecondaryOrder());
        this.remainingUpgradeTime = u.getRemainingUpgradeTime();
        this.orderTargetPosition = APosition.creteOrEmpty(u.getOrderTargetPosition());
        this.isBeingConstructed = u.isBeingConstructed();
        this.airWeaponCooldown = u.getAirWeaponCooldown();
        this.defenseMatrixTimer = u.getDefenseMatrixTimer();
        this.remainingResearchTime = u.getRemainingResearchTime();
        this.spaceRemaining = u.getSpaceRemaining();
        this.defenseMatrixPoints = u.getDefenseMatrixPoints();
        this.irradiateTimer = u.getIrradiateTimer();
        this.lastCommandFrame = u.getLastCommandFrame();
        this.acidSporeCount = u.getAcidSporeCount();
        this.spiderMineCount = u.getSpiderMineCount();
        this.maelstromTimer = u.getMaelstromTimer();
        this.groundWeaponCooldown = u.getGroundWeaponCooldown();
        this.isGatheringMinerals = u.isGatheringMinerals();
        this.isHoldingPosition = u.isHoldingPosition();
        this.isDefenseMatrixed = u.isDefenseMatrixed();
        this.isUnderDisruptionWeb = u.isUnderDisruptionWeb();
        this.isCarryingMinerals = u.isCarryingMinerals();
        this.player = APlayer.wrapPlayer(u.getPlayer()).get();
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
     *
     * @param includeCooldown if true, then unit will be considered able to attack only if the cooldown after
     *                        the last shot allows it
     */
    public boolean canAttackThisKindOfUnit(AUnitWrapper otherUnit, boolean includeCooldown) {

        // Enemy is GROUND unit
        if (!otherUnit.getType().isFlyer()) {
            return type.canAttackGroundUnits() && (!includeCooldown || getGroundWeaponCooldown() == 0);
        } // Enemy is AIR unit
        else {
            return type.canAttackAirUnits() && (!includeCooldown || getAirWeaponCooldown() == 0);
        }
    }

    /**
     * Returns <b>true</b> if this unit can attack <b>targetUnit</b> in terms of both min and max range
     * conditions fulfilled.
     *
     * @param safetyMargin allowed error (in tiles) applied to the max distance condition
     */
    public boolean hasRangeToAttack(AUnitWrapper targetUnit, double safetyMargin) {
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
    public AWeaponTypeWrapper getWeaponAgainst(AUnitWrapper target) {
        if (!target.getType().isFlyer()) {
            return getType().getGroundWeapon();
        } else {
            return getType().getAirWeapon();
        }
    }

    /**
     * Returns true if unit is starting an attack or already in the attack frame animation.
     */
    public boolean isJustShooting() {
        return isAttackFrame() || isStartingAttack();
    }

    public boolean isReallyIdle() {
        return isIdle() || !getLastCommand().isPresent() || getLastCommand().get().getUnitCommandType().equals(UnitCommandType.None);
    }

}