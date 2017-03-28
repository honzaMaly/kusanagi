package cz.jan.maly.model.game.wrappers;

import bwapi.Unit;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper for BWMirror Unit fields.
 */
public class AUnitWrapper {

    @Getter
    private final int resources;

    @Getter
    private final AUnitTypeWrapper type;

    @Getter
    private final AUnitWrapper target;

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
    private final ATechTypeWrapper tech;

    @Getter
    private final AOrderTypeWrapper order;

    @Getter
    private final boolean hasNuke;

    @Getter
    private final boolean isBlind;

    @Getter
    private final AUnitWrapper addon;

    @Getter
    private final List<AUnitWrapper> larva;

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

    final Unit unit;

    private final AUnitWrapper nydusExit;

    AUnitWrapper(Unit u) {
        this.unit = u;

        //fields
        //tech on units around, positions
        this.useTech = u.useTech();
        this.canBuild = u.canBuild();
        //can build type, can build on surrounding positions
        this.canBuild = u.canBuild();
        this.canBuild = u.canBuild();
        //on positions
        this.canLand = u.canLand();
        this.canLand = u.canLand();
        //on position
        this.canUnloadAtPosition = u.canUnloadAtPosition();
        //on position, tech
        this.canUseTechPosition = u.canUseTechPosition();
        //surrounding positions
        this.canUnloadAllPosition = u.canUnloadAllPosition();
        //distance to all surrounding units, in weapon range, in sight range, in region
        this.distance = u.getDistance();
        this.unitsInRadius = u.getUnitsInRadius();
        //surrounding units
        this.canTargetUnit = u.canTargetUnit();
        //units in transport
        this.canUnload = u.canUnload();
        this.canUnload = u.canUnload();
        //can gather unit
        this.canGather = u.canGather();
        this.canGather = u.canGather();
        //use tech on unit/position
        this.canUseTechUnit = u.canUseTechUnit();
        this.canUseTechUnit = u.canUseTechUnit();
        this.canUseTechUnit = u.canUseTechUnit();
        this.canUseTechUnit = u.canUseTechUnit();
        //can rapair unit
        this.canRepair = u.canRepair();
        this.canRepair = u.canRepair();
        //unit, position
        this.canAttack = u.canAttack();
        this.canAttack = u.canAttack();
        this.canAttack = u.canAttack();

        //tech, unit, position
        this.canUseTech = u.canUseTech();
        this.canUseTech = u.canUseTech();

        //unit, position
        this.canAttackMove = u.canAttackMove();
        this.canAttackMove = u.canAttackMove();

        //units in weapon range
        this.unitsInWeaponRange = u.getUnitsInWeaponRange();

        //todo get unit in region

        this.position = new APosition(u.getPosition());
        this.resources = u.getResources();
        this.type = WrapperTypeFactory.createFrom(u.getType());
        this.target = AUnit.wrapUnit(u.getTarget()).getAUnitWrapper();
        this.exists = u.exists();
        this.velocityX = u.getVelocityX();
        this.isLoaded = u.isLoaded();
        this.isIdle = u.isIdle();
        this.tech = WrapperTypeFactory.createFrom(u.getTech());
        this.order = WrapperTypeFactory.createFrom(u.getOrder());
        this.hasNuke = u.hasNuke();
        this.isBlind = u.isBlind();
        this.addon = AUnit.wrapUnit(u.getAddon()).getAUnitWrapper();
        this.larva = u.getLarva().stream()
                .map(AUnit::wrapUnit)
                .map(AUnit::getAUnitWrapper)
                .collect(Collectors.toList());
        this.isFlying = u.isFlying();
        this.isSieged = u.isSieged();
        this.isStuck = u.isStuck();
        this.isLifted = u.isLifted();
        this.isMoving = u.isMoving();
        this.nydusExit = AUnit.wrapUnit(u.getNydusExit()).getAUnitWrapper();
        this.trainingQueue = u.getTrainingQueue();
        this.removeTimer = u.getRemoveTimer();
        this.spellCooldown = u.getSpellCooldown();
        this.energy = u.getEnergy();
        this.scarabCount = u.getScarabCount();
        this.plagueTimer = u.getPlagueTimer();
        this.upgrade = u.getUpgrade();
        this.orderTimer = u.getOrderTimer();
        this.stasisTimer = u.getStasisTimer();
        this.ensnareTimer = u.getEnsnareTimer();
        this.orderTarget = u.getOrderTarget();
        this.shields = u.getShields();
        this.lockdownTimer = u.getLockdownTimer();
        this.stimTimer = u.getStimTimer();
        this.velocityY = u.getVelocityY();
        this.hitPoints = u.getHitPoints();
        this.lastCommand = u.getLastCommand();
        this.isAccelerating = u.isAccelerating();
        this.transport = u.getTransport();
        this.loadedUnits = u.getLoadedUnits();
        this.powerUp = u.getPowerUp();
        this.hatchery = u.getHatchery();
        this.isBeingGathered = u.isBeingGathered();
        this.isBraking = u.isBraking();
        this.isHallucination = u.isHallucination();
        this.isEnsnared = u.isEnsnared();
        this.isLockedDown = u.isLockedDown();
        this.isParasited = u.isParasited();
        this.isPatrolling = u.isPatrolling();
        this.isIrradiated = u.isIrradiated();
        this.interceptors = u.getInterceptors();
        this.isBurrowed = u.isBurrowed();
        this.isAttacking = u.isAttacking();
        this.isCloaked = u.isCloaked();
        this.isConstructing = u.isConstructing();
        this.isCarryingGas = u.isCarryingGas();
        this.isInvincible = u.isInvincible();
        this.isDetected = u.isDetected();
        this.isAttackFrame = u.isAttackFrame();
        this.isInWeaponRange = u.isInWeaponRange();
        this.isGatheringGas = u.isGatheringGas();
        this.isCompleted = u.isCompleted();
        this.isInterruptible = u.isInterruptible();
        this.isMaelstrommed = u.isMaelstrommed();
        this.isBeingHealed = u.isBeingHealed();
        this.isMorphing = u.isMorphing();
        this.carrier = u.getCarrier();
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
        this.interceptorCount = u.getInterceptorCount();
        this.targetPosition = u.getTargetPosition();
        this.secondaryOrder = u.getSecondaryOrder();
        this.remainingUpgradeTime = u.getRemainingUpgradeTime();
        this.orderTarPosition = u.getOrderTargetPosition();
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

//        this.player = u.getPlayer();


        // Repair & Heal
//        this._repairableMechanically = isBuilding() || isVehicle();
//        this._healable = isInfantry() || isWorker();

        // Military building
//        this._isMilitaryBuildingAntiGround = isType(
//                AUnitTypeWrapper.Terran_Bunker, AUnitTypeWrapper.Protoss_Photon_Cannon, AUnitTypeWrapper.Zerg_Sunken_Colony
//        );
//        this._isMilitaryBuildingAntiAir = isType(
//                AUnitTypeWrapper.Terran_Bunker, AUnitTypeWrapper.Terran_Missile_Turret,
//                AUnitTypeWrapper.Protoss_Photon_Cannon, AUnitTypeWrapper.Zerg_Spore_Colony
//        );
    }

