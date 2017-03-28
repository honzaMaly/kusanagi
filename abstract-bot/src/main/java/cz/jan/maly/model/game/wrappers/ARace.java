package cz.jan.maly.model.game.wrappers;

import bwapi.Race;
import lombok.Getter;

/**
 * Wrapper for race type
 * Created by Jan on 27-Mar-17.
 */
public class ARace extends AbstractWrapper<Race> {

    @Getter
    private final AUnitTypeWrapper supplyProvider;

    @Getter
    private final AUnitTypeWrapper transport;

    @Getter
    private final AUnitTypeWrapper worker;

    @Getter
    private final AUnitTypeWrapper refinery;

    @Getter
    private final AUnitTypeWrapper center;

    ARace(Race type) {
        super(type, type.toString());
        WrapperTypeFactory.add(this);

        //original fields
        this.supplyProvider = WrapperTypeFactory.createFrom(type.getSupplyProvider());
        this.transport = WrapperTypeFactory.createFrom(type.getTransport());
        this.worker = WrapperTypeFactory.createFrom(type.getWorker());
        this.refinery = WrapperTypeFactory.createFrom(type.getRefinery());
        this.center = WrapperTypeFactory.createFrom(type.getCenter());
    }
}
