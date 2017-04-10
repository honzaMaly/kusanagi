package cz.jan.maly.model.game.wrappers;

import bwapi.Position;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Wrapper for BWMirror Position
 */
public class APosition extends AbstractPositionWrapper<Position> {

    @Getter
    private final ATilePosition aTilePosition;

    @Getter
    private final double length;

    private APosition(Position p) {
        super(p, p.getX(), p.getY());
        this.length = p.getLength();
        this.aTilePosition = ATilePosition.wrap(p.toTilePosition());
    }

    /**
     * Wrap position
     *
     * @param toWrap
     * @return
     */
    public static APosition wrap(Position toWrap) {
        Map<Integer, Map<Integer, AbstractPositionWrapper<?>>> positionsByCoordinates = cache.computeIfAbsent(Position.class, aClass -> new ConcurrentHashMap<>());
        Map<Integer, AbstractPositionWrapper<?>> positionsByYCoordinates = positionsByCoordinates.computeIfAbsent(toWrap.getX(), integer -> new ConcurrentHashMap<>());
        return (APosition) positionsByYCoordinates.computeIfAbsent(toWrap.getY(), integer -> new APosition(toWrap));
    }

    /**
     * Returns X coordinate in tiles
     */
    public int getTileX() {
        return getX() / ATilePosition.SIZE_IN_PIXELS;
    }

    /**
     * Returns Y coordinate in tiles
     */
    public int getTileY() {
        return getY() / ATilePosition.SIZE_IN_PIXELS;
    }

}
