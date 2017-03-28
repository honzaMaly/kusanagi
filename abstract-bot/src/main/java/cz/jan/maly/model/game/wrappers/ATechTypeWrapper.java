package cz.jan.maly.model.game.wrappers;

import bwapi.TechType;
import lombok.Getter;

/**
 * Wrapper for TechType
 * Created by Jan on 27-Mar-17.
 */
public class ATechTypeWrapper extends AbstractWrapper<TechType> {

    @Getter
    private final ARace race;

    @Getter
    private final int gasPrice;

    @Getter
    private final AOrderTypeWrapper order;

    @Getter
    private final int energyCost;

    @Getter
    private final int researchTime;

    @Getter
    private final AWeaponTypeWrapper weapon;

    @Getter
    private final boolean targetsUnit;

    @Getter
    private final AUnitTypeWrapper whatResearches;

    @Getter
    private final int mineralPrice;

    @Getter
    private final AUnitTypeWrapper requiredUnit;

    @Getter
    private final boolean targetsPosition;

    ATechTypeWrapper(TechType type) {
        super(type, type.toString());
        WrapperTypeFactory.add(this);

        //original fields
        this.race = WrapperTypeFactory.createFrom(type.getRace());
        this.gasPrice = type.gasPrice();
        this.order = WrapperTypeFactory.createFrom(type.getOrder());
        this.energyCost = type.energyCost();
        this.researchTime = type.researchTime();
        this.weapon = WrapperTypeFactory.createFrom(type.getWeapon());
        this.targetsUnit = type.targetsUnit();
        this.whatResearches = WrapperTypeFactory.createFrom(type.whatResearches());
        this.mineralPrice = type.mineralPrice();
        this.requiredUnit = WrapperTypeFactory.createFrom(type.requiredUnit());
        this.targetsPosition = type.targetsPosition();
    }
}
