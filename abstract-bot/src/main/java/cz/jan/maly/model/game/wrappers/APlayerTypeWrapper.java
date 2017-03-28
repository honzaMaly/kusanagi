package cz.jan.maly.model.game.wrappers;

import bwapi.PlayerType;

/**
 * Wrapper for PlayerType
 * Created by Jan on 28-Mar-17.
 */
public class APlayerTypeWrapper extends AbstractWrapper<PlayerType> {
    APlayerTypeWrapper(PlayerType type) {
        super(type, type.toString());
        WrapperTypeFactory.add(this);
    }
}
