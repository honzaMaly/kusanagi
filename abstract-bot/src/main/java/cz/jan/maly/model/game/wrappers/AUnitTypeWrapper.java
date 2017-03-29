package cz.jan.maly.model.game.wrappers;

import bwapi.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wrapper for BWMirror UnitType
 * It also make it possible to use it by different threads
 */
public class AUnitTypeWrapper extends AbstractWrapper<UnitType> {

    @Getter
    private final UnitSizeType size;

    @Getter
    private final boolean isMechanical;

    @Getter
    private final boolean isRobotic;

    @Getter
    private final boolean canMove;

    @Getter
    private final boolean isSpell;

    @Getter
    private final int armor;

    @Getter
    private final ATilePosition tileSize;

    @Getter
    private final boolean isAddon;

    @Getter
    private final boolean isHero;

    @Getter
    private final int width;

    @Getter
    private final List<AUpgradeTypeWrapper> upgrades;

    @Getter
    private final int gasPrice;

    @Getter
    private final int height;

    @Getter
    private final boolean isWorker;

    @Getter
    private final double topSpeed;

    @Getter
    private final boolean isFlyer;

    @Getter
    private final Race race;

    @Getter
    private final boolean isBeacon;

    @Getter
    private final Pair<AUnitTypeWrapper, Integer> whatBuilds = new Pair<>();

    @Getter
    private final Map<AUnitTypeWrapper, Integer> requiredUnits = new HashMap<>();

    @Getter
    private final ATechTypeWrapper cloakingTech;

    @Getter
    private final List<ATechTypeWrapper> abilities;

    @Getter
    private final ATechTypeWrapper requiredTech;

    @Getter
    private final AUpgradeTypeWrapper armorUpgrade;

    @Getter
    private final int seekRange;

    @Getter
    private final int supplyRequired;

    @Getter
    private final int spaceRequired;

    @Getter
    private final int dimensionDown;

    @Getter
    private final int destroyScore;

    @Getter
    private final int tileHeight;

    @Getter
    private final AWeaponTypeWrapper groundWeapon;

    @Getter
    private final int maxShields;

    @Getter
    private final int dimensionUp;

    @Getter
    private final int sightRange;

    @Getter
    private final AWeaponTypeWrapper airWeapon;

    @Getter
    private final int maxAirHits;

    @Getter
    private final int acceleration;

    @Getter
    private final int turnRadius;

    @Getter
    private final boolean canProduce;

    @Getter
    private final int haltDistance;

    @Getter
    private final boolean regeneratesHP;

    @Getter
    private final int dimensionRight;

    @Getter
    private final int tileWidth;

    @Getter
    private final int spaceProvided;

    @Getter
    private final int maxGroundHits;

    @Getter
    private final boolean isSpellcaster;

    @Getter
    private final int dimensionLeft;

    @Getter
    private final boolean isInvincible;

    @Getter
    private final int buildTime;

    @Getter
    private final boolean canAttack;

    @Getter
    private final boolean isOrganic;

    @Getter
    private final int maxEnergy;

    @Getter
    private final int supplyProvided;

    @Getter
    private final int maxHitPoints;

    @Getter
    private final int buildScore;

    @Getter
    private final boolean hasPermanentCloak;

    @Getter
    private final boolean isSpecialBuilding;

    @Getter
    private final boolean isResourceContainer;

    @Getter
    private final boolean isTwoUnitsInOneEgg;

    @Getter
    private final boolean requiresPsi;

    @Getter
    private final boolean isMineralField;

    @Getter
    private final boolean canBuildAddon;

    @Getter
    private final boolean requiresCreep;

    @Getter
    private final boolean isRefinery;

    @Getter
    private final boolean isBurrowable;

    @Getter
    private final boolean producesCreep;

    @Getter
    private final boolean isFlyingBuilding;

    @Getter
    private final boolean isPowerup;

    @Getter
    private final boolean isFlagBeacon;

    @Getter
    private final boolean isResourceDepot;

    @Getter
    private final boolean isCloakable;

    @Getter
    private final boolean isBuilding;

    @Getter
    private final boolean isNeutral;

    @Getter
    private final boolean isDetector;

    @Getter
    private final boolean producesLarva;

    @Getter
    private final List<ATechTypeWrapper> researchesWhat;

    @Getter
    private final boolean isCritter;

    @Getter
    private final List<AUpgradeTypeWrapper> upgradesWhat;

    @Getter
    private final boolean isMelee;

    @Getter
    private final int mineralPrice;

    @Getter
    private final boolean isMilitaryBuilding;

