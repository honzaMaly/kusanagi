package cz.jan.maly.model.game.wrappers;

import bwapi.DamageType;

/**
 * Wrapper for DamageType
 * Created by Jan on 28-Mar-17.
 */
public class ADamageTypeWrapper extends AbstractWrapper<DamageType> {
    ADamageTypeWrapper(DamageType type) {
        super(type, type.toString());
        WrapperTypeFactory.add(this);
    }
}
