package cz.jan.maly.model.game.wrappers;

import bwapi.WeaponType;
import lombok.Getter;

/**
 * Wrapper for WeaponType
 * Created by Jan on 27-Mar-17.
 */
public class AWeaponTypeWrapper extends AbstractWrapper<WeaponType> {

    @Getter
    private final AUnitTypeWrapper whatUses;

    @Getter
    private final int maxRange;

    @Getter
    private final ATechTypeWrapper tech;

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
    private final AExplosionTypeWrapper explosionType;

    @Getter
    private final boolean targetsAir;

    @Getter
    private final ADamageTypeWrapper damageType;

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

    @Getter
    private final AUpgradeTypeWrapper upgradeType;

    @Getter
    private final boolean targetsOrganic;

    @Getter
    private final int damageBonus;

    AWeaponTypeWrapper(WeaponType type) {
        super(type, type.toString());
        WrapperTypeFactory.add(this);

        //fields
        this.whatUses = WrapperTypeFactory.createFrom(type.whatUses());
        this.maxRange = type.maxRange();
        this.tech = WrapperTypeFactory.createFrom(type.getTech());
        this.minRange = type.minRange();
        this.outerSplashRadius = type.outerSplashRadius();
        this.targetsNonRobotic = type.targetsNonRobotic();
        this.innerSplashRadius = type.innerSplashRadius();
        this.medianSplashRadius = type.medianSplashRadius();
        this.targetsMechanical = type.targetsMechanical();
        this.targetsNonBuilding = type.targetsNonBuilding();
        this.damageFactor = type.damageFactor();
        this.explosionType = WrapperTypeFactory.createFrom(type.explosionType());
        this.targetsAir = type.targetsAir();
        this.damageType = WrapperTypeFactory.createFrom(type.damageType());
        this.targetsTerrain = type.targetsTerrain();
        this.targetsOwn = type.targetsOwn();
        this.damageCooldown = type.damageCooldown();
        this.damageAmount = type.damageAmount();
        this.targetsGround = type.targetsGround();
        this.targetsOrgOrMech = type.targetsOrgOrMech();
        this.upgradeType = WrapperTypeFactory.createFrom(type.upgradeType());
        this.targetsOrganic = type.targetsOrganic();
        this.damageBonus = type.damageBonus();
    }

//    public static double getDamageNormalized(WeaponType weapon) {
//        if (weapon.equals(WeaponType.Psi_Blades)) {
//            return 16;
//        } else {
//            return weapon.damageAmount() * weapon.damageFactor();
//        }
//    }
}
