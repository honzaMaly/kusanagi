package cz.jan.maly.model.game.wrappers;

import bwapi.*;

import java.util.Set;

import static cz.jan.maly.model.game.wrappers.ATechTypeWrapper.TECH_TYPES;
import static cz.jan.maly.model.game.wrappers.AUnitTypeWrapper.BUILDING_TYPES;
import static cz.jan.maly.model.game.wrappers.AUnitTypeWrapper.UNITS_TYPES;
import static cz.jan.maly.model.game.wrappers.AUpgradeTypeWrapper.UPGRADE_TYPES;

/**
 * Class with static registers to wrap types
 * Created by Jan on 28-Mar-17.
 */
public class WrapperTypeFactory {
    private static final WrapperTypeRegister<TechType, ATechTypeWrapper> A_TECH_TYPE_REGISTER = new WrapperTypeRegister<>(ATechTypeWrapper::new);
    private static final WrapperTypeRegister<UpgradeType, AUpgradeTypeWrapper> UPGRADE_TYPE_REGISTER = new WrapperTypeRegister<>(AUpgradeTypeWrapper::new);
    private static final WrapperTypeRegister<WeaponType, AWeaponTypeWrapper> WEAPON_TYPE_REGISTER = new WrapperTypeRegister<>(AWeaponTypeWrapper::new);
    private static final WrapperTypeRegister<UnitType, AUnitTypeWrapper> UNIT_TYPE_REGISTER = new WrapperTypeRegister<>(AUnitTypeWrapper::new);

    /**
     * Clear cache
     */
    public static void clearCache() {
        A_TECH_TYPE_REGISTER.clear();
        UPGRADE_TYPE_REGISTER.clear();
        UPGRADE_TYPE_REGISTER.clear();
        UPGRADE_TYPE_REGISTER.clear();
    }

    /**
     * Returns corresponding wrapper for type instance
     *
     * @param type
     * @return
     */
    public static AUnitTypeWrapper createFrom(UnitType type) {
        return UNIT_TYPE_REGISTER.createFrom(type);
    }

    static void add(AUnitTypeWrapper type) {
        UNIT_TYPE_REGISTER.addWrappedType(type.type, type);
    }

    /**
     * Returns types for race
     *
     * @return
     */
    public static Set<AUnitTypeWrapper> buildings() {
        return BUILDING_TYPES;
    }

    /**
     * Returns types for race
     *
     * @return
     */
    public static Set<AUnitTypeWrapper> units() {
        return UNITS_TYPES;
    }

    /**
     * Returns types for race
     *
     * @return
     */
    public static Set<ATechTypeWrapper> techs() {
        return TECH_TYPES;
    }

    /**
     * Returns types for race
     *
     * @return
     */
    public static Set<AUpgradeTypeWrapper> upgrades() {
        return UPGRADE_TYPES;
    }

    /**
     * Returns corresponding wrapper for type instance
     *
     * @param type
     * @return
     */
    static AWeaponTypeWrapper createFrom(WeaponType type) {
        return WEAPON_TYPE_REGISTER.createFrom(type);
    }

    static void add(AWeaponTypeWrapper type) {
        WEAPON_TYPE_REGISTER.addWrappedType(type.type, type);
    }

    /**
     * Returns corresponding wrapper for type instance
     *
     * @param type
     * @return
     */
    static AUpgradeTypeWrapper createFrom(UpgradeType type) {
        return UPGRADE_TYPE_REGISTER.createFrom(type);
    }

    static void add(AUpgradeTypeWrapper type) {
        UPGRADE_TYPE_REGISTER.addWrappedType(type.type, type);
    }

    /**
     * Returns corresponding wrapper for type instance
     *
     * @param type
     * @return
     */
    static ATechTypeWrapper createFrom(TechType type) {
        return A_TECH_TYPE_REGISTER.createFrom(type);
    }

    static void add(ATechTypeWrapper type) {
        A_TECH_TYPE_REGISTER.addWrappedType(type.type, type);
    }
}
