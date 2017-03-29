package cz.jan.maly.model.game.wrappers;

import bwapi.Position;
import bwapi.TilePosition;
import cz.jan.maly.model.game.util.PositionUtil;
import lombok.Getter;

import java.util.Optional;

/**
 * Wrapper for BWMirror Position
 */
public class APosition {
    final Position p;

    @Getter
    private final int x, y;

    @Getter
    private final ATilePosition aTilePosition;

    @Getter
    private final double length;

    public APosition(int pixelX, int pixelY) {
        this.p = (new Position(pixelX, pixelY)).makeValid();
        this.x = pixelX;
        this.y = pixelY;
        this.length = this.p.getLength();
        this.aTilePosition = new ATilePosition(this.p.toTilePosition());
    }

    public APosition(Position p) {
        this.p = p.makeValid();
        this.x = this.p.getX();
        this.y = this.p.getY();
        this.length = this.p.getLength();
        this.aTilePosition = new ATilePosition(this.p.toTilePosition());
    }

    // =========================================================

    /**
     * Returns distance from one position to other in build tiles. One build tile equals to 32 pixels. Usage
     * of build tiles instead of pixels is preferable, because it's easier to imagine distances if one knows
     * building dimensions.
     */
    public double distanceTo(APosition position) {
        return PositionUtil.distanceTo(this, position);
    }

    /**
     * Returns distance from one position to other in build tiles. One build tile equals to 32 pixels. Usage
     * of build tiles instead of pixels is preferable, because it's easier to imagine distances if one knows
     * building dimensions.
     */
    public double distanceTo(AUnitWrapper unit) {
        return PositionUtil.distanceTo(this, unit.getPosition());
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

    /**
     * Returns new position object that is translated in x,y by given values.
     */
    public APosition translate(int pixelDX, int pixelDY) {
        return new APosition(getX() + pixelDX, getY() + pixelDY);
    }

    static Optional<APosition> creteOrEmpty(Position position) {
        if (position == null) {
            return Optional.empty();
        }
        return Optional.of(new APosition(position));
    }

}