    public APosition getPosition() {
        return null;
    }

    private boolean _repairableMechanically = false;
    private boolean _healable = false;
    private boolean _isMilitaryBuildingAntiGround = false;
    private boolean _isMilitaryBuildingAntiAir = false;

    public boolean canBeHealed() {
        return _repairableMechanically || _healable;
    }

    public boolean isRepairableMechanically() {
        return _repairableMechanically;
    }

    public boolean isHealable() {
        return _healable;
    }

//    public boolean isFullyHealthy() {
//        return getHitPoints() >= getMaxHitPoints();
//    }
//
//    public int getHPPercent() {
//        return 100 * getHitPoints() / getMaxHitPoints();
//    }
//
//    public boolean isWounded() {
//        return getHitPoints() < getMaxHP();
//    }
//
//    public int getHP() {
//        return getHitPoints();
//    }
//
//    public int getMaxHP() {
//        return getMaxHitPoints();
//    }

//    public int getHitPoints() {
//        return u.getHitPoints();
//    }
//
//    public int getMaxHitPoints() {
//        return u.getType().maxHitPoints();
//    }

    // =========================================================
    // RANGE and ATTACK methods

//    /**
//     * Returns true if this unit is capable of attacking <b>otherUnit</b>. For example Zerglings can't attack
//     * flying targets and Corsairs can't attack ground targets.
//     *
//     * @param includeCooldown if true, then unit will be considered able to attack only if the cooldown after
//     *                        the last shot allows it
//     */
//    public boolean canAttackThisKindOfUnit(AUnitWrapper otherUnit, boolean includeCooldown) {
//
//        // Enemy is GROUND unit
//        if (otherUnit.isGroundUnit()) {
//            return canAttackGroundUnits() && (!includeCooldown || getGroundWeaponCooldown() == 0);
//        } // Enemy is AIR unit
//        else {
//            return canAttackAirUnits() && (!includeCooldown || getAirWeaponCooldown() == 0);
//        }
//    }

//    /**
//     * Returns <b>true</b> if this unit can attack <b>targetUnit</b> in terms of both min and max range
//     * conditions fulfilled.
//     *
//     * @param safetyMargin allowed error (in tiles) applied to the max distance condition
//     */
//    public boolean hasRangeToAttack(AUnitWrapper targetUnit, double safetyMargin) {
//        WeaponType weaponAgainstThisUnit = getWeaponAgainst(targetUnit);
//        double dist = this.distanceTo(targetUnit);
//        return weaponAgainstThisUnit != WeaponType.None
//                && weaponAgainstThisUnit.maxRange() <= (dist + safetyMargin)
//                && weaponAgainstThisUnit.minRange() >= dist;
//    }

//    /**
//     * Returns weapon that would be used to attack given target.
//     * If no such weapon, then WeaponTypes.None will be returned.
//     */
//    public WeaponType getWeaponAgainst(AUnitWrapper target) {
//        if (target.isGroundUnit()) {
//            return getGroundWeapon();
//        } else {
//            return getAirWeapon();
//        }
//    }

//    /**
//     * Returns true if unit is starting an attack or already in the attack frame animation.
//     */
//    public boolean isJustShooting() {
//        return isAttackFrame() || isStartingAttack();
//    }

//    public boolean isIdle() {
//        return u.isIdle() || u.getLastCommand().getUnitCommandType().equals(UnitCommandType.None);
//    }

//    public boolean isAttackFrame() {
//        return u.isAttackFrame();
//    }
//
//    public boolean isStartingAttack() {
//        return u.isStartingAttack();
//    }

//    public List<AUnitTypeWrapper> getTrainingQueue() {
//        return (List<AUnitTypeWrapper>) AUnitTypeWrapper.convertToAUnitTypesCollection(u.getTrainingQueue());
//    }

//    public boolean isUpgrading() {
//        return u.isUpgrading();
//    }
//
//    public AUnitWrapper getTarget() {
//        return u.getTarget() != null ? AUnitWrapper.getFor(u.getTarget()) : null;
//    }

//    public APosition getTargetPosition() {
//        return new APosition(u.getTargetPosition());
//    }
//
//    public AUnitWrapper getOrderTarget() {
//        return u.getOrderTarget() != null ? AUnitWrapper.getFor(u.getOrderTarget()) : null;
//    }
//
//    public AUnitWrapper getBuildUnit() {
//        return u.getBuildUnit() != null ? AUnitWrapper.getFor(u.getBuildUnit()) : null;
//    }
//
//    public AUnitTypeWrapper getBuildType() {
//        return u.getBuildType() != null ? WrapperTypeFactory.createFrom(u.getBuildType()) : null;
//    }

}