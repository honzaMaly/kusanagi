package cz.jan.maly.model.game.wrappers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Register to keep registered types
 * Created by Jan on 27-Mar-17.
 */
class WrapperTypeRegister<T, V extends AbstractWrapper<T>> {
    private final Map<T, V> types = new ConcurrentHashMap<>();
    private final StrategyToWrapType<T, V> strategyToWrapType;

    WrapperTypeRegister(StrategyToWrapType<T, V> strategyToWrapType) {
        this.strategyToWrapType = strategyToWrapType;
    }

    void addWrappedType(T t, V v) {
        types.put(t, v);
    }

    /**
     * Returns corresponding wrapper for type instance
     *
     * @param type
     * @return
     */
    V createFrom(T type) {
        if (type == null || !types.containsKey(type)) {
            throw new RuntimeException("Type is null.");
        }
        return types.get(type);
    }

}
