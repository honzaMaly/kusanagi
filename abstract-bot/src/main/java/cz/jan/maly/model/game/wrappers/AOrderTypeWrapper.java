package cz.jan.maly.model.game.wrappers;

import bwapi.Order;

/**
 * Wrapper for order type
 * Created by Jan on 28-Mar-17.
 */
public class AOrderTypeWrapper extends AbstractWrapper<Order> {
    AOrderTypeWrapper(Order type) {
        super(type, type.toString());
        WrapperTypeFactory.add(this);
    }
}
