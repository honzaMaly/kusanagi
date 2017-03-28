package cz.jan.maly.model.game.wrappers;

import bwapi.UnitSizeType;

/**
 * Wrapper for UnitSizeType
 * Created by Jan on 27-Mar-17.
 */
public class AUnitSizeTypeWrapper extends AbstractWrapper<UnitSizeType> {
    AUnitSizeTypeWrapper(UnitSizeType type) {
        super(type, type.toString());
        WrapperTypeFactory.add(this);
    }
}
