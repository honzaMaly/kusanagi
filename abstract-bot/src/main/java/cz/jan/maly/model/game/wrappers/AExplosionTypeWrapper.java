package cz.jan.maly.model.game.wrappers;

import bwapi.ExplosionType;

/**
 * Wrapper for ExplosionType
 * Created by Jan on 28-Mar-17.
 */
public class AExplosionTypeWrapper extends AbstractWrapper<ExplosionType> {
    AExplosionTypeWrapper(ExplosionType type) {
        super(type, type.toString());
        WrapperTypeFactory.add(this);
    }
}
