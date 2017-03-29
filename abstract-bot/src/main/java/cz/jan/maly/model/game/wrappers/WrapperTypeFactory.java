package cz.jan.maly.model.game.wrappers;

import bwapi.TechType;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwapi.WeaponType;
import lombok.Getter;

/**
 * Class with static registers to wrap types
 * Created by Jan on 28-Mar-17.
 */
@Getter
class WrapperTypeFactory {
    private static final WrapperTypeRegister<TechType, ATechTypeWrapper> A_TECH_TYPE_REGISTER = new WrapperTypeRegister<>(ATechTypeWrapper::new);
    private static final WrapperTypeRegister<UpgradeType, AUpgradeTypeWrapper> UPGRADE_TYPE_REGISTER = new WrapperTypeRegister<>(AUpgradeTypeWrapper::new);
    private static final WrapperTypeRegister<WeaponType, AWeaponTypeWrapper> WEAPON_TYPE_REGISTER = new WrapperTypeRegister<>(AWeaponTypeWrapper::new);
    private static final WrapperTypeRegister<UnitType, AUnitTypeWrapper> UNIT_TYPE_REGISTER = new WrapperTypeRegister<>(AUnitTypeWrapper::new);

    /**
     * Returns corresponding wrapper for type instance
     *
     * @param type
     * @return
     */
    static AUnitTypeWrapper createFrom(UnitType type) {
        return UNIT_TYPE_REGISTER.createFrom(type);
    }

    static void add(AUnitTypeWrapper type) {
        UNIT_TYPE_REGISTER.addWrappedType(type.type, type);
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