    @Getter
    private final boolean isMilitaryBuildingAntiAir;

    @Getter
    private final boolean isMilitaryBuildingAntiGround;

    @Getter
    private final boolean isSupplyUnit;

    @Getter
    private final boolean isGasBuilding;

    @Getter
    private final boolean isBase;

    @Getter
    private final boolean repairableMechanically;

    @Getter
    private final boolean healable;

    AUnitTypeWrapper(UnitType type) {
        super(type, type.toString());
        WrapperTypeFactory.add(this);

        //original fields
        this.size = type.size();
        this.isMechanical = type.isMechanical();
        this.isRobotic = type.isRobotic();
        this.canMove = type.canMove();
        this.isSpell = type.isSpell();
        this.armor = type.armor();
        this.tileSize = new ATilePosition(type.tileSize());
        this.isAddon = type.isAddon();
        this.isHero = type.isHero();
        this.width = type.width();
        this.upgrades = type.upgrades().stream()
                .map(WrapperTypeFactory::createFrom)
                .collect(Collectors.toList());
        this.gasPrice = type.gasPrice();
        this.height = type.height();
        this.isWorker = type.isWorker();
        this.topSpeed = type.topSpeed();
        this.isFlyer = type.isFlyer();
        this.race = type.getRace();
        this.isBeacon = type.isBeacon();
        this.whatBuilds.first = WrapperTypeFactory.createFrom(type.whatBuilds().first);
        this.whatBuilds.second = type.whatBuilds().second;
        type.requiredUnits().forEach((key, value) -> requiredUnits.put(WrapperTypeFactory.createFrom(key), value));
        this.cloakingTech = WrapperTypeFactory.createFrom(type.cloakingTech());
        this.abilities = type.abilities().stream()
                .map(WrapperTypeFactory::createFrom)
                .collect(Collectors.toList());
        this.requiredTech = WrapperTypeFactory.createFrom(type.requiredTech());
        this.armorUpgrade = WrapperTypeFactory.createFrom(type.armorUpgrade());
        this.seekRange = type.seekRange();
        this.supplyRequired = type.supplyRequired();
        this.spaceRequired = type.spaceRequired();
        this.dimensionDown = type.dimensionDown();
        this.destroyScore = type.destroyScore();
        this.tileHeight = type.tileHeight();
        this.groundWeapon = WrapperTypeFactory.createFrom(type.groundWeapon());
        this.maxShields = type.maxShields();
        this.dimensionUp = type.dimensionUp();
        this.sightRange = type.sightRange();
        this.airWeapon = WrapperTypeFactory.createFrom(type.airWeapon());
        this.maxAirHits = type.maxAirHits();
        this.acceleration = type.acceleration();
        this.turnRadius = type.turnRadius();
        this.canProduce = type.canProduce();
        this.haltDistance = type.haltDistance();
        this.regeneratesHP = type.regeneratesHP();
        this.dimensionRight = type.dimensionRight();
        this.tileWidth = type.tileWidth();
        this.spaceProvided = type.spaceProvided();
        this.maxGroundHits = type.maxGroundHits();
        this.isSpellcaster = type.isSpellcaster();
        this.dimensionLeft = type.dimensionLeft();
        this.isInvincible = type.isInvincible();
        this.buildTime = type.buildTime();
        this.canAttack = type.canAttack();
        this.isOrganic = type.isOrganic();
        this.maxEnergy = type.maxEnergy();
        this.supplyProvided = type.supplyProvided();
        this.maxHitPoints = type.maxHitPoints();
        this.buildScore = type.buildScore();
        this.hasPermanentCloak = type.hasPermanentCloak();
        this.isSpecialBuilding = type.isSpecialBuilding();
        this.isResourceContainer = type.isResourceContainer();
        this.isTwoUnitsInOneEgg = type.isTwoUnitsInOneEgg();
        this.requiresPsi = type.requiresPsi();
        this.isMineralField = type.isMineralField();
        this.canBuildAddon = type.canBuildAddon();
        this.requiresCreep = type.requiresCreep();
        this.isRefinery = type.isRefinery();
        this.isBurrowable = type.isBurrowable();
        this.producesCreep = type.producesCreep();
        this.isFlyingBuilding = type.isFlyingBuilding();
        this.isPowerup = type.isPowerup();
        this.isFlagBeacon = type.isFlagBeacon();
        this.isResourceDepot = type.isResourceDepot();
        this.isCloakable = type.isCloakable();
        this.isBuilding = type.isBuilding();
        this.isNeutral = type.isNeutral();
        this.isDetector = type.isDetector();
        this.producesLarva = type.producesLarva();
        this.researchesWhat = type.researchesWhat().stream()
                .map(WrapperTypeFactory::createFrom)
                .collect(Collectors.toList());
        this.isCritter = type.isCritter();
        this.upgradesWhat = type.upgradesWhat().stream()
                .map(WrapperTypeFactory::createFrom)
                .collect(Collectors.toList());
        if (isType(type, new UnitType[]{UnitType.Zerg_Zergling})) {
            this.mineralPrice = type.mineralPrice() / 2;
        } else {
            this.mineralPrice = type.mineralPrice();
        }

        //additional fields
        this.isBase = type.isBuilding() && isType(type, new UnitType[]{UnitType.Terran_Command_Center, UnitType.Protoss_Nexus, UnitType.Zerg_Hatchery,
                UnitType.Zerg_Lair, UnitType.Zerg_Hive});
        this.isGasBuilding = type.isBuilding() && isType(type, new UnitType[]{UnitType.Terran_Refinery, UnitType.Protoss_Assimilator, UnitType.Zerg_Extractor});
        this.isSupplyUnit = isType(type, new UnitType[]{UnitType.Protoss_Pylon, UnitType.Terran_Supply_Depot, UnitType.Zerg_Overlord});
        this.isMilitaryBuilding = type.isBuilding() && isType(type, new UnitType[]{UnitType.Terran_Bunker, UnitType.Terran_Missile_Turret, UnitType.Protoss_Photon_Cannon,
                UnitType.Zerg_Sunken_Colony, UnitType.Zerg_Spore_Colony}
        );
        this.isMilitaryBuildingAntiAir = isMilitaryBuilding && isType(type, new UnitType[]{UnitType.Terran_Bunker,
                UnitType.Protoss_Photon_Cannon, UnitType.Zerg_Spore_Colony}
        );
        this.isMilitaryBuildingAntiGround = isMilitaryBuilding && isType(type, new UnitType[]{UnitType.Terran_Bunker, UnitType.Protoss_Photon_Cannon,
                UnitType.Zerg_Sunken_Colony}
        );

        this.isMelee = isType(type, new UnitType[]{UnitType.Terran_SCV, UnitType.Terran_SCV, UnitType.Terran_Firebat, UnitType.Protoss_Probe,
                UnitType.Protoss_Zealot, UnitType.Protoss_Dark_Templar, UnitType.Zerg_Drone, UnitType.Zerg_Zergling,
                UnitType.Zerg_Broodling}
        );

        // Repair & Heal
        this.repairableMechanically = isBuilding() || isMechanical();
        this.healable = isOrganic() || isWorker();
    }

