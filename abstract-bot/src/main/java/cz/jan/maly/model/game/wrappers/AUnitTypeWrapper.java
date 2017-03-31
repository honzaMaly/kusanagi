package cz.jan.maly.model.game.wrappers;

import bwapi.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wrapper for BWMirror UnitType
 * It also make it possible to use it by different threads
 */
public class AUnitTypeWrapper extends AbstractWrapper<UnitType> {

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

    private final List<UpgradeType> upgrades;

    public List<AUpgradeTypeWrapper> getUpgrades() {
        return upgrades.stream()
                .map(WrapperTypeFactory::createFrom)
                .collect(Collectors.toList());
    }

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

    private final Map<UnitType, Integer> requiredUnits;

    public Map<AUnitTypeWrapper, Integer> getRequiredUnits() {
        return requiredUnits.entrySet().stream()
                .collect(Collectors.toMap(entry -> WrapperTypeFactory.createFrom(entry.getKey()), Map.Entry::getValue));
    }

    private final TechType cloakingTech;

    public ATechTypeWrapper getCloakingTech() {
        return WrapperTypeFactory.createFrom(cloakingTech);
    }

    private final List<TechType> abilities;

    public List<ATechTypeWrapper> getAbilities() {
        return abilities.stream()
                .map(WrapperTypeFactory::createFrom)
                .collect(Collectors.toList());
    }

    private final TechType requiredTech;

    public ATechTypeWrapper getRequiredTech() {
        return WrapperTypeFactory.createFrom(requiredTech);
    }

    private final UpgradeType armorUpgrade;

    public AUpgradeTypeWrapper getArmorUpgrade() {
        return WrapperTypeFactory.createFrom(armorUpgrade);
    }

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

    private final WeaponType groundWeapon;

    public AWeaponTypeWrapper getGroundWeapon() {
        return WrapperTypeFactory.createFrom(groundWeapon);
    }

    @Getter
    private final int maxShields;

    @Getter
    private final int dimensionUp;

    @Getter
    private final int sightRange;

    private final WeaponType airWeapon;

    public AWeaponTypeWrapper getAirWeapon() {
        return WrapperTypeFactory.createFrom(airWeapon);
    }

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

    private final List<TechType> researchesWhat;

    public List<ATechTypeWrapper> getResearchesWhat() {
        return researchesWhat.stream()
                .map(WrapperTypeFactory::createFrom)
                .collect(Collectors.toList());
    }

    @Getter
    private final boolean isCritter;

    private final List<UpgradeType> upgradesWhat;

    public List<AUpgradeTypeWrapper> getUpgradesWhat() {
        return upgradesWhat.stream()
                .map(WrapperTypeFactory::createFrom)
                .collect(Collectors.toList());
    }

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
        this.isMechanical = type.isMechanical();
        this.isRobotic = type.isRobotic();
        this.canMove = type.canMove();
        this.isSpell = type.isSpell();
        this.armor = type.armor();
        this.tileSize = new ATilePosition(type.tileSize());
        this.isAddon = type.isAddon();
        this.isHero = type.isHero();
        this.width = type.width();
        this.upgrades = new ArrayList<>(type.upgrades());
        this.gasPrice = type.gasPrice();
        this.height = type.height();
        this.isWorker = type.isWorker();
        this.topSpeed = type.topSpeed();
        this.isFlyer = type.isFlyer();
        this.race = type.getRace();
        this.isBeacon = type.isBeacon();
        this.requiredUnits = type.requiredUnits().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.cloakingTech = type.cloakingTech();
        this.abilities = new ArrayList<>(type.abilities());
        this.requiredTech = type.requiredTech();
        this.armorUpgrade = type.armorUpgrade();
        this.seekRange = type.seekRange();
        this.supplyRequired = type.supplyRequired();
        this.spaceRequired = type.spaceRequired();
        this.dimensionDown = type.dimensionDown();
        this.destroyScore = type.destroyScore();
        this.tileHeight = type.tileHeight();
        this.groundWeapon = type.groundWeapon();
        this.maxShields = type.maxShields();
        this.dimensionUp = type.dimensionUp();
        this.sightRange = type.sightRange();
        this.airWeapon = type.airWeapon();
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
        this.researchesWhat = new ArrayList<>(type.researchesWhat());
        this.isCritter = type.isCritter();
        this.upgradesWhat = new ArrayList<>(type.upgradesWhat());
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

