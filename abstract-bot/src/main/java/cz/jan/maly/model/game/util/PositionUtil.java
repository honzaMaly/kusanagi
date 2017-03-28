package cz.jan.maly.model.game.util;

import cz.jan.maly.model.game.wrappers.APosition;
import cz.jan.maly.model.game.wrappers.ATilePosition;
import cz.jan.maly.model.game.wrappers.AUnitWrapper;

/**
 * Util class defining methods to work with position
 */
public class PositionUtil {

    /**
     * Returns distance from one position to other in build tiles. One build tile equals to 32 pixels. Usage
     * of build tiles instead of pixels is preferable, because it's easier to imagine distances if one knows
     * building dimensions.
     */
    public static double distanceTo(APosition one, APosition other) {
        int dx = one.getX() - other.getX();
        int dy = one.getY() - other.getY();
        return Math.sqrt(dx * dx + dy * dy) / ATilePosition.SIZE_IN_PIXELS;
    }

    /**
     * Returns distance from one position to other in build tiles. One build tile equals to 32 pixels. Usage
     * of build tiles instead of pixels is preferable, because it's easier to imagine distances if one knows
     * building dimensions.
     */
    public static double distanceTo(AUnitWrapper one, AUnitWrapper other) {
        return distanceTo(one.getPosition(), other.getPosition());
    }

}