    /**
     * Returns max shoot range (in build tiles) of this unit against land targets.
     */
    public double getShootRangeGround() {
        return getGroundWeapon().getMaxRange() / 32;
    }

    /**
     * Returns max shoot range (in build tiles) of this unit against air targets.
     */
    public double getShootRangeAir() {
        return getAirWeapon().getMaxRange() / 32;
    }

    /**
     * Returns max shoot range (in build tiles) of this unit against given <b>opponentUnit</b>.
     */
    public double getShootRangeAgainst(AUnitTypeWrapper type) {
        if (type.isFlyer()) {
            return getShootRangeAir();
        } else {
            return getShootRangeGround();
        }
    }

    public boolean isBunker() {
        return this.isForType(UnitType.Terran_Bunker);
    }

    public boolean isSpiderMine() {
        return this.isForType(UnitType.Terran_Vulture_Spider_Mine);
    }

    public boolean isLarvaOrEgg() {
        return isEgg() || isLarva();
    }

    public boolean isLarva() {
        return this.isForType(UnitType.Zerg_Larva);
    }

    public boolean isEgg() {
        return this.isForType(UnitType.Zerg_Egg);
    }

    /**
     * Not that we're racists, but spider mines and larvas aren't really units...
     */
    public boolean isNotActuallyUnit() {
        return isSpiderMine() || isLarvaOrEgg();
    }

    /**
     * Returns true if unit has anti-ground weapon.
     */
    public boolean canAttackGroundUnits() {
        return !getGroundWeapon().isForType(WeaponType.None);
    }

    /**
     * Returns true if unit has anti-air weapon.
     */
    public boolean canAttackAirUnits() {
        return !getAirWeapon().isForType(WeaponType.None);
    }

    /**
     * Returns true if unit has anti-air weapon.
     */
    public boolean isMedic() {
        return this.isForType(UnitType.Terran_Medic);
    }

    public boolean canBeHealed() {
        return repairableMechanically || healable;
    }

}
