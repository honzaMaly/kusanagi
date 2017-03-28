package cz.jan.maly.model.game.wrappers;

import bwapi.*;
import lombok.Getter;

/**
 * Class with static registers to wrap types
 * Created by Jan on 28-Mar-17.
 */
@Getter
class WrapperTypeFactory {
    private static final WrapperTypeRegister<DamageType, ADamageTypeWrapper> A_DAMAGE_TYPE_REGISTER = new WrapperTypeRegister<>(ADamageTypeWrapper::new);
    private static final WrapperTypeRegister<ExplosionType, AExplosionTypeWrapper> A_EXPLOSION_TYPE_REGISTER = new WrapperTypeRegister<>(AExplosionTypeWrapper::new);
    private static final WrapperTypeRegister<Order, AOrderTypeWrapper> ORDER_REGISTER = new WrapperTypeRegister<>(AOrderTypeWrapper::new);
    private static final WrapperTypeRegister<PlayerType, APlayerTypeWrapper> PLAYER_TYPE_REGISTER = new WrapperTypeRegister<>(APlayerTypeWrapper::new);
    private static final WrapperTypeRegister<Race, ARace> RACE_REGISTER = new WrapperTypeRegister<>(ARace::new);
    private static final WrapperTypeRegister<TechType, ATechTypeWrapper> A_TECH_TYPE_REGISTER = new WrapperTypeRegister<>(ATechTypeWrapper::new);
    private static final WrapperTypeRegister<UnitSizeType, AUnitSizeTypeWrapper> TYPE_A_UNIT_SIZE_REGISTER = new WrapperTypeRegister<>(AUnitSizeTypeWrapper::new);
    private static final WrapperTypeRegister<UpgradeType, AUpgradeTypeWrapper> UPGRADE_TYPE_REGISTER = new WrapperTypeRegister<>(AUpgradeTypeWrapper::new);
    private static final WrapperTypeRegister<WeaponType, AWeaponTypeWrapper> WEAPON_TYPE_REGISTER = new WrapperTypeRegister<>(AWeaponTypeWrapper::new);
    private static final WrapperTypeRegister<UnitType, AUnitTypeWrapper> UNIT_TYPE_REGISTER = new WrapperTypeRegister<>(AUnitTypeWrapper::new);

    //todo player, unit

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
    static AUnitSizeTypeWrapper createFrom(UnitSizeType type) {
        return TYPE_A_UNIT_SIZE_REGISTER.createFrom(type);
    }

    static void add(AUnitSizeTypeWrapper type) {
        TYPE_A_UNIT_SIZE_REGISTER.addWrappedType(type.type, type);
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

    /**
     * Returns corresponding wrapper for type instance
     *
     * @param type
     * @return
     */
    static ADamageTypeWrapper createFrom(DamageType type) {
        return A_DAMAGE_TYPE_REGISTER.createFrom(type);
    }

    static void add(ADamageTypeWrapper type) {
        A_DAMAGE_TYPE_REGISTER.addWrappedType(type.type, type);
    }

    /**
     * Returns corresponding wrapper for type instance
     *
     * @param type
     * @return
     */
    static AExplosionTypeWrapper createFrom(ExplosionType type) {
        return A_EXPLOSION_TYPE_REGISTER.createFrom(type);
    }

    static void add(AExplosionTypeWrapper type) {
        A_EXPLOSION_TYPE_REGISTER.addWrappedType(type.type, type);
    }

    /**
     * Returns corresponding wrapper for type instance
     *
     * @param order
     * @return
     */
    static AOrderTypeWrapper createFrom(Order order) {
        return ORDER_REGISTER.createFrom(order);
    }

    static void add(AOrderTypeWrapper type) {
        ORDER_REGISTER.addWrappedType(type.type, type);
    }

    /**
     * Returns corresponding wrapper for type instance
     *
     * @param type
     * @return
     */
    static APlayerTypeWrapper createFrom(PlayerType type) {
        return PLAYER_TYPE_REGISTER.createFrom(type);
    }

    static void add(APlayerTypeWrapper type) {
        PLAYER_TYPE_REGISTER.addWrappedType(type.type, type);
    }

    /**
     * Returns corresponding wrapper for type instance
     *
     * @param race
     * @return
     */
    static ARace createFrom(Race race) {
        return RACE_REGISTER.createFrom(race);
    }

    static void add(ARace type) {
        RACE_REGISTER.addWrappedType(type.type, type);
    }
}
