package cz.jan.maly.model.game.wrappers;

import bwta.BaseLocation;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Wrapper for BaseLocation
 * Created by Jan on 10-Apr-17.
 */
public class ABaseLocationWrapper extends AbstractPositionWrapper<BaseLocation> {

    @Getter
    private final APosition position;

    @Getter
    private final ATilePosition tilePosition;

    @Getter
    private final boolean isMineralOnly, isStartLocation, isIsland;

    //todo other fields - regions,... paths to other positions + distances - to access it easily

    private ABaseLocationWrapper(BaseLocation location) {
        super(location, location.getX(), location.getY());
        this.tilePosition = ATilePosition.wrap(location.getTilePosition());
        this.position = APosition.wrap(location.getPosition());
        this.isStartLocation = location.isStartLocation();
        this.isIsland = location.isIsland();
        this.isMineralOnly = location.isMineralOnly();
    }

    /**
     * Wrap location
     *
     * @param toWrap
     * @return
     */
    public static ABaseLocationWrapper wrap(BaseLocation toWrap) {
        Map<Integer, Map<Integer, AbstractPositionWrapper<?>>> positionsByCoordinates = cache.computeIfAbsent(BaseLocation.class, aClass -> new ConcurrentHashMap<>());
        Map<Integer, AbstractPositionWrapper<?>> positionsByYCoordinates = positionsByCoordinates.computeIfAbsent(toWrap.getX(), integer -> new ConcurrentHashMap<>());
        return (ABaseLocationWrapper) positionsByYCoordinates.computeIfAbsent(toWrap.getY(), integer -> new ABaseLocationWrapper(toWrap));
    }
}
