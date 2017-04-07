package cz.jan.maly.model.game.wrappers;

import bwapi.*;
import lombok.Getter;

/**
 * Wrapper for TechType
 * Created by Jan on 27-Mar-17.
 */
public class ATechTypeWrapper extends AbstractWrapper<TechType> {

    @Getter
    private final Race race;

    @Getter
    private final int gasPrice;

    @Getter
    private final Order order;

    @Getter
    private final int energyCost;

    @Getter
    private final int researchTime;

    private final WeaponType weapon;

    public AWeaponTypeWrapper getAWeaponTypeWrapper() {
        return WrapperTypeFactory.createFrom(weapon);
    }

    @Getter
    private final boolean targetsUnit;

    private final UnitType whatResearches;

    public AUnitTypeWrapper getWhatResearches() {
        return WrapperTypeFactory.createFrom(whatResearches);
    }

    @Getter
    private final int mineralPrice;

    @Getter
    private final UnitType requiredUnit;

    public AUnitTypeWrapper getRequiredUnit() {
        return WrapperTypeFactory.createFrom(requiredUnit);
    }

    @Getter
    private final boolean targetsPosition;

    ATechTypeWrapper(TechType type) {
        super(type, type.toString());
        WrapperTypeFactory.add(this);

        //original fields
        this.race = type.getRace();
        this.gasPrice = type.gasPrice();
        this.order = type.getOrder();
        this.energyCost = type.energyCost();
        this.researchTime = type.researchTime();
        this.weapon = type.getWeapon();
        this.targetsUnit = type.targetsUnit();
        this.whatResearches = type.whatResearches();
        this.mineralPrice = type.mineralPrice();
        this.requiredUnit = type.requiredUnit();
        this.targetsPosition = type.targetsPosition();
    }
}
