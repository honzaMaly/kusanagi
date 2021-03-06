package cz.jan.maly.model.game.wrappers;

import cz.jan.maly.utils.MyLogger;
import lombok.Getter;

/**
 * Common parent for all type wrappers
 * Created by Jan on 27-Mar-17.
 */
abstract class AbstractWrapper<T> {

    @Getter
    final T type;

    @Getter
    private final String name;

    AbstractWrapper(T type, String name) {
        this.name = name;
        if (type == null) {
            MyLogger.getLogger().warning("Constructor: type is null.");
            throw new RuntimeException("Constructor: type is null.");
        }
        this.type = type;
    }

    /**
     * Returns true if given type equals to one of types passed as parameter.
     */
    boolean isType(T myType, T[] types) {
        for (T otherType : types) {
            if (myType.equals(otherType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if wrapper represents bwapi type
     *
     * @param bwapiType
     * @return
     */
    public boolean isForType(T bwapiType) {
        return type.equals(bwapiType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractWrapper<?> that = (AbstractWrapper<?>) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
