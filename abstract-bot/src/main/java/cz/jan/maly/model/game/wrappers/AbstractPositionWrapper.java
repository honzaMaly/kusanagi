package cz.jan.maly.model.game.wrappers;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Template for wrapper with static position.
 * Created by Jan on 10-Apr-17.
 */
public abstract class AbstractPositionWrapper<T> {

    //cache to store position objects
    static Map<Class<?>, Map<Integer, Map<Integer, AbstractPositionWrapper<?>>>> cache = new ConcurrentHashMap<>();

    @Getter
    final T wrappedPosition;

    @Getter
    private final int x, y;

    AbstractPositionWrapper(T wrappedPosition, int x, int y) {
        this.wrappedPosition = wrappedPosition;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns distance from one position to other in build tiles. One build tile equals to 32 pixels. Usage
     * of build tiles instead of pixels is preferable, because it's easier to imagine distances if one knows
     * building dimensions.
     */
    public double distanceTo(AbstractPositionWrapper<?> other) {
        return distanceTo(x, y, other.getX(), other.getY());
    }

    public boolean isOnSameCoordinates(AbstractPositionWrapper<?> other) {
        return x == other.x && y == other.y;
    }

    private double distanceTo(int oneX, int oneY, int otherX, int otherY) {
        int dx = oneX - otherX;
        int dy = oneY - otherY;
        return Math.sqrt(dx * dx + dy * dy) / ATilePosition.SIZE_IN_PIXELS;
    }

    /**
     * Clear cache
     */
    public static void clearCache() {
        cache.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPositionWrapper<?> that = (AbstractPositionWrapper<?>) o;

        if (x != that.x) return false;
        return y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
