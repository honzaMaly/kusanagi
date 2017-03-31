package cz.jan.maly.model.game.wrappers;

import bwapi.*;
import lombok.Getter;

/**
 * Wrapper for WeaponType
 * Created by Jan on 27-Mar-17.
 */
public class AWeaponTypeWrapper extends AbstractWrapper<WeaponType> {

    private final UnitType whatUses;

    public AUnitTypeWrapper getWhatUses() {
        return WrapperTypeFactory.createFrom(whatUses);
    }

    @Getter
    private final int maxRange;

    private final TechType tech;

    public ATechTypeWrapper getTech() {
        return WrapperTypeFactory.createFrom(tech);
    }

    @Getter
    private final int minRange;

    @Getter
    private final int outerSplashRadius;

    @Getter
    private final boolean targetsNonRobotic;

    @Getter
    private final int innerSplashRadius;

    @Getter
    private final int medianSplashRadius;

    @Getter
    private final boolean targetsMechanical;

    @Getter
    private final boolean targetsNonBuilding;

    @Getter
    private final int damageFactor;

    @Getter
    private final ExplosionType explosionType;

    @Getter
    private final boolean targetsAir;

    @Getter
    private final DamageType damageType;

    @Getter
    private final boolean targetsTerrain;

    @Getter
    private final boolean targetsOwn;

    @Getter
    private final int damageCooldown;

    @Getter
    private final int damageAmount;

    @Getter
    private final boolean targetsGround;

    @Getter
    private final boolean targetsOrgOrMech;

    private final UpgradeType upgradeType;

    public AUpgradeTypeWrapper getUpgradeType() {
        return WrapperTypeFactory.createFrom(upgradeType);
    }

    @Getter
    private final boolean targetsOrganic;

    @Getter
    private final int damageBonus;

    AWeaponTypeWrapper(WeaponType type) {
        super(type, type.toString());
        WrapperTypeFactory.add(this);

        //fields
        this.whatUses = type.whatUses();
        this.maxRange = type.maxRange();
        this.tech = type.getTech();
        this.minRange = type.minRange();
        this.outerSplashRadius = type.outerSplashRadius();
        this.targetsNonRobotic = type.targetsNonRobotic();
        this.innerSplashRadius = type.innerSplashRadius();
        this.medianSplashRadius = type.medianSplashRadius();
        this.targetsMechanical = type.targetsMechanical();
        this.targetsNonBuilding = type.targetsNonBuilding();
        this.damageFactor = type.damageFactor();
        this.explosionType = type.explosionType();
        this.targetsAir = type.targetsAir();
        this.damageType = type.damageType();
        this.targetsTerrain = type.targetsTerrain();
        this.targetsOwn = type.targetsOwn();
        this.damageCooldown = type.damageCooldown();
        this.damageAmount = type.damageAmount();
        this.targetsGround = type.targetsGround();
        this.targetsOrgOrMech = type.targetsOrgOrMech();
        this.upgradeType = type.upgradeType();
        this.targetsOrganic = type.targetsOrganic();
        this.damageBonus = type.damageBonus();
    }

    public double getDamageNormalized() {
        if (isForType(WeaponType.Psi_Blades)) {
            return 16;
        } else {
            return getDamageAmount() * getDamageFactor();
        }
    }

