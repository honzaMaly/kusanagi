package cz.jan.maly.model.game.wrappers;

import bwapi.TilePosition;
import lombok.Getter;

import java.util.Optional;

/**
 * Wrapper for TilePosition
 * Created by Jan on 27-Mar-17.
 */
public class ATilePosition {
    public static final int SIZE_IN_PIXELS = TilePosition.SIZE_IN_PIXELS;

    final TilePosition tilePosition;

    @Getter
    private final int x, y;

    @Getter
    private final double length;

    public ATilePosition(TilePosition tilePosition) {
        this.tilePosition = tilePosition;
        this.x = tilePosition.getX();
        this.y = tilePosition.getY();
        this.length = tilePosition.getLength();
    }

    static Optional<ATilePosition> creteOrEmpty(TilePosition tilePosition) {
        if (tilePosition == null) {
            return Optional.empty();
        }
        return Optional.of(new ATilePosition(tilePosition));
    }
}
