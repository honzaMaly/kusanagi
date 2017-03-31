package cz.jan.maly.model.game.wrappers;

import bwapi.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    //reference on buildings by race
    private static final Map<Race, Set<AUnitTypeWrapper>> buildingsByRace = new HashMap<>();

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
        if (type.isBuilding()) {
            buildingsByRace.putIfAbsent(type.getRace(), new HashSet<>()).add(type);
        }
    }

    static Set<AUnitTypeWrapper> buildingsForRace(Race race) {
        return buildingsByRace.getOrDefault(race, new HashSet<>());
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