    public static void initTypes() {
        AWeaponTypeWrapper Gauss_Rifle = new AWeaponTypeWrapper(WeaponType.Gauss_Rifle);
        AWeaponTypeWrapper Gauss_Rifle_Jim_Raynor = new AWeaponTypeWrapper(WeaponType.Gauss_Rifle_Jim_Raynor);
        AWeaponTypeWrapper C_10_Canister_Rifle = new AWeaponTypeWrapper(WeaponType.C_10_Canister_Rifle);
        AWeaponTypeWrapper C_10_Canister_Rifle_Sarah_Kerrigan = new AWeaponTypeWrapper(WeaponType.C_10_Canister_Rifle_Sarah_Kerrigan);
        AWeaponTypeWrapper C_10_Canister_Rifle_Samir_Duran = new AWeaponTypeWrapper(WeaponType.C_10_Canister_Rifle_Samir_Duran);
        AWeaponTypeWrapper C_10_Canister_Rifle_Infested_Duran = new AWeaponTypeWrapper(WeaponType.C_10_Canister_Rifle_Infested_Duran);
        AWeaponTypeWrapper C_10_Canister_Rifle_Alexei_Stukov = new AWeaponTypeWrapper(WeaponType.C_10_Canister_Rifle_Alexei_Stukov);
        AWeaponTypeWrapper Fragmentation_Grenade = new AWeaponTypeWrapper(WeaponType.Fragmentation_Grenade);
        AWeaponTypeWrapper Fragmentation_Grenade_Jim_Raynor = new AWeaponTypeWrapper(WeaponType.Fragmentation_Grenade_Jim_Raynor);
        AWeaponTypeWrapper Spider_Mines = new AWeaponTypeWrapper(WeaponType.Spider_Mines);
        AWeaponTypeWrapper Twin_Autocannons = new AWeaponTypeWrapper(WeaponType.Twin_Autocannons);
        AWeaponTypeWrapper Twin_Autocannons_Alan_Schezar = new AWeaponTypeWrapper(WeaponType.Twin_Autocannons_Alan_Schezar);
        AWeaponTypeWrapper Hellfire_Missile_Pack = new AWeaponTypeWrapper(WeaponType.Hellfire_Missile_Pack);
        AWeaponTypeWrapper Hellfire_Missile_Pack_Alan_Schezar = new AWeaponTypeWrapper(WeaponType.Hellfire_Missile_Pack_Alan_Schezar);
        AWeaponTypeWrapper Arclite_Cannon = new AWeaponTypeWrapper(WeaponType.Arclite_Cannon);
        AWeaponTypeWrapper Arclite_Cannon_Edmund_Duke = new AWeaponTypeWrapper(WeaponType.Arclite_Cannon_Edmund_Duke);
        AWeaponTypeWrapper Fusion_Cutter = new AWeaponTypeWrapper(WeaponType.Fusion_Cutter);
        AWeaponTypeWrapper Gemini_Missiles = new AWeaponTypeWrapper(WeaponType.Gemini_Missiles);
        AWeaponTypeWrapper Gemini_Missiles_Tom_Kazansky = new AWeaponTypeWrapper(WeaponType.Gemini_Missiles_Tom_Kazansky);
        AWeaponTypeWrapper Burst_Lasers = new AWeaponTypeWrapper(WeaponType.Burst_Lasers);
        AWeaponTypeWrapper Burst_Lasers_Tom_Kazansky = new AWeaponTypeWrapper(WeaponType.Burst_Lasers_Tom_Kazansky);
        AWeaponTypeWrapper ATS_Laser_Battery = new AWeaponTypeWrapper(WeaponType.ATS_Laser_Battery);
        AWeaponTypeWrapper ATS_Laser_Battery_Hero = new AWeaponTypeWrapper(WeaponType.ATS_Laser_Battery_Hero);
        AWeaponTypeWrapper ATS_Laser_Battery_Hyperion = new AWeaponTypeWrapper(WeaponType.ATS_Laser_Battery_Hyperion);
        AWeaponTypeWrapper ATA_Laser_Battery = new AWeaponTypeWrapper(WeaponType.ATA_Laser_Battery);
        AWeaponTypeWrapper ATA_Laser_Battery_Hero = new AWeaponTypeWrapper(WeaponType.ATA_Laser_Battery_Hero);
        AWeaponTypeWrapper ATA_Laser_Battery_Hyperion = new AWeaponTypeWrapper(WeaponType.ATA_Laser_Battery_Hyperion);
        AWeaponTypeWrapper Flame_Thrower = new AWeaponTypeWrapper(WeaponType.Flame_Thrower);
        AWeaponTypeWrapper Flame_Thrower_Gui_Montag = new AWeaponTypeWrapper(WeaponType.Flame_Thrower_Gui_Montag);
        AWeaponTypeWrapper Arclite_Shock_Cannon = new AWeaponTypeWrapper(WeaponType.Arclite_Shock_Cannon);
        AWeaponTypeWrapper Arclite_Shock_Cannon_Edmund_Duke = new AWeaponTypeWrapper(WeaponType.Arclite_Shock_Cannon_Edmund_Duke);
        AWeaponTypeWrapper Longbolt_Missile = new AWeaponTypeWrapper(WeaponType.Longbolt_Missile);
        AWeaponTypeWrapper Claws = new AWeaponTypeWrapper(WeaponType.Claws);
        AWeaponTypeWrapper Claws_Devouring_One = new AWeaponTypeWrapper(WeaponType.Claws_Devouring_One);
        AWeaponTypeWrapper Claws_Infested_Kerrigan = new AWeaponTypeWrapper(WeaponType.Claws_Infested_Kerrigan);
        AWeaponTypeWrapper Needle_Spines = new AWeaponTypeWrapper(WeaponType.Needle_Spines);
        AWeaponTypeWrapper Needle_Spines_Hunter_Killer = new AWeaponTypeWrapper(WeaponType.Needle_Spines_Hunter_Killer);
        AWeaponTypeWrapper Kaiser_Blades = new AWeaponTypeWrapper(WeaponType.Kaiser_Blades);
        AWeaponTypeWrapper Kaiser_Blades_Torrasque = new AWeaponTypeWrapper(WeaponType.Kaiser_Blades_Torrasque);
        AWeaponTypeWrapper Toxic_Spores = new AWeaponTypeWrapper(WeaponType.Toxic_Spores);
        AWeaponTypeWrapper Spines = new AWeaponTypeWrapper(WeaponType.Spines);
        AWeaponTypeWrapper Acid_Spore = new AWeaponTypeWrapper(WeaponType.Acid_Spore);
        AWeaponTypeWrapper Acid_Spore_Kukulza = new AWeaponTypeWrapper(WeaponType.Acid_Spore_Kukulza);
        AWeaponTypeWrapper Glave_Wurm = new AWeaponTypeWrapper(WeaponType.Glave_Wurm);
        AWeaponTypeWrapper Glave_Wurm_Kukulza = new AWeaponTypeWrapper(WeaponType.Glave_Wurm_Kukulza);
        AWeaponTypeWrapper Seeker_Spores = new AWeaponTypeWrapper(WeaponType.Seeker_Spores);
        AWeaponTypeWrapper Subterranean_Tentacle = new AWeaponTypeWrapper(WeaponType.Subterranean_Tentacle);
        AWeaponTypeWrapper Suicide_Infested_Terran = new AWeaponTypeWrapper(WeaponType.Suicide_Infested_Terran);
        AWeaponTypeWrapper Suicide_Scourge = new AWeaponTypeWrapper(WeaponType.Suicide_Scourge);
        AWeaponTypeWrapper Particle_Beam = new AWeaponTypeWrapper(WeaponType.Particle_Beam);
        AWeaponTypeWrapper Psi_Blades = new AWeaponTypeWrapper(WeaponType.Psi_Blades);
        AWeaponTypeWrapper Psi_Blades_Fenix = new AWeaponTypeWrapper(WeaponType.Psi_Blades_Fenix);
        AWeaponTypeWrapper Phase_Disruptor = new AWeaponTypeWrapper(WeaponType.Phase_Disruptor);
        AWeaponTypeWrapper Phase_Disruptor_Fenix = new AWeaponTypeWrapper(WeaponType.Phase_Disruptor_Fenix);
        AWeaponTypeWrapper Psi_Assault = new AWeaponTypeWrapper(WeaponType.Psi_Assault);
        AWeaponTypeWrapper Psionic_Shockwave = new AWeaponTypeWrapper(WeaponType.Psionic_Shockwave);
        AWeaponTypeWrapper Psionic_Shockwave_TZ_Archon = new AWeaponTypeWrapper(WeaponType.Psionic_Shockwave_TZ_Archon);
        AWeaponTypeWrapper Dual_Photon_Blasters = new AWeaponTypeWrapper(WeaponType.Dual_Photon_Blasters);
        AWeaponTypeWrapper Dual_Photon_Blasters_Mojo = new AWeaponTypeWrapper(WeaponType.Dual_Photon_Blasters_Mojo);
        AWeaponTypeWrapper Dual_Photon_Blasters_Artanis = new AWeaponTypeWrapper(WeaponType.Dual_Photon_Blasters_Artanis);
        AWeaponTypeWrapper Anti_Matter_Missiles = new AWeaponTypeWrapper(WeaponType.Anti_Matter_Missiles);
        AWeaponTypeWrapper Anti_Matter_Missiles_Mojo = new AWeaponTypeWrapper(WeaponType.Anti_Matter_Missiles_Mojo);
        AWeaponTypeWrapper Anti_Matter_Missiles_Artanis = new AWeaponTypeWrapper(WeaponType.Anti_Matter_Missiles_Artanis);
        AWeaponTypeWrapper Phase_Disruptor_Cannon = new AWeaponTypeWrapper(WeaponType.Phase_Disruptor_Cannon);
        AWeaponTypeWrapper Phase_Disruptor_Cannon_Danimoth = new AWeaponTypeWrapper(WeaponType.Phase_Disruptor_Cannon_Danimoth);
        AWeaponTypeWrapper Pulse_Cannon = new AWeaponTypeWrapper(WeaponType.Pulse_Cannon);
        AWeaponTypeWrapper STS_Photon_Cannon = new AWeaponTypeWrapper(WeaponType.STS_Photon_Cannon);
        AWeaponTypeWrapper STA_Photon_Cannon = new AWeaponTypeWrapper(WeaponType.STA_Photon_Cannon);
        AWeaponTypeWrapper Scarab = new AWeaponTypeWrapper(WeaponType.Scarab);
        AWeaponTypeWrapper Neutron_Flare = new AWeaponTypeWrapper(WeaponType.Neutron_Flare);
        AWeaponTypeWrapper Halo_Rockets = new AWeaponTypeWrapper(WeaponType.Halo_Rockets);
        AWeaponTypeWrapper Corrosive_Acid = new AWeaponTypeWrapper(WeaponType.Corrosive_Acid);
        AWeaponTypeWrapper Subterranean_Spines = new AWeaponTypeWrapper(WeaponType.Subterranean_Spines);
        AWeaponTypeWrapper Warp_Blades = new AWeaponTypeWrapper(WeaponType.Warp_Blades);
        AWeaponTypeWrapper Warp_Blades_Hero = new AWeaponTypeWrapper(WeaponType.Warp_Blades_Hero);
        AWeaponTypeWrapper Warp_Blades_Zeratul = new AWeaponTypeWrapper(WeaponType.Warp_Blades_Zeratul);
        AWeaponTypeWrapper Independant_Laser_Battery = new AWeaponTypeWrapper(WeaponType.Independant_Laser_Battery);
        AWeaponTypeWrapper Twin_Autocannons_Floor_Trap = new AWeaponTypeWrapper(WeaponType.Twin_Autocannons_Floor_Trap);
        AWeaponTypeWrapper Hellfire_Missile_Pack_Wall_Trap = new AWeaponTypeWrapper(WeaponType.Hellfire_Missile_Pack_Wall_Trap);
        AWeaponTypeWrapper Flame_Thrower_Wall_Trap = new AWeaponTypeWrapper(WeaponType.Flame_Thrower_Wall_Trap);
        AWeaponTypeWrapper Hellfire_Missile_Pack_Floor_Trap = new AWeaponTypeWrapper(WeaponType.Hellfire_Missile_Pack_Floor_Trap);
        AWeaponTypeWrapper Yamato_Gun = new AWeaponTypeWrapper(WeaponType.Yamato_Gun);
        AWeaponTypeWrapper Nuclear_Strike = new AWeaponTypeWrapper(WeaponType.Nuclear_Strike);
        AWeaponTypeWrapper Lockdown = new AWeaponTypeWrapper(WeaponType.Lockdown);
        AWeaponTypeWrapper EMP_Shockwave = new AWeaponTypeWrapper(WeaponType.EMP_Shockwave);
        AWeaponTypeWrapper Irradiate = new AWeaponTypeWrapper(WeaponType.Irradiate);
        AWeaponTypeWrapper Parasite = new AWeaponTypeWrapper(WeaponType.Parasite);
        AWeaponTypeWrapper Spawn_Broodlings = new AWeaponTypeWrapper(WeaponType.Spawn_Broodlings);
        AWeaponTypeWrapper Ensnare = new AWeaponTypeWrapper(WeaponType.Ensnare);
        AWeaponTypeWrapper Dark_Swarm = new AWeaponTypeWrapper(WeaponType.Dark_Swarm);
        AWeaponTypeWrapper Plague = new AWeaponTypeWrapper(WeaponType.Plague);
        AWeaponTypeWrapper Consume = new AWeaponTypeWrapper(WeaponType.Consume);
        AWeaponTypeWrapper Stasis_Field = new AWeaponTypeWrapper(WeaponType.Stasis_Field);
        AWeaponTypeWrapper Psionic_Storm = new AWeaponTypeWrapper(WeaponType.Psionic_Storm);
        AWeaponTypeWrapper Disruption_Web = new AWeaponTypeWrapper(WeaponType.Disruption_Web);
        AWeaponTypeWrapper Restoration = new AWeaponTypeWrapper(WeaponType.Restoration);
        AWeaponTypeWrapper Mind_Control = new AWeaponTypeWrapper(WeaponType.Mind_Control);
        AWeaponTypeWrapper Feedback = new AWeaponTypeWrapper(WeaponType.Feedback);
        AWeaponTypeWrapper Optical_Flare = new AWeaponTypeWrapper(WeaponType.Optical_Flare);
        AWeaponTypeWrapper Maelstrom = new AWeaponTypeWrapper(WeaponType.Maelstrom);
        AWeaponTypeWrapper None = new AWeaponTypeWrapper(WeaponType.None);
        AWeaponTypeWrapper Unknown = new AWeaponTypeWrapper(WeaponType.Unknown);
    }
}