    public static void initTypes() {
        AUnitTypeWrapper Terran_Firebat = new AUnitTypeWrapper(UnitType.Terran_Firebat);
        AUnitTypeWrapper Terran_Ghost = new AUnitTypeWrapper(UnitType.Terran_Ghost);
        AUnitTypeWrapper Terran_Goliath = new AUnitTypeWrapper(UnitType.Terran_Goliath);
        AUnitTypeWrapper Terran_Marine = new AUnitTypeWrapper(UnitType.Terran_Marine);
        AUnitTypeWrapper Terran_Medic = new AUnitTypeWrapper(UnitType.Terran_Medic);
        AUnitTypeWrapper Terran_SCV = new AUnitTypeWrapper(UnitType.Terran_SCV);
        AUnitTypeWrapper Terran_Siege_Tank_Siege_Mode = new AUnitTypeWrapper(UnitType.Terran_Siege_Tank_Siege_Mode);
        AUnitTypeWrapper Terran_Siege_Tank_Tank_Mode = new AUnitTypeWrapper(UnitType.Terran_Siege_Tank_Tank_Mode);
        AUnitTypeWrapper Terran_Vulture = new AUnitTypeWrapper(UnitType.Terran_Vulture);
        AUnitTypeWrapper Terran_Vulture_Spider_Mine = new AUnitTypeWrapper(UnitType.Terran_Vulture_Spider_Mine);
        AUnitTypeWrapper Terran_Battlecruiser = new AUnitTypeWrapper(UnitType.Terran_Battlecruiser);
        AUnitTypeWrapper Terran_Dropship = new AUnitTypeWrapper(UnitType.Terran_Dropship);
        AUnitTypeWrapper Terran_Nuclear_Missile = new AUnitTypeWrapper(UnitType.Terran_Nuclear_Missile);
        AUnitTypeWrapper Terran_Science_Vessel = new AUnitTypeWrapper(UnitType.Terran_Science_Vessel);
        AUnitTypeWrapper Terran_Valkyrie = new AUnitTypeWrapper(UnitType.Terran_Valkyrie);
        AUnitTypeWrapper Terran_Wraith = new AUnitTypeWrapper(UnitType.Terran_Wraith);
        AUnitTypeWrapper Hero_Alan_Schezar = new AUnitTypeWrapper(UnitType.Hero_Alan_Schezar);
        AUnitTypeWrapper Hero_Alexei_Stukov = new AUnitTypeWrapper(UnitType.Hero_Alexei_Stukov);
        AUnitTypeWrapper Hero_Arcturus_Mengsk = new AUnitTypeWrapper(UnitType.Hero_Arcturus_Mengsk);
        AUnitTypeWrapper Hero_Edmund_Duke_Tank_Mode = new AUnitTypeWrapper(UnitType.Hero_Edmund_Duke_Tank_Mode);
        AUnitTypeWrapper Hero_Edmund_Duke_Siege_Mode = new AUnitTypeWrapper(UnitType.Hero_Edmund_Duke_Siege_Mode);
        AUnitTypeWrapper Hero_Gerard_DuGalle = new AUnitTypeWrapper(UnitType.Hero_Gerard_DuGalle);
        AUnitTypeWrapper Hero_Gui_Montag = new AUnitTypeWrapper(UnitType.Hero_Gui_Montag);
        AUnitTypeWrapper Hero_Hyperion = new AUnitTypeWrapper(UnitType.Hero_Hyperion);
        AUnitTypeWrapper Hero_Jim_Raynor_Marine = new AUnitTypeWrapper(UnitType.Hero_Jim_Raynor_Marine);
        AUnitTypeWrapper Hero_Jim_Raynor_Vulture = new AUnitTypeWrapper(UnitType.Hero_Jim_Raynor_Vulture);
        AUnitTypeWrapper Hero_Magellan = new AUnitTypeWrapper(UnitType.Hero_Magellan);
        AUnitTypeWrapper Hero_Norad_II = new AUnitTypeWrapper(UnitType.Hero_Norad_II);
        AUnitTypeWrapper Hero_Samir_Duran = new AUnitTypeWrapper(UnitType.Hero_Samir_Duran);
        AUnitTypeWrapper Hero_Sarah_Kerrigan = new AUnitTypeWrapper(UnitType.Hero_Sarah_Kerrigan);
        AUnitTypeWrapper Hero_Tom_Kazansky = new AUnitTypeWrapper(UnitType.Hero_Tom_Kazansky);
        AUnitTypeWrapper Terran_Civilian = new AUnitTypeWrapper(UnitType.Terran_Civilian);
        AUnitTypeWrapper Terran_Academy = new AUnitTypeWrapper(UnitType.Terran_Academy);
        AUnitTypeWrapper Terran_Armory = new AUnitTypeWrapper(UnitType.Terran_Armory);
        AUnitTypeWrapper Terran_Barracks = new AUnitTypeWrapper(UnitType.Terran_Barracks);
        AUnitTypeWrapper Terran_Bunker = new AUnitTypeWrapper(UnitType.Terran_Bunker);
        AUnitTypeWrapper Terran_Command_Center = new AUnitTypeWrapper(UnitType.Terran_Command_Center);
        AUnitTypeWrapper Terran_Engineering_Bay = new AUnitTypeWrapper(UnitType.Terran_Engineering_Bay);
        AUnitTypeWrapper Terran_Factory = new AUnitTypeWrapper(UnitType.Terran_Factory);
        AUnitTypeWrapper Terran_Missile_Turret = new AUnitTypeWrapper(UnitType.Terran_Missile_Turret);
        AUnitTypeWrapper Terran_Refinery = new AUnitTypeWrapper(UnitType.Terran_Refinery);
        AUnitTypeWrapper Terran_Science_Facility = new AUnitTypeWrapper(UnitType.Terran_Science_Facility);
        AUnitTypeWrapper Terran_Starport = new AUnitTypeWrapper(UnitType.Terran_Starport);
        AUnitTypeWrapper Terran_Supply_Depot = new AUnitTypeWrapper(UnitType.Terran_Supply_Depot);
        AUnitTypeWrapper Terran_Comsat_Station = new AUnitTypeWrapper(UnitType.Terran_Comsat_Station);
        AUnitTypeWrapper Terran_Control_Tower = new AUnitTypeWrapper(UnitType.Terran_Control_Tower);
        AUnitTypeWrapper Terran_Covert_Ops = new AUnitTypeWrapper(UnitType.Terran_Covert_Ops);
        AUnitTypeWrapper Terran_Machine_Shop = new AUnitTypeWrapper(UnitType.Terran_Machine_Shop);
        AUnitTypeWrapper Terran_Nuclear_Silo = new AUnitTypeWrapper(UnitType.Terran_Nuclear_Silo);
        AUnitTypeWrapper Terran_Physics_Lab = new AUnitTypeWrapper(UnitType.Terran_Physics_Lab);
        AUnitTypeWrapper Special_Crashed_Norad_II = new AUnitTypeWrapper(UnitType.Special_Crashed_Norad_II);
        AUnitTypeWrapper Special_Ion_Cannon = new AUnitTypeWrapper(UnitType.Special_Ion_Cannon);
        AUnitTypeWrapper Special_Power_Generator = new AUnitTypeWrapper(UnitType.Special_Power_Generator);
        AUnitTypeWrapper Special_Psi_Disrupter = new AUnitTypeWrapper(UnitType.Special_Psi_Disrupter);
        AUnitTypeWrapper Protoss_Archon = new AUnitTypeWrapper(UnitType.Protoss_Archon);
        AUnitTypeWrapper Protoss_Dark_Archon = new AUnitTypeWrapper(UnitType.Protoss_Dark_Archon);
        AUnitTypeWrapper Protoss_Dark_Templar = new AUnitTypeWrapper(UnitType.Protoss_Dark_Templar);
        AUnitTypeWrapper Protoss_Dragoon = new AUnitTypeWrapper(UnitType.Protoss_Dragoon);
        AUnitTypeWrapper Protoss_High_Templar = new AUnitTypeWrapper(UnitType.Protoss_High_Templar);
        AUnitTypeWrapper Protoss_Probe = new AUnitTypeWrapper(UnitType.Protoss_Probe);
        AUnitTypeWrapper Protoss_Reaver = new AUnitTypeWrapper(UnitType.Protoss_Reaver);
        AUnitTypeWrapper Protoss_Scarab = new AUnitTypeWrapper(UnitType.Protoss_Scarab);
        AUnitTypeWrapper Protoss_Zealot = new AUnitTypeWrapper(UnitType.Protoss_Zealot);
        AUnitTypeWrapper Protoss_Arbiter = new AUnitTypeWrapper(UnitType.Protoss_Arbiter);
        AUnitTypeWrapper Protoss_Carrier = new AUnitTypeWrapper(UnitType.Protoss_Carrier);
        AUnitTypeWrapper Protoss_Corsair = new AUnitTypeWrapper(UnitType.Protoss_Corsair);
        AUnitTypeWrapper Protoss_Interceptor = new AUnitTypeWrapper(UnitType.Protoss_Interceptor);
        AUnitTypeWrapper Protoss_Observer = new AUnitTypeWrapper(UnitType.Protoss_Observer);
        AUnitTypeWrapper Protoss_Scout = new AUnitTypeWrapper(UnitType.Protoss_Scout);
        AUnitTypeWrapper Protoss_Shuttle = new AUnitTypeWrapper(UnitType.Protoss_Shuttle);
        AUnitTypeWrapper Hero_Aldaris = new AUnitTypeWrapper(UnitType.Hero_Aldaris);
        AUnitTypeWrapper Hero_Artanis = new AUnitTypeWrapper(UnitType.Hero_Artanis);
        AUnitTypeWrapper Hero_Danimoth = new AUnitTypeWrapper(UnitType.Hero_Danimoth);
        AUnitTypeWrapper Hero_Dark_Templar = new AUnitTypeWrapper(UnitType.Hero_Dark_Templar);
        AUnitTypeWrapper Hero_Fenix_Dragoon = new AUnitTypeWrapper(UnitType.Hero_Fenix_Dragoon);
        AUnitTypeWrapper Hero_Fenix_Zealot = new AUnitTypeWrapper(UnitType.Hero_Fenix_Zealot);
        AUnitTypeWrapper Hero_Gantrithor = new AUnitTypeWrapper(UnitType.Hero_Gantrithor);
        AUnitTypeWrapper Hero_Mojo = new AUnitTypeWrapper(UnitType.Hero_Mojo);
        AUnitTypeWrapper Hero_Raszagal = new AUnitTypeWrapper(UnitType.Hero_Raszagal);
        AUnitTypeWrapper Hero_Tassadar = new AUnitTypeWrapper(UnitType.Hero_Tassadar);
        AUnitTypeWrapper Hero_Tassadar_Zeratul_Archon = new AUnitTypeWrapper(UnitType.Hero_Tassadar_Zeratul_Archon);
        AUnitTypeWrapper Hero_Warbringer = new AUnitTypeWrapper(UnitType.Hero_Warbringer);
        AUnitTypeWrapper Hero_Zeratul = new AUnitTypeWrapper(UnitType.Hero_Zeratul);
        AUnitTypeWrapper Protoss_Arbiter_Tribunal = new AUnitTypeWrapper(UnitType.Protoss_Arbiter_Tribunal);
        AUnitTypeWrapper Protoss_Assimilator = new AUnitTypeWrapper(UnitType.Protoss_Assimilator);
        AUnitTypeWrapper Protoss_Citadel_of_Adun = new AUnitTypeWrapper(UnitType.Protoss_Citadel_of_Adun);
        AUnitTypeWrapper Protoss_Cybernetics_Core = new AUnitTypeWrapper(UnitType.Protoss_Cybernetics_Core);
        AUnitTypeWrapper Protoss_Fleet_Beacon = new AUnitTypeWrapper(UnitType.Protoss_Fleet_Beacon);
        AUnitTypeWrapper Protoss_Forge = new AUnitTypeWrapper(UnitType.Protoss_Forge);
        AUnitTypeWrapper Protoss_Gateway = new AUnitTypeWrapper(UnitType.Protoss_Gateway);
        AUnitTypeWrapper Protoss_Nexus = new AUnitTypeWrapper(UnitType.Protoss_Nexus);
        AUnitTypeWrapper Protoss_Observatory = new AUnitTypeWrapper(UnitType.Protoss_Observatory);
        AUnitTypeWrapper Protoss_Photon_Cannon = new AUnitTypeWrapper(UnitType.Protoss_Photon_Cannon);
        AUnitTypeWrapper Protoss_Pylon = new AUnitTypeWrapper(UnitType.Protoss_Pylon);
        AUnitTypeWrapper Protoss_Robotics_Facility = new AUnitTypeWrapper(UnitType.Protoss_Robotics_Facility);
        AUnitTypeWrapper Protoss_Robotics_Support_Bay = new AUnitTypeWrapper(UnitType.Protoss_Robotics_Support_Bay);
        AUnitTypeWrapper Protoss_Shield_Battery = new AUnitTypeWrapper(UnitType.Protoss_Shield_Battery);
        AUnitTypeWrapper Protoss_Stargate = new AUnitTypeWrapper(UnitType.Protoss_Stargate);
        AUnitTypeWrapper Protoss_Templar_Archives = new AUnitTypeWrapper(UnitType.Protoss_Templar_Archives);
        AUnitTypeWrapper Special_Khaydarin_Crystal_Form = new AUnitTypeWrapper(UnitType.Special_Khaydarin_Crystal_Form);
        AUnitTypeWrapper Special_Protoss_Temple = new AUnitTypeWrapper(UnitType.Special_Protoss_Temple);
        AUnitTypeWrapper Special_Stasis_Cell_Prison = new AUnitTypeWrapper(UnitType.Special_Stasis_Cell_Prison);
        AUnitTypeWrapper Special_Warp_Gate = new AUnitTypeWrapper(UnitType.Special_Warp_Gate);
        AUnitTypeWrapper Special_XelNaga_Temple = new AUnitTypeWrapper(UnitType.Special_XelNaga_Temple);
        AUnitTypeWrapper Zerg_Broodling = new AUnitTypeWrapper(UnitType.Zerg_Broodling);
        AUnitTypeWrapper Zerg_Defiler = new AUnitTypeWrapper(UnitType.Zerg_Defiler);
        AUnitTypeWrapper Zerg_Drone = new AUnitTypeWrapper(UnitType.Zerg_Drone);
        AUnitTypeWrapper Zerg_Egg = new AUnitTypeWrapper(UnitType.Zerg_Egg);
        AUnitTypeWrapper Zerg_Hydralisk = new AUnitTypeWrapper(UnitType.Zerg_Hydralisk);
        AUnitTypeWrapper Zerg_Infested_Terran = new AUnitTypeWrapper(UnitType.Zerg_Infested_Terran);
        AUnitTypeWrapper Zerg_Larva = new AUnitTypeWrapper(UnitType.Zerg_Larva);
        AUnitTypeWrapper Zerg_Lurker = new AUnitTypeWrapper(UnitType.Zerg_Lurker);
        AUnitTypeWrapper Zerg_Lurker_Egg = new AUnitTypeWrapper(UnitType.Zerg_Lurker_Egg);
        AUnitTypeWrapper Zerg_Ultralisk = new AUnitTypeWrapper(UnitType.Zerg_Ultralisk);
        AUnitTypeWrapper Zerg_Zergling = new AUnitTypeWrapper(UnitType.Zerg_Zergling);
        AUnitTypeWrapper Zerg_Cocoon = new AUnitTypeWrapper(UnitType.Zerg_Cocoon);
        AUnitTypeWrapper Zerg_Devourer = new AUnitTypeWrapper(UnitType.Zerg_Devourer);
        AUnitTypeWrapper Zerg_Guardian = new AUnitTypeWrapper(UnitType.Zerg_Guardian);
        AUnitTypeWrapper Zerg_Mutalisk = new AUnitTypeWrapper(UnitType.Zerg_Mutalisk);
        AUnitTypeWrapper Zerg_Overlord = new AUnitTypeWrapper(UnitType.Zerg_Overlord);
        AUnitTypeWrapper Zerg_Queen = new AUnitTypeWrapper(UnitType.Zerg_Queen);
        AUnitTypeWrapper Zerg_Scourge = new AUnitTypeWrapper(UnitType.Zerg_Scourge);
        AUnitTypeWrapper Hero_Devouring_One = new AUnitTypeWrapper(UnitType.Hero_Devouring_One);
        AUnitTypeWrapper Hero_Hunter_Killer = new AUnitTypeWrapper(UnitType.Hero_Hunter_Killer);
        AUnitTypeWrapper Hero_Infested_Duran = new AUnitTypeWrapper(UnitType.Hero_Infested_Duran);
        AUnitTypeWrapper Hero_Infested_Kerrigan = new AUnitTypeWrapper(UnitType.Hero_Infested_Kerrigan);
        AUnitTypeWrapper Hero_Kukulza_Guardian = new AUnitTypeWrapper(UnitType.Hero_Kukulza_Guardian);
        AUnitTypeWrapper Hero_Kukulza_Mutalisk = new AUnitTypeWrapper(UnitType.Hero_Kukulza_Mutalisk);
        AUnitTypeWrapper Hero_Matriarch = new AUnitTypeWrapper(UnitType.Hero_Matriarch);
        AUnitTypeWrapper Hero_Torrasque = new AUnitTypeWrapper(UnitType.Hero_Torrasque);
        AUnitTypeWrapper Hero_Unclean_One = new AUnitTypeWrapper(UnitType.Hero_Unclean_One);
        AUnitTypeWrapper Hero_Yggdrasill = new AUnitTypeWrapper(UnitType.Hero_Yggdrasill);
        AUnitTypeWrapper Zerg_Creep_Colony = new AUnitTypeWrapper(UnitType.Zerg_Creep_Colony);
        AUnitTypeWrapper Zerg_Defiler_Mound = new AUnitTypeWrapper(UnitType.Zerg_Defiler_Mound);
        AUnitTypeWrapper Zerg_Evolution_Chamber = new AUnitTypeWrapper(UnitType.Zerg_Evolution_Chamber);
        AUnitTypeWrapper Zerg_Extractor = new AUnitTypeWrapper(UnitType.Zerg_Extractor);
        AUnitTypeWrapper Zerg_Greater_Spire = new AUnitTypeWrapper(UnitType.Zerg_Greater_Spire);
        AUnitTypeWrapper Zerg_Hatchery = new AUnitTypeWrapper(UnitType.Zerg_Hatchery);
        AUnitTypeWrapper Zerg_Hive = new AUnitTypeWrapper(UnitType.Zerg_Hive);
        AUnitTypeWrapper Zerg_Hydralisk_Den = new AUnitTypeWrapper(UnitType.Zerg_Hydralisk_Den);
        AUnitTypeWrapper Zerg_Infested_Command_Center = new AUnitTypeWrapper(UnitType.Zerg_Infested_Command_Center);
        AUnitTypeWrapper Zerg_Lair = new AUnitTypeWrapper(UnitType.Zerg_Lair);
        AUnitTypeWrapper Zerg_Nydus_Canal = new AUnitTypeWrapper(UnitType.Zerg_Nydus_Canal);
        AUnitTypeWrapper Zerg_Queens_Nest = new AUnitTypeWrapper(UnitType.Zerg_Queens_Nest);
        AUnitTypeWrapper Zerg_Spawning_Pool = new AUnitTypeWrapper(UnitType.Zerg_Spawning_Pool);
        AUnitTypeWrapper Zerg_Spire = new AUnitTypeWrapper(UnitType.Zerg_Spire);
        AUnitTypeWrapper Zerg_Spore_Colony = new AUnitTypeWrapper(UnitType.Zerg_Spore_Colony);
        AUnitTypeWrapper Zerg_Sunken_Colony = new AUnitTypeWrapper(UnitType.Zerg_Sunken_Colony);
        AUnitTypeWrapper Zerg_Ultralisk_Cavern = new AUnitTypeWrapper(UnitType.Zerg_Ultralisk_Cavern);
        AUnitTypeWrapper Special_Cerebrate = new AUnitTypeWrapper(UnitType.Special_Cerebrate);
        AUnitTypeWrapper Special_Cerebrate_Daggoth = new AUnitTypeWrapper(UnitType.Special_Cerebrate_Daggoth);
        AUnitTypeWrapper Special_Mature_Chrysalis = new AUnitTypeWrapper(UnitType.Special_Mature_Chrysalis);
        AUnitTypeWrapper Special_Overmind = new AUnitTypeWrapper(UnitType.Special_Overmind);
        AUnitTypeWrapper Special_Overmind_Cocoon = new AUnitTypeWrapper(UnitType.Special_Overmind_Cocoon);
        AUnitTypeWrapper Special_Overmind_With_Shell = new AUnitTypeWrapper(UnitType.Special_Overmind_With_Shell);
        AUnitTypeWrapper Critter_Bengalaas = new AUnitTypeWrapper(UnitType.Critter_Bengalaas);
        AUnitTypeWrapper Critter_Kakaru = new AUnitTypeWrapper(UnitType.Critter_Kakaru);
        AUnitTypeWrapper Critter_Ragnasaur = new AUnitTypeWrapper(UnitType.Critter_Ragnasaur);
        AUnitTypeWrapper Critter_Rhynadon = new AUnitTypeWrapper(UnitType.Critter_Rhynadon);
        AUnitTypeWrapper Critter_Scantid = new AUnitTypeWrapper(UnitType.Critter_Scantid);
        AUnitTypeWrapper Critter_Ursadon = new AUnitTypeWrapper(UnitType.Critter_Ursadon);
        AUnitTypeWrapper Resource_Mineral_Field = new AUnitTypeWrapper(UnitType.Resource_Mineral_Field);
        AUnitTypeWrapper Resource_Mineral_Field_Type_2 = new AUnitTypeWrapper(UnitType.Resource_Mineral_Field_Type_2);
        AUnitTypeWrapper Resource_Mineral_Field_Type_3 = new AUnitTypeWrapper(UnitType.Resource_Mineral_Field_Type_3);
        AUnitTypeWrapper Resource_Vespene_Geyser = new AUnitTypeWrapper(UnitType.Resource_Vespene_Geyser);
        AUnitTypeWrapper Spell_Dark_Swarm = new AUnitTypeWrapper(UnitType.Spell_Dark_Swarm);
        AUnitTypeWrapper Spell_Disruption_Web = new AUnitTypeWrapper(UnitType.Spell_Disruption_Web);
        AUnitTypeWrapper Spell_Scanner_Sweep = new AUnitTypeWrapper(UnitType.Spell_Scanner_Sweep);
        AUnitTypeWrapper Special_Protoss_Beacon = new AUnitTypeWrapper(UnitType.Special_Protoss_Beacon);
        AUnitTypeWrapper Special_Protoss_Flag_Beacon = new AUnitTypeWrapper(UnitType.Special_Protoss_Flag_Beacon);
        AUnitTypeWrapper Special_Terran_Beacon = new AUnitTypeWrapper(UnitType.Special_Terran_Beacon);
        AUnitTypeWrapper Special_Terran_Flag_Beacon = new AUnitTypeWrapper(UnitType.Special_Terran_Flag_Beacon);
        AUnitTypeWrapper Special_Zerg_Beacon = new AUnitTypeWrapper(UnitType.Special_Zerg_Beacon);
        AUnitTypeWrapper Special_Zerg_Flag_Beacon = new AUnitTypeWrapper(UnitType.Special_Zerg_Flag_Beacon);
        AUnitTypeWrapper Powerup_Data_Disk = new AUnitTypeWrapper(UnitType.Powerup_Data_Disk);
        AUnitTypeWrapper Powerup_Flag = new AUnitTypeWrapper(UnitType.Powerup_Flag);
        AUnitTypeWrapper Powerup_Khalis_Crystal = new AUnitTypeWrapper(UnitType.Powerup_Khalis_Crystal);
        AUnitTypeWrapper Powerup_Khaydarin_Crystal = new AUnitTypeWrapper(UnitType.Powerup_Khaydarin_Crystal);
        AUnitTypeWrapper Powerup_Mineral_Cluster_Type_1 = new AUnitTypeWrapper(UnitType.Powerup_Mineral_Cluster_Type_1);
        AUnitTypeWrapper Powerup_Mineral_Cluster_Type_2 = new AUnitTypeWrapper(UnitType.Powerup_Mineral_Cluster_Type_2);
        AUnitTypeWrapper Powerup_Protoss_Gas_Orb_Type_1 = new AUnitTypeWrapper(UnitType.Powerup_Protoss_Gas_Orb_Type_1);
        AUnitTypeWrapper Powerup_Protoss_Gas_Orb_Type_2 = new AUnitTypeWrapper(UnitType.Powerup_Protoss_Gas_Orb_Type_2);
        AUnitTypeWrapper Powerup_Psi_Emitter = new AUnitTypeWrapper(UnitType.Powerup_Psi_Emitter);
        AUnitTypeWrapper Powerup_Terran_Gas_Tank_Type_1 = new AUnitTypeWrapper(UnitType.Powerup_Terran_Gas_Tank_Type_1);
        AUnitTypeWrapper Powerup_Terran_Gas_Tank_Type_2 = new AUnitTypeWrapper(UnitType.Powerup_Terran_Gas_Tank_Type_2);
        AUnitTypeWrapper Powerup_Uraj_Crystal = new AUnitTypeWrapper(UnitType.Powerup_Uraj_Crystal);
        AUnitTypeWrapper Powerup_Young_Chrysalis = new AUnitTypeWrapper(UnitType.Powerup_Young_Chrysalis);
        AUnitTypeWrapper Powerup_Zerg_Gas_Sac_Type_1 = new AUnitTypeWrapper(UnitType.Powerup_Zerg_Gas_Sac_Type_1);
        AUnitTypeWrapper Powerup_Zerg_Gas_Sac_Type_2 = new AUnitTypeWrapper(UnitType.Powerup_Zerg_Gas_Sac_Type_2);
        AUnitTypeWrapper Special_Floor_Gun_Trap = new AUnitTypeWrapper(UnitType.Special_Floor_Gun_Trap);
        AUnitTypeWrapper Special_Floor_Missile_Trap = new AUnitTypeWrapper(UnitType.Special_Floor_Missile_Trap);
        AUnitTypeWrapper Special_Right_Wall_Flame_Trap = new AUnitTypeWrapper(UnitType.Special_Right_Wall_Flame_Trap);
        AUnitTypeWrapper Special_Right_Wall_Missile_Trap = new AUnitTypeWrapper(UnitType.Special_Right_Wall_Missile_Trap);
        AUnitTypeWrapper Special_Wall_Flame_Trap = new AUnitTypeWrapper(UnitType.Special_Wall_Flame_Trap);
        AUnitTypeWrapper Special_Wall_Missile_Trap = new AUnitTypeWrapper(UnitType.Special_Wall_Missile_Trap);
        AUnitTypeWrapper Special_Pit_Door = new AUnitTypeWrapper(UnitType.Special_Pit_Door);
        AUnitTypeWrapper Special_Right_Pit_Door = new AUnitTypeWrapper(UnitType.Special_Right_Pit_Door);
        AUnitTypeWrapper Special_Right_Upper_Level_Door = new AUnitTypeWrapper(UnitType.Special_Right_Upper_Level_Door);
        AUnitTypeWrapper Special_Upper_Level_Door = new AUnitTypeWrapper(UnitType.Special_Upper_Level_Door);
        AUnitTypeWrapper Special_Cargo_Ship = new AUnitTypeWrapper(UnitType.Special_Cargo_Ship);
        AUnitTypeWrapper Special_Floor_Hatch = new AUnitTypeWrapper(UnitType.Special_Floor_Hatch);
        AUnitTypeWrapper Special_Independant_Starport = new AUnitTypeWrapper(UnitType.Special_Independant_Starport);
        AUnitTypeWrapper Special_Map_Revealer = new AUnitTypeWrapper(UnitType.Special_Map_Revealer);
        AUnitTypeWrapper Special_Mercenary_Gunship = new AUnitTypeWrapper(UnitType.Special_Mercenary_Gunship);
        AUnitTypeWrapper Special_Start_Location = new AUnitTypeWrapper(UnitType.Special_Start_Location);
        AUnitTypeWrapper None = new AUnitTypeWrapper(UnitType.None);
        AUnitTypeWrapper AllUnits = new AUnitTypeWrapper(UnitType.AllUnits);
        AUnitTypeWrapper Men = new AUnitTypeWrapper(UnitType.Men);
        AUnitTypeWrapper Buildings = new AUnitTypeWrapper(UnitType.Buildings);
        AUnitTypeWrapper Factories = new AUnitTypeWrapper(UnitType.Factories);
        AUnitTypeWrapper Unknown = new AUnitTypeWrapper(UnitType.Unknown);
    }

}
